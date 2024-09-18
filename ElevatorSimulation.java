
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import building.Elevator;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
//import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;
import passengers.Passengers;


// TODO: Auto-generated Javadoc
/**
 * @author Vijay
 * The Class ElevatorSimulation.
 */
public class ElevatorSimulation extends Application {
	
	/**  Instantiate the GUI fields. */
	private ElevatorSimController controller;
	
	/** The num floors. */
	private final int NUM_FLOORS;
	
	/** The curr floor. */
	private int currFloor;
	
	/** The passengers. */
	private int passengers;
	
	/** The time. */
	private int time;
	
	/** The num ticks. */
	private int numTicks;
	
	/** The Constant startX. */
	private final static int startX = 0;
	
	/** The Constant endX. */
	private final static int endX = 400;
	
	/** The Constant elevRightBorder. */
	private final static int elevRightBorder = 500;
	
	/** The Constant endY. */
	private final static int endY = 599;
	
	/** The Constant width. */
	private final static int width = 600;
	
	/** The Constant height. */
	private final static int height = 600;
	
	/** The Constant startY. */
	private final static int startY = 50;
	
	/** The Constant flrSpacing. */
	private final static int flrSpacing = 75;
	
	/** The Constant numFlrLines. */
	private final static int numFlrLines = 5;
	
	/** The Constant elevWidth. */
	private final static int elevWidth = 200;
	
	/** The Constant initElevX. */
	private final static int initElevX = 400;
	
	/** The Constant initElevY. */
	private final static int initElevY = 425;
	
	/** The Constant labelWidth. */
	private final static int labelWidth = 80;
	
	/** The Constant labelHeight. */
	private final static int labelHeight = 25;
	
	/** The Constant buttonWidth. */
	private final static int buttonWidth = 100;
	
	/** The Constant buttonHeight. */
	private final static int buttonHeight = 100;
	
	/** The Constant displayWidth. */
	private final static int displayWidth = 300;
	
	/** The Constant displayHeight. */
	private final static int displayHeight = 100;
	
	/** The Constant passX. */
	private final static int passX = 25;
	
	/** The Constant floors. */
	private final static int floors = 6;
	
	/** The t. */
	private Timeline t;
	
	/**  you MUST use millisPerTick as the duration for your timeline. */
	private static int millisPerTick = 250;

	/**  Local copies of the states for tracking purposes. */
	private final int STOP = Elevator.STOP;
	
	/** The mvtoflr. */
	private final int MVTOFLR = Elevator.MVTOFLR;
	
	/** The opendr. */
	private final int OPENDR = Elevator.OPENDR;
	
	/** The offld. */
	private final int OFFLD = Elevator.OFFLD;
	
	/** The board. */
	private final int BOARD = Elevator.BOARD;
	
	/** The closedr. */
	private final int CLOSEDR = Elevator.CLOSEDR;
	
	/** The mv1flr. */
	private final int MV1FLR = Elevator.MV1FLR;
	
	/** The pane. */
	private BorderPane pane;
	
	/** The curr state label. */
	private Label currStateLabel;
	
	/** The curr time label. */
	private Label currTimeLabel;
	
	/** The curr dir label. */
	private Label currDirLabel;
	
	/** The curr num pass label. */
	private Label currNumPassLabel;
	
	/** The curr state. */
	private TextField currState;
	
	/** The curr time. */
	private TextField currTime;
	
	/** The curr dir. */
	private TextField currDir;
	
	/** The curr num pass. */
	private TextField currNumPass;
	
	/** The run. */
	private Button run;
	
	/** The step. */
	private Button step;
	
	/** The log. */
	private Button log;
	
	/** The button display. */
	private TextField buttonDisplay;
	
	/** The elevator. */
	private Rectangle elevator;
	
	/** The curr X. */
	private int currX;
	
	/** The curr Y. */
	private int currY;
	
	/** The flr layout. */
	private Line[] flrLayout;
	
