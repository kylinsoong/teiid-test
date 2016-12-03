package org.teiid.test.teiid4441;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.nio.file.Files;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

public class TestFTPClient {

    public static void main(String[] args) throws SocketException, IOException {

        String server = "10.66.192.120";
        int port = 21;
        String user = "kylin";
        String pass = "redhat";
        
        FTPClient ftpClient = new FTPClient();
        
        ftpClient.connect(server, port);
        ftpClient.login(user, pass);
        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        
        String remoteFile1 = "/home/kylin/vsftpd/marketdata-price.txt";
        File downloadFile1 = Files.createTempFile("ftp", "download").toFile();
        
        OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
        boolean success = ftpClient.retrieveFile(remoteFile1, outputStream1);
        outputStream1.close();
        
        if (success) {
            System.out.println("File #1 has been downloaded successfully." + downloadFile1);
        }
        
//        InputStream inputStream = ftpClient.retrieveFileStream(remoteFile);
//        
//        System.out.println(ftpClient.isConnected());

    }

}
