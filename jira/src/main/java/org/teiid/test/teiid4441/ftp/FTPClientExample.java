package org.teiid.test.teiid4441.ftp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FTPClientExample {

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
        
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        client.retrieveFile("marketdata-price.txt", output);
        byte[] bytes = output.toByteArray();
        System.out.println(new String(bytes));
        
        client.storeFile("marketdata-price2.txt", new ByteArrayInputStream(bytes));
        InputStream in = client.retrieveFileStream("marketdata-price2.txt");
        byte[] newBytes = convert(in);
        if(!Arrays.equals(bytes, newBytes)) {
            throw new Exception("Should equals");
        }
        in.close();
        client.completePendingCommand();
        
        boolean success = client.deleteFile("marketdata-price2.txt");
        if(!success) {
            throw new Exception("Delete Failed");
        }
        
        client.disconnect();
        
    }

    private static byte[] convert(InputStream in) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[2048];
        while ((nRead = in.read(data, 0, data.length)) != -1){
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        return buffer.toByteArray();
    }

}