	/** The pass groups. */
	private Node[][] passGroups;
	
	/** The Constant passGroupRadius. */
	private final static int passGroupRadius = 15;
	
	/** The num pass on flr. */
	private int [] numPassOnFlr;
	
	/** The Constant UP. */
	private final static int UP = 1;
	
	/** The Constant DOWN. */
	private final static int DOWN = -1;
	
	/** The Constant arrivalX. */
	private final static int arrivalX = 100;
	
	/** The Constant flr6. */
	private final static int flr6 = 5;
	
	/** The Constant flr5. */
	private final static int flr5 = 4;
	
	/** The Constant flr4. */
	private final static int flr4 = 3;
	
	/** The Constant flr3. */
	private final static int flr3 = 2;
	
	/** The Constant flr2. */
	private final static int flr2 = 1;
	
	/** The Constant flr1. */
	private final static int flr1 = 0;
	
	/** The Constant flr6Pass. */
	private final static int flr6Pass = 88;
	
	/** The Constant flr5Pass. */
	private final static int flr5Pass = 163;
	
	/** The Constant flr4Pass. */
	private final static int flr4Pass = 238;
	
	/** The Constant flr3Pass. */
	private final static int flr3Pass = 313;
	
	/** The Constant flr2Pass. */
	private final static int flr2Pass = 388;
	
	/** The Constant flr1Pass. */
	private final static int flr1Pass = 463;
	
	/** The doors. */
	private Line[] doors;
	
	/** The Constant numFlrTicks. */
	private final static int numFlrTicks = 5;
	
	/** The dr 6. */
	private Line dr6;
	
	/** The dr 5. */
	private Line dr5;
	
	/** The dr 4. */
	private Line dr4;
	
	/** The dr 3. */
	private Line dr3;
	
	/** The dr 2. */
	private Line dr2;
	
	/** The dr 1. */
	private Line dr1;
	
	/** The Constant dr5Offset. */
	private final static int  dr5Offset = flrSpacing*2;
	
	/** The Constant dr4Offset. */
	private final static int  dr4Offset = flrSpacing*3;
	
	/** The Constant dr3Offset. */
	private final static int dr3Offset = flrSpacing*4;
	
	/** The Constant dr2OffSet. */
	private final static int dr2Offset = flrSpacing*5;
	
	/** The Constant dr1Offset. */
	private final static int dr1Offset = flrSpacing*6;
	
	/** The Constant numPplIndex. */
	private final static int numPplIndex = 2;
	
	/** The Constant passOffset. */
	private final static int passOffset = 50;
	
	/** The Constant passGroupsLength. */
	private final static int passGroupsLength = 20;
	
	/** The arrivals. */
	private ArrayList<Circle> arrivals;
	
	/** The Constant arrivalIndex. */
	private final static int arrivalIndex = 0;
	
	/** The floor dir. */
	private Polygon[][] floorDir;
	
	/** The Constant initDirX1. */
	private final static double initDirX1 = 370.0;
	
	/** The Constant initDirY1. */
	private final static double initDirY1 = 55.0;
	
	/** The Constant initDirX2. */
	private final static double initDirX2 = 350.0;
	
	/** The Constant initDirY2. */
	private final static double initDirY2 = 80.0;
	
	/** The Constant initDirX3. */
	private final static double initDirX3 = 390.0;
	
	/** The Constant initDirY3. */
	private final static double initDirY3 = 80.0;
	
	/** The Constant flrUpIndex. */
	private final static int flrUpIndex = 0;
	
	/** The Constant flrDownIndex. */
	private final static int flrDownIndex = 1;
	
	/** The Constant numDirs. */
	private final static int numDirs = 2;
	
	private final static double dirYOffset = 55.0;
	
	private final static double dirXOffset = 1.0;
	
	private final static double dirYOffset2 = 5.0;
	


