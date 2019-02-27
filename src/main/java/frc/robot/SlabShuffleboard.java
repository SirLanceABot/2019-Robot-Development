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
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.SimpleWidget;

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
        StartingLocation startingLocation = StartingLocation.kNone;
        RobotType robotType = RobotType.kCompetition;
        MotorSpeed motorSpeed = MotorSpeed.k100percent;

        GamePiece task1GamePiece = GamePiece.kNone;
        Objective task1Objective = Objective.kNothing;
        RocketLevel task1RocketLevel = RocketLevel.kNone;
        RocketHatch task1RocketHatch = RocketHatch.kNone;
        CargoShip task1CargoShip = CargoShip.kNone;

        GamePiece task2GamePiece = GamePiece.kNone;
        Objective task2Objective = Objective.kNothing;
        RocketLevel task2RocketLevel = RocketLevel.kNone;
        RocketHatch task2RocketHatch = RocketHatch.kNone;
        CargoShip task2CargoShip = CargoShip.kNone;

        @Override
        public String toString()
        {
            String str;

            str = String.format("\n\n*****  PREGAME SELECTION  *****\n");
            str += String.format("Starting Location: %s\n", startingLocation);
            str += String.format("Robot Type:   %s\n", robotType);
            str += String.format("Motor Speed:  %s\n\n", motorSpeed);
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

    private PregameSetupTabData pregameSetupTabData = new PregameSetupTabData();

    // Input boxes on the Autonomous tab
    private ShuffleboardTab pregameSetupTab;
    private SendableChooser<StartingLocation> startingLocationComboBox = new SendableChooser<>();
    private SendableChooser<RobotType> robotTypeComboBox = new SendableChooser<>();
    private SendableChooser<MotorSpeed> motorSpeedComboBox = new SendableChooser<>();

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

    private ShuffleboardTab configurationTab;
    private NetworkTableEntry maxSpeed;
    private SimpleWidget maxSpeedWidget;

    private static SlabShuffleboard instance = new SlabShuffleboard();

    private SlabShuffleboard()
    {
        System.out.println(this.getClass().getName() + ": Started Constructing");
        // Shuffleboard.startRecording();
        // setupConfigurationTab();
        createPregameSetupTab();
        System.out.println(this.getClass().getName() + ": Started Constructing");
    }

    public static SlabShuffleboard getInstance()
    {
        return instance;
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
        pregameSetupTab.add(motorSpeedComboBox).withWidget(BuiltInWidgets.kComboBoxChooser).withPosition(0, 6).withSize(4,
                1);

        // ComboBox for Task 1: Preloaded Game Piece
        task1GamePieceComboBox.setName("Task 1 - Preloaded Game Piece");
        task1GamePieceComboBox.setDefaultOption("None (default)", GamePiece.kNone);
        task1GamePieceComboBox.addOption("Hatch Panel", GamePiece.kHatchPanel);
        task1GamePieceComboBox.addOption("Cargo", GamePiece.kCargo);
        pregameSetupTab.add(task1GamePieceComboBox).withWidget(BuiltInWidgets.kComboBoxChooser).withPosition(5, 0).withSize(8,
                1);

        // ComboBox for Task 1: Objective
        task1ObjectiveComboBox.setName("Task 1 - Objective");
        task1ObjectiveComboBox.setDefaultOption("Nothing (default)", Objective.kNothing);
        task1ObjectiveComboBox.addOption("Leave Platform", Objective.kLeaveHabPlatform);
        task1ObjectiveComboBox.addOption("Cargo Ship", Objective.kCargoShip);
        task1ObjectiveComboBox.addOption("Rocket", Objective.kRocket);
        pregameSetupTab.add(task1ObjectiveComboBox).withWidget(BuiltInWidgets.kComboBoxChooser).withPosition(5, 3).withSize(8, 1);

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
        pregameSetupTab.add(task1CargoShipComboBox).withWidget(BuiltInWidgets.kComboBoxChooser).withPosition(9, 6).withSize(4,
                1);

        // ComboBox for Task 2: Game Piece
        task2GamePieceComboBox.setName("Task 2 - Game Piece");
        task2GamePieceComboBox.setDefaultOption("None (default)", GamePiece.kNone);
        task2GamePieceComboBox.addOption("Hatch Panel", GamePiece.kHatchPanel);
        task2GamePieceComboBox.addOption("Cargo", GamePiece.kCargo);
        pregameSetupTab.add(task2GamePieceComboBox).withWidget(BuiltInWidgets.kComboBoxChooser).withPosition(14, 0).withSize(8,
                1);

        // ComboBox for Task 2: Objective
        task2ObjectiveComboBox.setName("Task 2 - Objective");
        task2ObjectiveComboBox.setDefaultOption("Nothing (default)", Objective.kNothing);
        task2ObjectiveComboBox.addOption("Leave Platform", Objective.kLeaveHabPlatform);
        task2ObjectiveComboBox.addOption("Cargo Ship", Objective.kCargoShip);
        task2ObjectiveComboBox.addOption("Rocket", Objective.kRocket);
        pregameSetupTab.add(task2ObjectiveComboBox).withWidget(BuiltInWidgets.kComboBoxChooser).withPosition(14, 3).withSize(8, 1);

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
        pregameSetupTab.add(task2CargoShipComboBox).withWidget(BuiltInWidgets.kComboBoxChooser).withPosition(18, 6).withSize(4,
                1);

        // Split Button Chooser
        sendDataButton.setName("Send Data?");
        sendDataButton.setDefaultOption("No", false);
        sendDataButton.addOption("Yes", true);
        pregameSetupTab.add(sendDataButton).withWidget(BuiltInWidgets.kSplitButtonChooser).withPosition(23, 9).withSize(4,
                1);
    }

    public PregameSetupTabData getPregameSetupTabData()
    {
        pregameSetupTabData.startingLocation = startingLocationComboBox.getSelected();
        pregameSetupTabData.robotType = robotTypeComboBox.getSelected();
        pregameSetupTabData.motorSpeed = motorSpeedComboBox.getSelected();

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

    public void setupConfigurationTab()
    {
        configurationTab = Shuffleboard.getTab("Configuration");
        Shuffleboard.selectTab("Configuration");

        Map<String, Object> mapMaxSpeedSlider = new HashMap<String, Object>();
        mapMaxSpeedSlider.put("Min", 0);
        mapMaxSpeedSlider.put("Max", 1);
        mapMaxSpeedSlider.put("Block increment", 0.1);

        maxSpeedWidget = configurationTab.add("Max Speed", 0).withWidget(BuiltInWidgets.kNumberSlider)
                .withPosition(1, 1).withSize(2, 1).withProperties(mapMaxSpeedSlider);

        maxSpeed = maxSpeedWidget.getEntry();
    }

    public double getMaxSpeed()
    {
        return maxSpeed.getDouble(0);
    }
    
}
