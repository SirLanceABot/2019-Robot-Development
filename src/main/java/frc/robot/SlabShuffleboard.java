/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
//import edu.wpi.first.wpilibj.shuffleboard.SimpleWidget;

/**
 * Add your docs here.
 */
public class SlabShuffleboard
{
    public enum StartingLocation
    {
        kNone, kRight, kCenter, kLeft;
    }

    public enum RobotType
    {
        kCompetition, kPractice;
    }

    public enum MotorSpeed
    {
        k25percent(0.25), k50percent(0.50), k75percent(0.75), k100percent(1.00);

        public double value;

        private MotorSpeed(double value)
        {
            this.value = value;
        }
    }

    public enum Recording
    {
        kEntireMatch, kAutoOnly, kTeleopOnly, kDoNotRecord;
    }

    public enum GamePiece
    {
        kNone, kHatchPanel, kCargo;
    }

    public enum Objective
    {
        kNothing, kLeaveHabPlatform, kRocket, kCargoShip;
    }

    public enum RocketLevel
    {
        kNone, kBottom, kMiddle, kTop;
    }

    public enum RocketHatch
    {
        kNone, kFront, kBack;
    }

    public enum CargoShip
    {
        kNone, kFrontLeft, kFrontRight, kSideNear, kSideMiddle, kSideFar;
    }

    public class PregameSetupTabData
    {
        public StartingLocation startingLocation = StartingLocation.kNone;
        public RobotType robotType = RobotType.kCompetition;
        public MotorSpeed motorSpeed = MotorSpeed.k100percent;
        public Recording recording = Recording.kEntireMatch;

        public GamePiece task1GamePiece = GamePiece.kNone;
        public Objective task1Objective = Objective.kNothing;
        public RocketLevel task1RocketLevel = RocketLevel.kNone;
        public RocketHatch task1RocketHatch = RocketHatch.kNone;
        public CargoShip task1CargoShip = CargoShip.kNone;

        public GamePiece task2GamePiece = GamePiece.kNone;
        public Objective task2Objective = Objective.kNothing;
        public RocketLevel task2RocketLevel = RocketLevel.kNone;
        public RocketHatch task2RocketHatch = RocketHatch.kNone;
        public CargoShip task2CargoShip = CargoShip.kNone;

        @Override
        public String toString()
        {
            String str;

            str = String.format("\n\n*****  PREGAME SELECTION  *****\n");
            str += String.format("Starting Location: %s\n", startingLocation);
            str += String.format("Robot Type:   %s\n", robotType);
            str += String.format("Motor Speed:  %s\n", motorSpeed);
            str += String.format("Recording:    %s\n\n", recording);
            str += String.format("***** TASK 1  *****\n");
            str += String.format("Game Piece:   %s\n", task1GamePiece);
            str += String.format("Objective:    %s\n", task1Objective);
            str += String.format("Rocket Level: %s\n", task1RocketLevel);
            str += String.format("Rocket Hatch: %s\n", task1RocketHatch);
            str += String.format("Cargo Ship:   %s\n\n", task1CargoShip);
            str += String.format("***** TASK 2  *****\n");
            str += String.format("Game Piece:   %s\n", task2GamePiece);
            str += String.format("Objective:    %s\n", task2Objective);
            str += String.format("Rocket Level: %s\n", task2RocketLevel);
            str += String.format("Rocket Hatch: %s\n", task2RocketHatch);
            str += String.format("Cargo Ship:   %s\n", task2CargoShip);

            return str;
        }
    }

    public class MotorsAndSensorsTabData
    {
        public String frontLeftMotor = "NA";
        public String frontRightMotor = "NA";
        public String backLeftMotor = "NA";
        public String backRightMotor = "NA";
        public String omniWheel = "NA";
        public String elevator = "NA";
        public String arm = "NA";
        public String wrist = "NA";
        public String cargoIntakeRoller = "NA";
        public String hatchPanelGrabber = "NA";
        public String climber = "NA";
        public String climberPinSolenoid = "NA";
        public String lidar = "NA";
        public String navX = "NA";
    }

    public class ControllersTabData
    {
        // Booleans for Xbox Controller buttons    
        public boolean driverXboxAButton = false;
        public boolean driverXboxBButton = false;
        public boolean driverXboxXButton = false;
        public boolean driverXboxYButton = false;
        public boolean driverXboxRightBumperButton = false;
        public boolean driverXboxLeftBumperButton = false;
        public boolean driverXboxBackButton = false;
        public boolean driverXboxStartButton = false;
        public boolean driverXboxLeftStickButton = false;
        public boolean driverXboxRightStickButton = false;

        // Doubles for all axes on xbox controller
        public double driverXboxLeftXAxis = 0.0;
        public double driverXboxLeftYAxis = 0.0;
        public double driverXboxRightXAxis = 0.0;
        public double driverXboxRightYAxis = 0.0;
        public double driverXboxLeftTriggerAxis = 0.0;
        public double driverXboxRightTriggerAxis = 0.0;
        public double driverXboxDpadAxis = 0.0;

        // Double for the Joystick axis on button board
        public double operatorButtonBoardJoystickXAxis = 0.0;
        public double operatorButtonBoardJoystickYAxis = 0.0;
        // Boolean for the buttons on button board
        public boolean operatorButtonBoardButton1 = false;
        public boolean operatorButtonBoardButton2 = false;
        public boolean operatorButtonBoardButton3 = false;
        public boolean operatorButtonBoardButton4 = false;
        public boolean operatorButtonBoardButton5 = false;
        public boolean operatorButtonBoardButton6 = false;
        public boolean operatorButtonBoardButton7 = false;
        public boolean operatorButtonBoardButton8 = false;
        public boolean operatorButtonBoardButton9 = false;
        public boolean operatorButtonBoardButton10 = false;
    }

