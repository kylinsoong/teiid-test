package org.teiid.test.jdbc.client.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

public class ResultSetRenderer extends TableRenderer implements AutoCloseable {
    
    private final ResultSet rs;
    private final ResultSetMetaData meta;
    private final int columns;
    
    private final long clobLimit = 8192;
    
    public ResultSetRenderer(ResultSet rs) throws SQLException {
        super(new PrintStreamOutputDevice(System.out));
        this.rs = rs;
        this.meta = rs.getMetaData();
        this.columns = meta.getColumnCount();
        setMeta(getDisplayMeta(meta));
    }
    
    public ResultSetRenderer(ResultSet rs, String path) throws SQLException, FileNotFoundException {
        super(new PrintStreamOutputDevice(new PrintStream(new FileOutputStream(new File(path)), true)));
        this.rs = rs;
        this.meta = rs.getMetaData();
        this.columns = meta.getColumnCount();
        setMeta(getDisplayMeta(meta));
    }
    
    @Override
    public void renderer() {
        
        try {
            while(rs.next()) {
                Column[] currentRow = new Column[ columns ];
                for (int i = 0 ; i < columns ; ++i) {
                    int col = i+1;
                            String colString;
                            if (meta.getColumnType( col ) == Types.CLOB) {
                                colString = readClob(rs.getClob( col ));
                            }
                            else {
                                colString = rs.getString( col );
                            }
                    Column thisCol = new Column(colString);
                    currentRow[i] = thisCol;
                }
                addRow(currentRow);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
            
        super.renderer();
    }

    private ColumnMetaData[] getDisplayMeta(ResultSetMetaData m) throws SQLException {
        
        ColumnMetaData result[] = new ColumnMetaData [ columns ];
    
        for (int i = 0; i < result.length; ++i) {
            int col = i+1;
            int alignment  = ColumnMetaData.ALIGN_LEFT;
            String columnLabel = m.getColumnLabel( col );
            /*
            int width = Math.max(m.getColumnDisplaySize(i),
                     columnLabel.length());
            */
            switch (m.getColumnType( col )) {
            case Types.NUMERIC:  
            case Types.INTEGER: 
            case Types.REAL:
            case Types.SMALLINT:
            case Types.TINYINT:
                alignment = ColumnMetaData.ALIGN_RIGHT;
                break;
            }
            result[i] = new ColumnMetaData(columnLabel,alignment);
        }
        return result;
    }
    
    private String readClob(Clob c) throws SQLException {
        if (c == null) return null;
        StringBuffer result = new StringBuffer();
        long restLimit = clobLimit;
        try {
            Reader in = c.getCharacterStream();
            char buf[] = new char [ 4096 ];
            int r;

            while (restLimit > 0 
                   && (r = in.read(buf, 0, (int)Math.min(buf.length,restLimit))) > 0) 
                {
                    result.append(buf, 0, r);
                    restLimit -= r;
                }
        }
        catch (Exception e) {
           out.println(e.toString());
        }
        if (restLimit == 0) {
            result.append("...");
        }
        return result.toString();
    }

    @Override
    public void close() throws Exception {
        out.close();
    }

}
