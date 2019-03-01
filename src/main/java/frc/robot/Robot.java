package frc.robot;

import frc.components.Arm;
import frc.components.Climber;
import frc.components.Drivetrain;
import frc.components.Elevator;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.control.DriverXbox;
import frc.robot.Teleop;
import frc.robot.SlabShuffleboard.MotorsAndSensorsTabData;
import frc.robot.SlabShuffleboard.PregameSetupTabData;
import frc.control.Xbox;
import frc.control.Xbox.Constants;

public class Robot extends TimedRobot
{
    private Drivetrain drivetrain = Drivetrain.getInstance();
    private DriverXbox driverXbox = DriverXbox.getInstance();
    private Teleop teleop = Teleop.getInstance();
    private Elevator elevator = Elevator.getInstance();
    private Arm arm = Arm.getInstance();
    private Climber climber = Climber.getInstance();
    private Autonomous autonomous = Autonomous.getInstance();

    private SlabShuffleboard slabShuffleboard;
    private PregameSetupTabData pregameSetupTabData;
    private MotorsAndSensorsTabData motorsAndSensorsTabData;
    private boolean isPregame = true;
    private boolean isNewPregameData = true;

    public Robot()
    {
        System.out.println(this.getClass().getName() + ": Started Constructing");
        System.out.println(this.getClass().getName() + ": Finished Constructing");
    }

    @Override
    public void robotInit()
    {
        slabShuffleboard = SlabShuffleboard.getInstance();

        pregameSetupTabData = slabShuffleboard.getPregameSetupTabData();
        motorsAndSensorsTabData = slabShuffleboard.getMotorsAndSensorsTabData();
    }

    @Override
    public void robotPeriodic()
    {
        getPregameSetupData();
        updateAllShuffleboardData();
    }

    @Override
    public void autonomousInit()
    {
        autonomous.autoInit();
    }

    @Override
    public void autonomousPeriodic()
    {
        //drivetrain.driveCartesian(0, .2, 0);
        System.out.println(drivetrain);
    }

    @Override
    public void teleopInit()
    {
        teleop.teleopInit();
    }

    @Override
    public void teleopPeriodic()
    {
        double[] scaledArray = driverXbox.getScaledAxes(Constants.LEFT_STICK_AXES, Xbox.Constants.PolynomialDrive.kCubicDrive);
        teleop.teleop();
        //System.out.println(drivetrain);

        // System.out.printf("X = %5.3f Y = %5.3f X = %5.3f Y = %5.3f \n",
        //         driverXbox.getRawAxis(Constants.LEFT_STICK_X_AXIS), driverXbox.getRawAxis(Constants.LEFT_STICK_Y_AXIS),
        //         scaledArray[0], scaledArray[1]);
    }

    public void getPregameSetupData()
    {
        if (isAutonomous() && isEnabled())
        {
            isPregame = false;
        }

        if (isTest() && isEnabled())
        {
            isPregame = true;
        }

        if (isPregame)
        {
            if (slabShuffleboard.getSendData() && isNewPregameData)
            {
                pregameSetupTabData = slabShuffleboard.getPregameSetupTabData();
                System.out.println(pregameSetupTabData);
                isNewPregameData = false;

                // TODO: Maxwell needs to create a set function to set the pregame data
                // autonomous.setPregameSetupData(pregameSetupTabData);
            }

            if (!slabShuffleboard.getSendData() && !isNewPregameData)
            {
                isNewPregameData = true;
            }
        }
    }

    public void updateAllShuffleboardData()
    {
        // Motors and Sensors Tab Data
        motorsAndSensorsTabData.frontLeftMotor = drivetrain.getFrontLeftMotorData();
        motorsAndSensorsTabData.frontRightMotor = drivetrain.getFrontRightMotorData();
        motorsAndSensorsTabData.backLeftMotor = drivetrain.getBackLeftMotorData();
        motorsAndSensorsTabData.backRightMotor = drivetrain.getBackRightMotorData();
        motorsAndSensorsTabData.omniWheel = drivetrain.getOmniWheelData();
        motorsAndSensorsTabData.elevator = elevator.getMasterLegElevatorMotorData();
        motorsAndSensorsTabData.arm = arm.getArmMotorData();
        motorsAndSensorsTabData.wrist = arm.getWristSolenoidData();
        motorsAndSensorsTabData.cargoIntakeRoller = arm.getIntakeRollerMotorData();
        motorsAndSensorsTabData.hatchPanelGrabber = arm.getGrabberSolenoidData();
        motorsAndSensorsTabData.climber = climber.getMasterLegMotorData();
        motorsAndSensorsTabData.climberPinSolenoid = climber.getPinSolenoidData();
        // motorsAndSensorsTabData.lidar = 
        motorsAndSensorsTabData.navX = drivetrain.getNavXData();
        slabShuffleboard.updateMotorsAndSensorsTabData(motorsAndSensorsTabData);
    }
}