package frc.robot.components;

/**
 * This class represents the robot's drivetrain.
 * It contains all the code for properly controlling
 * and measuring the movements of the robot.
 * 
 * Author: Yash Gautam
 * Created: 1/15/19
 * Last Worked On: 1/15/19
 */

import frc.robot.control.DriverXbox;

import edu.wpi.first.wpilibj.drive.MecanumDrive;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;


public class Drivetrain extends MecanumDrive
{
    private static WPI_TalonSRX frontRightMotor = new WPI_TalonSRX(0);
    private static WPI_TalonSRX frontLeftMotor = new WPI_TalonSRX(1);
    private static WPI_TalonSRX backRightMotor = new WPI_TalonSRX(2);
    private static WPI_TalonSRX backLeftMotor = new WPI_TalonSRX(3);

    //constructor for drivetrain class
    public Drivetrain()
    {
        super(frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor);
    }

    //main loop for teleop mode
    public void teleop()
    {
        
    }
}
