/**
 * This class represents the robot's drivetrain.
 * It contains all the code for properly controlling
 * and measuring the movements of the robot.
 * 
 * Author: Yash Gautam
 * Created: 1/15/19
 * Last Worked On: 1/15/19
 */
package frc.robot.components;

import frc.robot.control.Xbox;
import frc.robot.control.DriverXbox;

import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import com.kauailabs.navx.frc.AHRS;


public class Drivetrain extends MecanumDrive
{
    private static Drivetrain instance = new Drivetrain();

    private DriverXbox driverXbox = DriverXbox.getInstance();

    private static WPI_TalonSRX frontRightMotor = new WPI_TalonSRX(Constants.FRONT_RIGHT_MOTOR_PORT);
    private static WPI_TalonSRX frontRightMotorFollower = new WPI_TalonSRX(Constants.FRONT_RIGHT_FOLLOWER_MOTOR_PORT);


    private static WPI_TalonSRX frontLeftMotor = new WPI_TalonSRX(Constants.FRONT_LEFT_MOTOR_PORT);
    private static WPI_TalonSRX frontLeftMotorFollower = new WPI_TalonSRX(Constants.FRONT_LEFT_FOLLOWER_MOTOR_PORT);


    private static WPI_TalonSRX backRightMotor = new WPI_TalonSRX(Constants.BACK_RIGHT_MOTOR_PORT);
    private static WPI_TalonSRX backRightMotorFollower = new WPI_TalonSRX(Constants.BACK_RIGHT_FOLLOWER_MOTOR_PORT);


    private static WPI_TalonSRX backLeftMotor = new WPI_TalonSRX(Constants.BACK_LEFT_MOTOR_PORT);
    private static WPI_TalonSRX backLeftMotorFollower = new WPI_TalonSRX(Constants.BACK_LEFT_FOLLOWER_MOTOR_PORT);


    private AHRS navX = new AHRS(I2C.Port.kMXP);

    //constructor for drivetrain class
    private Drivetrain()
    {
        super(frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor);

        frontRightMotorFollower.follow(frontRightMotor);
        frontLeftMotorFollower.follow(frontLeftMotor);
        backRightMotorFollower.follow(backRightMotor);
        backLeftMotorFollower.follow(backLeftMotor);
        
    }

    public static Drivetrain getInstance()
    {
        return instance;
    }
    //main loop for teleop mode
    public void teleop()
    {
        double rightXAxis = driverXbox.getRawAxis(Xbox.Constants.RIGHT_STICK_X_AXIS);
        double leftXAxis = driverXbox.getRawAxis(Xbox.Constants.LEFT_STICK_X_AXIS);
        double leftYAxis = driverXbox.getRawAxis(Xbox.Constants.LEFT_STICK_Y_AXIS);

        try
        {
            this.driveCartesian(leftXAxis, leftYAxis, rightXAxis);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static class Constants
    {
        public static final int FRONT_RIGHT_MOTOR_PORT = 15;
        public static final int FRONT_RIGHT_FOLLOWER_MOTOR_PORT = 14;

        public static final int FRONT_LEFT_MOTOR_PORT = 13;
        public static final int FRONT_LEFT_FOLLOWER_MOTOR_PORT = 12;

        public static final int BACK_RIGHT_MOTOR_PORT = 0;
        public static final int BACK_RIGHT_FOLLOWER_MOTOR_PORT = 1;

        public static final int BACK_LEFT_MOTOR_PORT = 2;
        public static final int BACK_LEFT_FOLLOWER_MOTOR_PORT = 3;


    }
}