	/**
	 * Instantiates a new elevator simulation.
	 * 
	 * Reviewed by Arjun
	 */
	public ElevatorSimulation() {
		controller = new ElevatorSimController(this);	
		NUM_FLOORS = controller.getNumFloors();
		currFloor = 0;
		arrivals = new ArrayList<Circle>();
	}

	/**
	 * Start.
	 *
	 * @param primaryStage the primary stage
	 * @throws Exception the exception
	 * 
	 * Reviewed by Arjun
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {	
		primaryStage.setTitle("Elevator Simulation - "+ controller.getTestName());
		primaryStage.show();
		
		pane = new BorderPane();	
		Scene scene = new Scene(pane, width, height);
		passGroups = new Circle[floors][passGroupsLength];
		
		upperSetup();	
		lowerSetup();
		floorElevatorSetup();
		doorSetup();
		
		t = new Timeline(new KeyFrame(Duration.millis(millisPerTick), e -> {controller.stepSim(); if (t.getStatus() == Animation.Status.STOPPED) {t.stop();}}));
    	t.setCycleCount(Animation.INDEFINITE);
		
    	
    	primaryStage.setScene(scene);
	}
	
	/**
	 * Update state.
	 * 
	 * Reviewed by Arjun
	 */
	public void updateState() {
		int state = controller.getElevState();
		if(state == MV1FLR || state == MVTOFLR) {
			moveToFloor();
		} else if(state == BOARD) {
			boarding();
		} else if(state == OFFLD) {
			offloading();
		} 
		
		if(state != OFFLD && arrivals.size() > 0) {
			pane.getChildren().remove(arrivals.get(arrivalIndex));
			arrivals.remove(arrivalIndex);
		}
	}
	
	/**
	 * Upper lower setup.
	 * 
	 * Reviewed by Arjun
	 */
	private void upperSetup() {
		currStateLabel = new Label("   State:   ");
		currTimeLabel = new Label("    Time:   ");
		currDirLabel = new Label("   Dir:   ");
		currNumPassLabel = new Label("   NumPass:   ");
		
		currState = new TextField();
		currState.setPrefSize(labelWidth, labelHeight);
		currTime = new TextField();
		currTime.setPrefSize(labelWidth, labelHeight);
		currDir = new TextField();
		currDir.setPrefSize(labelWidth, labelHeight);
		currNumPass = new TextField();
		currNumPass.setPrefSize(labelWidth, labelHeight);
		
		currState.setEditable(false);
		currTime.setEditable(false);
		currDir.setEditable(false);
		currNumPass.setEditable(false);
		
		HBox top = new HBox(currStateLabel, currState, currTimeLabel, currTime, currDirLabel, currDir, currNumPassLabel, currNumPass);
		pane.setTop(top);
	}
	
	/**
	 * Door setup.
	 * 
	 * Reviewed by Arjun
	 */
	private void doorSetup() {
		floorDir = new Polygon[floors][numDirs];
		int mult = 0;
		
		for(int i = 0; i < 6; i++) {
			Polygon up = new Polygon();
			up.getPoints().addAll(new Double[]{initDirX1, initDirY1 + (flrSpacing*mult),
					initDirX2, initDirY2 + (flrSpacing*mult), initDirX3, initDirY3 + (flrSpacing*mult) });
			
			Polygon down = new Polygon();
			down.getPoints().addAll(new Double[]{initDirX1, initDirY1 + dirYOffset + (flrSpacing*mult), initDirX2 + dirXOffset,
					initDirY2 + dirYOffset2 + (flrSpacing*mult), initDirX3, initDirY3 + dirYOffset2 + (flrSpacing*mult) });
			
			floorDir[i][flrUpIndex] = up;
			floorDir[i][flrDownIndex] = down;
			
			pane.getChildren().addAll(floorDir[i][flrUpIndex], floorDir[i][flrDownIndex]);
			
			mult++;
		}
		
		List<Polygon[]> list = Arrays.asList(floorDir);
	    Collections.reverse(list);
		
	
	}
	
