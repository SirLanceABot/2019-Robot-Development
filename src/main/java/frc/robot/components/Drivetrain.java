package frc.robot.components;

import frc.robot.control.Xbox;
import frc.robot.control.DriverXbox;

import edu.wpi.first.wpilibj.drive.MecanumDrive;
// import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;

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

    private double previousNavXValue = 999.999;
	private boolean abortAutonomous = false;

    private static CANSparkMax frontRightMotor = new CANSparkMax(Constants.FRONT_RIGHT_MOTOR_PORT, CANSparkMaxLowLevel.MotorType.kBrushless);
    private static CANSparkMax frontLeftMotor = new CANSparkMax(Constants.FRONT_LEFT_MOTOR_PORT, CANSparkMaxLowLevel.MotorType.kBrushless);
    private static CANSparkMax backRightMotor = new CANSparkMax(Constants.BACK_RIGHT_MOTOR_PORT, CANSparkMaxLowLevel.MotorType.kBrushless);
    private static CANSparkMax backLeftMotor = new CANSparkMax(Constants.BACK_LEFT_MOTOR_PORT, CANSparkMaxLowLevel.MotorType.kBrushless);

    private AHRS navX = new AHRS(I2C.Port.kMXP);

    private Encoder omniWheelEncoder = new Encoder(0, 1, false, Encoder.EncodingType.k4X);

    private Timer startUpTimer = new Timer();
	private Timer t = new Timer();
	private Timer timer = new Timer();

	private boolean doRestartSpinTimer = true;
	private boolean doResetTimer = true;
	private boolean isTimerDone = false;

    private static Drivetrain instance = new Drivetrain();

    // constructor for drivetrain class
    private Drivetrain()
    {
        super(frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor);

        frontRightMotor.setSmartCurrentLimit(Constants.PRIMARY_MOTOR_CURRENT_LIMIT);
        //frontRightMotor.setSecondaryCurrentLimit(Constants.SECONDARY_MOTOR_CURRENT_LIMIT);
        frontRightMotor.setRampRate(Constants.DRIVE_RAMP_TIME);

        frontLeftMotor.setSmartCurrentLimit(Constants.PRIMARY_MOTOR_CURRENT_LIMIT);
        //frontLeftMotor.setSecondaryCurrentLimit(Constants.SECONDARY_MOTOR_CURRENT_LIMIT);
        frontLeftMotor.setRampRate(Constants.DRIVE_RAMP_TIME);

        backRightMotor.setSmartCurrentLimit(Constants.PRIMARY_MOTOR_CURRENT_LIMIT);
        //backRightMotor.setSecondaryCurrentLimit(Constants.SECONDARY_MOTOR_CURRENT_LIMIT);
        backRightMotor.setRampRate(Constants.DRIVE_RAMP_TIME);

        backLeftMotor.setSmartCurrentLimit(Constants.PRIMARY_MOTOR_CURRENT_LIMIT);
        //backLeftMotor.setSecondaryCurrentLimit(Constants.SECONDARY_MOTOR_CURRENT_LIMIT);
        backLeftMotor.setRampRate(Constants.DRIVE_RAMP_TIME);

        navX.reset();
    }

    public static Drivetrain getInstance()
    {
        return instance;
    }

    /**
     * Gets the distance the robot has driven converted to inches.
     * @return Distance traveled.
     */
	public double getEncoderDistance()
	{
        return omniWheelEncoder.getRaw() / 135.0;
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

        System.out.println(toString());
    }

    /**
	 * Drive the distance passed into the method.
	 * @return If the robot has completed the drive.
	 */
	public boolean driveDistance(int inches, double maxSpeed, int heading, int stoppingDistance)
	{
        boolean isDoneDriving = false;
        double distanceTravelled = Math.abs(getEncoderDistance());
        double startingSpeed = 0.3;
		double stoppingSpeed = 0.175;
		int startingDistance = 12;
        int direction = 1;
        double rotate = (navX.getYaw() - heading) / 50;

        if(maxSpeed < 0)
		{
			direction = -1;
        }
        
        if(distanceTravelled <= inches)
        {
            if(distanceTravelled <= startingDistance) 
            {
            driveCartesian(0, ((maxSpeed - (startingSpeed * direction)) / startingDistance) * distanceTravelled + (startingSpeed * direction), -rotate);
            }
            else if(distanceTravelled >= startingDistance && distanceTravelled <= inches - stoppingDistance)
            {
                driveCartesian(0, maxSpeed, -rotate);
            }
            else
            {
                driveCartesian(0, stoppingSpeed * direction, -rotate);
            }
        }
        else
        {
            driveCartesian(0, 0, 0);
			isDoneDriving = true;
        }

        return isDoneDriving;
    }

    public boolean driveSeconds(double speed, double time, int heading)
	{
		isTimerDone = false;
		double rotate = (navX.getYaw() - heading) / 50;

		if(doResetTimer)
		{
			t.stop();
			t.reset();
			t.start();
			doResetTimer = false;
		}

		if(t.get() <= time)
		{
			driveCartesian(0, speed, -rotate);
		}
		else
		{
			driveCartesian(0, 0, 0);
			isTimerDone = true;
			doResetTimer = true;
		}
		return isTimerDone;
    }
    
    /**
	 * Strafe perpendicular to robot. 0 degrees is North.
	 * @return If the robot has completed the strafe.
	 */
	public boolean strafeSeconds(double time, double strafeSpeed, double heading)
	{
		boolean isTimerDone = false;
		double rotate = (navX.getYaw() - heading) / 50;

		if(doResetTimer)
		{
			t.stop();
			t.reset();
			t.start();
		}

		if(strafeSpeed > 0 && t.get() < time)
		{
			driveCartesian(strafeSpeed, 0, rotate);
		}
		else if(strafeSpeed < 0 && t.get() < time)
		{
			driveCartesian(-strafeSpeed, 0, rotate);
		}
		else
		{
			driveCartesian(0, 0, 0);
			isTimerDone = true;
			doResetTimer = true;
		}

		return isTimerDone;
	}

	/**
	 * Strafe at a specific angle. 0 degrees is North
	 * @return If the robot has completed the strafe.
	 */
	public boolean strafeDistanceAtAngle(int inches, double angle, double speed, int heading)
	{
		boolean isDoneDriving = false;
		double x = Math.abs(getEncoderDistance());
		double rotate = (navX.getYaw() - heading) / 50;
		double strafeSpeed = Math.sin(Math.abs(angle)) * speed;
		double forwardSpeed = Math.cos(Math.abs(angle)) * speed;

		if(angle < 0)
		{
			strafeSpeed *= -1;
		}

		if(speed < 0)
		{
			forwardSpeed *= -1;
		}

		if(x < inches)
		{
			driveCartesian(strafeSpeed, forwardSpeed, rotate);
		}
		else
		{
			driveCartesian(0, 0, 0);
			isDoneDriving = true;
		}

		return isDoneDriving;
	}

     /**
     * Method to return whether the robot should abort autonomous.
     * @return Whether to abort autonomous or not.
     */
	public boolean abortAutonomous()
	{
		return abortAutonomous;
	}

    /**
     * Restart the timer.
     */
	public void restartTimer()
	{
		timer.stop();
		timer.reset();
		timer.start();
	}

    public boolean spinToBearing(int bearing, double speed)
	{
        boolean doneSpinning = false;
        boolean spin = true;

        double heading = navX.getYaw();
			
			if(doRestartSpinTimer)
			{
				restartTimer();
				doRestartSpinTimer = false;
			}
			
			if(timer.get() >= 0.2)
			{
				if(previousNavXValue == heading)
				{
					spin = false;
				}
				else
				{
					restartTimer();
					previousNavXValue = heading;
				}
			}
			else
			{
				spin = true;
			}

			if(spin)
			{

				int threshold = 20;
				if(bearing - heading > 0)
				{
					speed *= -1;
				}

				if(Math.abs(bearing - heading) >= threshold)
				{
					driveCartesian(0, 0, -speed);
				}
				else
				{
					driveCartesian(0, 0, 0);
					doneSpinning = true;
				}
			}
			else
			{
				abortAutonomous = true;
				System.out.println("\nNAVX REPEATED VALUES.\n");
				driveCartesian(0, 0, 0);
			}


        return doneSpinning;
    }

    public String toString()
    {
        return String.format("Enc: %.2f, FRC: %.2f, FLC: %.2f, BRC: %.2f, BLC: %.2f, Yaw: %.2f", 
        getEncoderDistance(), frontRightMotor.getOutputCurrent(), frontLeftMotor.getOutputCurrent(), backRightMotor.getOutputCurrent(), backLeftMotor.getOutputCurrent(), navX.getYaw());
    }

    public static class Constants
    {
        public static final int FRONT_RIGHT_MOTOR_PORT = 0;
        public static final int FRONT_LEFT_MOTOR_PORT = 1;
        public static final int BACK_RIGHT_MOTOR_PORT = 3;
        public static final int BACK_LEFT_MOTOR_PORT = 2;

        public static final int PRIMARY_MOTOR_CURRENT_LIMIT = 35;
        public static final int SECONDARY_MOTOR_CURRENT_LIMIT = 45;

		public static final double DRIVE_RAMP_TIME = 0.10;

		public static final int SERVO_PORT = 0;

    }
}
