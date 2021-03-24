import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;




/**
 * This "Hotel" has only one room, it allows
 * users to make reservation and cancel their
 * reservations.
 * 
 * @author Jonathan Yang
 */

public class Hotel {
	
	private ArrayList<Reservation> rList; // An arrayList of Reservatinos
	private Lock reservationLock = new ReentrantLock();
	
	
	/**
	 * Constructor of the hotel.
	 * Has one variable: the arraylist of Reservations
	 */
	public Hotel() {
		this.rList = new ArrayList<>();
	}
	
	
	/**
	 * The reservation class.
	 * Three data members: 
	 * 1, name of the user
	 * 2, the first day of the reservation
	 * 3, the last day of the reservation
	 *
	 * @author Jonathan Yang
	 */
	public class Reservation{
		private String name;
		private int firstDay;
		private int lastDay;
		
		
		/**
		 * Constructor of Reservation
		 * @param username
		 * @param firstDay of reservation
		 * @param lastDay of reservation
		 */
		public Reservation(String name, int firstDay, int lastDay) {
			this.name = name;
			this.firstDay = firstDay;
			this.lastDay = lastDay;
		}
	}
	
	/**
	 * This method make a reservation, and shows 
	 * whether if the reservation is successfully made
	 * @param username
	 * @param firstDay of reservation
	 * @param lastDay of reservation
	 * @return TURE if successfully made reservation
	 */
	public boolean makeReservation(String name, int firstDay, int lastDay) {
		reservationLock.lock();
		try {
			for(Reservation r: rList) {
				if(name.equals(r.name)) {
					return false;
				} //Same username: each user can only make one reservation
				if(firstDay >= r.firstDay && firstDay <= r.lastDay) {
					return false;
				} // If the firstDay is in any of the date range 
				if(lastDay >= r.firstDay && lastDay <= r.lastDay) {
					return false;
				} // If the lastDay is in any of the date range 
				if(firstDay <= r.firstDay && lastDay >= r.lastDay) {
					return false;
				}// If the new reservation contains the range of made reservations
			}
			if(firstDay < 1 || lastDay > 31) {
				return false;
			} // not allowed values
			else {
				rList.add(new Reservation(name, firstDay, lastDay));
				return true; // successfully made
			}
		}finally {
			reservationLock.unlock();
		}
		
	}
	
	/**
	 * Cancel a reservation by given username.
	 * @param username
	 * @return true if successfully canceled
	 */
	public boolean cancelReservation(String name) {
		reservationLock.lock();
		boolean complete = false;
		try {
			for(Reservation r: rList) {
				if(r.name.equals(name)) {
					rList.remove(r);
					complete = true;
					break;
				} // successfully canceled
			}
		}finally {
			reservationLock.unlock();
		}
		return complete;
		
	}
	
	/**
	 * Prints out the information of the hotel 
	 * availability by dates
	 * @return
	 * --------------------------
	 * August Reservation History:
	 * Aug.1st......
	 * Aug.2nd......
	 * .
	 * .
	 * .
	 * .
	 * .
	 * ---------------------------
	 */
	public String reservationInformation() {
		reservationLock.lock();
		String info = "Error.";
		boolean done = false;
		
		try {
			String suffix = "th";
			info = "-------------------------\n";
			info += "August Reservation History: \n";
				for(int i = 1; i < 32; i++) {
					suffix = "th";
					switch(i) {
					case 1: 
					case 21: 
					case 31: suffix = "st";
					break;
					case 2: 
					case 22: suffix = "nd";
					break;
					case 3: 
					case 23: suffix = "rd";
					break;
					}
					if(rList.size() > 0) {
						done = false;
						for(Reservation r : rList) {
							if(i >= r.firstDay && i <= r.lastDay) {
								info += "Aug." + i + suffix + ": " + r.name + "\n";
								done = true;
								break;
							}
						}
						if(done == false) {
							info += "Aug." + i + suffix + ": Available\n";
						}
						
					}
					else {
						info += "Aug." + i + suffix + ": Available\n";
					}
					
				}
			
			info += "-------------------------\n";
		}finally {
			reservationLock.unlock();
		}
		return info;
	}
	
	
	
}
