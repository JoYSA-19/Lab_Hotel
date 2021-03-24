import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This is a server class for Hotel class.
 * 		The client can connect the server through
 * the PORT number.
 *
 *
 * @author Jonathan Yang
 */
public class HotelServer {

	public static void main(String[] args) {
		
		Hotel hotel = new Hotel();
		final int PORT = 1181;
		
		
		try(ServerSocket server = new ServerSocket(PORT)){
			System.out.println("August Hotel!");
			System.out.println("Waiting for client to connect......");
			while(true) {
				Socket s = server.accept();
		        System.out.println("Client connected.");
		        HotelService service = new HotelService(s, hotel);
		        Thread t = new Thread(service);
		        t.start();
			}
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
