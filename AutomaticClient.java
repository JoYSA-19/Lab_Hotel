import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

/**
 * AutomaticClient is a Runnable class.
 * 
 * It creates an automatic client that does random actions.
 * Each action will load by a second to start the next action.
 * It will get interrupted when the countdown is up.
 * 
 * I added a new feature to this lab: 
 * In order to make the actions that are more resonable to activate,
 * I set each actions by different possibility ratio to activate:
 * command "USER": 10%
 * command "RESERVE": 40%
 * command "CANCEL": 35%
 * command "AVAIL": 15%
 * 
 * When the Runnable class is get interrupted, it will show
 * the final Availability information;
 * And prints out the times that each action was activated.
 *
 * @author Jonathan Yang
 */
public class AutomaticClient implements Runnable{

	//private Hotel hotel;
	private static final int DELAY = 1000;
	private final int PORT = 1181;
	private Socket s;
	private InputStream instream;
	private OutputStream outstream;
	private DataInputStream in;
	private DataOutputStream out;
	private Random r = new Random();
	private int clientOrder = 1; 
	private String clientName; 
	private String response = "";
	
	private int use, res, can, ava; //Times for each action activated
	
	/**
	 * Constructor for AutomaticClient.
	 * 
	 * @throws IOException
	 */
	public AutomaticClient() throws IOException{
		//this.hotel = hotel;
		s = new Socket("localhost", PORT);
		instream = s.getInputStream();
		outstream = s.getOutputStream();
		in = new DataInputStream(instream);
		out = new DataOutputStream(outstream);
		
	}
	@Override
	/**
	 * Override method from Runnable.
	 * 
	 * Set default username.
	 * In the loop:
	 * Generate a random number between 0 - 99, 
	 * make actions by each possibility ratio.
	 * Loop for long time until it's being interrupted
	 */
	public void run() {
		use = 0;
		res = 0;
		can = 0;
		ava = 0;
		
		int actionGenerator;
		try {
			System.out.println("Receiving: \n" + in.readUTF()); //Please enter the name
			clientName = "Client" + clientOrder;
			out.writeUTF(clientName);
			out.flush();
			System.out.println("Sending: \n" + clientName);
			response = in.readUTF();
			System.out.println("Receiving: \n" + response + "\n");
			while(true) {
				actionGenerator = r.nextInt(100);
				Thread.sleep(DELAY);
				if(actionGenerator < 10) {
					commandUser();
				}
				else if(actionGenerator < 50) {
					commandReserve();
				}
				else if(actionGenerator < 85) {
					commandCancel();
				}
				else {
					commandAvail();
				}
			}
		}catch(InterruptedException e) {
			System.out.println("Actions complete!");
			try {
				commandAvail();
				System.out.println(timesOfEachMethod());
				s.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		
		
		
	}
	
	/**
	 * Change the username.
	 * @throws IOException
	 */
	public void commandUser() throws IOException {
		use++;
		String command = "USER";
		out.writeUTF(command);
		out.flush();
		clientName = newClientName();
		out.writeUTF(clientName);
		out.flush();
		System.out.println("Sending: changing username to: " + clientName);
		response = in.readUTF();
		System.out.println("Receiving: \n" + response + "\n");
	}
	/**
	 * Make a reservation.
	 * @throws IOException
	 */
	public void commandReserve() throws IOException {
		res++;
		String command = "RESERVE";
		int firstDay = 1 + r.nextInt(30);
		int lastDay = firstDay + r.nextInt(31 - firstDay);
		out.writeUTF(command);
		out.writeInt(firstDay);
		out.writeInt(lastDay);
		out.flush();
		System.out.println("Sending: \n" + command);
		response = in.readUTF();
		System.out.println("Receiving: \n" + response + "\n");
	}
	/**
	 * Cancel a reservation by current clientName.
	 * @throws IOException
	 */
	public void commandCancel() throws IOException {
		can++;
		String command = "CANCEL";
		out.writeUTF(command);
		out.flush();
		System.out.println("Sending: \n" + command);
		response = in.readUTF();
		System.out.println("Receiving: \n" + response + "\n");
	}
	/**
	 * Display the Availability information.
	 * @throws IOException
	 */
	public void commandAvail() throws IOException {
		ava++;
		String command = "AVAIL";
		out.writeUTF(command);
		out.flush();
		System.out.println("Sending: \n" + command);
		response = in.readUTF();
		System.out.println("Receiving: \n" + response + "\n");
	}

	/**
	 * Generate a new Client name.
	 * Form: Client[number]
	 * @return newClientName
	 */
	public String newClientName() {
		clientOrder += 1;
		clientName = "Client" + clientOrder;
		return clientName;
	}
	/**
	 * Return the times that each actions were called.
	 * @return information
	 */
	public String timesOfEachMethod() {
		String result = "Methods that were called: \n";
		result += "User: " + use + "\n";
		result += "Reservation: " + res + "\n";
		result += "Cancel: " + can + "\n";
		result += "Avail: " + ava + "\n";
		return result;
		
	}
	
}
