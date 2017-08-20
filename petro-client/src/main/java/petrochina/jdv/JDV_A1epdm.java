package petrochina.jdv;

import static org.teiid.test.jdbc.client.JDBCUtils.*;

import java.sql.Connection;

public class JDV_A1epdm {
    
    private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:a1epdmvdb@mm://11.11.208.135:31100;version=1";
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";

    public static void main(String[] args) throws Exception {


        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        
        execute(conn, "SELECT COUNT(*) AS A1_1 FROM a1epdm_10_88_107_253.A1ZJK.A1_DICTIONARY");
        execute(conn, "SELECT COUNT(*) AS A1_2 FROM a1epdm_10_82_249_131.A1ZJK.A1_DICTIONARY");
        execute(conn, "SELECT COUNT(*) AS A1_3 FROM a1epdm_10_188_57_47.A1ZJK.A1_DICTIONARY");
        execute(conn, "SELECT COUNT(*) AS A1_4 FROM a1epdm_10_76_32_26.A1ZJK.A1_DICTIONARY");
        execute(conn, "SELECT COUNT(*) AS A1_5 FROM a1epdm_10_86_14_191.A1ZJK.A1_DICTIONARY");
        
        execute(conn, "SELECT COUNT(*) AS A1_ALL FROM A1_DICTIONARYS");
        
//        execute(conn, "SELECT DIC_ID, P_ID, ITEM_CODE, ITEM_NAME, ORDER_ID FROM a1epdm_10_88_107_253.A1ZJK.A1_DICTIONARY UNION ALL SELECT DIC_ID, P_ID, ITEM_CODE, ITEM_NAME, ORDER_ID FROM a1epdm_10_82_249_131.A1ZJK.A1_DICTIONARY UNION ALL SELECT DIC_ID, P_ID, ITEM_CODE, ITEM_NAME, ORDER_ID FROM a1epdm_10_188_57_47.A1ZJK.A1_DICTIONARY UNION ALL SELECT DIC_ID, P_ID, ITEM_CODE, ITEM_NAME, ORDER_ID FROM a1epdm_10_76_32_26.A1ZJK.A1_DICTIONARY UNION ALL SELECT DIC_ID, P_ID, ITEM_CODE, ITEM_NAME, ORDER_ID FROM a1epdm_10_86_14_191.A1ZJK.A1_DICTIONARY");
        
        close(conn);
    }

}
