package building;
// ListIterater can be used to look at the contents of the floor queues for 
// debug/display purposes...
import java.util.ListIterator;

import genericqueue.GenericQueue;
import passengers.Passengers;

// TODO: Auto-generated Javadoc
/**
 * @author Arjun
 * The Class Floor. This class provides the up/down queues to hold
 * Passengers as they wait for the Elevator.
 */
public class Floor {
	/**  Constant for representing direction. */
	private static final int UP = 1;
	
	/** The Constant DOWN. */
	private static final int DOWN = -1;

	/**  The queues to represent Passengers going UP or DOWN. */	
	private GenericQueue<Passengers> down;
	
	/** The up. */
	private GenericQueue<Passengers> up;

	/**
	 * Instantiates a new floor.
	 *
	 * @param qSize the q size
	 * 
	 * reviewed by vijay.
	 */
	public Floor(int qSize) {
		down = new GenericQueue<Passengers>(qSize);
		up = new GenericQueue<Passengers>(qSize);
	}
	
	// TODO: Write the helper methods needed for this class. 
	// You probably will only be accessing one queue at any
	// given time based upon direction - you could choose to 
	// account for this in your methods.
	
	/**
	 * Queue string. This method provides visibility into the queue
	 * contents as a string. What exactly you would want to visualize 
	 * is up to you
	 *
	 * @param dir determines which queue to look at
	 * @return the string of queue contents
	 * 
	 * reviewed by vijay.
	 */
	String queueString(int dir) {
		String str = "";
		ListIterator<Passengers> list;
		list = (dir == UP) ?up.getListIterator() : down.getListIterator();
		if (list != null) {
			while (list.hasNext()) {
				// choose what you to add to the str here.
				// Example: str += list.next().getNumPass();
				if (list.hasNext()) str += ",";
			}
		}
		return str;	
	}
	
	/**
	 * Checks whether the up queue is empty.
	 *
	 * @return a boolean that tells whether the queue is empty
	 * 
	 * reviewed by vijay.
	 */
	protected boolean isUpEmpty() {
		return up.isEmpty();
	}
	
	/**
	 * Checks whether the down queue is empty.
	 *
	 * @return a boolean that tells whether the queue is empty
	 * 
	 * reviewed by vijay.
	 */
	protected boolean isDownEmpty() {
		return down.isEmpty();
	}
	
	/**
	 * Peeks the up queue.
	 *
	 * @return the passenger at the front of the queue
	 * 
	 * reviewed by vijay.
	 */
	protected Passengers peekUp() {
		return up.peek();
	}
	
	/**
	 * Peeks the down queue.
	 *
	 * @return the passenger at the front of the queue
	 * 
	 * reviewed by vijay.
	 */
	protected Passengers peekDown() {
		return down.peek();
	}
	
	/**
	 * Polls the up queue.
	 *
	 * @return the passenger at the front of the queue
	 * 
	 * reviewed by vijay.
	 */
	protected Passengers pollUp() {
		return up.poll();
	}
	
	/**
	 * Polls the down queue.
	 *
	 * @return the passenger at the front of the queue
	 * 
	 * reviewed by vijay.
	 */
	protected Passengers pollDown() {
		return down.poll();
	}
	
	/**
	 * Adds a passenger to the correct queue.
	 * 
	 * reviewed by vijay.
	 *
	 * @param p the passenger
	 */
	protected void addPassenger(Passengers p) {
		if(p.getDirection() == UP) {
			up.add(p);
		} else {
			down.add(p);
		}
	}
	
	
	
	
}
