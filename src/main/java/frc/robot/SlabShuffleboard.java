/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

//import java.util.HashMap;
//import java.util.Map;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
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
        kEntireMatch, kThisModeOnly, kDoNotRecord;
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


    private static SlabShuffleboard instance = new SlabShuffleboard();

    private SlabShuffleboard()
    {
        System.out.println(this.getClass().getName() + ": Started Constructing");

        createMotorsAndSensorsTab();
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
        recordingComboBox.addOption("This Mode Only", Recording.kThisModeOnly);
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

    public void createMotorsAndSensorsTab()
    {
        // Create and select the Motors and Sensors tab
        motorsAndSensorsTab = Shuffleboard.getTab("Motors and Sensors");
        Shuffleboard.selectTab("Motors and Sensors");

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
}
