package org.jboss.teiid.dashboard.hibernate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.jboss.dashboard.commons.misc.ReflectionUtils;
import org.jboss.dashboard.profiler.CodeBlockTrace;
import org.jboss.dashboard.profiler.CodeBlockType;
import org.jboss.dashboard.profiler.CoreCodeBlockTypes;
import org.jboss.dashboard.profiler.Profiler;
import org.jboss.dashboard.profiler.ThreadProfile;



public class SimpleHibernateTxFragment {
    
    Logger log = Logger.getLogger(SimpleHibernateTxFragment.class.getName());
    
    /**
     * Flag to prevent double execution
     */
    private boolean alreadyExecuted;

    /**
     * Flag to control the reuse of the current transaction.
     * If true then a brand new transaction is opened and added to the list of transactions linked with the current thread.
     */
    protected boolean newTransactionRequested;

    /**
     * Flag to control the Hibernate flush.
     * If true a <code>Session.flush()</code> call will be performed just before the finish of the tx fragment.
     */
    protected boolean flushAfterFinish;

    /**
     * If true then this fragment will receive callback invocations both before and after the transaction completion.
     */
    protected boolean callbacksEnabled;

    /**
     * The parent fragment. It only applies for child fragments.
     */
    protected SimpleHibernateTxFragment parentFragment;
    
    public SimpleHibernateTxFragment(boolean newTransactionRequested) {
        this(newTransactionRequested, false);
    }

    public SimpleHibernateTxFragment(boolean newTransactionRequested, boolean callbacksEnabled) {
        this(newTransactionRequested, callbacksEnabled, false);
    }

    public SimpleHibernateTxFragment(boolean newTransactionRequested,  boolean callbacksEnabled, boolean flushAfterFinish) {
        this.newTransactionRequested = newTransactionRequested;
        this.callbacksEnabled = callbacksEnabled;
        this.flushAfterFinish = flushAfterFinish;
        this.alreadyExecuted = false;
        this.parentFragment = null;
    }
    
    public final void execute() throws Exception {
        if (alreadyExecuted) {
            log.log(Level.SEVERE, "Double execution of fragment is not allowed.");
        } else {
            SimpleHibernateTransaction tx = SimpleHibernateTransaction.getCurrentTx();
            if (!tx.isActive()){
                executeInitiator(tx);
            } else {
                executeChild(tx);
            }
        }
    }

    private void executeInitiator(SimpleHibernateTransaction tx) throws Exception {
        tx.begin();
        CodeBlockTrace trace = new SimpleHibernateTxTrace(tx).begin();
        try {
            // Execute the tx fragment
            alreadyExecuted = true;
            tx.executeFragment(this);
        } finally {
            // Complete the tx
            tx.complete();
            trace.end();
        }
        
    }

    private void executeChild(SimpleHibernateTransaction tx) throws Exception {
        if (newTransactionRequested) {
            // Nested tx's are deferred until the end of the current tx.
            tx.followers.add(this);
        } else {
            // If it is just a child fragment then execute it right now.
            alreadyExecuted = true;
            tx.executeFragment(this);
        }
    }
    
    /**
     * Callback method invoked before the transaction is commited.
     */
    protected void beforeCommit() throws Throwable {
    }

    /**
     * Callback method invoked before the transaction is rolled back.
     */
    protected void beforeRollback() throws Throwable {
    }

    /**
     * Callback method invoked after a transaction commit.
     */
    protected void afterRollback() throws Throwable {
    }

    /**
     * Callback method invoked after a transaction rollback.
     */
    protected void afterCommit() throws Throwable {
    }
    
    class SimpleHibernateTxTrace extends CodeBlockTrace {

        public static final String CONNECTION_ID = "Tx Connection id";
        public static final String PROCESS_ID = "Tx Process id";
        public static final String TX_ISOLATION = "Tx Isolation";
        public static final String AUTO_COMMIT = "Tx Auto commit";

        protected Map<String,Object> context;

        public SimpleHibernateTxTrace(SimpleHibernateTransaction tx) throws Exception {
            super(Integer.toString(tx.hashCode()));
            context = buildContext(tx);
        }

        public CodeBlockType getType() {
            return CoreCodeBlockTypes.TRANSACTION;
        }

        public String getDescription() {
            return "Transaction " + id;
        }

        public Map<String,Object> getContext() {
            return context;
        }

        public Map<String,Object> buildContext(SimpleHibernateTransaction tx) throws Exception {
            final Map<String,Object> ctx = new LinkedHashMap<String,Object>();
            tx.getSession().doWork(new Work() {
            public void execute(Connection conn) throws SQLException {

                // Generic
                ctx.put("Tx id", id);
                ctx.put(TX_ISOLATION, Integer.toString(conn.getTransactionIsolation()));
                ctx.put(AUTO_COMMIT, conn.getAutoCommit());

                // SQLServer-specific
                Object tdsChannel = ReflectionUtils.getPrivateField(conn, "tdsChannel");
                if (tdsChannel != null) {
                    Object spid = ReflectionUtils.getPrivateField(tdsChannel, "spid");
                    if (spid != null) ctx.put(PROCESS_ID, spid.toString());
                }
                // SQLServer-specific
                Object connId = ReflectionUtils.getPrivateField(conn, "connectionID");
                if (connId == null) connId = ReflectionUtils.getPrivateField(conn, "traceID");
                if (connId != null) {
                    ctx.put(CONNECTION_ID, connId.toString());
                }

                ThreadProfile threadProfile = Profiler.lookup().getCurrentThreadProfile();
                if (threadProfile != null) threadProfile.addContextProperties(ctx);
            }});
            return ctx;
        }
    }

    protected void txFragment(Session session) throws Throwable {
    }

}
