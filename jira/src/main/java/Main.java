import org.teiid.test.jira.TEIID3664;


public class Main {

	public static void main(String[] args) {

		int success = 0;
		int failed = 0;
		for(int i = 0 ; i < 100 ; i ++) {
			try {
				TEIID3664.main(args);
				success ++;
				System.out.println(i + ": Success" );
			} catch (Exception e) {
				failed ++;
				System.out.println(i + ": Failed" );
				e.printStackTrace();
			}
		}
		
		System.out.println("Success: " +  success + ", Failed: " +  failed);
	}

}
