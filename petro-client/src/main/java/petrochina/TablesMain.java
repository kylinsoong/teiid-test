package petrochina;

public class TablesMain {

    public static void main(String[] args) throws InterruptedException {

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    A1epdm10_188_57_47.main(new String[]{"console"});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
        }).start();
        
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    A1epdm10_76_32_26.main(new String[]{"console"});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
        }).start();
        
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    A1epdm10_82_249_131.main(new String[]{"console"});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
        }).start();
        
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                try {
//                    A1epdm10_82_249_132_meta.main(new String[0]);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            
//        }).start();
        
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    A1epdm10_86_14_191.main(new String[]{"console"});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
        }).start();
        
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    A1epdm10_88_107_253.main(new String[]{"console"});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
        }).start();
        
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Epbank10_88_110_149.main(new String[]{"console"});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
        }).start();
        
        Thread.currentThread().join();
    }

}
