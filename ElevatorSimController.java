import java.io.BufferedReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;


import building.Building;
import genericqueue.GenericQueue;
import myfileio.MyFileIO;
import passengers.Passengers;

// TODO: Auto-generated Javadoc
/**
 * @author Arjun
 * The Class ElevatorSimController.
 */
// TODO: Auto-generated Javadoc
public class ElevatorSimController {
	
	/**  Constant to specify the configuration file for the simulation. */
	private static final String SIM_CONFIG = "ElevatorSimConfig.csv";
	
	/**  Constant to make the Passenger queue contents visible after initialization. */
	private boolean PASSQ_DEBUG=false;
	
	/** The gui. */
	private ElevatorSimulation gui;
	
	/** The building. */
	private Building building;
	
	/** The fio. */
	private MyFileIO fio;

	/** The num floors. */
	private final int NUM_FLOORS;
	
	/** The num floors. */
	private int numFloors;
	
	/** The capacity. */
	private int capacity;
	
	/** The floor ticks. */
	private int floorTicks;
	
	/** The door ticks. */
	private int doorTicks;
	
	/** The pass per tick. */
	private int passPerTick;
	
	/** The testfile. */
	private String testfile;
	
	/** The logfile. */
	private String logfile;
	
	/** The step cnt. */
	private int stepCnt = 0;
	
	/** The end sim. */
	private boolean endSim = false;
	
	/** passQ holds the time-ordered queue of Passengers, initialized at the start 
	 *  of the simulation. At the end of the simulation, the queue will be empty.
	 */
	private GenericQueue<Passengers> passQ;

	/**  The size of the queue to store Passengers at the start of the simulation. */
	private final int PASSENGERS_QSIZE = 1000;	
	
	/**
	 * Instantiates a new elevator sim controller. 
	 * Reads the configuration file to configure the building and
	 * the elevator characteristics and also select the test
	 * to run. Reads the passenger data for the test to run to
	 * initialize the passenger queue in building...
	 *
	 * @param gui the gui
	 * 
	 * reviewed by vijay.
	 */
	public ElevatorSimController(ElevatorSimulation gui) {
		this.gui = gui;
		fio = new MyFileIO();
		Passengers.resetStaticID();
		// IMPORTANT: DO NOT CHANGE THE NEXT LINE!!! Update the config file itself
		// (ElevatorSimConfig.csv) to change the configuration or test being run.
		configSimulation(SIM_CONFIG);
		NUM_FLOORS = numFloors;
		logfile = testfile.replaceAll(".csv", ".log");
		building = new Building(NUM_FLOORS,logfile);
		passQ = new GenericQueue<>(PASSENGERS_QSIZE);
		//TODO: YOU still need to configure the elevator in the building here....
		initializePassengerData(testfile);
		building.configElevators(capacity, floorTicks, doorTicks, passPerTick);
		
	}
	
	//TODO: Write methods to update the GUI display
	//      Needs to cover the Elevator state, Elevator passengers
	//      and queues for each floor, as well as the current time
	
	/**
	 * Config simulation. Reads the filename, and parses the
	 * parameters.
	 *
	 * @param filename the filename
	 * 
	 * reviewed by vijay.
	 */
	private void configSimulation(String filename) {
		File configFile = fio.getFileHandle(filename);
		try ( BufferedReader br = fio.openBufferedReader(configFile)) {
			String line;
			while ((line = br.readLine())!= null) {
				parseElevatorConfigData(line);
			}
			fio.closeFile(br);
		} catch (IOException e) { 
			System.err.println("Error in reading file: "+filename);
			e.printStackTrace();
		}
	}
	
	/**
	 * Parses the elevator simulation config file to configure the simulation:
	 * number of floors and elevators, the actual test file to run, and the
	 * elevator characteristics.
	 *
	 * @param line the line
	 * @throws IOException Signals that an I/O exception has occurred.
	 * 
	 * reviewed by vijay.
	 */
	private void parseElevatorConfigData(String line) throws IOException {
		String[] values = line.split(",");
		if (values[0].equals("numFloors")) {
			numFloors = Integer.parseInt(values[1]);
		} else if (values[0].equals("passCSV")) {
			testfile = values[1];
		} else if (values[0].equals("capacity")) {
			capacity = Integer.parseInt(values[1]);
		} else if (values[0].equals("floorTicks")) {
			floorTicks = Integer.parseInt(values[1]);
		} else if (values[0].equals("doorTicks")) {
			doorTicks = Integer.parseInt(values[1]);
		} else if (values[0].equals("passPerTick")) {
			passPerTick = Integer.parseInt(values[1]);
		}
	}
	
