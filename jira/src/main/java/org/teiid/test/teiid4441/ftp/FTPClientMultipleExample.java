package org.teiid.test.teiid4441.ftp;


import java.io.InputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FTPClientMultipleExample {

    public static void main(String[] args) throws Exception {

        FTPClient client = new FTPClient();
        client.setControlEncoding(FTP.DEFAULT_CONTROL_ENCODING);
        
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
        
        InputStream is = client.retrieveFileStream("marketdata-price.txt");
        System.out.println(is);
        is.close();
        client.completePendingCommand();
        InputStream is1 = client.retrieveFileStream("marketdata-price1.txt");
        System.out.println(is1);
        
        client.disconnect();
        
        
    }

}
