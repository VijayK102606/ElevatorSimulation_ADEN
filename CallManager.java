package building;
import java.lang.Math;
import passengers.Passengers;

// TODO: Auto-generated Javadoc
/**
 * @author Arjun
 * The Class CallManager. This class models all of the calls on each floor,
 * and then provides methods that allow the building to determine what needs
 * to happen (ie, state transitions).
 */
public class CallManager {
	
	/** The floors. */
	private Floor[] floors;
	
	/** The num floors. */
	private final int NUM_FLOORS;
	
	/** The Constant UP. */
	private final static int UP = 1;
	
	/** The Constant DOWN. */
	private final static int DOWN = -1;
	
	/** The up calls array indicates whether or not there is a up call on each floor. */
	private boolean[] upCalls;
	
	/** The down calls array indicates whether or not there is a down call on each floor. */
	private boolean[] downCalls;
	
	/** The up call pending - true if any up calls exist */
	private boolean upCallPending;
	
	/** The down call pending - true if any down calls exit */
	private boolean downCallPending;
	
	//TODO: Add any additional fields here..
	
	/**
	 * Instantiates a new call manager.
	 *
	 * @param floors the floors
	 * @param numFloors the num floors
	 * 
	 * reviewed by vijay.
	 */
	public CallManager(Floor[] floors, int numFloors) {
		this.floors = floors;
		NUM_FLOORS = numFloors;
		upCalls = new boolean[NUM_FLOORS];
		downCalls = new boolean[NUM_FLOORS];
		upCallPending = false;
		downCallPending = false;
		
		//TODO: Initialize any added fields here
	}
	
	/**
	 * Update call status. This is an optional method that could be used to compute
	 * the values of all up and down call fields statically once per tick (to be
	 * more efficient, could only update when there has been a change to the floor queues -
	 * either passengers being added or being removed. The alternative is to dynamically
	 * recalculate the values of specific fields when needed.
	 * 
	 * reviewed by vijay.
	 */
	void updateCallStatus() {
		//System.out.println(floors[0].peekUp());
		for(int i =0; i< NUM_FLOORS; i++) {
			if(floors[i].isUpEmpty()) {
				upCalls[i] = false;
			}
			else {
				upCalls[i] = true;
			}
			
			if(floors[i].isDownEmpty()) {
				downCalls[i] = false;
			}
			else {
				downCalls[i] = true;
			}
		}
	}

	/**
	 * Prioritize passenger calls from STOP STATE
	 *
	 * @param floor the floor
	 * @return the passengers
	 * 
	 * reviewed by vijay.
	 */
	Passengers prioritizePassengerCalls(int floor) {
		if(!callPending()) {
			return null;
		}
		if(upCalls[floor] == true && downCalls[floor] == false) {
			return floors[floor].peekUp();
		}
		if(upCalls[floor] == false && downCalls[floor] == true) {
			return floors[floor].peekDown();
		}
		if(upCalls[floor] == true && downCalls[floor] == true) {
			if(numDownCalls(0, floor) > numUpCalls(floor+1, NUM_FLOORS) ) {
				return floors[floor].peekDown();
			}
			else {
				return floors[floor].peekUp();
			}
		}
		return noCallonCurrFloor(floor);

		
	}
	/**
	 * Call prioritization when there's no call on the current floor
	 * @param floor, the current floor
	 * @return the passenger to prioritize
	 * 
	 * reviewed by vijay.
	 */
	protected Passengers noCallonCurrFloor(int floor) {
		int lowestUp = findLowestUp();
		int highestDown = findHighestDown();
		if(numUpCalls(0, NUM_FLOORS) > numDownCalls(0,NUM_FLOORS)) {
			return floors[lowestUp].peekUp();
		}
		if(numUpCalls(0, NUM_FLOORS) < numDownCalls(0,NUM_FLOORS)) {
			return floors[highestDown].peekDown();
		}
		if(lowestUp == -1 && highestDown != -1) {
			return floors[highestDown].peekDown();
		}
		if(lowestUp != -1 && lowestUp == -1) {
			return floors[lowestUp].peekUp();
		}
		if(Math.abs(lowestUp - floor) > Math.abs(highestDown - floor)) {
			return floors[highestDown].peekDown();
		}
		return floors[lowestUp].peekUp();
	}
	