    // PREGAME SETUP TAB
    private ShuffleboardTab pregameSetupTab;
    private PregameSetupTabData pregameSetupTabData = new PregameSetupTabData();
    private SendableChooser<StartingLocation> startingLocationComboBox = new SendableChooser<>();
    private SendableChooser<RobotType> robotTypeComboBox = new SendableChooser<>();
    private SendableChooser<MotorSpeed> motorSpeedComboBox = new SendableChooser<>();
    private SendableChooser<Recording> recordingComboBox = new SendableChooser<>();
    private SendableChooser<GamePiece> task1GamePieceComboBox = new SendableChooser<>();
    private SendableChooser<Objective> task1ObjectiveComboBox = new SendableChooser<>();
    private SendableChooser<RocketLevel> task1RocketLevelComboBox = new SendableChooser<>();
    private SendableChooser<RocketHatch> task1RocketHatchComboBox = new SendableChooser<>();
    private SendableChooser<CargoShip> task1CargoShipComboBox = new SendableChooser<>();
    private SendableChooser<GamePiece> task2GamePieceComboBox = new SendableChooser<>();
    private SendableChooser<Objective> task2ObjectiveComboBox = new SendableChooser<>();
    private SendableChooser<RocketLevel> task2RocketLevelComboBox = new SendableChooser<>();
    private SendableChooser<RocketHatch> task2RocketHatchComboBox = new SendableChooser<>();
    private SendableChooser<CargoShip> task2CargoShipComboBox = new SendableChooser<>();
    private SendableChooser<Boolean> sendDataButton = new SendableChooser<>();

    
    // Camera Tab
    private ShuffleboardTab cameraTab;

    private NetworkTableEntry timeEllapsedEntry;
    private NetworkTableEntry matchEntry;
    private NetworkTableEntry startingFieldPositionEntry;

    // MOTORS AND SENSORS TAB
    private ShuffleboardTab motorsAndSensorsTab;
    private MotorsAndSensorsTabData motorsAndSensorsTabData = new MotorsAndSensorsTabData();
    // Network table entries for the Motors and Sensors Tab
    // Network table entries for the 4 drivetrain motors
    private NetworkTableEntry drivetrainFLValuesEntry;
    private NetworkTableEntry drivetrainFRValuesEntry;
    private NetworkTableEntry drivetrainBLValuesEntry;
    private NetworkTableEntry drivetrainBRValuesEntry;
    // Network table entries for the 2 omniwheel servos
    private NetworkTableEntry omniWheelValuesEntry;
    // Network table entry for the 1 arm motor
    private NetworkTableEntry armValuesEntry;
    // Network table entry for the 2 elevator motors
    private NetworkTableEntry elevatorValuesEntry;
    // Network table entry for the wrist pneumatics
    private NetworkTableEntry wristValuesEntry;
    // Network table entry for the 1 cargo intake roller motor
    private NetworkTableEntry cargoIntakeRollerMotorEntry;
    // Network table entry for the hatch panel pneumatics
    private NetworkTableEntry hatchPanelGrabberValuesEntry;
    // Network table entry for the 2 climber motors
    private NetworkTableEntry climberValuesEntry;
    // Network table entry for the 1 climber pin solenoid
    private NetworkTableEntry climberPinSolenoidValuesEntry;
    // Network table entry for the lidar
    private NetworkTableEntry lidarValuesEntry;
    // Network table entry for the NavX
    private NetworkTableEntry navXValuesEntry;


    // Controllers Tab boxes
    private ShuffleboardTab controllersTab;
    private ControllersTabData controllersTabData = new ControllersTabData();
    
    // Button Network Table Entries
    private NetworkTableEntry driverXboxAButtonEntry;
    private NetworkTableEntry driverXboxBButtonEntry;
    private NetworkTableEntry driverXboxXButtonEntry;
    private NetworkTableEntry driverXboxYButtonEntry;
    private NetworkTableEntry driverXboxLeftBumperEntry;
    private NetworkTableEntry driverXboxRightBumperEntry;
    private NetworkTableEntry driverXboxStartButtonEntry;
    private NetworkTableEntry driverXboxBackButtonEntry;
    private NetworkTableEntry driverXboxLeftStickButtonEntry;
    private NetworkTableEntry driverXboxRightStickButtonEntry;

    // Axes Network Table Entries
    private NetworkTableEntry driverXboxLeftXAxisEntry;
    private NetworkTableEntry driverXboxLeftYAxisEntry;
    private NetworkTableEntry driverXboxRightXAxisEntry;
    private NetworkTableEntry driverXboxRightYAxisEntry;
    private NetworkTableEntry driverXboxLeftTriggerAxisEntry;
    private NetworkTableEntry driverXboxRightTriggerAxisEntry;

    // Dpad Network Table Entry
    private NetworkTableEntry driverXboxDpadEntry;
    
    // Network Table Entries for the Operator Button Board
    private NetworkTableEntry operatorButtonBoardJoystickXAxisEntry;
    private NetworkTableEntry operatorButtonBoardJoystickYAxisEntry;
    private NetworkTableEntry operatorButtonBoardButton1Entry;
    private NetworkTableEntry operatorButtonBoardButton2Entry;
    private NetworkTableEntry operatorButtonBoardButton3Entry;
    private NetworkTableEntry operatorButtonBoardButton4Entry;
    private NetworkTableEntry operatorButtonBoardButton5Entry;
    private NetworkTableEntry operatorButtonBoardButton6Entry;
    private NetworkTableEntry operatorButtonBoardButton7Entry;
    private NetworkTableEntry operatorButtonBoardButton8Entry;
    private NetworkTableEntry operatorButtonBoardButton9Entry;
    private NetworkTableEntry operatorButtonBoardButton10Entry;


    private static SlabShuffleboard instance = new SlabShuffleboard();
    private static DriverStation dStation = DriverStation.getInstance();

    private SlabShuffleboard()
    {
        System.out.println(this.getClass().getName() + ": Started Constructing");

        createControllersTab();
        createMotorsAndSensorsTab();
        createCameraTab();
        createPregameSetupTab();

        System.out.println(this.getClass().getName() + ": Started Constructing");
    }

    public static SlabShuffleboard getInstance()
    {
        return instance;
    }

    public void startRecording()
    {
        Shuffleboard.startRecording();
    }

    public void stopRecording()
    {
        Shuffleboard.stopRecording();
    }