	/**
	 * Lower setup.
	 * 
	 * Reviewed by Arjun
	 */
	private void lowerSetup() {
		run = new Button("Run");
		run.setPrefSize(buttonWidth, buttonHeight);
		run.setOnAction(e -> {if (t.getStatus() == Animation.Status.RUNNING) 
		{t.pause(); t.setCycleCount(Animation.INDEFINITE);} else {
		t.setCycleCount(Animation.INDEFINITE); t.play();}});
		
		step = new Button("Step");
		step.setPrefSize(buttonWidth, buttonHeight);
		
		log = new Button("log");
		log.setPrefSize(buttonWidth, buttonHeight);
		log.setOnAction(e -> controller.enableLogging());
		
		buttonDisplay = new TextField();
		buttonDisplay.setPrefSize(displayWidth, displayHeight);
		step.setOnAction(e -> {if (!emptyTextField(buttonDisplay.getText())) {buttonDisplay.setText("enter integer");} else {
			t.stop(); t.setCycleCount(Integer.parseInt(buttonDisplay.getText())); t.play();}});
		
		HBox bottom = new HBox(run, step, log, buttonDisplay);
		pane.setBottom(bottom);
	}
	
	/**
	 * Empty text field.
	 *
	 * @param text the text
	 * @return true, if successful
	 * 
	 * Reviewed by Arjun
	 */
	private boolean emptyTextField(String text) {
		if(text.matches("\\d+")) {
			return true;
		}
			return false;
	}
	
	/**
	 * Floor elevator setup.
	 * 
	 * Reviewed by Arjun
	 */
	private void floorElevatorSetup() {
		flrLayout = new Line[numFlrLines];
		elevator = new Rectangle(elevWidth, flrSpacing);
		currX = initElevX;
		currY = initElevY;
		elevator.setX(currX);
		elevator.setY(currY);
		elevator.setStyle("-fx-fill: white; -fx-stroke: black; -fx-stroke-width: 1;");
		
		int flrMult = 1;
		for(int i = 0; i < flrLayout.length; i++) {
			int flrDist = startY + (flrSpacing*flrMult);
			flrLayout[i] = new Line(startX, flrDist, endX, flrDist);
			flrMult++;
		}
		dr6 = new Line(initElevX, startY, initElevX, startY + flrSpacing);
		dr5 = new Line(initElevX, startY + flrSpacing, initElevX, startY + dr5Offset);
		dr4 = new Line(initElevX, startY + dr5Offset, initElevX, startY + dr4Offset);
		dr3 = new Line(initElevX, startY + dr4Offset, initElevX, startY + dr3Offset);
		dr2 = new Line(initElevX, startY + dr3Offset, initElevX, startY + dr2Offset);
		dr1 = new Line(initElevX, startY + dr2Offset, initElevX, startY + dr1Offset);

		Line elevTop = new Line(startX, startY, endY, startY);
		Line elevBttm = new Line(startX, elevRightBorder, endY, elevRightBorder);
		Line elevLeft = new Line(startX, startY, startX, elevRightBorder);
		Line elevRight = new Line(endY, startY, endY, elevRightBorder);

		pane.getChildren().addAll(elevTop, elevBttm, elevLeft, elevRight, dr6, dr5, dr4, dr3, dr2, dr1, elevator);
		pane.getChildren().addAll(flrLayout);
	}
	
	/**
	 * Move to floor.
	 * 
	 * Reviewed by Arjun
	 */
	private void moveToFloor() {
		int dir = controller.getElevDirection();
			if(dir == UP) {
				pane.getChildren().remove(elevator);
				currY -= (flrSpacing / numFlrTicks);
				elevator.setY(currY);
				pane.getChildren().add(elevator);
			} else if(dir == DOWN) {
				pane.getChildren().remove(elevator);
				currY += (flrSpacing / numFlrTicks);
				elevator.setY(currY);
				pane.getChildren().add(elevator);
			}
	}
	
	
	
