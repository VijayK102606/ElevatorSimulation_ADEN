package building;
import java.util.ArrayList;
//<<<<<<< HEAD
//import java.util.logging.FileHandler;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//=======
//>>>>>>> 4045674eb18b254afc5dcaa58e5011beb26b0e2e

import passengers.Passengers;


// TODO: Auto-generated Javadoc
/**
 * The Class Elevator.
 *
 * @author Arjun
 * 
 * This class will represent an elevator, and will contain
 * configuration information (capacity, speed, etc) as well
 * as state information - such as stopped, direction, and count
 * of passengers targeting each floor...
 */
public class Elevator {
	
	/**  Elevator State Variables - These are visible publicly. */
	public final static int STOP = 0;
	
	/** The Constant MVTOFLR. */
	public final static int MVTOFLR = 1;
	
	/** The Constant OPENDR. */
	public final static int OPENDR = 2;
	
	/** The Constant OFFLD. */
	public final static int OFFLD = 3;
	
	/** The Constant BOARD. */
	public final static int BOARD = 4;
	
	/** The Constant CLOSEDR. */
	public final static int CLOSEDR = 5;
	
	/** The Constant MV1FLR. */
	public final static int MV1FLR = 6;

	/** Default configuration parameters for the elevator. These should be
	 *  updated in the constructor.
	 */
	private int capacity = 15;				// The number of PEOPLE the elevator can hold
	
	/** The ticks per floor. */
	private int ticksPerFloor = 5;			// The time it takes the elevator to move between floors
	
	/** The ticks door open close. */
	private int ticksDoorOpenClose = 2;  	// The time it takes for doors to go from OPEN <=> CLOSED
	
	/** The pass per tick. */
	private int passPerTick = 3;            // The number of PEOPLE that can enter/exit the elevator per tick
	
	/**  Finite State Machine State Variables. */
	private int currState;		// current state
	
	/** The prev state. */
	private int prevState;      // prior state
	
	/** The prev floor. */
	private int prevFloor;      // prior floor
	
	/** The curr floor. */
	private int currFloor;      // current floor
	
	/** The direction. */
	private int direction;      // direction the Elevator is traveling in.

	/** The time in state. */
	private int timeInState;    // represents the time in a given state
	                            // reset on state entry, used to determine if
	                            // state has completed or if floor has changed
	                            // *not* used in all states 

	/** The door state. */
                            	private int doorState;      // used to model the state of the doors - OPEN, CLOSED
	                            // or moving

	
	/** The passengers. */
                            	private int passengers;  	// the number of people in the elevator
	
	/** The pass by floor. */
	private ArrayList<Passengers>[] passByFloor;  // Passengers to exit on the corresponding floor

	/** The move to floor. */
	private int moveToFloor;	// When exiting the STOP state, this is the floor to move to without
	                            // stopping.
	
	/** The board delay. */
                            	private int boardDelay = 0;
	
	/** The off load delay. */
	private int offLoadDelay = 0;
	
	/** The num boarded. */
	private int numBoarded = 0;
	
	/** The num offloaded. */
	private int numOffloaded = 0;
	
	/** The skipped. */
	private Passengers skipped = null;
	
	


	/** The post move to floor dir. */
                            	private int postMoveToFloorDir; // This is the direction that the elevator will travel AFTER reaching
	                                // the moveToFloor in MVTOFLR state.

	/**
                                	 * Instantiates a new elevator.
                                	 *
                                	 * @param numFloors the num floors
                                	 * @param capacity the capacity
                                	 * @param floorTicks the floor ticks
                                	 * @param doorTicks the door ticks
                                	 * @param passPerTick the pass per tick
                                	 * 
                                	 * reviewed by vijay.
                                	 */
                                	@SuppressWarnings("unchecked")
	public Elevator(int numFloors,int capacity, int floorTicks, int doorTicks, int passPerTick) {		
		this.prevState = STOP;
		this.currState = STOP;
		this.timeInState = 0;
		this.currFloor = 0;
		passByFloor = new ArrayList[numFloors];
		
		for (int i = 0; i < numFloors; i++) {
			passByFloor[i] = new ArrayList<Passengers>(); 
		}

		//TODO: Finish this constructor, adding configuration initialiation and
		//      initialization of any other private fields, etc.
		this.capacity = capacity;
		this.passPerTick = passPerTick;
		this.ticksDoorOpenClose = doorTicks;
		this.ticksPerFloor = floorTicks;
		this.direction = 0;
		this.doorState = 0;
	}

	
	//TODO: Add Getter/Setters and any methods that you deem are required. Examples 
	//      include:
	//      1) moving the elevator
	//      2) closing the doors
	//      3) opening the doors
	//      and so on...
    
