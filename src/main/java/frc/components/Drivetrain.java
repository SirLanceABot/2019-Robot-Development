package frc.components;

import frc.control.Xbox;
import frc.components.Drivetrain.Constants.OmniEncoder;
import frc.control.DriverXbox;

import edu.wpi.first.wpilibj.drive.MecanumDrive;
// import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Servo;

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
    private static CANSparkMax frontRightMotor = new CANSparkMax(Constants.FRONT_RIGHT_MOTOR_PORT,
            CANSparkMaxLowLevel.MotorType.kBrushless);
    private static CANSparkMax frontLeftMotor = new CANSparkMax(Constants.FRONT_LEFT_MOTOR_PORT,
            CANSparkMaxLowLevel.MotorType.kBrushless);
    private static CANSparkMax backRightMotor = new CANSparkMax(Constants.BACK_RIGHT_MOTOR_PORT,
            CANSparkMaxLowLevel.MotorType.kBrushless);
    private static CANSparkMax backLeftMotor = new CANSparkMax(Constants.BACK_LEFT_MOTOR_PORT,
            CANSparkMaxLowLevel.MotorType.kBrushless);

    private static Servo leftServo = new Servo(Constants.LEFT_SERVO_PORT);
    private static Servo rightServo = new Servo(Constants.RIGHT_SERVO_PORT);
    private double servoPosition = 0.5;
    private boolean omniWheelUp = true;

    // TODO: find actual values for encoders channel A and B
    private Encoder leftEncoder = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
    private Encoder rightEncoder = new Encoder(2, 3, false, Encoder.EncodingType.k4X);

    private AHRS navX = new AHRS(I2C.Port.kOnboard);
    private double previousNavXValue = 999.999;
    private boolean abortAutonomous = false;
    private boolean driveInFieldOriented = true;
    private boolean navXIsCalibrated = false;
    private Timer navXTimer = new Timer();

    private Timer timer = new Timer();

    private static Drivetrain instance = new Drivetrain();

    // constructor for drivetrain class
    private Drivetrain()
    {
        super(frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor);

        System.out.println(this.getClass().getName() + ": Started Constructing");

        // TODO: bring this back in
        // setSafetyEnabled(false);

        frontRightMotor.setSmartCurrentLimit(Constants.PRIMARY_MOTOR_CURRENT_LIMIT);
        // frontRightMotor.setSecondaryCurrentLimit(Constants.SECONDARY_MOTOR_CURRENT_LIMIT);
        //frontRightMotor.setOpenLoopRampRate(Constants.DRIVE_RAMP_TIME);

        frontLeftMotor.setSmartCurrentLimit(Constants.PRIMARY_MOTOR_CURRENT_LIMIT);
        // frontLeftMotor.setSecondaryCurrentLimit(Constants.SECONDARY_MOTOR_CURRENT_LIMIT);
        //frontLeftMotor.setOpenLoopRampRate(Constants.DRIVE_RAMP_TIME);

        backRightMotor.setSmartCurrentLimit(Constants.PRIMARY_MOTOR_CURRENT_LIMIT);
        // backRightMotor.setSecondaryCurrentLimit(Constants.SECONDARY_MOTOR_CURRENT_LIMIT);
        //backRightMotor.setOpenLoopRampRate(Constants.DRIVE_RAMP_TIME);

        backLeftMotor.setSmartCurrentLimit(Constants.PRIMARY_MOTOR_CURRENT_LIMIT);
        // backLeftMotor.setSecondaryCurrentLimit(Constants.SECONDARY_MOTOR_CURRENT_LIMIT);
        //backLeftMotor.setOpenLoopRampRate(Constants.DRIVE_RAMP_TIME);

        navXTimer.reset();
        navXTimer.start();
        System.out.println("NavX is calibrating");
        while (navX.isCalibrating() && (navXTimer.get() <= 7.0))
        {
        }

        if(!navX.isCalibrating())
        {
            System.out.println("NavX is done calibrating" + navXTimer.get());
            System.out.println("Firmware version: " + navX.getFirmwareVersion());
            navXIsCalibrated = true;
            navX.reset();
        }
        else
        {
            System.out.println("NavX calibration timed out");
        }

        System.out.println(this.getClass().getName() + ": Finished Constructing");
    }

    public static Drivetrain getInstance()
    {
        return instance;
    }

    /**
     * Gets the distance the robot has driven converted to inches.
     * 
     * @return Distance traveled.
     */
    public double getLeftDistanceInInches()
    {
        return leftEncoder.getRaw() / Constants.ENCODER_TICKS_PER_INCH;
    }

    public double getRightDistanceInInches()
    {
        return rightEncoder.getRaw() / Constants.ENCODER_TICKS_PER_INCH;
    }

    public double getAvgDistanceInInches()
    {
        return (getRightDistanceInInches() + getLeftDistanceInInches()) / 2.0;
    }

    public double getDistaceInInches()
    {
        double rightDistance = getRightDistanceInInches();
        double leftDistance = getLeftDistanceInInches();
        
        return rightDistance > leftDistance ? rightDistance : leftDistance;
    }

    public void moveOmniWheel()
    {
        if (omniWheelUp)
        {
            servoPosition = 1.5;
            leftServo.set(servoPosition);
            rightServo.set(servoPosition);

            omniWheelUp = false;
        }
        else
        {
            servoPosition = 1.5 + (1.0 / 8.5) * 1.5;
            leftServo.set(servoPosition);
            rightServo.set(servoPosition);

            omniWheelUp = true;
        }
    }

    public double getLeftServo()
    {
        return leftServo.get();
    }

    public double getRightServo()
    {
        return rightServo.get();
    }

    public void resetLeftServo()
    {
        leftServo.set(0.00);
    }

    public void resetRightServo()
    {
        rightServo.set(0.00);
    }

    // public void driveFieldOriented(double leftXAxis, double leftYAxis, double rightXAxis)
    // {
    //     driveCartesian(leftXAxis, leftYAxis, rightXAxis, getHeadingFieldOriented());
    // }

    public double getFieldOrientedHeading()
    {
        return -navX.getYaw();
    }

    public double getHeadingInDegrees()
    {
        return navX.getYaw();
    }

    public void toggleDriveInFieldOriented()
    {
        driveInFieldOriented = !driveInFieldOriented;
    }

    public boolean getDriveInFieldOriented()
    {
        return driveInFieldOriented;
    }

    public void resetNavX()
    {
        navX.reset();
    }

    public double getAngleToRotate(double bearing)
    {
        //direction: 1 = clockwise, -1 = counter-clockwise
        double direction = 1;

        double bearingX = Math.sin(Math.toRadians(bearing));
        double bearingY = Math.cos(Math.toRadians(bearing));
        double headingX = Math.sin(Math.toRadians(getHeadingInDegrees()));
        double headingY = Math.cos(Math.toRadians(getHeadingInDegrees()));

        double angleToRotateRad = Math.acos((bearingX * headingX) + (bearingY * headingY));

        double positiveRotateX = (Math.cos(angleToRotateRad) * headingX) - (Math.sin(angleToRotateRad) * headingY);
        double positiveRotateY = (Math.sin(angleToRotateRad) * headingX) + (Math.cos(angleToRotateRad) * headingY);
        double negativeRotateX = (Math.cos(-angleToRotateRad) * headingX) - (Math.sin(-angleToRotateRad) * headingY);
        double negativeRotateY = (Math.sin(-angleToRotateRad) * headingX) + (Math.cos(-angleToRotateRad) * headingY);

        double positiveRotateBearing = Math.toDegrees(Math.atan2(positiveRotateY, positiveRotateX));
        double negativeRotateBearing = Math.toDegrees(Math.atan2(negativeRotateY, negativeRotateX));

        if (Math.abs(bearing - positiveRotateBearing) > Math.abs(bearing - negativeRotateBearing))
        {
            direction = -1;
        }
        
        return Math.toDegrees(angleToRotateRad) * direction;
    }

    /**
     * Drive the distance passed into the method.
     * 
     * @return If the robot has completed the drive.
     */
    public boolean driveDistanceInInches(int inches, double maxSpeed, int bearing, int stoppingDistance,
            OmniEncoder encoder)
    {
        inches = Math.abs(inches);
        boolean isDoneDriving = false;
        double startingDistance = maxSpeed * 12.0;
        double distanceTravelled;
        int driveDirection = 1;
        double angleToRotate = getAngleToRotate(bearing);

        double rotate = angleToRotate / 30.0;

        if (encoder == OmniEncoder.kLeft)
        {
            distanceTravelled = Math.abs(getLeftDistanceInInches());
        }
        else if (encoder == OmniEncoder.kRight)
        {
            distanceTravelled = Math.abs(getRightDistanceInInches());
        }
        else
        {
            distanceTravelled = Math.abs(getDistaceInInches());
        }

        if (maxSpeed < 0)
        {
            driveDirection = -1;
        }

        if (distanceTravelled <= inches)
        {
            if (distanceTravelled <= startingDistance)
            {
                driveCartesian(0, ((maxSpeed - (Constants.STARTING_SPEED * driveDirection)) / startingDistance)
                        * distanceTravelled + (Constants.STARTING_SPEED * driveDirection), rotate);
            }
            else if (distanceTravelled >= startingDistance && distanceTravelled <= inches - stoppingDistance)
            {
                driveCartesian(0, maxSpeed, rotate);
            }
            else
            {
                driveCartesian(0, Constants.STOPPING_SPEED * driveDirection, rotate);
            }
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
     * 
     * @return Whether to abort autonomous or not.
     */
    public boolean abortAutonomous()
    {
        return abortAutonomous;
    }

    public boolean spinToBearing(int bearing, double speed)
    {
        boolean doneSpinning = false;
        boolean isSpinning = true;
        double angleToRotate = getAngleToRotate(bearing);

        // TODO: reset timer, make speed proportional to angleToRotate

        double heading = getHeadingInDegrees();

        if (timer.get() >= 0.2)
        {
            if (previousNavXValue == heading)
            {
                isSpinning = false;
            }
            else
            {
                previousNavXValue = heading;
            }
        }
        else
        {
            isSpinning = true;
        }

        if (isSpinning)
        {
            
            if(angleToRotate != 0)
            {
                speed *= angleToRotate / Math.abs(angleToRotate);
            }

            if (Math.abs(angleToRotate) >= Constants.ROTATE_THRESHOLD)
            {
                driveCartesian(0, 0, speed);
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

    @Override
    public String toString()
    {
        return String.format(" FR: %.2f, FL: %.2f, BR: %.2f, BL: %.2f, Yaw: %.2f",
                 frontRightMotor.getEncoder().getVelocity(), frontLeftMotor.getEncoder().getVelocity(),
                backRightMotor.getEncoder().getVelocity(), backLeftMotor.getEncoder().getVelocity(), getHeadingInDegrees());
    }

    public static class Constants
    {
        public static enum OmniEncoder
        {
            kLeft, kRight, kBoth;
        }

        public static final int FRONT_LEFT_MOTOR_PORT = 4;
        public static final int FRONT_RIGHT_MOTOR_PORT = 1;
        public static final int BACK_RIGHT_MOTOR_PORT = 2;
        public static final int BACK_LEFT_MOTOR_PORT = 3;

        public static final int PRIMARY_MOTOR_CURRENT_LIMIT = 35;
        public static final int SECONDARY_MOTOR_CURRENT_LIMIT = 45;

        public static final double DRIVE_RAMP_TIME = 0.10;

        public static final double STARTING_SPEED = 0.3;
        public static final double STOPPING_SPEED = 0.175;
        public static final int ROTATE_THRESHOLD = 10;

        public static final int LEFT_SERVO_PORT = 0;
        public static final int RIGHT_SERVO_PORT = 1;

        public static final double ENCODER_TICKS_PER_INCH = (360.0 * 4.0) / (3.25 * Math.PI);

    }
}