	/**
	 * Initialize passenger data. Reads the supplied filename,
	 * and for each passenger group, identifies the pertinent information
	 * and adds it to the passengers queue in Building...
	 *
	 * @param filename the filename
	 * 
	 * reviewed by vijay.
	 */
	private void initializePassengerData(String filename) {
		boolean firstLine = true;
		File passInput = fio.getFileHandle(filename);
		try (BufferedReader br = fio.openBufferedReader(passInput)) {
			String line;
			while ((line = br.readLine())!= null) {
				if (firstLine) {
					firstLine = false;
					continue;
				}
				parsePassengerData(line);
			}
			fio.closeFile(br);
		} catch (IOException e) { 
			System.err.println("Error in reading file: "+filename);
			e.printStackTrace();
		}
		if (PASSQ_DEBUG) dumpPassQ();
	}	
	
	/**
	 * Parses the line of passenger data into tokens, and 
	 * passes those values to the building to be added to the
	 * passenger queue.
	 *
	 * @param line the line of passenger input data
	 * 
	 * reviewed by vijay.
	 */
	private void parsePassengerData(String line) {
		int time=0, numPass=0,fromFloor=0, toFloor=0;
		boolean polite = true;
		int wait = 1000;
		String[] values = line.split(",");
		for (int i = 0; i < values.length; i++) {
			switch (i) {
				case 0 : time      = Integer.parseInt(values[i]); break;
				case 1 : numPass   = Integer.parseInt(values[i]); break;
				case 2 : fromFloor   = Integer.parseInt(values[i]); break;
				case 3 : toFloor  = Integer.parseInt(values[i]); break;
				case 5 : wait      = Integer.parseInt(values[i]); break;
				case 4 : polite = "TRUE".equalsIgnoreCase(values[i]); break;
			}
		}
		passQ.add(new Passengers(time,numPass,fromFloor,toFloor,polite,wait));	
	}
	
	/**
	 * Gets the number of floors in the building
	 *
	 * @return the num floors
	 * 
	 * reviewed by vijay.
	 */
	public int getNumFloors() {
		return NUM_FLOORS;
	}
	
	/**
	 * Gets the test name.
	 *
	 * @return the test name
	 * 
	 * reviewed by vijay.
	 */
	public String getTestName() {
		return (testfile.replaceAll(".csv", ""));
	}

	/**
	 * Enable logging. A pass-through from the GUI to building
	 * 
	 * reviewed by vijay.
	 */
	public void enableLogging() {
		building.enableLogging();
	}
	
	// TODO: Write any other helper methods that you may need to access data from the building...
	
	
 	/**
	 * Step sim. See the comments below for the functionality you
	 * must implement......
	 * 
	 * reviewed by vijay.
	 */
	public void stepSim() {
 		// DO NOT MOVE THIS - YOU MUST INCREMENT TIME FIRST!
		stepCnt++;
		
		if(!passQ.isEmpty() || !building.isStopped()) { 
			ArrayList<Passengers> boardingPassengers = new ArrayList<Passengers>();
			while(!passQ.isEmpty() && (passQ.peek().getTime() == stepCnt)) {
				boardingPassengers.add(passQ.peek());
				building.addPassengersToFloor(passQ.poll());
			}
			//alert building that everythings been added
			building.updateElevator(stepCnt);
			if(gui != null) {
				gui.updateTicks();
				gui.updateState(); 
				
				if(boardingPassengers.size() > 0) {
					gui.passengerSetup(boardingPassengers);
				}
			}
			//update GUI
		} else {
			//update GUI
			building.processPassengerData();
			building.closeLogs(stepCnt);
			if(gui!= null) {
				gui.endSim();
			}
			
			
			
			
		}
		
		// TODO: Write the rest of this method
		// If simulation is not completed (not all passengers have been processed
		// or elevator is not all in STOP state), then
		// 		1) check passQ for appearance of new passengers at this time
		//         - if there are, add all new passengers to building 
		//         - let building know that all new passengers for this tick have
		//           been added.
		// 		2) update the elevator
		// 		3) update the GUI 
		//  else 
		//    	1) update the GUI
		//		2) close the logs
		//		3) process the passenger results
		//		4) send endSimulation to the GUI to stop ticks.
	}

