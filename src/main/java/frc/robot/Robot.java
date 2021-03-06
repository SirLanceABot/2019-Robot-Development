package frc.robot;

// import frc.components.Arm;
import frc.components.Climber;
import frc.components.Carriage;
import frc.components.NewArm;
import frc.components.Wrist;
import frc.components.Intake;
import frc.components.Grabber;
import frc.components.Drivetrain;
// import frc.components.Elevator;
import frc.control.DriverXbox;
import frc.control.Xbox;
import frc.control.Xbox.Constants;
import frc.robot.Teleop;
import frc.robot.SlabShuffleboard.MotorsAndSensorsTabData;
import frc.robot.SlabShuffleboard.PregameSetupTabData;
import frc.robot.SlabShuffleboard.Recording;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends TimedRobot
{
    private Drivetrain drivetrain = Drivetrain.getInstance();
    private DriverXbox driverXbox = DriverXbox.getInstance();
    private Teleop teleop = Teleop.getInstance();
    // private Elevator elevator = Elevator.getInstance();
    // private Arm arm = Arm.getInstance();
    private Climber climber = Climber.getInstance();
    private Carriage carriage = Carriage.getInstance();
    private NewArm newArm = NewArm.getInstance();
    private Wrist wrist = Wrist.getInstance();
    private Intake intake = Intake.getInstance();
    private Grabber grabber = Grabber.getInstance();

    // private Autonomous autonomous = Autonomous.getInstance();

    private SlabShuffleboard slabShuffleboard;
    private PregameSetupTabData pregameSetupTabData;
    private MotorsAndSensorsTabData motorsAndSensorsTabData;
    private boolean hasAutoRun = false;
    private boolean hasTeleopRun = false;
    private boolean isNewPregameDataAvailable = true;

    private int shuffleboardPrintCounter = 0;

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
        // double time1 = Timer.getFPGATimestamp();
        slabShuffleboard.updateShuffleboardTime();
        updateAllShuffleboardData();
        // System.out.println(Timer.getFPGATimestamp() - time1);
    }

    @Override
    public void disabledInit()
    {
        if (pregameSetupTabData.recording == Recording.kEntireMatch && hasAutoRun && hasTeleopRun)
        {
            slabShuffleboard.stopRecording();
            hasAutoRun = false;
            hasTeleopRun = false;
        }
        else if (pregameSetupTabData.recording == Recording.kAutoOnly && hasAutoRun)
        {
            slabShuffleboard.stopRecording();
            hasAutoRun = false;
        }
        else if (pregameSetupTabData.recording == Recording.kTeleopOnly && hasTeleopRun)
        {
            slabShuffleboard.stopRecording();
            hasTeleopRun = false;
        }        

        // isPregame = true;
    }

    @Override
    public void disabledPeriodic()
    {
        // if (isPregame)
        // {
            getPregameSetupData();
        // }
    }

    @Override
    public void autonomousInit()
    {
        // isPregame = false;

        if (pregameSetupTabData.recording == Recording.kEntireMatch ||
                pregameSetupTabData.recording == Recording.kAutoOnly)
        {
            slabShuffleboard.startRecording();
        }
        
        //autonomous.autoInit();
        teleop.init();
    
        hasAutoRun = true;
    }

    @Override
    public void autonomousPeriodic()
    {
        //drivetrain.driveCartesian(0, .2, 0);
        teleop.periodic();
        System.out.println(drivetrain);
    }

    @Override
    public void teleopInit()
    {
        // isPregame = false;
        
        if (pregameSetupTabData.recording == Recording.kEntireMatch ||
                pregameSetupTabData.recording == Recording.kTeleopOnly)
        {
            slabShuffleboard.startRecording();
        }

        teleop.init();

        hasTeleopRun = true;
    }

    @Override
    public void teleopPeriodic()
    {
        // double[] scaledArray = driverXbox.getScaledAxes(Constants.LEFT_STICK_AXES, Xbox.Constants.PolynomialDrive.kCubicDrive);
        teleop.periodic();
        //System.out.println(drivetrain);

        // System.out.printf("X = %5.3f Y = %5.3f X = %5.3f Y = %5.3f \n",
        //         driverXbox.getRawAxis(Constants.LEFT_STICK_X_AXIS), driverXbox.getRawAxis(Constants.LEFT_STICK_Y_AXIS),
        //         scaledArray[0], scaledArray[1]);
    }

    @Override
    public void testInit()
    {
        System.out.println("*** Resetting setup values ***");

        // isPregame = true;
        isNewPregameDataAvailable = true;
        hasAutoRun = false;
        hasTeleopRun = false;
    }

    @Override
    public void testPeriodic()
    {

    }

    /**
     * this function will return the pregame data
     * that will decide which robot it is, motor speed
     * and autonomous.
     */
    public void getPregameSetupData()
    {
        if (slabShuffleboard.getSendData() && isNewPregameDataAvailable)
        {
            pregameSetupTabData = slabShuffleboard.getPregameSetupTabData();
            System.out.println(pregameSetupTabData);
            isNewPregameDataAvailable = false;

            // TODO: Maxwell needs to create a set function to set the pregame data
            // autonomous.setPregameSetupData(pregameSetupTabData);
        }
        else if (!slabShuffleboard.getSendData() && !isNewPregameDataAvailable)
        {
            isNewPregameDataAvailable = true;
        }
    }

    /**
     * this function updates the shuffleboard
     * one step at a time so that there is not
     * significant lag with the system. Right 
     * now it updates one thing every loop through
     */
    public void updateAllShuffleboardData()
    {
        shuffleboardPrintCounter++;

        // Motors and Sensors Tab Data
        switch (shuffleboardPrintCounter)
        {
        case 1:
            motorsAndSensorsTabData.frontLeftMotor = drivetrain.getFrontLeftMotorData();
            break;
        case 2:
            motorsAndSensorsTabData.frontRightMotor = drivetrain.getFrontRightMotorData();
            break;
        case 3:
            motorsAndSensorsTabData.backLeftMotor = drivetrain.getBackLeftMotorData();
            break;
        case 4:
            motorsAndSensorsTabData.backRightMotor = drivetrain.getBackRightMotorData();
            break;
        case 5:
            motorsAndSensorsTabData.omniWheel = drivetrain.getOmniWheelData();
            break;
        case 6:
            motorsAndSensorsTabData.elevator = carriage.getMasterLegCarriageMotorData();
            // motorsAndSensorsTabData.elevator = elevator.getMasterLegElevatorMotorData();
            break;
        case 7:
            motorsAndSensorsTabData.arm = newArm.getArmMotorData();
            // motorsAndSensorsTabData.arm = arm.getArmMotorData();
            break;
        case 8:
            motorsAndSensorsTabData.wrist = wrist.getWristSolenoidData();
            // motorsAndSensorsTabData.wrist = arm.getWristSolenoidData();
            break;
        case 9:
            motorsAndSensorsTabData.cargoIntakeRoller = intake.getIntakeRollerMotorData();
            // motorsAndSensorsTabData.cargoIntakeRoller = arm.getIntakeRollerMotorData();
            break;
        case 10:
            motorsAndSensorsTabData.hatchPanelGrabber = grabber.getGrabberSolenoidData();
            // motorsAndSensorsTabData.hatchPanelGrabber = arm.getGrabberSolenoidData();
            break;
        case 11:
            motorsAndSensorsTabData.climber = climber.getMasterLegMotorData();
            break;
        case 12:
            motorsAndSensorsTabData.climberPinSolenoid = climber.getPinSolenoidData();
            break;
        case 13:
            // motorsAndSensorsTabData.lidar = 
            break;
        case 14:
            motorsAndSensorsTabData.navX = drivetrain.getNavXData();
            break;
        case 15:
            slabShuffleboard.updateMotorsAndSensorsTabData(motorsAndSensorsTabData);
            shuffleboardPrintCounter = 0;
            break;
        default:
            shuffleboardPrintCounter = 0;
            break;
        }
    }
}