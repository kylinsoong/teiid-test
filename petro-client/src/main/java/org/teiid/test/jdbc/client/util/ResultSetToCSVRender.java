package org.teiid.test.jdbc.client.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ResultSetToCSVRender implements AutoCloseable {
    
    private String renderFileName; 
    private ResultSet rs;
    private boolean columnNames = true;
    
    public ResultSetToCSVRender(ResultSet rs, String renderFileName) {
        this.rs = rs;
        this.renderFileName = renderFileName;
    }
    
    public ResultSetToCSVRender(ResultSet rs, String renderFileName, boolean columnNames) {
        this.rs = rs;
        this.renderFileName = renderFileName;
        this.columnNames = columnNames;
    }
    
    public void renderer() throws SQLException, IOException {
        
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        
        Path path = Paths.get(renderFileName);
        if(Files.exists(path)) {
            Files.delete(path);
        }
        File renderFile = Files.createFile(path).toFile();
        try (OutputStream os = new FileOutputStream(renderFile)) {
            os.write(239);
            os.write(187);
            os.write(191);
            
            try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(os,"UTF-8"))) {
                
                if(columnNames) {
                    for(int i = 1; i<=columnCount; i++) {
                        pw.print(metaData.getColumnName(i));
                        if(i<columnCount) {
                            pw.print(",");
                            pw.flush();
                        }
                        if(i==columnCount) {
                            pw.println();
                            pw.flush();
                        }
                    }
                }
                
                while(rs.next()) {
                    for (int i = 1; i <=columnCount; i++) {
                        pw.print(rs.getObject(i));
                        if(i<columnCount) {
                            pw.print(",");
                            pw.flush();
                        }
                        if(i==columnCount) {
                            pw.println();
                            pw.flush();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void close() throws Exception {
        if(rs != null) {
            rs.close();
        }
    }

}
