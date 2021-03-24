import java.io.IOException;


/**
 * This is a tester for AutomaticClient class.
 * 
 * Record the start time and make a count down
 * of 30 seconds. Run the runnable class. When
 * the time is up, interrupt the running thread
 *
 * @author Jonathan Yang
 */
public class AutoClientTester {

	public static void main(String[] args) {
		
		
		try {
			AutomaticClient client = new AutomaticClient();
			long startTime = System.currentTimeMillis();
			final double randomTime = 30000;
			Thread C1 = new Thread(client);
			C1.start();

			while(C1.isAlive()) {
				if(System.currentTimeMillis() - startTime > randomTime) {
					C1.interrupt();
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		

	}

}