    private void createPregameSetupTab()
    {
        pregameSetupTab = Shuffleboard.getTab("Pregame Setup");
        Shuffleboard.selectTab("Pregame Setup");

        // ComboBox for the Starting Location
        startingLocationComboBox.setName("Starting Location");
        startingLocationComboBox.setDefaultOption("None (default)", StartingLocation.kNone);
        startingLocationComboBox.addOption("Right", StartingLocation.kRight);
        startingLocationComboBox.addOption("Center", StartingLocation.kCenter);
        startingLocationComboBox.addOption("Left", StartingLocation.kLeft);
        pregameSetupTab.add(startingLocationComboBox).withWidget(BuiltInWidgets.kComboBoxChooser).withPosition(0, 0)
                .withSize(4, 1);

        // ComboBox for the Robot Type
        robotTypeComboBox.setName("Robot Type");
        robotTypeComboBox.setDefaultOption("Competition (default)", RobotType.kCompetition);
        robotTypeComboBox.addOption("Practice", RobotType.kPractice);
        pregameSetupTab.add(robotTypeComboBox).withWidget(BuiltInWidgets.kComboBoxChooser).withPosition(0, 3)
                .withSize(4, 1);

        // ComboBox for the Motor Speed
        motorSpeedComboBox.setName("Motor Speed");
        motorSpeedComboBox.setDefaultOption("100% (default)", MotorSpeed.k100percent);
        motorSpeedComboBox.addOption("75%", MotorSpeed.k75percent);
        motorSpeedComboBox.addOption("50%", MotorSpeed.k50percent);
        motorSpeedComboBox.addOption("25%", MotorSpeed.k25percent);
        pregameSetupTab.add(motorSpeedComboBox).withWidget(BuiltInWidgets.kComboBoxChooser).withPosition(0, 6)
                .withSize(4, 1);

        // ComboBox for the Recording Option
        recordingComboBox.setName("Recording Option");
        recordingComboBox.setDefaultOption("Entire Match (default)", Recording.kEntireMatch);
        recordingComboBox.addOption("Autonomous Only", Recording.kAutoOnly);
        recordingComboBox.addOption("Teleop Only", Recording.kTeleopOnly);
        recordingComboBox.addOption("Do Not Record", Recording.kDoNotRecord);
        pregameSetupTab.add(recordingComboBox).withWidget(BuiltInWidgets.kComboBoxChooser).withPosition(0, 9)
                .withSize(4, 1);

        // ComboBox for Task 1: Preloaded Game Piece
        task1GamePieceComboBox.setName("Task 1 - Preloaded Game Piece");
        task1GamePieceComboBox.setDefaultOption("None (default)", GamePiece.kNone);
        task1GamePieceComboBox.addOption("Hatch Panel", GamePiece.kHatchPanel);
        task1GamePieceComboBox.addOption("Cargo", GamePiece.kCargo);
        pregameSetupTab.add(task1GamePieceComboBox).withWidget(BuiltInWidgets.kComboBoxChooser).withPosition(5, 0)
                .withSize(8, 1);

        // ComboBox for Task 1: Objective
        task1ObjectiveComboBox.setName("Task 1 - Objective");
        task1ObjectiveComboBox.setDefaultOption("Nothing (default)", Objective.kNothing);
        task1ObjectiveComboBox.addOption("Leave Platform", Objective.kLeaveHabPlatform);
        task1ObjectiveComboBox.addOption("Cargo Ship", Objective.kCargoShip);
        task1ObjectiveComboBox.addOption("Rocket", Objective.kRocket);
        pregameSetupTab.add(task1ObjectiveComboBox).withWidget(BuiltInWidgets.kComboBoxChooser).withPosition(5, 3)
                .withSize(8, 1);

        // ComboBox for Task 1: Rocket Level
        task1RocketLevelComboBox.setName("Task 1 - Rocket Level");
        task1RocketLevelComboBox.setDefaultOption("None (default)", RocketLevel.kNone);
        task1RocketLevelComboBox.addOption("Bottom", RocketLevel.kBottom);
        task1RocketLevelComboBox.addOption("Middle", RocketLevel.kMiddle);
        task1RocketLevelComboBox.addOption("Top", RocketLevel.kTop);
        pregameSetupTab.add(task1RocketLevelComboBox).withWidget(BuiltInWidgets.kComboBoxChooser).withPosition(5, 6)
                .withSize(4, 1);

        // ComboBox for Task 1: Rocket Hatch
        task1RocketHatchComboBox.setName("Task 1 - Rocket Hatch");
        task1RocketHatchComboBox.setDefaultOption("None (default)", RocketHatch.kNone);
        task1RocketHatchComboBox.addOption("Front", RocketHatch.kFront);
        task1RocketHatchComboBox.addOption("Back", RocketHatch.kBack);
        pregameSetupTab.add(task1RocketHatchComboBox).withWidget(BuiltInWidgets.kComboBoxChooser).withPosition(5, 9)
                .withSize(4, 1);

        // ComboBox for Task 1: Cargo Ship
        task1CargoShipComboBox.setName("Task 1 - Cargo Ship");
        task1CargoShipComboBox.setDefaultOption("None (default)", CargoShip.kNone);
        task1CargoShipComboBox.addOption("Front Side, Left Bay", CargoShip.kFrontLeft);
        task1CargoShipComboBox.addOption("Front Side, Right Bay", CargoShip.kFrontRight);
        task1CargoShipComboBox.addOption("Left/Right Side, Near Bay", CargoShip.kSideNear);
        task1CargoShipComboBox.addOption("Left/Right Side, Middle Bay", CargoShip.kSideMiddle);
        task1CargoShipComboBox.addOption("Left/Right Side, Far Bay", CargoShip.kSideFar);
        pregameSetupTab.add(task1CargoShipComboBox).withWidget(BuiltInWidgets.kComboBoxChooser).withPosition(9, 6)
                .withSize(4, 1);

        // ComboBox for Task 2: Game Piece
        task2GamePieceComboBox.setName("Task 2 - Game Piece");
        task2GamePieceComboBox.setDefaultOption("None (default)", GamePiece.kNone);
        task2GamePieceComboBox.addOption("Hatch Panel", GamePiece.kHatchPanel);
        task2GamePieceComboBox.addOption("Cargo", GamePiece.kCargo);
        pregameSetupTab.add(task2GamePieceComboBox).withWidget(BuiltInWidgets.kComboBoxChooser).withPosition(14, 0)
                .withSize(8, 1);

        // ComboBox for Task 2: Objective
        task2ObjectiveComboBox.setName("Task 2 - Objective");
        task2ObjectiveComboBox.setDefaultOption("Nothing (default)", Objective.kNothing);
        task2ObjectiveComboBox.addOption("Leave Platform", Objective.kLeaveHabPlatform);
        task2ObjectiveComboBox.addOption("Cargo Ship", Objective.kCargoShip);
        task2ObjectiveComboBox.addOption("Rocket", Objective.kRocket);
        pregameSetupTab.add(task2ObjectiveComboBox).withWidget(BuiltInWidgets.kComboBoxChooser).withPosition(14, 3)
                .withSize(8, 1);

        // ComboBox for Task 2: Rocket Level
        task2RocketLevelComboBox.setName("Task 2 - Rocket Level");
        task2RocketLevelComboBox.setDefaultOption("None (default)", RocketLevel.kNone);
        task2RocketLevelComboBox.addOption("Bottom", RocketLevel.kBottom);
        task2RocketLevelComboBox.addOption("Middle", RocketLevel.kMiddle);
        task2RocketLevelComboBox.addOption("Top", RocketLevel.kTop);
        pregameSetupTab.add(task2RocketLevelComboBox).withWidget(BuiltInWidgets.kComboBoxChooser).withPosition(14, 6)
                .withSize(4, 1);

        // ComboBox for Task 2: Rocket Hatch
        task2RocketHatchComboBox.setName("Task 2 - Rocket Hatch");
        task2RocketHatchComboBox.setDefaultOption("None (default)", RocketHatch.kNone);
        task2RocketHatchComboBox.addOption("Front", RocketHatch.kFront);
        task2RocketHatchComboBox.addOption("Back", RocketHatch.kBack);
        pregameSetupTab.add(task2RocketHatchComboBox).withWidget(BuiltInWidgets.kComboBoxChooser).withPosition(14, 9)
                .withSize(4, 1);

        // ComboBox for Task 2: Cargo Ship
        task2CargoShipComboBox.setName("Task 2 - Cargo Ship");
        task2CargoShipComboBox.setDefaultOption("None (default)", CargoShip.kNone);
        task2CargoShipComboBox.addOption("Front Side, Left Bay", CargoShip.kFrontLeft);
        task2CargoShipComboBox.addOption("Front Side, Right Bay", CargoShip.kFrontRight);
        task2CargoShipComboBox.addOption("Left/Right Side, Near Bay", CargoShip.kSideNear);
        task2CargoShipComboBox.addOption("Left/Right Side, Middle Bay", CargoShip.kSideMiddle);
        task2CargoShipComboBox.addOption("Left/Right Side, Far Bay", CargoShip.kSideFar);
        pregameSetupTab.add(task2CargoShipComboBox).withWidget(BuiltInWidgets.kComboBoxChooser).withPosition(18, 6)
                .withSize(4, 1);

        // Split Button Chooser
        sendDataButton.setName("Send Data?");
        sendDataButton.setDefaultOption("No", false);
        sendDataButton.addOption("Yes", true);
        pregameSetupTab.add(sendDataButton).withWidget(BuiltInWidgets.kSplitButtonChooser).withPosition(23, 9)
                .withSize(4, 1);
    }

