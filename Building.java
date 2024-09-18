package building;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import myfileio.MyFileIO;
import passengers.Passengers;

// TODO: Auto-generated Javadoc
/**
 * @author Vijay
 * The Class Building.
 */
// TODO: Auto-generated Javadoc
public class Building {
	
	/**  Constants for direction. */
	private final static int UP = 1;
	
	/** The Constant DOWN. */
	private final static int DOWN = -1;
	
	/** The Constant initFlr. */
	private final static int initFlr = 0;
	
	/** The Constant LOGGER. */
	private final static Logger LOGGER = Logger.getLogger(Building.class.getName());
	
	/**  The fh - used by LOGGER to write the log messages to a file. */
	private FileHandler fh;
	
	/**  The fio for writing necessary files for data analysis. */
	private MyFileIO fio;
	
	/**  File that will receive the information for data analysis. */
	private File passDataFile;

	/**  passSuccess holds all Passengers who arrived at their destination floor. */
	private ArrayList<Passengers> passSuccess;
	
	/**  gaveUp holds all Passengers who gave up and did not use the elevator. */
	private ArrayList<Passengers> gaveUp;
	
	/**  The number of floors - must be initialized in constructor. */
	private final int NUM_FLOORS;
	
	/**  The size of the up/down queues on each floor. */
	private final int FLOOR_QSIZE = 10;	
	
	/** The floors. */
	public Floor[] floors;
	
	/** The elevator. */
	private Elevator elevator;
	
	/**  The Call Manager - it tracks calls for the elevator, analyzes them to answer questions and prioritize calls. */
	private CallManager callMgr;
	
	// Add any fields that you think you might need here...

	/**
	 * Instantiates a new building.
	 *
	 * @param numFloors the num floors
	 * @param logfile the logfile
	 * 
	 * Reviewed by Arjun
	 */
	public Building(int numFloors, String logfile) {
		NUM_FLOORS = numFloors;
		passSuccess = new ArrayList<Passengers>();
		gaveUp = new ArrayList<Passengers>();
		initializeBuildingLogger(logfile);
		
		// passDataFile is where you will write all the results for those passengers who successfully
		// arrived at their destination and those who gave up...
		writeSuccessPass(logfile);
		
		
		// create the floors, call manager and the elevator arrays
		// note that YOU will need to create and config each specific elevator...
		floors = new Floor[NUM_FLOORS];
		for (int i = 0; i < NUM_FLOORS; i++) {
			floors[i]= new Floor(FLOOR_QSIZE); 	
		}
		callMgr = new CallManager(floors,NUM_FLOORS);
		//TODO: if you defined new fields, make sure to initialize them here
		
	}
	
	/**
	 * Write success pass.
	 *
	 * @param logFile the log file
	 * 
	 * Reviewed by Arjun
	 */
	protected void writeSuccessPass(String logFile) {
		fio = new MyFileIO();
		passDataFile = fio.getFileHandle(logFile.replaceAll(".log","PassData.csv"));
		BufferedWriter br = fio.openBufferedWriter(passDataFile);

		try {
			for(int i = 0; i< passSuccess.size(); i++) {
				String copy = passSuccess.get(i).toString() + ";";
				br.write(copy);
				br.newLine();
			}
			for(int i = 0; i < gaveUp.size(); i++) {
				String copy = gaveUp.get(i).toString() + ";";
				br.write(copy);
				br.newLine();
			}
				fio.closeFile(br);
		} catch(Exception e) {
			System.out.println("error");
		}
	}

	// TODO: Place all of your code HERE - state methods and helpers...
	
	/**
	 * Adds the passengers to floor.
	 *
	 * @param e the e
	 * 
	 * Reviewed by Arjun
	 */
	public void addPassengersToFloor(Passengers e) {
		int onFlr = e.getOnFloor();
		int dir = e.getDirection();	
		int time = e.getTime();
		int numPass = e.getNumPass();
		int id = e.getId();
		
		floors[onFlr].addPassenger(e);
		callMgr.updateCallStatus();
		logCalls(time, numPass, onFlr, dir, id);
		callMgr.updateCallStatus();
	}
	