	/**
	 * Dump passQ contents. Debug hook to view the contents of the passenger queue...
	 * 
	 * reviewed by vijay.
	 */
	public void dumpPassQ() {
		ListIterator<Passengers> passengers = passQ.getListIterator();
		if (passengers != null) {
			System.out.println("Passengers Queue:");
			while (passengers.hasNext()) {
				Passengers p = passengers.next();
				System.out.println(p);
			}
		}
	}


	/**
	 * Gets the building. ONLY USED FOR JUNIT TESTING - YOUR GUI SHOULD NOT ACCESS THIS!.
	 *
	 * @return the building
	 * 
	 * reviewed by vijay.
	 */
	Building getBuilding() {
		return building;
	}
	/**
	 * Finds all passengers at the current time
	 * 
	 * @return An arraylist with all the passengers
	 * 
	 * reviewed by vijay.
	 */
	public ArrayList<Passengers> checkPassengers() {
		ArrayList<Passengers> p = new ArrayList<Passengers>();
		while(passQ.peek().getTime() == stepCnt) {
			p.add(passQ.poll());
		}
		return p;
	}
	
	/**
	 * Gets the step count
	 * @return StepCnt
	 * 
	 * reviewed by vijay.
	 */
	public int getStepCnt() {
		return stepCnt;
	}
	
	/**
	 * Gets the elevators state
	 * @return the elevators state as a string
	 * 
	 * reviewed by vijay.
	 */
	public String getElevatorState() {
		return building.getElevatorState();
	}
	/**
	 * Gets floorTicks
	 * @return floorTicks
	 * 
	 * reviewed by vijay.
	 */
	public int getFloorTicks() {
		return building.getFloorTicks();
	}
	
	/**
	 * Gets the time in state.
	 *
	 * @return the time in state
	 * 
	 * reviewed by vijay.
	 */
	public int getTimeInState() {
		return building.getTimeInState();
	}
	
	/**
	 * Gets the num passengers in elevator.
	 *
	 * @return the num passengers in elevator
	 * 
	 * reviewed by vijay.
	 */
	public int getNumPassengersInElevator() {
		return building.getNumPassengers();
	}
	/**
	 * Gets the elevators Direction.
	 *
	 * @return the elevators direction a
	 * 
	 * reviewed by vijay.
	 */
	public String getElevatorDirection() {
		int dir = building.getElevatorDirection();
		
		if(dir == 1) {
			return "UP";
		} else if(dir == -1) {
			return "DOWN";
		}
			return "";
	}
	/**
	 * Gets the elev direction.
	 *
	 * @return the elev direction
	 * 
	 * reviewed by vijay.
	 */
	public int getElevDirection() {
		return building.getElevatorDirection();
	}
	
	/**
	 * Gets the elev state.
	 *
	 * @return the elev state
	 * 
	 * reviewed by vijay.
	 */
	public int getElevState() {
		return building.getElevState();
	}
	
	/**
	 * Gets the curr floor.
	 *
	 * @return the curr floor
	 * 
	 * reviewed by vijay.
	 */
	public int getCurrFloor() {
		return building.getCurrFloor();
	}
	
	/**
	 * Gets the num pass.
	 *
	 * @return the num pass
	 * 
	 * reviewed by vijay.
	 */
	public String getNumPass() {
		return Integer.toString(building.getNumPassengers());
	}

	/**
	 * Gets the passenger data.
	 *
	 * @return the passenger data
	 * 
	 * reviewed by vijay.
	 */
	public ArrayList<Integer>[] getPassengerData(ArrayList<Passengers> p) {
		ArrayList<Integer>[] fullData = new ArrayList[p.size()];
		for(int i =0;i< p.size();i++) {
			ArrayList<Integer> data = new ArrayList<Integer>();
			
			data.add(p.get(i).getOnFloor());
			data.add(p.get(i).getDestFloor());
			data.add(p.get(i).getNumPass());
			fullData[i] = data;
		}
		return fullData;
		
	}
	
	

}