    public PregameSetupTabData getPregameSetupTabData()
    {
        pregameSetupTabData.startingLocation = startingLocationComboBox.getSelected();
        pregameSetupTabData.robotType = robotTypeComboBox.getSelected();
        pregameSetupTabData.motorSpeed = motorSpeedComboBox.getSelected();
        pregameSetupTabData.recording = recordingComboBox.getSelected();

        pregameSetupTabData.task1GamePiece = task1GamePieceComboBox.getSelected();
        pregameSetupTabData.task1Objective = task1ObjectiveComboBox.getSelected();
        pregameSetupTabData.task1RocketLevel = task1RocketLevelComboBox.getSelected();
        pregameSetupTabData.task1RocketHatch = task1RocketHatchComboBox.getSelected();
        pregameSetupTabData.task1CargoShip = task1CargoShipComboBox.getSelected();

        pregameSetupTabData.task2GamePiece = task2GamePieceComboBox.getSelected();
        pregameSetupTabData.task2Objective = task2ObjectiveComboBox.getSelected();
        pregameSetupTabData.task2RocketLevel = task2RocketLevelComboBox.getSelected();
        pregameSetupTabData.task2RocketHatch = task2RocketHatchComboBox.getSelected();
        pregameSetupTabData.task2CargoShip = task2CargoShipComboBox.getSelected();

        return pregameSetupTabData;
    }

    public boolean getSendData()
    {
        return sendDataButton.getSelected();
    }

    public void createCameraTab()
    {
        // Create the Camera tab
        cameraTab = Shuffleboard.getTab("Camera");

        // Camera widgets created on Rasp Pi

        timeEllapsedEntry = cameraTab.add("Time Ellapsed", "NA")
                .withWidget(BuiltInWidgets.kTextView).withPosition(9, 0).withSize(8, 2).getEntry();
                
        matchEntry = cameraTab.add("Match", "NA")
                .withWidget(BuiltInWidgets.kTextView).withPosition(9, 2).withSize(8, 2).getEntry();
        
        startingFieldPositionEntry = cameraTab.add("Starting Field Position", "NA")
                .withWidget(BuiltInWidgets.kTextView).withPosition(9, 4).withSize(8, 2).getEntry();
    }

    public void updateCameraTab()
    {
        String timeEllapsedString;
        String matchString;
        String startingFieldPositionString;

        timeEllapsedString = "Time Ellapsed: " + dStation.getMatchTime();
        matchString = "Match Type:\n" + dStation.getMatchType() + "\nMatch #:\n" + dStation.getMatchNumber();
        startingFieldPositionString = "Alliance: " + dStation + ", Location: " + dStation.getLocation();

        timeEllapsedEntry.setString(timeEllapsedString);
        matchEntry.setString(matchString);
        startingFieldPositionEntry.setString(startingFieldPositionString);
    }

