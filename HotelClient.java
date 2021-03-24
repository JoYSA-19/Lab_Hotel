import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * This is a manual client class.
 * 
 * Connect to the HotelServer, and 
 * make actions through the Communication.
 *
 * @author Jonathan Yang
 */
public class HotelClient {

	public static void main(String[]args) throws IOException {
		final int PORT = 1181;
		String menu = "-----------------------------------------------------\nPlease type in your command choose from following:\n";
		menu += "USER (Change the username)\n";
		menu += "RESERVE (Make a reservation, each person \n\tcan only have one reservation)\n";			
		menu += "CANCEL (Cancel your reservation)\n";
		menu += "AVAIL (Show the availability of the hotel)\n";
		menu += "QUIT (Disconnect)\n";
		menu += "Illegal comand other than above is not allowed.\n";
		menu += "-----------------------------------------------------\n\ncomand: ";
		final String MENU = menu; 
		Socket s = new Socket("localhost", PORT);
		InputStream instream = s.getInputStream();
		OutputStream outstream = s.getOutputStream();
		DataInputStream in = new DataInputStream(instream);
		DataOutputStream out = new DataOutputStream(outstream);
		Scanner input = new Scanner(System.in);
		
		String command = "";
		String response = "";
		
		System.out.println("Receiving: \n" + in.readUTF()); //Please enter the name
		command = input.next();
		out.writeUTF(command); //Sending name
		out.flush();
		System.out.println("Sending: \n" + command);
		response = in.readUTF();
		System.out.println("Receiving: \n" + response + "\n"); // Menu
		
		
		while(true) {
			System.out.print(MENU);
			command = input.next();
			
			//QUIT (disconnect)
			if(command.equalsIgnoreCase("QUIT")) {
				System.exit(0);
			}
			
			//RESERVE command
			else if(command.equalsIgnoreCase("RESERVE")) {
				System.out.print("First Day of Reservation: ");
				int firstDay = input.nextInt();
				System.out.print("Last Day of Reservation: ");
				int lastDay = input.nextInt();
				out.writeUTF(command);
				out.writeInt(firstDay);
				out.writeInt(lastDay);
				out.flush();
				System.out.println("Sending: \n" + command);
				response = in.readUTF();
				System.out.println("Receiving: \n" + response + "\n");
			}
			
			
			//CANCEL command (by Username)
			else if(command.equalsIgnoreCase("CANCEL")) {
				out.writeUTF(command);
				out.flush();
				System.out.println("Sending: \n" + command);
				response = in.readUTF();
				System.out.println("Receiving: \n" + response + "\n");
			}
			
			
			
			//AVAIL Show the availabillity
			else if(command.equalsIgnoreCase("AVAIL")) {
				out.writeUTF(command);
				out.flush();
				System.out.println("Sending: \n" + command);
				response = in.readUTF();
				System.out.println("Receiving: \n" + response + "\n");
			}
			
			else if(command.equalsIgnoreCase("USER")) {
				System.out.print("Enter your username: ");
				String newUsername = input.next();
				out.writeUTF(command);
				out.flush();
				out.writeUTF(newUsername);
				out.flush();
				System.out.println("Sending: changing username to: " + newUsername);
				response = in.readUTF();
				System.out.println("Receiving: \n" + response);
			}
			
			else {
				out.writeUTF(command);
				out.flush();
				System.out.println("Sending: \n" + command);
				response = in.readUTF();
				System.out.println("Receiving: \n" + response + "\n");
			}

		}
		
		
		
		
		
	}
}
