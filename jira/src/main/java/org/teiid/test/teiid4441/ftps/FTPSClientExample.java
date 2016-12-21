package org.teiid.test.teiid4441.ftps;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;

public class FTPSClientExample {

    public static void main(String[] args) throws Exception {

        FTPSClient client = new FTPSClient("TLS", false);
        client.setConnectTimeout(2000);
        client.setDefaultTimeout(2000);
        client.setDataTimeout(2000);
        client.setControlEncoding(FTP.DEFAULT_CONTROL_ENCODING);
        
        client.execPBSZ(0);
        client.execPROT("P");
        
        client.connect("10.66.192.120", 21);
        if (!FTPReply.isPositiveCompletion(client.getReplyCode())){
            throw new Exception("Failed to connect to ftp server");
        }
        
        if (!client.login("kylin", "redhat")) {
            throw new Exception("Failed to login to ftp server");
        }
        
        client.changeWorkingDirectory("/home/kylin/vsftpd");
        client.enterLocalActiveMode();
        client.setFileType(FTP.BINARY_FILE_TYPE);
        client.setBufferSize(2048);
    }

}