    public void createMotorsAndSensorsTab()
    {
        // Create the Motors and Sensors tab
        motorsAndSensorsTab = Shuffleboard.getTab("Motors and Sensors");

        // TextView for the "Drivetrain FL Motor Values"
        drivetrainFLValuesEntry = motorsAndSensorsTab.add("Drivetrain Front Left", "NA")
                .withWidget(BuiltInWidgets.kTextView).withPosition(0, 0).withSize(6, 1).getEntry();

        // TextView for the "Drivetrain FR Motor Values"
        drivetrainFRValuesEntry = motorsAndSensorsTab.add("Drivetrain Front Right", "NA")
                .withWidget(BuiltInWidgets.kTextView).withPosition(0, 2).withSize(6, 1).getEntry();

        // TextView for the "Drivetrain BL Motor Values"
        drivetrainBLValuesEntry = motorsAndSensorsTab.add("Drivetrain Back Left", "NA")
                .withWidget(BuiltInWidgets.kTextView).withPosition(0, 4).withSize(6, 1).getEntry();

        // TextView for the "Drivetrain BR Motor Values"
        drivetrainBRValuesEntry = motorsAndSensorsTab.add("Drivetrain Back Right", "NA")
                .withWidget(BuiltInWidgets.kTextView).withPosition(0, 6).withSize(6, 1).getEntry();

        // TextView for both of the "Omni Wheels"
        omniWheelValuesEntry = motorsAndSensorsTab.add("Omni Wheels", "NA").withWidget(BuiltInWidgets.kTextView)
                .withPosition(0, 8).withSize(6, 1).getEntry();

        // TextView for the "Elevator"
        elevatorValuesEntry = motorsAndSensorsTab.add("Elevator", "NA").withWidget(BuiltInWidgets.kTextView)
                .withPosition(6, 0).withSize(6, 1).getEntry();

        // TextView for the "Arm"
        armValuesEntry = motorsAndSensorsTab.add("Arm", "NA").withWidget(BuiltInWidgets.kTextView).withPosition(6, 2)
                .withSize(6, 1).getEntry();

        // TextView for the "Wrist"
        wristValuesEntry = motorsAndSensorsTab.add("Wrist", "NA").withWidget(BuiltInWidgets.kTextView).withPosition(6, 4)
                .withSize(6, 1).getEntry();

        // TextView for the "Cargo Intake Roller"
        cargoIntakeRollerMotorEntry = motorsAndSensorsTab.add("Cargo Intake Roller", "NA")
                .withWidget(BuiltInWidgets.kTextView).withPosition(6, 6).withSize(6, 1).getEntry();

        // TextView for the "Hatch Panel Grabber"
        hatchPanelGrabberValuesEntry = motorsAndSensorsTab.add("Hatch Panel Grabber", "NA")
                .withWidget(BuiltInWidgets.kTextView).withPosition(6, 8).withSize(6, 1).getEntry();

        // TextView for the "Climber"
        climberValuesEntry = motorsAndSensorsTab.add("Climber", "NA").withWidget(BuiltInWidgets.kTextView)
                .withPosition(12, 0).withSize(6, 1).getEntry();

        // TextView for the "Climber Pin Solenoid"
        climberPinSolenoidValuesEntry = motorsAndSensorsTab.add("Climber Pin Solenoid", "NA").withWidget(BuiltInWidgets.kTextView)
                .withPosition(12, 2).withSize(6, 1).getEntry();

        // TextView for the "Lidar"
        lidarValuesEntry = motorsAndSensorsTab.add("Lidar", "NA").withWidget(BuiltInWidgets.kTextView)
                .withPosition(18, 0).withSize(6, 1).getEntry();

        // TextView for the "NavX"
        navXValuesEntry = motorsAndSensorsTab.add("NavX", "NA").withWidget(BuiltInWidgets.kTextView).withPosition(18, 2)
                .withSize(6, 1).getEntry();
    } // Finished creating the motors and sensors tab

    // Call up Yash's getString() function
    public MotorsAndSensorsTabData getMotorsAndSensorsTabData()
    {
        motorsAndSensorsTabData.frontLeftMotor = drivetrainFLValuesEntry.getString("NA");
        motorsAndSensorsTabData.frontRightMotor = drivetrainFRValuesEntry.getString("NA");
        motorsAndSensorsTabData.backLeftMotor = drivetrainBLValuesEntry.getString("NA");
        motorsAndSensorsTabData.backRightMotor = drivetrainBRValuesEntry.getString("NA");
        motorsAndSensorsTabData.omniWheel = omniWheelValuesEntry.getString("NA");
        motorsAndSensorsTabData.elevator = elevatorValuesEntry.getString("NA");
        motorsAndSensorsTabData.arm = armValuesEntry.getString("NA");
        motorsAndSensorsTabData.wrist = wristValuesEntry.getString("NA");
        motorsAndSensorsTabData.cargoIntakeRoller = cargoIntakeRollerMotorEntry.getString("NA");
        motorsAndSensorsTabData.hatchPanelGrabber = hatchPanelGrabberValuesEntry.getString("NA");
        motorsAndSensorsTabData.climber = climberValuesEntry.getString("NA");
        motorsAndSensorsTabData.climberPinSolenoid = climberPinSolenoidValuesEntry.getString("NA");
        motorsAndSensorsTabData.lidar = lidarValuesEntry.getString("NA");
        motorsAndSensorsTabData.navX = navXValuesEntry.getString("NA");

        return motorsAndSensorsTabData;
    } // Finished getting the motorsAndSensorsTabData

    // Update the Motors and Sensors tab data with the set strings
    public void updateMotorsAndSensorsTabData(MotorsAndSensorsTabData motorsAndSensorsTabData)
    {
        drivetrainFLValuesEntry.setString(motorsAndSensorsTabData.frontLeftMotor);
        drivetrainFRValuesEntry.setString(motorsAndSensorsTabData.frontRightMotor);
        drivetrainBLValuesEntry.setString(motorsAndSensorsTabData.backLeftMotor);
        drivetrainBRValuesEntry.setString(motorsAndSensorsTabData.backRightMotor);
        omniWheelValuesEntry.setString(motorsAndSensorsTabData.omniWheel);
        elevatorValuesEntry.setString(motorsAndSensorsTabData.elevator);
        armValuesEntry.setString(motorsAndSensorsTabData.arm);
        wristValuesEntry.setString(motorsAndSensorsTabData.wrist);
        cargoIntakeRollerMotorEntry.setString(motorsAndSensorsTabData.cargoIntakeRoller);
        hatchPanelGrabberValuesEntry.setString(motorsAndSensorsTabData.hatchPanelGrabber);
        climberValuesEntry.setString(motorsAndSensorsTabData.climber);
        climberPinSolenoidValuesEntry.setString(motorsAndSensorsTabData.climberPinSolenoid);
        lidarValuesEntry.setString(motorsAndSensorsTabData.lidar);
        navXValuesEntry.setString(motorsAndSensorsTabData.navX);
    } // Finished updating the Motors and Sensors tab