    /**
	 * executes neccessary actions for OPENDR state.
	 * 
	 * reviewed by vijay.
	 */
    protected void OPENDR() {
    	prevFloor = currFloor;
    	doorState++;
    }
    
    /**
     * executes neccessary actions for OPENDR state.
     * 
     * reviewed by vijay.
     */
    protected void CLOSEDR() {
    	doorState--;
    }
    
    /**
     * Executes actions for STOP state.
     * 
     * reviewed by vijay.
     */
    protected void STOP() {
    	timeInState++;
    }
    
    /**
     * Moves the elevator.
     * 
     * reviewed by vijay.
     */
    protected void moveElevator() {
    	
    	timeInState++;
    	prevFloor = currFloor;
    	if((timeInState % ticksPerFloor) == 0) {
    		if(direction == 1) {
    			currFloor++;
    		}
    		else {
    			currFloor--;
    		}
    	}
    }
    
    /**
     * Checks if the elevator is between floors.
     *
     * @return whether or not the elevator is between floors
     * 
     * reviewed by vijay.
     */
    protected boolean isBetweenFloors() {
    	if(timeInState % ticksPerFloor != 0) {
    		return true;
    	}
    	return false;
    }
    
    /**
     * If passengers have to exit at a certain floor.
     *
     * @param floor the floor
     * @return whether or not there's passengers to exit
     * 
     * reviewed by vijay.
     */
    protected boolean passengersToExit(int floor) {
    	if(passByFloor[floor].isEmpty()) {
    		return false;
    	}
    	return true;
    }
    
    /**
     * checks if the elevator is empty.
     *
     * @return whether or not the elevator is empty
     * 
     * reviewed by vijay.
     */
    protected boolean isEmpty() {
    	if(passengers == 0) {
    		return true;
    	}
    	return false;
    }
	/**
	 * Gets the capacity.
	 *
	 * @return the capacity
	 * 
	 * reviewed by vijay.
	 */
	int getCapacity() {
		return capacity;
	}

	/**
	 * Gets the ticks per floor.
	 *
	 * @return the ticks per floor
	 * 
	 * reviewed by vijay.
	 */
	int getTicksPerFloor() {
		return ticksPerFloor;
	}

	/**
	 * Gets the ticks door open close.
	 *
	 * @return the ticks door open close
	 * 
	 * reviewed by vijay.
	 */
	int getTicksDoorOpenClose() {
		return ticksDoorOpenClose;
	}

	/**
	 * Gets the pass per tick.
	 *
	 * @return the pass per tick
	 * 
	 * reviewed by vijay.
	 */
	int getPassPerTick() {
		return passPerTick;
	}

	/**
	 * Gets the curr state.
	 *
	 * @return the curr state
	 * 
	 * reviewed by vijay.
	 */
	int getCurrState() {
		return currState;
	}

	/**
	 * Gets the prev state.
	 *
	 * @return the prev state
	 * 
	 * reviewed by vijay.
	 */
	int getPrevState() {
		return prevState;
	}

	/**
	 * Gets the prev floor.
	 *
	 * @return the prev floor
	 * 
	 * reviewed by vijay.
	 */
	int getPrevFloor() {
		return prevFloor;
	}

	/**
	 * Gets the curr floor.
	 *
	 * @return the curr floor
	 * 
	 * reviewed by vijay.
	 */
	int getCurrFloor() {
		return currFloor;
	}

	/**
	 * Update curr state.
	 *
	 * @param nextState the next state
	 * 
	 * reviewed by vijay.
	 */
	protected void updateCurrState(int nextState) {
		prevState = currState;
		currState = nextState;
		if(prevState != currState) {
			timeInState = 0;
			boardDelay = 0;
			offLoadDelay = 0;
			numBoarded = 0;
			numOffloaded = 0;
		}
	}
	
	/**
	 * gets direction.
	 *
	 * @return direction
	 * 
	 * reviewed by vijay.
	 */
	protected int getDirection() {
		return direction;
	}

	/**
	 * set direction.
	 *
	 * @param direction the new direction
	 * 
	 * reviewed by vijay.
	 */
	protected void setDirection(int direction) {
		this.direction = direction;
	}

	/**
	 * gets the current state.
	 *
	 * @param currState the new currState
	 * 
	 * reviewed by vijay.
	 */
	protected void setCurrState(int currState) {
		this.currState = currState;
	}

	/**
	 * Gets moveToFloor.
	 *
	 * @return moveToFloor
	 * 
	 * reviewed by vijay.
	 */
	protected int getMoveToFloor() {
		return moveToFloor;
	}