	//TODO: Write any additional methods here. Things that you might consider:
	//      1. pending calls - are there any? only up? only down?
	//      2. is there a call on the current floor in the current direction
	//      3. How many up calls are pending? how many down calls are pending? 
	//      4. How many calls are pending in the direction that the elevator is going
	//      5. Should the elevator change direction?
	//
	//      These are an example - you may find you don't need some of these, or you may need more...
	
	/**
	 * Determines if there's a call on any floor
	 * @return if there is a call pending
	 * 
	 * reviewed by vijay.
	 */
	protected boolean callPending() {
		for(int i =0;i<NUM_FLOORS;i++) {
			if(upCalls[i] == true || downCalls[i] == true) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if there's an up call pending on the current floor
	 * @param currFloor, the current floor
	 * @return whether or not there's an up call
	 * 
	 * reviewed by vijay.
	 */
	protected boolean upCallPendingOnCurrFloor(int currFloor) {
		if(upCalls[currFloor] == true) {
			return true;
		}
			return false;
	}
	/**
	 * Checks if there's a down call pending on the current floor
	 * @param currFloor, the current floor
	 * @return whether or not there's a down call
	 * 
	 * reviewed by vijay.
	 */
	protected boolean downCallPendingOnCurrFloor(int currFloor) {
		if(downCalls[currFloor] == true) {
			return true;
		}
			return false;
	}
	/**
	 * Counts the number of up calls within a range
	 * @param startInd the start index
	 * @param endInd the end index
	 * @return the number of calls
	 * 
	 * reviewed by vijay.
	 */
	protected int numUpCalls(int startInd, int endInd) {
		int count = 0;
		for(int i =startInd;i< endInd;i++) {
			if(upCalls[i]) {
				count++;
			}
		}
		return count;
	}
	
	/**
	 * Counts the number of down calls within a range
	 * @param startInd the start index
	 * @param endInd the end index
	 * @return the number of calls
	 * 
	 * reviewed by vijay.
	 */
	protected int numDownCalls(int startInd, int endInd) {
		int count = 0;
		for(int i =startInd;i< endInd;i++) {
			if(downCalls[i]) {
				count++;
			}
		}
		return count;
	}
	/**
	 * Find the lowest up call 
	 * @param floor the floor you're on
	 * @return the floor the call is on
	 * 
	 * reviewed by vijay.
	 */
	protected int findLowestUp() {
		for(int i =0;i<NUM_FLOORS;i++) {
			if(upCalls[i]) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Find the highest down call 
	 * @param floor the floor you're on
	 * @return the floor the call is on
	 * 
	 * reviewed by vijay.
	 */
	protected int findHighestDown() {
		for(int i =NUM_FLOORS-1;i>=0;i--) {
			if(downCalls[i]) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Determines whether the elevator should change direction
	 * @param floor, the floor the elevator is on
	 * @param direction , the current direction
	 * @return boolean that tells whether the elevator should change direction or not
	 * 
	 * reviewed by vijay.
	 */
	protected boolean changeDirection(int floor, int direction) {
		if(direction == UP && numUpCalls(floor, NUM_FLOORS) == 0 && numDownCalls(floor, NUM_FLOORS) == 0
			&& (numUpCalls(0, floor) > 0 || numDownCalls(0, floor) >0)) {
				return true;

		}
		if(direction == DOWN && numUpCalls(0,floor) == 0 && numDownCalls(0, floor) == 0
				&& (numUpCalls(floor, NUM_FLOORS) >0 || numDownCalls(floor, NUM_FLOORS) > 0)) {
			return true;
		}
		return false;
	}
	/**
	 * Checks if there are passengers getting on in the current direction
	 * @param floor the floor they get on
	 * @param direction the current direction 
	 * @return whether there are passengers who fit the requirements or not
	 * 
	 * reviewed by vijay.
	 */
	protected boolean passengersGetOnInCurrDir(int floor, int direction) {
		if(direction == UP) {
			if(upCalls[floor]) {
				return true;
			}
		}
		else {
			if(downCalls[floor]) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Checks if there are passengers to board
	 * @param floor the floor
	 * @param direction the direction
	 * @return whether there's passengers to board
	 * 
	 * reviewed by vijay.
	 */
	protected boolean passengersToBoard(int floor, int direction) {
		if(direction == UP) {
			if(floors[floor].isUpEmpty()) {
				return false;
			}
			return true;
		}
		else {
			if(floors[floor].isDownEmpty()) {
				return false;
			}
			return true;
		}
		
		
	}
	
	
	
}