    private void createControllersTab()
    {
        controllersTab = Shuffleboard.getTab("Controllers");
        Shuffleboard.selectTab("Controllers");

        // All of the Text View Boxes and Boolean Boxes for the Xbox Controller. -Mikey and Annika
        
        // Xbox A button
        Map<String, Object> mapAButtonColor = new HashMap<String,Object>();
        mapAButtonColor.put("Color when true", "#FF4500");        
        mapAButtonColor.put("Color when false", "#00FF00");
        driverXboxAButtonEntry = controllersTab.add("A Button", false).withWidget(BuiltInWidgets.kBooleanBox)
            .withPosition(11, 8).withSize(3, 1).withProperties(mapAButtonColor).getEntry();
        
        // Xbox B button
        Map<String, Object> mapBButtonColor = new HashMap<String,Object>();
        mapBButtonColor.put("Color when true", "#321ACE");        
        mapBButtonColor.put("Color when false", "#FF0000");    
        driverXboxBButtonEntry = controllersTab.add("B Button", false).withWidget(BuiltInWidgets.kBooleanBox)
            .withPosition(13, 6).withSize(3, 1).withProperties(mapBButtonColor).getEntry();
        
        // Xbox X button
        Map<String, Object> mapXButtonColor = new HashMap<String,Object>();
        mapXButtonColor.put("Color when true", "#DC143C");        
        mapXButtonColor.put("Color when false", "#321ACE");    
        driverXboxXButtonEntry = controllersTab.add("X Button", false).withWidget(BuiltInWidgets.kBooleanBox)
            .withPosition(9, 6).withSize(3, 1).withProperties(mapXButtonColor).getEntry();
        
        // Xbox Y button
        Map<String, Object> mapYButtonColor = new HashMap<String,Object>();
        mapYButtonColor.put("Color when true", "#8B008B");        
        mapYButtonColor.put("Color when false", "#FFD700");    
        driverXboxYButtonEntry = controllersTab.add("Y Button", false).withWidget(BuiltInWidgets.kBooleanBox)
            .withPosition(11, 4).withSize(3, 1).withProperties(mapYButtonColor).getEntry();
            
        // Xbox Left X Axis 
        driverXboxLeftXAxisEntry = controllersTab.add("Left X Axis", 0.0).withWidget(BuiltInWidgets.kTextView)
            .withPosition(0, 8).withSize(3, 1).getEntry();

        // Xbox Left Y Axis 
        driverXboxLeftYAxisEntry = controllersTab.add("Left Y Axis", 0.0).withWidget(BuiltInWidgets.kTextView)
            .withPosition(0, 10).withSize(3, 1).getEntry();

        // Xbox Right X Axis 
        driverXboxRightXAxisEntry = controllersTab.add("Right X Axis", 0.0).withWidget(BuiltInWidgets.kTextView)
            .withPosition(7, 12).withSize(3, 1).getEntry();

        // Xbox Right Y Axis
        driverXboxRightYAxisEntry = controllersTab.add("Right Y Axis", 0.0).withWidget(BuiltInWidgets.kTextView)
            .withPosition(7, 14).withSize(3, 1).getEntry();
        
        // Xbox Left Bumper button
        Map<String, Object> mapLeftBumperColor = new HashMap<String,Object>();
        mapLeftBumperColor.put("Color when true", "#00BFFF");        
        mapLeftBumperColor.put("Color when false", "#000000");
        driverXboxLeftBumperEntry = controllersTab.add("Left Bumper", false).withWidget(BuiltInWidgets.kBooleanBox)
            .withPosition(0, 2).withSize(3, 1).withProperties(mapLeftBumperColor).getEntry();
            
        // Xbox Right Bumper button
        Map<String, Object> mapRightBumperColor = new HashMap<String,Object>();
            mapRightBumperColor.put("Color when true", "#00BFFF");        
            mapRightBumperColor.put("Color when false", "#000000");    
        driverXboxRightBumperEntry = controllersTab.add("Right Bumper", false).withWidget(BuiltInWidgets.kBooleanBox)
            .withPosition(4, 2).withSize(3, 1).withProperties(mapRightBumperColor).getEntry();
        
        // Xbox Left Trigger Axis
        driverXboxLeftTriggerAxisEntry = controllersTab.add("Left Trigger Axis", 0).withWidget(BuiltInWidgets.kTextView)
            .withPosition(0, 0).withSize(3, 1).getEntry();
        
        // Xbox Right Trigger Axis
        driverXboxRightTriggerAxisEntry = controllersTab.add("Right Trigger Axis", 0).withWidget(BuiltInWidgets.kTextView)
            .withPosition(4, 0).withSize(3, 1).getEntry();
        
        // Xbox Start button
        Map<String, Object> mapStartButtonColor = new HashMap<String,Object>();
        mapStartButtonColor.put("Color when true", "#3CB371");        
        mapStartButtonColor.put("Color when false", "#000000");    
        driverXboxStartButtonEntry = controllersTab.add("Start Button", false).withWidget(BuiltInWidgets.kBooleanBox)
            .withPosition(6, 6).withSize(3, 1).withProperties(mapStartButtonColor).getEntry();
        
        // Xbox Back button
        Map<String, Object> mapBackButtonColor = new HashMap<String,Object>();
        mapBackButtonColor.put("Color when true", "#3CB371");        
        mapBackButtonColor.put("Color when false", "#000000"); 
        driverXboxBackButtonEntry = controllersTab.add("Back Button", false).withWidget(BuiltInWidgets.kBooleanBox)
            .withPosition(3, 6).withSize(3, 1).withProperties(mapBackButtonColor).getEntry();
        
        // Xbox Left Stick button
        Map<String, Object> mapLeftStickColor = new HashMap<String,Object>();
        mapLeftStickColor.put("Color when true", "#FF1493");        
        mapLeftStickColor.put("Color when false", "#40E0D0");    
        driverXboxLeftStickButtonEntry = controllersTab.add("Left Stick", false).withWidget(BuiltInWidgets.kBooleanBox)
            .withPosition(0, 6).withSize(3, 1).withProperties(mapLeftStickColor).getEntry();
        
        // Xbox Right Stick button
        Map<String, Object> mapRightStickColor = new HashMap<String,Object>();
        mapRightStickColor.put("Color when true", "#FF1493");        
        mapRightStickColor.put("Color when false", "#40E0D0");    
        driverXboxRightStickButtonEntry = controllersTab.add("Right Stick", false).withWidget(BuiltInWidgets.kBooleanBox)
            .withPosition(7, 10).withSize(3, 1).withProperties(mapRightStickColor).getEntry();
        
        // Xbox Dpad Value
        driverXboxDpadEntry = controllersTab.add("Dpad Value", -1).withWidget(BuiltInWidgets.kTextView).withPosition(3, 12)
            .withSize(3, 1).getEntry();


        
        // Text View Boxes and Boolean Boxes for the Button Board. -Mikey and Annika


        // Button Board Floor button
        Map<String, Object> mapFloorButtonColor = new HashMap<String,Object>();
        mapFloorButtonColor.put("Color when true", "#FF0000");        
        mapFloorButtonColor.put("Color when false", "#0000FF");
        operatorButtonBoardButton1Entry = controllersTab.add("Floor Button", false).withWidget(BuiltInWidgets.kBooleanBox).withPosition(21, 7)
            .withSize(3, 1).withProperties(mapFloorButtonColor).getEntry();
        
        // Button Board Cargo Ship Cargo button
        Map<String, Object> mapCargoShipCargoButtonColor = new HashMap<String,Object>();
        mapCargoShipCargoButtonColor.put("Color when true", "#FF0000");        
        mapCargoShipCargoButtonColor.put("Color when false", "#0000FF");    
        operatorButtonBoardButton2Entry = controllersTab.add("Cargo Ship Cargo", false).withWidget(BuiltInWidgets.kBooleanBox).withPosition(21, 5)
            .withSize(3, 1).withProperties(mapCargoShipCargoButtonColor).getEntry();
        
        // Button Board Bottom Cargo button
        Map<String, Object> mapBottomCargoButtonColor = new HashMap<String,Object>();
        mapBottomCargoButtonColor.put("Color when true", "#191970");        
        mapBottomCargoButtonColor.put("Color when false", "#FFD700");     
        operatorButtonBoardButton3Entry = controllersTab.add("Bottom Cargo", false).withWidget(BuiltInWidgets.kBooleanBox).withPosition(24, 6)
            .withSize(3, 1).withProperties(mapBottomCargoButtonColor).getEntry();
        
        // Button Board Center Cargo button
        Map<String, Object> mapCenterCargoButtonColor = new HashMap<String,Object>();
        mapCenterCargoButtonColor.put("Color when true", "#191970");        
        mapCenterCargoButtonColor.put("Color when false", "#FFD700");
        operatorButtonBoardButton4Entry = controllersTab.add("Center Cargo", false).withWidget(BuiltInWidgets.kBooleanBox).withPosition(24, 4)
            .withSize(3, 1).withProperties(mapCenterCargoButtonColor).getEntry();
        
        // Button Board Top Cargo button
        Map<String, Object> mapTopCargoButtonColor = new HashMap<String,Object>();
        mapTopCargoButtonColor.put("Color when true", "#191970");        
        mapTopCargoButtonColor.put("Color when false", "#FFD700");
        operatorButtonBoardButton5Entry = controllersTab.add("Top Cargo", false).withWidget(BuiltInWidgets.kBooleanBox).withPosition(24, 2)
            .withSize(3, 1).withProperties(mapTopCargoButtonColor).getEntry();
        
        // Button Board Bottom Hatch button
        Map<String, Object> mapBottomHatchButtonColor = new HashMap<String,Object>();
        mapBottomHatchButtonColor.put("Color when true", "#008B8B");        
        mapBottomHatchButtonColor.put("Color when false", "#FF0000");
        operatorButtonBoardButton6Entry = controllersTab.add("Bottom Hatch", false).withWidget(BuiltInWidgets.kBooleanBox).withPosition(18, 6)
            .withSize(3, 1).withProperties(mapBottomHatchButtonColor).getEntry();
        
        // Button Board Center Hatch button
        Map<String, Object> mapMiddleHatchButtonColor = new HashMap<String,Object>();
        mapMiddleHatchButtonColor.put("Color when true", "#008B8B");        
        mapMiddleHatchButtonColor.put("Color when false", "#FF0000");
        operatorButtonBoardButton7Entry = controllersTab.add("Center Hatch", false).withWidget(BuiltInWidgets.kBooleanBox).withPosition(18, 4)
            .withSize(3, 1).withProperties(mapMiddleHatchButtonColor).getEntry();
        
        // Button Board Top Hatch button
        Map<String, Object> mapTopHatchButtonColor = new HashMap<String,Object>();
        mapTopHatchButtonColor.put("Color when true", "#008B8B");        
        mapTopHatchButtonColor.put("Color when false", "#FF0000");    
        operatorButtonBoardButton8Entry = controllersTab.add("Top Hatch     ", false).withWidget(BuiltInWidgets.kBooleanBox).withPosition(18, 2)
            .withSize(3, 1).withProperties(mapTopHatchButtonColor).getEntry();
        
        // Button Board Arm Manual Override button
        Map<String, Object> mapArmOverrideButtonColor = new HashMap<String,Object>();
        mapArmOverrideButtonColor.put("Color when true", "#000000");        
        mapArmOverrideButtonColor.put("Color when false", "#FFFFFF");
        operatorButtonBoardButton9Entry = controllersTab.add("Arm Override", false).withWidget(BuiltInWidgets.kBooleanBox).withPosition(24, 0)
            .withSize(3, 1).withProperties(mapArmOverrideButtonColor).getEntry();
        
        // Button Board Elevator Manual Override button
        Map<String, Object> mapElevatorOverrideButtonColor = new HashMap<String,Object>();
        mapElevatorOverrideButtonColor.put("Color when true", "#000000");        
        mapElevatorOverrideButtonColor.put("Color when false", "#FFFFFF");
        operatorButtonBoardButton10Entry = controllersTab.add("Elevator Override", false).withWidget(BuiltInWidgets.kBooleanBox).withPosition(18, 0)
            .withSize(3, 1).withProperties(mapElevatorOverrideButtonColor).getEntry();
            
        // Button Board Button Board X Axis button
        operatorButtonBoardJoystickXAxisEntry = controllersTab.add("Joystick X Axis", 0.0).withWidget(BuiltInWidgets.kTextView).withPosition(27, 2)
            .withSize(3, 1).getEntry();

        // Button Board Y Axis button
        operatorButtonBoardJoystickYAxisEntry = controllersTab.add("Joystick Y Axis", 0.0).withWidget(BuiltInWidgets.kTextView).withPosition(27, 4)
            .withSize(3, 1).getEntry();

    }    
    
