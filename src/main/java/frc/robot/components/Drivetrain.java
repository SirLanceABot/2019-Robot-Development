package frc.robot.components;

import frc.robot.control.Xbox;
import frc.robot.control.DriverXbox;

import edu.wpi.first.wpilibj.drive.MecanumDrive;
// import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
// import edu.wpi.first.wpilibj.RobotDrive;

// import javax.lang.model.util.ElementScanner6;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
// import com.revrobotics.CANSparkMaxLowLevel.MotorType;
// import com.revrobotics.CANEncoder;


import com.kauailabs.navx.frc.AHRS;

/**
 * This class represents the robot's drivetrain. It contains all the code for
 * properly controlling and measuring the movements of the robot.
 * 
 * @author: Yash Gautam Created: 1/15/19 Last Worked On: 1/26/19
 */
public class Drivetrain extends MecanumDrive
{
    private DriverXbox driverXbox = DriverXbox.getInstance();

    private static CANSparkMax frontRightMotor = new CANSparkMax(Constants.FRONT_RIGHT_MOTOR_PORT, CANSparkMaxLowLevel.MotorType.kBrushless);
    private static CANSparkMax frontLeftMotor = new CANSparkMax(Constants.FRONT_LEFT_MOTOR_PORT, CANSparkMaxLowLevel.MotorType.kBrushless);
    private static CANSparkMax backRightMotor = new CANSparkMax(Constants.BACK_RIGHT_MOTOR_PORT, CANSparkMaxLowLevel.MotorType.kBrushless);
    private static CANSparkMax backLeftMotor= new CANSparkMax(Constants.BACK_LEFT_MOTOR_PORT, CANSparkMaxLowLevel.MotorType.kBrushless);

    // frontRightMotor = new CANSparkMax(Constants.FRONT_RIGHT_MOTOR_PORT, MotorType.kBrushless);
    // frontLeftMotor = new CANSparkMax(Constants.FRONT_LEFT_MOTOR_PORT, MotorType.kBrushless);
    // backRightMotor = new CANSparkMax(Constants.BACK_RIGHT_MOTOR_PORT, MotorType.kBrushless);
    // backLeftMotor = new CANSparkMax(Constants.BACK_LEFT_MOTOR_PORT, MotorType.kBrushless);

    // private RobotDrive driveRobot = new RobotDrive(frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor);

    private AHRS navX = new AHRS(I2C.Port.kMXP);

    private static Drivetrain instance = new Drivetrain();

    // constructor for drivetrain class
    private Drivetrain()
    {
        super(frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor);

        //frontRightMotorFollower.follow(frontRightMotor);
        //frontLeftMotorFollower.follow(frontLeftMotor);
        //backRightMotorFollower.follow(backRightMotor);
        //backLeftMotorFollower.follow(backLeftMotor);

        // frontRightMotor.configContinuousCurrentLimit(Constants.DRIVE_40_AMP_LIMIT, 10);
		// frontRightMotor.configPeakCurrentLimit(Constants.DRIVE_40_AMP_TRIGGER, Constants.DRIVE_40_AMP_TIME);
        // frontRightMotor.configOpenloopRamp(Constants.DRIVE_RAMP_TIME, Constants.DRIVE_RAMP_RATE_TIMEOUT);
        
        // frontLeftMotor.configContinuousCurrentLimit(Constants.DRIVE_40_AMP_LIMIT, 10);
		// frontLeftMotor.configPeakCurrentLimit(Constants.DRIVE_40_AMP_TRIGGER, Constants.DRIVE_40_AMP_TIME);
        // frontLeftMotor.configOpenloopRamp(Constants.DRIVE_RAMP_TIME, Constants.DRIVE_RAMP_RATE_TIMEOUT);

        // backRightMotor.configContinuousCurrentLimit(Constants.DRIVE_40_AMP_LIMIT, 10);
		// backRightMotor.configPeakCurrentLimit(Constants.DRIVE_40_AMP_TRIGGER, Constants.DRIVE_40_AMP_TIME);
        // backRightMotor.configOpenloopRamp(Constants.DRIVE_RAMP_TIME, Constants.DRIVE_RAMP_RATE_TIMEOUT);

        // backLeftMotor.configContinuousCurrentLimit(Constants.DRIVE_40_AMP_LIMIT, 10);
		// backLeftMotor.configPeakCurrentLimit(Constants.DRIVE_40_AMP_TRIGGER, Constants.DRIVE_40_AMP_TIME);
        // backLeftMotor.configOpenloopRamp(Constants.DRIVE_RAMP_TIME, Constants.DRIVE_RAMP_RATE_TIMEOUT);

        navX.reset();
    }

    public static Drivetrain getInstance()
    {
        return instance;
    }

    // main loop for teleop mode
    public void teleop()
    {
        double rightXAxis = driverXbox.getRawAxis(Xbox.Constants.RIGHT_STICK_X_AXIS);
        double leftXAxis = driverXbox.getRawAxis(Xbox.Constants.LEFT_STICK_X_AXIS);
        double leftYAxis = driverXbox.getRawAxis(Xbox.Constants.LEFT_STICK_Y_AXIS);

        if (driverXbox.getRawButton(Xbox.Constants.RIGHT_BUMPER))
        {
            if (Math.abs(navX.getYaw()) <= 135 && Math.abs(navX.getYaw()) >= 45)
            {
                this.driveCartesian(-leftXAxis, -leftYAxis, rightXAxis, navX.getYaw());
            }
            else
            {
                this.driveCartesian(leftXAxis, leftYAxis, rightXAxis, navX.getYaw());
            }

            System.out.println(navX.getAngle() + ", " + navX.getYaw());
        }
        else
        {
            this.driveCartesian(leftXAxis, leftYAxis, rightXAxis);
        }
    }

    public static class Constants
    {
        public static final int FRONT_RIGHT_MOTOR_PORT = 0;

        public static final int FRONT_LEFT_MOTOR_PORT = 1;

        public static final int BACK_RIGHT_MOTOR_PORT = 2;

        public static final int BACK_LEFT_MOTOR_PORT = 3;

        public static final int DRIVE_40_AMP_TRIGGER = 60;
		public static final int DRIVE_40_AMP_LIMIT = 30;
		public static final int DRIVE_40_AMP_TIME = 4000;

		public static final int DRIVE_30_AMP_TRIGGER = 45;
		public static final int DRIVE_30_AMP_LIMIT = 25;
		public static final int DRIVE_30_AMP_TIME = 3000;

		public static final int DRIVE_RAMP_RATE_TIMEOUT = 10; //ms

		public static final double DRIVE_RAMP_TIME = 0.25;

		public static final int SERVO_PORT = 0;

    }
}
