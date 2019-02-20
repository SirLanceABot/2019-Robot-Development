package frc.components;

import frc.components.Drivetrain.Constants.OmniEncoder;

import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Servo;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.CANSparkMaxLowLevel.ConfigParameter;
import com.revrobotics.CANSparkMax.IdleMode;

import javax.lang.model.util.ElementScanner6;

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
    private double leftServoPosition = 0.225;
    private double rightServoPosition = 0.3;
    private boolean omniWheelUp = false;

    // TODO: find actual values for encoders channel A and B
    private Encoder leftEncoder = new Encoder(Constants.LEFT_ENCODER_CHANEL_A, Constants.LEFT_ENCODER_CHANEL_B, false, Encoder.EncodingType.k4X);
    private Encoder rightEncoder = new Encoder(Constants.RIGHT_ENCODER_CHANEL_A, Constants.RIGHT_ENCODER_CHANEL_B, false, Encoder.EncodingType.k4X);
    private boolean needToResetEncoder = true;
    private boolean isEncoderResetting = false;

    private AHRS navX = new AHRS(I2C.Port.kOnboard);
    private double previousNavXValue = 999.999;
    private boolean abortAutonomous = false;
    private boolean driveInFieldOriented = true;
    private boolean navXIsCalibrated = false;
    private Timer navXTimer = new Timer();
    private boolean needToResetStartingHeading = true;
    private double startingHeading = -999;
    private double startingAngleToRotate = -999;
    

    private Timer timer = new Timer();

    private static Drivetrain instance = new Drivetrain();

    // constructor for drivetrain class
    private Drivetrain()
    {
        super(frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor);

        frontRightMotor.restoreFactoryDefaults();
        frontLeftMotor.restoreFactoryDefaults();
        backRightMotor.restoreFactoryDefaults();
        backLeftMotor.restoreFactoryDefaults();


        System.out.println(this.getClass().getName() + ": Started Constructing");

        setSafetyEnabled(false);

        frontRightMotor.setSmartCurrentLimit(Constants.PRIMARY_MOTOR_CURRENT_LIMIT);
        // frontRightMotor.setSecondaryCurrentLimit(Constants.SECONDARY_MOTOR_CURRENT_LIMIT);
        frontRightMotor.setOpenLoopRampRate(Constants.DRIVE_RAMP_TIME);
        frontRightMotor.setIdleMode(IdleMode.kBrake);
        frontRightMotor.setParameter(ConfigParameter.kHardLimitFwdEn, false);
        frontRightMotor.setParameter(ConfigParameter.kHardLimitRevEn, false);
        frontRightMotor.setParameter(ConfigParameter.kSoftLimitFwdEn, false);
        frontRightMotor.setParameter(ConfigParameter.kSoftLimitRevEn, false);
        frontRightMotor.setParameter(ConfigParameter.kInputDeadband, Constants.MOTOR_DEADBAND);

        frontLeftMotor.setSmartCurrentLimit(Constants.PRIMARY_MOTOR_CURRENT_LIMIT);
        // frontLeftMotor.setSecondaryCurrentLimit(Constants.SECONDARY_MOTOR_CURRENT_LIMIT);
        frontLeftMotor.setOpenLoopRampRate(Constants.DRIVE_RAMP_TIME);
        frontLeftMotor.setIdleMode(IdleMode.kBrake);
        frontLeftMotor.setParameter(ConfigParameter.kHardLimitFwdEn, false);
        frontLeftMotor.setParameter(ConfigParameter.kHardLimitRevEn, false);
        frontLeftMotor.setParameter(ConfigParameter.kSoftLimitFwdEn, false);
        frontLeftMotor.setParameter(ConfigParameter.kSoftLimitRevEn, false);
        frontLeftMotor.setParameter(ConfigParameter.kInputDeadband, 0);

        backRightMotor.setSmartCurrentLimit(Constants.PRIMARY_MOTOR_CURRENT_LIMIT);
        // backRightMotor.setSecondaryCurrentLimit(Constants.SECONDARY_MOTOR_CURRENT_LIMIT);
        backRightMotor.setOpenLoopRampRate(Constants.DRIVE_RAMP_TIME);
        backRightMotor.setIdleMode(IdleMode.kBrake);
        backRightMotor.setParameter(ConfigParameter.kHardLimitFwdEn, false);
        backRightMotor.setParameter(ConfigParameter.kHardLimitRevEn, false);
        backRightMotor.setParameter(ConfigParameter.kSoftLimitFwdEn, false);
        backRightMotor.setParameter(ConfigParameter.kSoftLimitRevEn, false);
        backRightMotor.setParameter(ConfigParameter.kInputDeadband, 0);

        backLeftMotor.setSmartCurrentLimit(Constants.PRIMARY_MOTOR_CURRENT_LIMIT);
        // backLeftMotor.setSecondaryCurrentLimit(Constants.SECONDARY_MOTOR_CURRENT_LIMIT);
        backLeftMotor.setOpenLoopRampRate(Constants.DRIVE_RAMP_TIME);
        backLeftMotor.setIdleMode(IdleMode.kBrake);
        backLeftMotor.setParameter(ConfigParameter.kHardLimitFwdEn, false);
        backLeftMotor.setParameter(ConfigParameter.kHardLimitRevEn, false);
        backLeftMotor.setParameter(ConfigParameter.kSoftLimitFwdEn, false);
        backLeftMotor.setParameter(ConfigParameter.kSoftLimitRevEn, false);
        backLeftMotor.setParameter(ConfigParameter.kInputDeadband, 0);

        navXTimer.reset();
        navXTimer.start();
        System.out.println("NavX is calibrating");
        while (navX.isCalibrating() && (navXTimer.get() <= 7.0))
        {
        }

        if (!navX.isCalibrating())
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
            leftServoPosition = 0.225;
            rightServoPosition = 0.3;
            leftServo.set(leftServoPosition);
            rightServo.set(rightServoPosition);

            omniWheelUp = false;
        }
        else
        {
            leftServoPosition = 0.3;
            rightServoPosition = 0.225;
            leftServo.set(leftServoPosition);
            rightServo.set(rightServoPosition);

            omniWheelUp = true;
        }
    }

    // public void moveOmniWheelUp()
    // {
    // leftServoPosition = leftServoPosition + .05;
    // leftServo.set(leftServoPosition);
    // }

    // public void moveOmniWheelDown()
    // {
    // leftServoPosition = leftServoPosition - .05;
    // leftServo.set(leftServoPosition);
    // }

    public double getLeftServo()
    {
        return leftServo.get();
    }

    public double getLeftServoPosition()
    {
        return leftServoPosition;
    }

    public double getRightServo()
    {
        return rightServo.get();
    }

    public void resetLeftServo()
    {
        leftServoPosition = 0.225;
        leftServo.set(leftServoPosition);
    }

    public void resetRightServo()
    {
        rightServoPosition = 0.3;
        rightServo.set(rightServoPosition);
    }

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

    public void resetBothEncoders()
    {
        leftEncoder.reset();
        rightEncoder.reset();
    }

    public double getAngleToRotate(double bearing)
    {
        // direction: 1 = clockwise, -1 = counter-clockwise
        double direction = 1;

        double bearingX = Math.cos(Math.toRadians(bearing));
        double bearingY = Math.sin(Math.toRadians(bearing));
        double headingX = Math.cos(Math.toRadians(getHeadingInDegrees()));
        double headingY = Math.sin(Math.toRadians(getHeadingInDegrees()));

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
        boolean isDoneDriving = false;

        if (needToResetEncoder)
        {
            resetBothEncoders();
            isEncoderResetting = true;
            needToResetEncoder = false;
        }
        else if (isEncoderResetting)
        {
            if (Math.abs(getLeftDistanceInInches()) < 2 && Math.abs(getRightDistanceInInches()) < 2)
            {
                isEncoderResetting = false;
                // System.out.println("Encoder has Reset");
            }

        }
        else
        {
            inches = Math.abs(inches);
            double startingDistance = maxSpeed * 12.0;
            double distanceTravelled;
            int driveDirection = maxSpeed < 0 ? -1 : 1;
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
                needToResetEncoder = true;
            }

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
        speed = Math.abs(speed);
        boolean doneSpinning = false;
        boolean isSpinning = true;
        double angleToRotate = getAngleToRotate(bearing);
        double direction = Math.signum(angleToRotate);

        if(needToResetStartingHeading)
        {
            startingHeading = getHeadingInDegrees();
            startingAngleToRotate = angleToRotate;
            needToResetStartingHeading = false;
            timer.reset();
            timer.start();
        }
        double angleTravelled = Math.abs(startingHeading - getHeadingInDegrees());
        double startingAngle = startingAngleToRotate >= 90 ? 30 : speed * 0.3 * startingAngleToRotate;
        double stoppingAngle = startingAngleToRotate >= 90 ? 20 : speed * 0.2 * startingAngleToRotate;

        double heading = getHeadingInDegrees();

        //check out navX.getUpdateCounter
        if (timer.get() >= 0.2)
        {
            if (previousNavXValue == heading)
            {
                isSpinning = false;
            }
            else
            {
                previousNavXValue = heading;
                timer.reset();
                timer.start();
            }
        }
        else
        {
            isSpinning = true;
        }

        if (isSpinning)
        {
            speed *= direction;

            if (Math.abs(angleToRotate) >= Constants.ROTATE_THRESHOLD)
            {
                if(angleTravelled <= startingAngle)
                {
                    driveCartesian(0, 0, ((speed - Constants.STARTING_SPEED) * direction / startingAngle)
                        * angleTravelled + Constants.STARTING_SPEED * direction);
                }
                else if(angleTravelled > startingAngle && angleTravelled < startingAngleToRotate - stoppingAngle)
                {
                    driveCartesian(0, 0, speed);
                }
                else
                {
                    driveCartesian(0, 0, Constants.STOPPING_SPEED * direction);
                }

            }
            else
            {
                driveCartesian(0, 0, 0);
                doneSpinning = true;
                needToResetStartingHeading = true;
            }
        }
        else
        {
            abortAutonomous = true;
            System.out.println("\nNAVX REPEATED VALUES.\n");
            driveCartesian(0, 0, 0);
            doneSpinning = true;
            needToResetStartingHeading = true;
        }

        return doneSpinning;
    }

    @Override
    public String toString()
    {
        return String.format(" FR: %.2f, FL: %.2f, BR: %.2f, BL: %.2f, Yaw: %.2f",
                frontRightMotor.getEncoder().getVelocity(), frontLeftMotor.getEncoder().getVelocity(),
                backRightMotor.getEncoder().getVelocity(), backLeftMotor.getEncoder().getVelocity(),
                getHeadingInDegrees());
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

        public static final double MOTOR_DEADBAND = 0.01;

        public static final double STARTING_SPEED = 0.3;
        public static final double STOPPING_SPEED = 0.175;
        public static final int ROTATE_THRESHOLD = 10;

        public static final int LEFT_SERVO_PORT = 1;
        public static final int RIGHT_SERVO_PORT = 0;

        public static final int LEFT_ENCODER_CHANEL_A = 16;
        public static final int LEFT_ENCODER_CHANEL_B = 18;
        public static final int RIGHT_ENCODER_CHANEL_A = 14;
        public static final int RIGHT_ENCODER_CHANEL_B = 15;

        public static final double ENCODER_TICKS_PER_INCH = (360.0 * 4.0) / (3.25 * Math.PI);

    }
}