    public void setControllersTabData(ControllersTabData controllersTabData)
    {
        // The set booleans for Xbox Controller buttons.    
        driverXboxAButtonEntry.setBoolean(controllersTabData.driverXboxAButton);
        driverXboxBButtonEntry.setBoolean(controllersTabData.driverXboxBButton);
        driverXboxXButtonEntry.setBoolean(controllersTabData.driverXboxXButton);
        driverXboxYButtonEntry.setBoolean(controllersTabData.driverXboxYButton);
        driverXboxLeftBumperEntry.setBoolean(controllersTabData.driverXboxLeftBumperButton);
        driverXboxRightBumperEntry.setBoolean(controllersTabData.driverXboxRightBumperButton);
        driverXboxLeftStickButtonEntry.setBoolean(controllersTabData.driverXboxLeftStickButton);
        driverXboxRightStickButtonEntry.setBoolean(controllersTabData.driverXboxRightStickButton);
        driverXboxStartButtonEntry.setBoolean(controllersTabData.driverXboxStartButton);
        driverXboxBackButtonEntry.setBoolean(controllersTabData.driverXboxBackButton);
        
        // Set booleans for the Button Board buttons.
        operatorButtonBoardButton1Entry.setBoolean(controllersTabData.operatorButtonBoardButton1);
        operatorButtonBoardButton2Entry.setBoolean(controllersTabData.operatorButtonBoardButton2);
        operatorButtonBoardButton3Entry.setBoolean(controllersTabData.operatorButtonBoardButton3);
        operatorButtonBoardButton4Entry.setBoolean(controllersTabData.operatorButtonBoardButton4);
        operatorButtonBoardButton5Entry.setBoolean(controllersTabData.operatorButtonBoardButton5);
        operatorButtonBoardButton6Entry.setBoolean(controllersTabData.operatorButtonBoardButton6);
        operatorButtonBoardButton7Entry.setBoolean(controllersTabData.operatorButtonBoardButton7);
        operatorButtonBoardButton8Entry.setBoolean(controllersTabData.operatorButtonBoardButton8);
        operatorButtonBoardButton9Entry.setBoolean(controllersTabData.operatorButtonBoardButton9);
        operatorButtonBoardButton10Entry.setBoolean(controllersTabData.operatorButtonBoardButton10);

        // Having all of the axes rounded so we don't get crazy long decimals.
        driverXboxLeftXAxisEntry.setDouble(Math.round(controllersTabData.driverXboxLeftXAxis * 1000.0) / 1000.0);
        driverXboxLeftYAxisEntry.setDouble(Math.round(controllersTabData.driverXboxLeftYAxis * 1000.0) / 1000.0);
        driverXboxRightXAxisEntry.setDouble(Math.round(controllersTabData.driverXboxRightXAxis * 1000.0) / 1000.0);
        driverXboxRightYAxisEntry.setDouble(Math.round(controllersTabData.driverXboxRightYAxis * 1000.0) / 1000.0);
        driverXboxLeftTriggerAxisEntry.setDouble(Math.round(controllersTabData.driverXboxLeftTriggerAxis * 1000.0) / 1000.0);
        driverXboxRightTriggerAxisEntry.setDouble(Math.round(controllersTabData.driverXboxRightTriggerAxis * 1000.0) / 1000.0);
        driverXboxDpadEntry.setDouble(Math.round(controllersTabData.driverXboxDpadAxis * 1000.0) / 1000.0);
        
        operatorButtonBoardJoystickXAxisEntry.setDouble(Math.round(controllersTabData.operatorButtonBoardJoystickXAxis * 1000.0) / 1000.0);
        operatorButtonBoardJoystickYAxisEntry.setDouble(Math.round(controllersTabData.operatorButtonBoardJoystickYAxis * 1000.0) / 1000.0);
    }

