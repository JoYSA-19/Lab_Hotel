import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * HotelService is a Runnable class.
 * It is the command handler for several 
 * hotel services: 
 * 		1, Change the username
 * 		2, Make a reservation
 * 		3, Cancel the reservation
 * 		4, Show the availability information for the name
 *
 *
 * @author Jonathan Yang
 */
public class HotelService implements Runnable{

	private Socket s;
	private DataInputStream in;
	private DataOutputStream out;
	private Hotel hotel;
	private String clientName = null;
	
	
	/**
	 * Constructor of HotelService.
	 * @param s (current server socket)
	 * @param hotel 
	 */
	public HotelService(Socket s, Hotel hotel) {
		this.s = s;
		this.hotel = hotel;
	}
	
	
	@Override
	/**
	 * Override method implements Runnable.
	 * Run the doService method until the thread
	 * is interrupetd.
	 */
	public void run() {
		try{
	         try{
	            in = new DataInputStream(s.getInputStream());
	            out = new DataOutputStream(s.getOutputStream());
	            doService();
	         }finally{
	            s.close();
	         }
	      }catch(IOException e){
	         System.out.println(clientName + " has disconnected!");
	      }
		
	}

	
	private static boolean connect = true;
	
	/**
	 * Start an service. 
	 * 		First, ask the username.
	 * 		Then enter the main service,
	 * 		Make actions as the user types in
	 * 		commands.
	 * @throws IOException (The user disconnect itself from the server)
	 */
	private void doService() throws IOException{
		out.writeUTF("Hi, please enter your name: ");
		out.flush();
		clientName = in.readUTF();
		out.writeUTF("Hi! " + clientName);
		out.flush();
		while (true) {

			if(connect == false) {
				System.out.println(clientName + " has entered a invalid input.");
				System.out.println(clientName + " has been disconnected!");
				return;
	        }
			String command = in.readUTF();
	        if (command.equals("QUIT")) {
	        	 System.out.println(clientName + " has quitted.");
	        	 connect = false;
	        }
	        else {
	        	executeCommand(command);
	        }
	        
		}	
	}

	/**
	 * Make actions by the given command.
	 * @param command
	 * @throws IOException
	 */
	private void executeCommand(String command) throws IOException {
			
			if(command.equals("USER")) {
				clientName = in.readUTF();
				out.writeUTF("Hello, " + clientName);
			}
			else if(command.equals("CANCEL")) {
				if(hotel.cancelReservation(clientName)) {
					out.writeUTF("Reservations successfully canceled for " + clientName);
				}
				else {
					out.writeUTF("Reservations not canceled for " + clientName + ", no current reservation.");
				}
			}
			else if(command.equals("RESERVE")) {
				int answer = in.readInt();
				int firstDay = answer;
				answer = in.readInt();
				int lastDay = answer;
				
				if(hotel.makeReservation(clientName, firstDay, lastDay)) {
					out.writeUTF("Reservation made: " + clientName + " from " + firstDay + " through " + lastDay);
				}
				else {
					out.writeUTF("Reservation unsuccessful: " + clientName + " from " + firstDay + " through " + lastDay);
				}
			}
			else if(command.equals("AVAIL")) {
				out.writeUTF(hotel.reservationInformation());
			}
			else {
				out.writeUTF("Invalid command: Closing Connection.");
				out.flush();
				connect = false;
			}
			out.flush();
			
		
		
	}

}