	/**
	 * sets moveToFloor.
	 *
	 * @param moveToFloor the new value of moveToFloor
	 * 
	 * reviewed by vijay.
	 */
	protected void setMoveToFloor(int moveToFloor) {
		this.moveToFloor = moveToFloor;
	}

	/**
	 * gets postMoveToFloorDir.
	 *
	 * @return postMoveToFloorDir
	 * 
	 * reviewed by vijay.
	 */
	protected int getPostMoveToFloorDir() {
		return postMoveToFloorDir;
	}

	/**
	 * sets postMoveToFloorDir.
	 *
	 * @param postMoveToFloorDir the new postMoveToFloorDir
	 * 
	 * reviewed by vijay.
	 */
	protected void setPostMoveToFloorDir(int postMoveToFloorDir) {
		this.postMoveToFloorDir = postMoveToFloorDir;
	}

	

	/**
	 * Checks if the door is open.
	 *
	 * @return whether or not the door is open
	 * 
	 * reviewed by vijay.
	 */
	protected boolean isDoorOpen() {
		if(doorState == ticksDoorOpenClose) {
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if the door is close.
	 *
	 * @return whether or not the door is close
	 * 
	 * reviewed by vijay.
	 */
	protected boolean isDoorClosed() {
		if(doorState == 0) {
			return true;
		}
		return false;
	}

	/**
	 * Boards all a passenger group.
	 *
	 * @param p the p
	 * @return if the there's enough space for passengers
	 * 
	 * reviewed by vijay.
	 */
	protected boolean board(Passengers p) {
		
		//timeInState++;
		if(passengers + p.getNumPass() <= capacity) {
			passByFloor[p.getDestFloor()].add(p);
			passengers += p.getNumPass();
			numBoarded += p.getNumPass();
			boardDelay = (numBoarded + passPerTick-1)/passPerTick;
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if board is finished.
	 *
	 * @return whether or not board is finished
	 * 
	 * reviewed by vijay.
	 */
	protected boolean isBoardFinished() {
		timeInState++;
		if(timeInState >= boardDelay) {
			return true;
		}
		return false;
	}
	
	/**
	 * Offloads passengers.
	 *
	 * @param floor the floor to offload them on
	 * 
	 * reviewed by vijay.
	 */
	protected void offLoad(int floor) {
		//timeInState++;
		while(passByFloor[floor].size() > 0) {
			int numPass = passByFloor[floor].get(0).getNumPass();
			passengers -= numPass;
			numOffloaded += numPass;
			offLoadDelay = (numOffloaded + passPerTick-1)/passPerTick;
			passByFloor[floor].remove(0);
		}
	}
	
	/**
	 * Checks if offload is finished.
	 *
	 * @return whether or not offload is finished
	 * 
	 * reviewed by vijay.
	 */
	protected boolean isOffloadFinished() {
		timeInState++;
		if(timeInState == offLoadDelay) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Checks if the elevator is full.
	 *
	 * @return a boolean telling whether the elevator is full
	 * 
	 * reviewed by vijay.
	 */
	protected boolean isFull() {
		if(passengers >= capacity) {
			return true;
		}
		return false;
	}
	
	/**
	 * gets number of passengers.
	 *
	 * @return passengers
	 * 
	 * reviewed by vijay.
	 */
	protected int getNumPass() {
		return passengers;
	}

	/**
	 * Returns the passengers to get off on current floor.
	 *
	 * @return Arraylist containing these passengers
	 * 
	 * reviewed by vijay.
	 */
	protected ArrayList<Passengers> getPassengersList() {
		return passByFloor[currFloor];
		
	}
	
	/**
	 * Checks if passengers have to get off in a range.
	 *
	 * @param start the start of the range
	 * @param end the end of the range
	 * @return Whether passengers have to get off
	 * 
	 * reviewed by vijay.
	 */
	protected boolean passGetOff(int start, int end) {
		for(int i = start;i<end;i++) {
			if(!passByFloor[i].isEmpty()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the elevator has changed floors.
	 *
	 * @return whether or not it's changed floors
	 * 
	 * reviewed by vijay.
	 */
	protected boolean changedFloors() {
		if(prevFloor != currFloor) {
			return true;
		}
		return false;
	}
	
	/**
	 * gets Skipped.
	 *
	 * @return skipped
	 * 
	 * reviewed by vijay.
	 */
	public Passengers getSkipped() {
		return skipped;
	}

	/**
	 * sets Skipped.
	 *
	 * @param skipped the new value of skipped
	 * 
	 * reviewed by vijay.
	 */
	public void setSkipped(Passengers skipped) {
		this.skipped = skipped;
	}

	/**
	 * Gets the time in state.
	 *
	 * @return timeInState
	 * 
	 * reviewed by vijay.
	 */
	public int getTimeInState() {
		return timeInState;
	}






	
	
}