	/**
	 * Passenger setup.
	 * 
	 * Reviewed by Arjun
	 */
	public void passengerSetup(ArrayList<Passengers> p) {
		ArrayList<Integer>[] passData = controller.getPassengerData(p);
			
			for(int i = 0; i < passData.length; i++) {
				int currFlr = passData[i].get(0);
				int destFlr = passData[i].get(1);
				
				if(passGroups[currFlr][i] == null) {
					passGroups[currFlr][i] = new Circle(passX + (passOffset*i), getYPos(currFlr), passGroupRadius);
					pane.getChildren().add(passGroups[currFlr][i]);
				}
				
				if(destFlr > currFlr) {
					Polygon c = floorDir[currFlr][flrUpIndex];
					c.setFill(Color.BLUE);
				} else if(destFlr < currFlr) {
					Polygon c = floorDir[currFlr][flrDownIndex];
					c.setFill(Color.BLUE);
				}
			}
	}
	
	
	/**
	 * Boarding.
	 * 
	 * Reviewed by Arjun
	 */
	private void boarding() {
		int currFlr = controller.getCurrFloor();
		int dir = controller.getElevDirection();
		
			for(int i = 0; i < passGroups[0].length; i++) {
				if(passGroups[currFlr][i] != null) {
					pane.getChildren().remove(passGroups[currFlr][i]);
				}
			}
			
			if(dir == UP) {
				Polygon up = floorDir[currFlr][flrUpIndex];
				up.setFill(Color.BLACK);
			} else if(dir == DOWN) {
				Polygon down = floorDir[currFlr][flrDownIndex];
				down.setFill(Color.BLACK);
			}
	}
	
	/**
	 * Offloading.
	 * 
	 * Reviewed by Arjun
	 */
	private void offloading() {
		int currFlr = controller.getCurrFloor();
		int currState = controller.getElevState();
		int yPos = getYPos(currFlr);
		
		if(currState == OFFLD) {
			Circle arrival = new Circle(arrivalX, yPos, passGroupRadius);
			arrival.setFill(Color.BLUE);
			pane.getChildren().add(arrival);
			arrivals.add(arrival);
		}
	}
	
	/**
	 * Gets the y pos.
	 *
	 * @param currFlr the curr flr
	 * @return the y pos
	 * 
	 * Reviewed by Arjun
	 */
	private int getYPos(int currFlr) {
		
		if(currFlr == flr6) {
			return flr6Pass;
		} else if(currFlr == flr5) {
			return flr5Pass;
		} else if(currFlr == flr4) {
			return flr4Pass;
		} else if(currFlr == flr3) {
			return flr3Pass;
		} else if(currFlr == flr2) {
			return flr2Pass;
		} else if(currFlr == flr1) {
			return flr1Pass;
		}
			return -1;
	}
	
	/**
	 * Update ticks.
	 * 
	 * Reviewed by Arjun
	 */
	public void updateTicks() {
		currState.setText("");
		currTime.setText("");
		currDir.setText("");
		
		numTicks = controller.getStepCnt();
		String numTicksDisplay = String.valueOf(numTicks);
		
		currState.setText(controller.getElevatorState());
		currTime.setText(numTicksDisplay);
		currDir.setText(controller.getElevatorDirection());
		currNumPass.setText(controller.getNumPass());
	}
	
	/**
	 * End sim.
	 * 
	 * Reviewed by Arjun
	 */
	public void endSim() {
		t.stop();
	}
	/**
	 * The main method. Allows command line to modulate the speed of the simulation.
	 *
	 * @param args the arguments
	 * 
	 * Reviewed by Arjun
	 */
	public static void main (String[] args) {
		if (args.length>0) {
			for (int i = 0; i < args.length-1; i++) {
				if ("-m".equals(args[i])) {
					try {
						ElevatorSimulation.millisPerTick = Integer.parseInt(args[i+1]);
					} catch (NumberFormatException e) {
						System.out.println("Unable to update millisPerTick to "+args[i+1]);
					}
				}
			}
		}
		Application.launch(args);
	}

}