    public ControllersTabData getControllersTabData()
    {
        // Get Booleans for buttons on Xbox Controller.
        controllersTabData.driverXboxAButton = driverXboxAButtonEntry.getBoolean(false);
        controllersTabData.driverXboxBButton = driverXboxBButtonEntry.getBoolean(false);
        controllersTabData.driverXboxXButton = driverXboxXButtonEntry.getBoolean(false);
        controllersTabData.driverXboxYButton = driverXboxYButtonEntry.getBoolean(false);
        controllersTabData.driverXboxLeftBumperButton = driverXboxLeftBumperEntry.getBoolean(false);
        controllersTabData.driverXboxRightBumperButton = driverXboxRightBumperEntry.getBoolean(false);
        controllersTabData.driverXboxLeftStickButton = driverXboxLeftStickButtonEntry.getBoolean(false);
        controllersTabData.driverXboxRightStickButton = driverXboxRightStickButtonEntry.getBoolean(false);
        controllersTabData.driverXboxStartButton = driverXboxStartButtonEntry.getBoolean(false);
        controllersTabData.driverXboxBackButton = driverXboxBackButtonEntry.getBoolean(false);

        // Get the Axes for the Xbox controller.
        controllersTabData.driverXboxLeftXAxis = driverXboxLeftXAxisEntry.getDouble(0.0);
        controllersTabData.driverXboxLeftYAxis = driverXboxLeftYAxisEntry.getDouble(0.0);
        controllersTabData.driverXboxLeftTriggerAxis = driverXboxLeftTriggerAxisEntry.getDouble(0.0);
        controllersTabData.driverXboxRightTriggerAxis = driverXboxRightTriggerAxisEntry.getDouble(0.0);
        controllersTabData.driverXboxRightXAxis = driverXboxRightXAxisEntry.getDouble(0.0);
        controllersTabData.driverXboxRightYAxis = driverXboxRightYAxisEntry.getDouble(0.0);
        controllersTabData.driverXboxDpadAxis = driverXboxDpadEntry.getDouble(-1);

        // Get Booleans for buttons on button board.
        controllersTabData.operatorButtonBoardButton1 = operatorButtonBoardButton1Entry.getBoolean(false);
        controllersTabData.operatorButtonBoardButton2 = operatorButtonBoardButton2Entry.getBoolean(false);
        controllersTabData.operatorButtonBoardButton3 = operatorButtonBoardButton3Entry.getBoolean(false);
        controllersTabData.operatorButtonBoardButton4 = operatorButtonBoardButton4Entry.getBoolean(false);
        controllersTabData.operatorButtonBoardButton5 = operatorButtonBoardButton5Entry.getBoolean(false);
        controllersTabData.operatorButtonBoardButton6 = operatorButtonBoardButton6Entry.getBoolean(false);
        controllersTabData.operatorButtonBoardButton7 = operatorButtonBoardButton7Entry.getBoolean(false);
        controllersTabData.operatorButtonBoardButton8 = operatorButtonBoardButton8Entry.getBoolean(false);
        controllersTabData.operatorButtonBoardButton9 = operatorButtonBoardButton9Entry.getBoolean(false);
        controllersTabData.operatorButtonBoardButton10 = operatorButtonBoardButton10Entry.getBoolean(false);

        // Get Doubles for the Button Board joystick axes
        controllersTabData.operatorButtonBoardJoystickXAxis = operatorButtonBoardJoystickXAxisEntry.getDouble(0.0);
        controllersTabData.operatorButtonBoardJoystickYAxis = operatorButtonBoardJoystickYAxisEntry.getDouble(-0.0);
        
        return controllersTabData;
    }
}
