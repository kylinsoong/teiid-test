package com.simba.couchbase.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.simba.couchbase.core.CBConnectionSettings;
import com.simba.couchbase.exceptions.CBJDBCMessageKey;
import com.simba.couchbase.utils.CBQueryUtils;
import com.simba.support.ILogger;
import com.simba.support.LogUtilities;
import com.simba.support.Pair;
import com.simba.support.exceptions.ErrorException;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.SSLContext;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;

public class N1QLClient implements Runnable {
    
    private final String CLUSTER_NODES = "/admin/clusters/default/nodes";
    private final int REFRESH_INTERVAL = 30000;
    private final CBConnectionSettings m_settings;
    private final String PREPARE_PREFIX = "prepare ";

    private Pair<String, Integer> m_currNode;
    private List<Pair<String, Integer>> m_clusterNodes;
    private String m_host;
    private boolean m_isHostLocalHost;
    private HttpClient m_httpClient;
    private ILogger m_log;
    private int m_port;
    private boolean m_connected;

    private final Object lock = new Object();
    
    private ErrorException m_cachedBakThreadException;

    public N1QLClient(ILogger log, CBConnectionSettings settings) throws ErrorException {

        this.m_log = log;
        this.m_settings = settings;
        this.m_currNode = null;
        this.m_clusterNodes = Collections.synchronizedList(new ArrayList<>());
        this.m_isHostLocalHost = false;
        
        if (settings.m_enableSSL) {
            try {
                SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(new File(this.m_settings.m_sslCert)).useProtocol("TLS").build();
                this.m_httpClient = HttpClients.custom().setSslcontext(sslcontext).build();    
            } catch (Exception ex) {
                ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.QUERY_EXECUTION_FAIL_ERR.name(), new String[] { ex.getMessage() });
                err.initCause(ex);
                throw err;    
            }    
        } else {
            this.m_httpClient = HttpClients.createDefault();    
        }    
    }
    
    public N1QLQueryResult connect(String host, int port, String requestType, String query) throws ErrorException {
        
        this.m_host = host;
        this.m_port = port;
        this.m_isHostLocalHost = ((host.contains("localhost")) || (host.contains("127.0.0.1")));
        this.m_clusterNodes.add(new Pair(this.m_host, Integer.valueOf(this.m_port)));
        this.m_connected = true;
        
        HttpResponse connectionResq = N1QLClientUtil.GET(this.m_httpClient, this.m_host, this.m_port, N1QLRequestType.statement.name(), query, this.m_settings, false, null, null, this.m_log);
        N1QLQueryResult connectionResult = (N1QLQueryResult)N1QLClientUtil.toResultSet(connectionResq, N1QLRequestType.statement.name());

        if (this.m_settings.m_redundancy == true) {
            identifyClusterNodes();
            new Thread(this).start();    
        }
        
        return connectionResult;    
    }

    public N1QLQueryResult executePrepareStatement(N1QLPlan plan, boolean setScanConsistency, ArrayList<String> posParams, HashMap<String, String> namedParams) throws ErrorException {

        LogUtilities.logFunctionEntrance(this.m_log, new Object[] { plan });
        String query = plan.getPlanValue();
        checkCachedExceptions();
        return execute(query, N1QLRequestType.prepared.name(), setScanConsistency, posParams, namedParams);    
    }
    
    public N1QLQueryResult executeStatementDirectly(String query, boolean setScanConsistency) throws ErrorException {
        return executeStatementDirectly(query, setScanConsistency, null, null);    
    }
    
    public N1QLQueryResult executeStatementDirectly(String query, boolean setScanConsistency, ArrayList<String> posParams, HashMap<String, String> namedParams) throws ErrorException {

        LogUtilities.logFunctionEntrance(this.m_log, new Object[] { query });
        checkCachedExceptions();
        return execute(query, N1QLRequestType.statement.name(), setScanConsistency, posParams, namedParams);    
    }
    
    public N1QLRowCountSet executeUpdate(String query, ArrayList<String> posParams, HashMap<String, String> namedParams) throws ErrorException {

        LogUtilities.logFunctionEntrance(this.m_log, new Object[] { query });
        
        try {
            checkCachedExceptions();
            this.m_currNode = randomizeNodes();
            HttpResponse directExeResq = N1QLClientUtil.POST(this.m_httpClient, (String)this.m_currNode.key(), ((Integer)this.m_currNode.value()).intValue(), N1QLRequestType.statement.name(), query, this.m_settings, posParams, namedParams, this.m_log);
            return (N1QLRowCountSet)N1QLClientUtil.toResultSet(directExeResq, N1QLRequestType.update.name());    
        } catch (ErrorException ex) {
            LogUtilities.logFunctionEntrance(this.m_log, new Object[] { query });
            if (ex.getMessage().equals(CBJDBCMessageKey.CONN_FAIL_ERR)) {
                if (this.m_clusterNodes.size() > 0) {
                    identifyClusterNodes();    
                }
                executeUpdate(query, posParams, namedParams);    
            }
            throw ex;    
        }    
    }
    
    public void disconnect() {
        N1QLClientUtil.DISCONNECT(this.m_httpClient);
        this.m_connected = false;
        synchronized (this.lock) {
            this.lock.notify();    
        }    
    }
    
    public synchronized void identifyClusterNodes() throws ErrorException {

        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        
        boolean identifiedNodes = false;
        List<Pair<String, Integer>> currClusterNodes = new ArrayList(this.m_clusterNodes);
        this.m_clusterNodes.clear();
        
        try {
            int nodeIndex = 0;
            while ((!identifiedNodes) && (nodeIndex < currClusterNodes.size())) {
                CloseableHttpClient httpClient;
                String uriStr;
                if (this.m_settings.m_enableSSL) {
                    uriStr = "https://";
                    SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(new File(this.m_settings.m_sslCert)).useProtocol("TLS").build();
                    httpClient = HttpClients.custom().setSslcontext(sslcontext).build();    
                } else {
                    uriStr = "http://";
                    httpClient = HttpClients.createDefault();    
                }
                
                Pair<String, Integer> nodeForConnect = (Pair)currClusterNodes.get(nodeIndex);
                uriStr = uriStr + (String)nodeForConnect.key() + ":" + nodeForConnect.value() + CLUSTER_NODES;

                URL url = new URI(uriStr).toURL();
                HttpGet getRequest = new HttpGet(url.toString());
                getRequest.addHeader("accept", "application/json");
                HttpResponse response = httpClient.execute(getRequest);
                if (response.getStatusLine().getStatusCode() == 200) {
                    List<String> allNodes = new ArrayList<>();
                    JsonNode rootNode = N1QLClientUtil.toJsonNode(response);
                    for (int i = 0; i < rootNode.size(); i++) {
                        JsonNode currNode = rootNode.get(i);
                        if (this.m_settings.m_enableSSL) {
                            JsonNode secureEndPoint = currNode.get("querySecure");
                            if (null != secureEndPoint) {
                                allNodes.add(secureEndPoint.toString());    
                            }    
                        } else {
                            String currValue = currNode.get("queryEndpoint").toString();
                            allNodes.add(currValue);    
                        }    
                    }

                    for (int i = 0; i < allNodes.size(); i++) {
                        StringBuffer ip = new StringBuffer();
                        Pattern p = Pattern.compile("(/query/service|\"https?://)");
                        Matcher m = p.matcher((CharSequence)allNodes.get(i));
                        while (m.find()) {
                            m.appendReplacement(ip, "");    
                        }
                        String ipStr = ip.toString();
                        int colon = ipStr.indexOf(":");
                        if ((this.m_isHostLocalHost) || ((!ipStr.contains("localhost")) && (!ipStr.contains("127.0.0.1")))) {
                            this.m_clusterNodes.add(new Pair(ipStr.substring(0, colon), Integer.valueOf(Integer.parseInt(ipStr.substring(colon + 1)))));    
                        }    
                    }
                    
                    identifiedNodes = true;
                    if (!this.m_clusterNodes.contains(nodeForConnect)) {
                        this.m_clusterNodes.add(nodeForConnect);    
                    }    
                }
                
                nodeIndex++;
                httpClient.close();    
            }
            
            if (this.m_clusterNodes.isEmpty()) {
                ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.CLUSTER_IDEN_ERR.name(), new String[] { "Failed for retrieve cluster nodes information." });
                throw err;    
            }    
        } catch (Exception ex) {
            LogUtilities.logFunctionEntrance(this.m_log, new Object[] { "Error Detected during retrieving available nodes" });
            ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.CLUSTER_IDEN_ERR.name(), new String[] { ex.getMessage() });
            err.initCause(ex);
            throw err;    
        }    
    }
    
    public synchronized Pair<String, Integer> randomizeNodes() {
        
        Random randomGenerator = new Random();
        return (Pair)this.m_clusterNodes.get(randomGenerator.nextInt(this.m_clusterNodes.size()));    
    }
    
    public N1QLPlan prepareStatement(String query, ArrayList<String> posParams, HashMap<String, String> namedParams) throws ErrorException {

        LogUtilities.logFunctionEntrance(this.m_log, new Object[] { query });
        
        try {
            checkCachedExceptions();
            StringBuilder prepareQuery = new StringBuilder();
            prepareQuery.append(PREPARE_PREFIX).append(query);
            
            this.m_currNode = randomizeNodes();
            HttpResponse prepareResq = N1QLClientUtil.GET(this.m_httpClient, (String)this.m_currNode.key(), ((Integer)this.m_currNode.value()).intValue(), N1QLRequestType.statement.name(), prepareQuery.toString(), this.m_settings, false, posParams, namedParams, this.m_log);

            return N1QLClientUtil.toQueryPlan(prepareResq);    
        } catch (ErrorException ex) {
            LogUtilities.logFunctionEntrance(this.m_log, new Object[] { query });
            if (ex.getMessage().equals(CBJDBCMessageKey.CONN_FAIL_ERR)) {
                if (this.m_clusterNodes.size() > 0) {
                    identifyClusterNodes();    
                }
                prepareStatement(query, posParams, namedParams);    
            }
            
            throw ex;    
        }    
    }
    
    private N1QLQueryResult execute(String query, String requestType, boolean setScanConsistency, ArrayList<String> posParams, HashMap<String, String> namedParams) throws ErrorException {

        LogUtilities.logFunctionEntrance(this.m_log, new Object[] { query, requestType });

        try {
            checkCachedExceptions();
            
            this.m_currNode = randomizeNodes();
            HttpResponse directExeResq = N1QLClientUtil.GET(this.m_httpClient, (String)this.m_currNode.key(), ((Integer)this.m_currNode.value()).intValue(), requestType, query, this.m_settings, setScanConsistency, posParams, namedParams, this.m_log);
            return (N1QLQueryResult)N1QLClientUtil.toResultSet(directExeResq, N1QLRequestType.statement.name());    
        } catch (ErrorException ex) {
            LogUtilities.logFunctionEntrance(this.m_log, new Object[] { query });
            if (ex.getMessage().equals(CBJDBCMessageKey.CONN_FAIL_ERR)) {
                if (this.m_clusterNodes.size() > 0) {
                    identifyClusterNodes();    
                }
                execute(query, requestType, setScanConsistency, posParams, namedParams);    
            }
            throw ex;    
        }    
    }
    
    public void checkCachedExceptions() throws ErrorException {
        if (null != this.m_cachedBakThreadException) {
            throw this.m_cachedBakThreadException;    
        }    
    }
    
    public void run() {
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        while (this.m_connected) {
            refreshClusterNodeInfo();    
        }    
    }
    
    public void refreshClusterNodeInfo() {
        
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        synchronized (this.lock) {
            try {
                identifyClusterNodes();
                this.lock.wait(30000L);    
            } catch (ErrorException ex) {
                LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
                this.m_cachedBakThreadException = ex;    
            } catch (InterruptedException ex) {
                LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
                ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.CLUSTER_BAK_FETCHING_ERR.name(), new String[] { ex.getMessage() });
                this.m_cachedBakThreadException = err;    
            }    
        }    
    }
}