	/**
	 * Config elevators.
	 *
	 * @param capacity the capacity
	 * @param floorTicks the floor ticks
	 * @param doorTicks the door ticks
	 * @param ticksPerPass the ticks per pass
	 * 
	 * Reviewed by Arjun
	 */
	public void configElevators(int capacity, int floorTicks, int doorTicks, int ticksPerPass) {
		elevator = new Elevator(NUM_FLOORS, capacity, floorTicks, doorTicks, ticksPerPass);
		logElevatorConfig(capacity, floorTicks, doorTicks, ticksPerPass, elevator.STOP, initFlr);
	}
	
	/**
	 * Initialize building logger. Sets formating, file to log to, and
	 * turns the logger OFF by default
	 *
	 * @param logfile the file to log information to
	 * 
	 * Reviewed by Arjun
	 */
	void initializeBuildingLogger(String logfile) {
		System.setProperty("java.util.logging.SimpleFormatter.format","%4$-7s %5$s%n");
		LOGGER.setLevel(Level.OFF);
		try {
			fh = new FileHandler(logfile);
			LOGGER.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 *  Implement the state methods here. Implements stop state of elevator.
	 *
	 * @param time the time
	 * @return the int
	 * 
	 * Reviewed by Arjun
	 */
	private int currStateStop(int time) {
		elevator.STOP();

		Passengers e = callMgr.prioritizePassengerCalls(elevator.getCurrFloor());
		
		if(e == null) {
			return elevator.STOP;
		}
		
		int currFlr = elevator.getCurrFloor();
		int onFlr = e.getOnFloor();
		int dir = e.getDirection();
		
		if(!callMgr.callPending()) {
			return elevator.STOP;
		} else {
			if(onFlr == currFlr) {
				elevator.setDirection(dir);
				return elevator.OPENDR;
			} else if(onFlr > currFlr) {
				elevator.setDirection(UP);
				elevator.setMoveToFloor(onFlr);
				return elevator.MVTOFLR;
			} else {
				elevator.setDirection(DOWN);
				elevator.setMoveToFloor(onFlr);
				return elevator.MVTOFLR;
			}
		}
	}

	/**
	 * Curr state mv to flr. Implements move to floor state of elevator.
	 *
	 * @param time the time
	 * @return the int
	 * 
	 * Reviewed by Arjun
	 */
	private int currStateMvToFlr(int time) {
		elevator.moveElevator();
		
		int currFlr = elevator.getCurrFloor();
		int targetFlr = elevator.getMoveToFloor();
		
		if(currFlr != targetFlr) {
			return elevator.MVTOFLR;
		} else {
			return elevator.OPENDR;
		}
	}

	/**
	 * Curr state open dr. Implements open door state of elevator.
	 *
	 * @param time the time
	 * @return the int
	 * 
	 * Reviewed by Arjun
	 */
	private int currStateOpenDr(int time) {
		elevator.OPENDR();
		
		int currFlr = elevator.getCurrFloor();
		int dir = elevator.getDirection();
		boolean isDoorOpen = elevator.isDoorOpen();
		boolean passWaitingToGetOffOnCurrFlr = elevator.passengersToExit(currFlr);
		boolean passGetOnInCurrDir = callMgr.passengersGetOnInCurrDir(currFlr, dir);
		boolean passGetOnInOppDir = callMgr.passengersGetOnInCurrDir(currFlr, dir*-1);
		
		if(!isDoorOpen) {
			return elevator.OPENDR;
		} else if(passWaitingToGetOffOnCurrFlr) {
			return elevator.OFFLD;
		} else if(passGetOnInCurrDir) {
			return elevator.BOARD;
		} else if(passGetOnInOppDir) {
			elevator.setDirection(dir*-1);
			return elevator.BOARD;
		}
			return -1;
	}
	
	/**
	 * Curr state off ld. Implements offload state of elevator.
	 *
	 * @param time the time
	 * @return the int
	 * 
	 * Reviewed by Arjun
	 */
	private int currStateOffLd(int time) {
		int currFlr = elevator.getCurrFloor();
		int dir = elevator.getDirection();
		int prevState = elevator.getPrevState();
		passArrivals(currFlr, dir, prevState, time);
		boolean offloadFinished = elevator.isOffloadFinished();
		boolean passGetOnInCurrDir = callMgr.passengersGetOnInCurrDir(currFlr, dir);
		boolean passGetOnInOppDir = callMgr.passengersGetOnInCurrDir(currFlr, dir*-1);
		boolean isEmpty = elevator.isEmpty();
		int numUpCalls = callMgr.numUpCalls(currFlr+1, NUM_FLOORS);
		int numDownCalls = callMgr.numDownCalls(currFlr+1, NUM_FLOORS);
		int numUpCallsBelow = callMgr.numUpCalls(0, currFlr);
		int numDownCallsBelow = callMgr.numDownCalls(0, currFlr);
		if(offloadFinished) {
			if(passGetOnInCurrDir) {
				return elevator.BOARD;
			} else if(dir == UP && isEmpty && numUpCalls == 0 && numDownCalls == 0 && passGetOnInOppDir) {
				elevator.setDirection(DOWN);
				return elevator.BOARD;
			} else if(dir == DOWN && isEmpty && numUpCallsBelow == 0 && numDownCallsBelow == 0 && passGetOnInOppDir) {
				elevator.setDirection(UP);
				return elevator.BOARD;
			} else {
				return elevator.CLOSEDR;
			}
		} else {
			return elevator.OFFLD;
		}
	}
	
	/**
	 * Curr state board.
	 *
	 * @param time the time
	 * @return the int
	 * 
	 * Reviewed by Arjun
	 */
	private int currStateBoard(int time) {
		int dir = elevator.getDirection();
		int currFlr = elevator.getCurrFloor();
		callMgr.updateCallStatus();
		while(!elevator.isFull() && callMgr.passengersToBoard(currFlr, dir)) {
			Passengers up = floors[currFlr].peekUp();
			Passengers down = floors[currFlr].peekDown();
			
			if(dir == UP && up.gaveUp(time)) {
				passGaveUp(up, down, time, currFlr, dir, up.getNumPass());
			} else if(dir == DOWN && down.gaveUp(time)) {
				passGaveUp(up, down, time, currFlr, dir, down.getNumPass());
			} else {
				
				if(dir == UP) {
					if(failedToBoard(dir, up, time, currFlr)) {
						break;
					}		
				} else if(dir == DOWN) {
					if(failedToBoard(dir, down, time, currFlr)) {
						break;
					}
				}
			}
		}
		checkGiveUp(dir, currFlr, time);
		return isBoardingFinished();
	}
	/**
	 * Checks if there are remaining passengers that have to give up
	 * @param dir the direction
	 * @param currFlr the current floor
	 * @param time the time
	 * 
	 * Reviewed by Arjun
	 */
	private void checkGiveUp(int dir, int currFlr, int time) {
		if(elevator.isFull() && callMgr.passengersToBoard(currFlr, dir)) {
			if(dir == UP) {
				failedToBoard(elevator.getDirection(), floors[currFlr].peekUp(), time, currFlr);
			}
			else if(dir == DOWN) {
				failedToBoard(elevator.getDirection(), floors[currFlr].peekDown(), time, currFlr);
			}
		}
	}
	/**
	 * Curr state close dr.
	 *
	 * @param time the time
	 * @return the next State
	 * 
	 * Reviewed by Arjun
	 */
	private int currStateCloseDr(int time) {
		elevator.CLOSEDR();
		int dir = elevator.getDirection();
		int currFlr = elevator.getCurrFloor();
		boolean isEmpty = elevator.isEmpty();
		boolean doorClosed = elevator.isDoorClosed();
		if(dir == UP) {
			Passengers upPass = floors[currFlr].peekUp();
			if(upPass != null && !upPass.isPolite()) {
				upPass.setPolite(true);
				return elevator.OPENDR;
			}
		}
		else if(dir == DOWN) {
			Passengers downPass = floors[currFlr].peekDown();
			if(downPass != null && !downPass.isPolite()) {
				downPass.setPolite(true);
				return elevator.OPENDR;
			}
		}
		if(!doorClosed) {
			return elevator.CLOSEDR;
		}
		if(!isEmpty) {
			return elevator.MV1FLR;
		} else if(isEmpty && !callMgr.callPending()) {
			return elevator.STOP;
		} else {
			return elevatorIsEmpty(currFlr, dir);
		}
	}
	
	/**
	 * Curr state mv1flr.
	 *
	 * @param time the time
	 * @return the next state
	 * 
	 * Reviewed by Arjun
	 */
	private int currStateMv1Flr(int time) {
		elevator.moveElevator();
		int currFlr = elevator.getCurrFloor();
		int dir = elevator.getDirection();

		if(elevator.changedFloors()) {
			if(elevator.passengersToExit(currFlr) || 
					callMgr.passengersGetOnInCurrDir(currFlr, dir)) {
				return elevator.OPENDR;
			} else if(elevator.isEmpty()) {
				if(dir == UP && callMgr.numUpCalls(currFlr + 1, NUM_FLOORS) == 0 
							&& callMgr.numDownCalls(currFlr + 1, NUM_FLOORS) == 0  
							&& callMgr.passengersGetOnInCurrDir(currFlr, dir*-1)) {
					elevator.setDirection(dir*-1);
					return elevator.OPENDR;
				} else if(dir == DOWN && callMgr.numUpCalls(0, currFlr) == 0 
							&& callMgr.numDownCalls(0, currFlr) == 0  
							&& callMgr.passengersGetOnInCurrDir(currFlr, dir*-1)) {
					elevator.setDirection(dir*-1);
					return elevator.OPENDR;
				} else {
					return elevator.MV1FLR;
				}	
			} else {
				return elevator.MV1FLR;
			}
		} else {
			return elevator.MV1FLR;
		}
	}
	
	/**
	 * Pass arrivals.
	 *
	 * @param currFlr the curr flr
	 * @param dir the dir
	 * @param prevState the prev state
	 * @param time the time
	 * 
	 * Reviewed by Arjun
	 */
	private void passArrivals(int currFlr, int dir, int prevState, int time) {
		if(prevState != elevator.OFFLD) {
			ArrayList<Passengers> passArrivals = elevator.getPassengersList();

			for(int i = 0; i < passArrivals.size(); i++) {
				Passengers e = passArrivals.get(i);
				int destFlr = e.getDestFloor();
				if(destFlr == currFlr) {
					e.setArrivalTime(time);
					logArrival(time, e.getNumPass(), currFlr, e.getId());
					passSuccess.add(e);
				}			
			}
			elevator.offLoad(currFlr);
		}
	}
	
	/**
	 * Checks if is boarding finished.
	 *
	 * @return the int
	 * 
	 * Reviewed by Arjun
	 */
	private int isBoardingFinished() {
		if(elevator.isBoardFinished()) {
			elevator.setSkipped(null);
			return elevator.CLOSEDR;
		} else {
			return elevator.BOARD;
		}
	}
	
	/**
	 * Failed to board.
	 *
	 * @param dir the dir
	 * @param e the e
	 * @param time the time
	 * @param currFlr the curr flr
	 * @return true, if successful
	 * 
	 * Reviewed by Arjun
	 */
	private boolean failedToBoard(int dir, Passengers e, int time, int currFlr) {
		if(elevator.board(e)) {
			e.setBoardTime(time);
			boardPassengers(time, e);
			return false;
		} else {
			e.setPolite(true);
			passSkip(time, e, currFlr, dir);
			return true;
		}				
	}
	/**
	 * Pass skip.
	 *
	 * @param time the time
	 * @param currFlr the curr flr
	 * @param dir the dir
	 * @param e the e
	 * 
	 * Reviewed by Arjun
	 */
	private void passSkip(int time, Passengers e, int currFlr, int dir) {	
		if(dir == UP) {
			if(e != elevator.getSkipped()) {
				logSkip(time, e.getNumPass(), currFlr, dir, e.getId());
				elevator.setSkipped(e);
			}
		} else if(dir == DOWN) {
			if(e != elevator.getSkipped()) {
				logSkip(time, e.getNumPass(), currFlr, dir, e.getId());
				elevator.setSkipped(e);
			}
		}
	}
	
	/**
	 * Pass gave up.
	 *
	 * @param up the up
	 * @param down the down
	 * @param time the time
	 * @param currFlr the curr flr
	 * @param dir the dir
	 * @param numPass the num pass
	 * 
	 * Reviewed by Arjun
	 */
	private void passGaveUp(Passengers up, Passengers down, int time, int currFlr, int dir, int numPass) {
		if(dir == UP && up.gaveUp(time)) {
			floors[currFlr].pollUp();
			callMgr.updateCallStatus();
			gaveUp.add(up);
			logGiveUp(time, numPass, currFlr, dir, up.getId());
		} else if(dir == DOWN && down.gaveUp(time)) {
			floors[currFlr].pollDown();
			callMgr.updateCallStatus();
			gaveUp.add(down);
			logGiveUp(time, numPass, currFlr, dir, down.getId());
		}
	}
	
	/**
	 * Board passengers.
	 *
	 * @param time the time
	 * @param currFlr the curr flr
	 * @param dir the dir
	 * @param e the e
	 * 
	 * Reviewed by Arjun
	 */
	private void boardPassengers(int time, Passengers e) {
		int currFlr = elevator.getCurrFloor();
		int dir = elevator.getDirection();
		
		if(dir == UP) {	
				//passSuccess.add(e);
				floors[currFlr].pollUp();						
				callMgr.updateCallStatus();
				logBoard(time, e.getNumPass(), currFlr, dir, e.getId());	
		} else if(dir == DOWN) {
				//passSuccess.add(e);
				floors[currFlr].pollDown();
				callMgr.updateCallStatus();
				logBoard(time, e.getNumPass(), currFlr, dir, e.getId());
		}
	}
	
	/**
	 * Elevator is empty.
	 *
	 * @param currFlr the curr flr
	 * @param dir the dir
	 * @return the int
	 * 
	 * Reviewed by Arjun
	 */
	private int elevatorIsEmpty(int currFlr, int dir) {
		int numUpCalls = callMgr.numUpCalls(currFlr+1, NUM_FLOORS);
		int numDownCalls = callMgr.numDownCalls(currFlr+1, NUM_FLOORS);
		int numUpCallsBelow = callMgr.numUpCalls(0, currFlr);
		int numDownCallsBelow = callMgr.numDownCalls(0, currFlr);
		boolean passGetOff = elevator.passGetOff(currFlr +1, NUM_FLOORS);
		boolean passGetOffBelow = elevator.passGetOff(0, currFlr);
		boolean upCallPending = callMgr.upCallPendingOnCurrFloor(currFlr);
		boolean downCallPending = callMgr.downCallPendingOnCurrFloor(currFlr);
		boolean elevChangeDir = callMgr.changeDirection(currFlr, dir);
		if(dir == UP) {
			if(numUpCalls == 0 && numDownCalls == 0 && upCallPending) {
				return elevator.OPENDR;
			} else if(passGetOff || numUpCalls > 0 || numDownCalls > 0) {
				elevator.setMoveToFloor(currFlr + dir);
				return elevator.MV1FLR;
			}
		} else if(dir == DOWN) {
			if(numUpCallsBelow == 0 && numDownCallsBelow == 0 && downCallPending) {
				return elevator.OPENDR;
			} else if(passGetOffBelow || numUpCallsBelow > 0 || numDownCallsBelow > 0) {
				elevator.setMoveToFloor(currFlr + dir);
				return elevator.MV1FLR;
			}
		} 
		if(elevChangeDir) {
			return changeElevDir(dir, currFlr, upCallPending, downCallPending);
		}
		return elevator.OPENDR;
	}
	
	/**
	 * Change elev dir.
	 *
	 * @param dir the dir
	 * @param currFlr the curr flr
	 * @param upCallPending the up call pending
	 * @param downCallPending the down call pending
	 * @return the int
	 * 
	 * Reviewed by Arjun
	 */
	private int changeElevDir(int dir, int currFlr, boolean upCallPending, boolean downCallPending) {
		elevator.setDirection(dir*-1);
		
		if(dir*-1 == UP && upCallPending) {
			return elevator.OPENDR;
		} else if(dir*-1 == DOWN && downCallPending) {
			return elevator.OPENDR;
		}
		elevator.setMoveToFloor(currFlr + elevator.getDirection());
		return elevator.MV1FLR;
	}
	
	/**
	 * Checks whether the elevator is stopped.
	 *
	 * @return Whether or not the elevator is stopped
	 * 
	 * Reviewed by Arjun
	 */
	public boolean isStopped() {
		int prevState = elevator.getPrevState();
		int currState = elevator.getCurrState();
		
		boolean callPending = callMgr.callPending();
		
		if(prevState == 0 && currState == 0 && !callPending) {
			return true;
		}
			return false;
	}
	
	/**
	 * Updates calls.
	 * 
	 * Reviewed by Arjun
	 */
	protected void updateCalls () {
		callMgr.updateCallStatus();
	}
	/**
	 * Gets the elevators state
	 * @return the elevators current state as a string
	 * 
	 * Reviewed by Arjun
	 */
	public String getElevatorState() {
		return printState(elevator.getCurrState());
	}
	
	/**
	 * Gets the elev state.
	 *
	 * @return the elev state
	 * 
	 * Reviewed by Arjun
	 */
	public int getElevState() {
		return elevator.getCurrState();
	}
	/**
	 * Gets the elevators direction
	 * @return the directino of the elevator
	 * 
	 * Reviewed by Arjun
	 */
	public int getElevatorDirection() {
		return elevator.getDirection();
	}
	
	/**
	 * Gets the curr floor.
	 *
	 * @return the curr floor
	 * 
	 * Reviewed by Arjun
	 */
	public int getCurrFloor() {
		return elevator.getCurrFloor();
	}
	
	/**
	 * Elevator state or floor changed.
	 *
	 * @return true, if successful
	 * 
	 * Reviewed by Arjun
	 */
	protected boolean elevatorStateOrFloorChanged() {
		int prevState = elevator.getPrevState();
		int currState = elevator.getCurrState();
		int prevFloor = elevator.getPrevFloor();
		int currFloor = elevator.getCurrFloor();
		
		if(prevState != currState ||  prevFloor != currFloor) {
			return true;
		}
		return false;
	}
	
	/**
	 * Update elevator - this is called AFTER time has been incremented.
	 * -  Logs any state changes, if the have occurred,
	 * -  Calls appropriate method based upon currState to perform
	 *    any actions and calculate next state...
	 *
	 * @param time the time
	 * 
	 * Reviewed by Arjun
	 */
	public void updateElevator(int time) {
		if(elevatorStateOrFloorChanged()) {
		logElevatorStateOrFloorChanged(time, elevator.getPrevState(),
				elevator.getCurrState(), elevator.getPrevFloor(), elevator.getCurrFloor());
		}

		switch (elevator.getCurrState()) {
		case Elevator.STOP: elevator.updateCurrState(currStateStop(time)); break;
		case Elevator.MVTOFLR: elevator.updateCurrState(currStateMvToFlr(time)); break;
		case Elevator.OPENDR: elevator.updateCurrState(currStateOpenDr(time)); break;
		case Elevator.OFFLD: elevator.updateCurrState(currStateOffLd(time)); break;
		case Elevator.BOARD: elevator.updateCurrState(currStateBoard(time)); break;
		case Elevator.CLOSEDR: elevator.updateCurrState(currStateCloseDr(time)); break;
		case Elevator.MV1FLR: elevator.updateCurrState(currStateMv1Flr(time)); break;
		}

	}

	/**
	 * Process passenger data. Do NOT change this - it simply dumps the 
	 * collected passenger data for successful arrivals and give ups. These are
	 * assumed to be ArrayLists...
	 * 
	 * Reviewed by Arjun
	 */
	public void processPassengerData() {
		
		try {
			BufferedWriter out = fio.openBufferedWriter(passDataFile);
			out.write("ID,Number,From,To,WaitToBoard,TotalTime\n");
			for (Passengers p : passSuccess) {
				String str = p.getId()+","+p.getNumPass()+","+(p.getOnFloor()+1)+","+(p.getDestFloor()+1)+","+
				             (p.getBoardTime() - p.getTime())+","+(p.getTimeArrived() - p.getTime())+"\n";
				out.write(str);
			}
			for (Passengers p : gaveUp) {
				String str = p.getId()+","+p.getNumPass()+","+(p.getOnFloor()+1)+","+(p.getDestFloor()+1)+","+
				             p.getWaitTime()+",-1\n";
				out.write(str);
			}
			fio.closeFile(out);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Enable logging. Prints the initial configuration message.
	 * For testing, logging must be enabled BEFORE the run starts.
	 * 
	 * Reviewed by Arjun
	 */
	public void enableLogging() {
		LOGGER.setLevel(Level.INFO);
			logElevatorConfig(elevator.getCapacity(),elevator.getTicksPerFloor(), elevator.getTicksDoorOpenClose(), 
					          elevator.getPassPerTick(), elevator.getCurrState(),elevator.getCurrFloor());
		
	}
	
	/**
	 * Close logs, and pause the timeline in the GUI.
	 *
	 * @param time the time
	 * 
	 * Reviewed by Arjun
	 */
	public void closeLogs(int time) {
		if (LOGGER.getLevel() == Level.INFO) {
			logEndSimulation(time);
			fh.flush();
			fh.close();
		}
	}
	
	/**
	 * Prints the state.
	 *
	 * @param state the state
	 * @return the string
	 * 
	 * Reviewed by Arjun
	 */
	private String printState(int state) {
		String str = "";
		
		switch (state) {
			case Elevator.STOP: 		str =  "STOP   "; break;
			case Elevator.MVTOFLR: 		str =  "MVTOFLR"; break;
			case Elevator.OPENDR:   	str =  "OPENDR "; break;
			case Elevator.CLOSEDR:		str =  "CLOSEDR"; break;
			case Elevator.BOARD:		str =  "BOARD  "; break;
			case Elevator.OFFLD:		str =  "OFFLD  "; break;
			case Elevator.MV1FLR:		str =  "MV1FLR "; break;
			default:					str =  "UNDEF  "; break;
		}
		return(str);
	}
	
	/**
	 * Log elevator config.
	 *
	 * @param capacity the capacity
	 * @param ticksPerFloor the ticks per floor
	 * @param ticksDoorOpenClose the ticks door open close
	 * @param passPerTick the pass per tick
	 * @param state the state
	 * @param floor the floor
	 * 
	 * Reviewed by Arjun
	 */
	private void logElevatorConfig(int capacity, int ticksPerFloor, int ticksDoorOpenClose, 
			                       int passPerTick, int state, int floor) {
		LOGGER.info("CONFIG:   Capacity="+capacity+"   Ticks-Floor="+ticksPerFloor+"   Ticks-Door="+ticksDoorOpenClose+
				    "   Ticks-Passengers="+passPerTick+"   CurrState=" + (printState(state))+"   CurrFloor="+(floor+1));
	}
		
	/**
	 * Log elevator state changed.
	 *
	 * @param time the time
	 * @param prevState the prev state
	 * @param currState the curr state
	 * @param prevFloor the prev floor
	 * @param currFloor the curr floor
	 * 
	 * Reviewed by Arjun
	 */
	private void logElevatorStateOrFloorChanged(int time, int prevState, int currState, int prevFloor, int currFloor) {
		LOGGER.info("Time="+time+"   Prev State: " + printState(prevState) + "   Curr State: "+printState(currState)
		            +"   PrevFloor: "+(prevFloor+1) + "   CurrFloor: " + (currFloor+1));
	}
	
	/**
	 * Log arrival.
	 *
	 * @param time the time
	 * @param numPass the num pass
	 * @param floor the floor
	 * @param id the id
	 * 
	 * Reviewed by Arjun
	 */
	private void logArrival(int time, int numPass, int floor,int id) {
		LOGGER.info("Time="+time+"   Arrived="+numPass+" Floor="+ (floor+1)
		            +" passID=" + id);						
	}
	
	/**
	 * Log calls.
	 *
	 * @param time the time
	 * @param numPass the num pass
	 * @param floor the floor
	 * @param dir the dir
	 * @param id the id
	 * 
	 * Reviewed by Arjun
	 */
	private void logCalls(int time, int numPass, int floor, int dir, int id) {
		LOGGER.info("Time="+time+"   Called="+numPass+" Floor="+ (floor +1)
			 	    +" Dir="+((dir>0)?"Up":"Down")+"   passID=" + id);
	}
	
	/**
	 * Log give up.
	 *
	 * @param time the time
	 * @param numPass the num pass
	 * @param floor the floor
	 * @param dir the dir
	 * @param id the id
	 * 
	 * Reviewed by Arjun
	 */
	private void logGiveUp(int time, int numPass, int floor, int dir, int id) {
		LOGGER.info("Time="+time+"   GaveUp="+numPass+" Floor="+ (floor+1) 
				    +" Dir="+((dir>0)?"Up":"Down")+"   passID=" + id);				
	}

	/**
	 * Log skip.
	 *
	 * @param time the time
	 * @param numPass the num pass
	 * @param floor the floor
	 * @param dir the dir
	 * @param id the id
	 * 
	 * Reviewed by Arjun
	 */
	private void logSkip(int time, int numPass, int floor, int dir, int id) {
		LOGGER.info("Time="+time+"   Skip="+numPass+" Floor="+ (floor+1) 
			   	    +" Dir="+((dir>0)?"Up":"Down")+"   passID=" + id);				
	}
	
	/**
	 * Log board.
	 *
	 * @param time the time
	 * @param numPass the num pass
	 * @param floor the floor
	 * @param dir the dir
	 * @param id the id
	 * 
	 * Reviewed by Arjun
	 */
	private void logBoard(int time, int numPass, int floor, int dir, int id) {
		LOGGER.info("Time="+time+"   Board="+numPass+" Floor="+ (floor+1) 
				    +" Dir="+((dir>0)?"Up":"Down")+"   passID=" + id);				
	}
	
	/**
	 * Log end simulation.
	 *
	 * @param time the time
	 * 
	 * Reviewed by Arjun
	 */
	private void logEndSimulation(int time) {
		LOGGER.info("Time="+time+"   Detected End of Simulation");
	}
	/**
	 * Gets ticks per floor
	 * @return floorTicks
	 * 
	 * Reviewed by Arjun
	 */
	public int getFloorTicks() {
		return elevator.getTicksPerFloor();
	}

	/**
	 * Gets the time in state.
	 *
	 * @return the time in state
	 * 
	 * Reviewed by Arjun
	 */
	public int getTimeInState() {
		return elevator.getTimeInState();
	}

	/**
	 * Gets the num passengers.
	 *
	 * @return the num passengers
	 * 
	 * Reviewed by Arjun
	 */
	public int getNumPassengers() {
		return elevator.getNumPass();
	}


	
	
}