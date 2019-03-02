package frc.components;

import frc.components.Drivetrain.Constants.OmniEncoder;
import frc.robot.SlabShuffleboard;

import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Servo;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.CANSparkMaxLowLevel.ConfigParameter;
import com.revrobotics.CANSparkMax.IdleMode;
import frc.util.MotorConstants;


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

    private Encoder leftEncoder = new Encoder(Constants.LEFT_ENCODER_CHANNEL_A, Constants.LEFT_ENCODER_CHANNEL_B, false, Encoder.EncodingType.k4X);
    private Encoder rightEncoder = new Encoder(Constants.RIGHT_ENCODER_CHANNEL_A, Constants.RIGHT_ENCODER_CHANNEL_B, false, Encoder.EncodingType.k4X);
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

    private double speedFactor = 1.0;

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

        frontRightMotor.setSmartCurrentLimit(MotorConstants.getMotorStallCurrent(MotorConstants.Constants.MotorType.kNeoMotor, 0.3));
        // frontRightMotor.setSecondaryCurrentLimit(Constants.SECONDARY_MOTOR_CURRENT_LIMIT);
        frontRightMotor.setOpenLoopRampRate(Constants.DRIVE_RAMP_TIME);
        frontRightMotor.setIdleMode(IdleMode.kBrake);
        frontRightMotor.setParameter(ConfigParameter.kHardLimitFwdEn, false);
        frontRightMotor.setParameter(ConfigParameter.kHardLimitRevEn, false);
        frontRightMotor.setParameter(ConfigParameter.kSoftLimitFwdEn, false);
        frontRightMotor.setParameter(ConfigParameter.kSoftLimitRevEn, false);
        frontRightMotor.setParameter(ConfigParameter.kInputDeadband, Constants.MOTOR_DEADBAND);

        frontLeftMotor.setSmartCurrentLimit(MotorConstants.getMotorStallCurrent(MotorConstants.Constants.MotorType.kNeoMotor, 0.3));
        // frontLeftMotor.setSecondaryCurrentLimit(Constants.SECONDARY_MOTOR_CURRENT_LIMIT);
        frontLeftMotor.setOpenLoopRampRate(Constants.DRIVE_RAMP_TIME);
        frontLeftMotor.setIdleMode(IdleMode.kBrake);
        frontLeftMotor.setParameter(ConfigParameter.kHardLimitFwdEn, false);
        frontLeftMotor.setParameter(ConfigParameter.kHardLimitRevEn, false);
        frontLeftMotor.setParameter(ConfigParameter.kSoftLimitFwdEn, false);
        frontLeftMotor.setParameter(ConfigParameter.kSoftLimitRevEn, false);
        frontLeftMotor.setParameter(ConfigParameter.kInputDeadband, 0);

        backRightMotor.setSmartCurrentLimit(MotorConstants.getMotorStallCurrent(MotorConstants.Constants.MotorType.kNeoMotor, 0.3));
        // backRightMotor.setSecondaryCurrentLimit(Constants.SECONDARY_MOTOR_CURRENT_LIMIT);
        backRightMotor.setOpenLoopRampRate(Constants.DRIVE_RAMP_TIME);
        backRightMotor.setIdleMode(IdleMode.kBrake);
        backRightMotor.setParameter(ConfigParameter.kHardLimitFwdEn, false);
        backRightMotor.setParameter(ConfigParameter.kHardLimitRevEn, false);
        backRightMotor.setParameter(ConfigParameter.kSoftLimitFwdEn, false);
        backRightMotor.setParameter(ConfigParameter.kSoftLimitRevEn, false);
        backRightMotor.setParameter(ConfigParameter.kInputDeadband, 0);

        backLeftMotor.setSmartCurrentLimit(MotorConstants.getMotorStallCurrent(MotorConstants.Constants.MotorType.kNeoMotor, 0.3));
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

    //returns the instance of the drivetrain
    public static Drivetrain getInstance()
    {
        return instance;
    }

    /**
     * Gets the distance from the left encoder converted to inches.
     * 
     * @return Distance traveled.
     */
    public double getLeftDistanceInInches()
    {
        return -leftEncoder.getRaw() / Constants.ENCODER_TICKS_PER_INCH;
    }

    /**
     * Gets the distance from the right encoder converted to inches.
     * 
     * @return Distance traveled.
     */
    public double getRightDistanceInInches()
    {
        return rightEncoder.getRaw() / Constants.ENCODER_TICKS_PER_INCH;
    }

    /**
     * Gets the average distance of the encoders converted to inches.
     * 
     * @return Distance traveled.
     */
    public double getAvgDistanceInInches()
    {
        return (getRightDistanceInInches() + getLeftDistanceInInches()) / 2.0;
    }

    /**
     * Gets the further distance of the encoders converted to inches.
     * 
     * @return Distance traveled.
     */
    public double getDistanceInInches()
    {
        double rightDistance = getRightDistanceInInches();
        double leftDistance = getLeftDistanceInInches();

        return rightDistance > leftDistance ? rightDistance : leftDistance;
    }

    //moves the omni wheel up and down
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

    /**
     * returns the value of the left servo
     * @ leftServo.get
     */
    public double getLeftServo()
    {
        return leftServo.get();
    }

    /**
     * returns the value of the leftServoPosition variable
     * @return leftServoPosition
     */
    public double getLeftServoPosition()
    {
        return leftServoPosition;
    }

    /**
     * returns the value of the right servo
     * @return rightServo.get
     */
    public double getRightServo()
    {
        return rightServo.get();
    }

    /**
     * returns the value of the rightServoPosition variable
     * @return rightServoPosition
     */
    public double getRightServoPosition()
    {
        return rightServoPosition;
    }

    //resets the left servo to its up position
    public void resetLeftServo()
    {
        leftServoPosition = 0.225;
        leftServo.set(leftServoPosition);
    }

    //resets the right servo to its up position
    public void resetRightServo()
    {
        rightServoPosition = 0.3;
        rightServo.set(rightServoPosition);
    }

    /**
     * gets the reverse yaw from the navX for field oriented driving 
     * @return -navX.getYaw
     */
    public double getFieldOrientedHeading()
    {
        return -navX.getYaw();
    }

    /**
     * gets the yaw from the navX 
     * @return navX.getYaw
     */
    public double getHeadingInDegrees()
    {
        return navX.getYaw();
    }

    //toggles driving in field oriented
    public void toggleDriveInFieldOriented()
    {
        driveInFieldOriented = !driveInFieldOriented;
    }

    /** 
     * returns wether or not the robot is in field oriented view
     *  @return driveInFieldOriented 
     * 
     */
    public boolean getDriveInFieldOriented()
    {
        return driveInFieldOriented;
    }

    //resets the navX
    public void resetNavX()
    {
        navX.reset();
    }

    //resets both the encoder values
    public void resetBothEncoders()
    {
        leftEncoder.reset();
        rightEncoder.reset();
    }

    public void setMotorSpeedFactor(SlabShuffleboard.MotorSpeed speedFactor)
    {
        this.speedFactor = speedFactor.value;
    }

    @Override
    public void driveCartesian(double ySpeed, double xSpeed, double zRotation)
    {
       this.driveCartesian(ySpeed, xSpeed, zRotation, 0.0);
    }

    @Override
    public void driveCartesian(double ySpeed, double xSpeed, double zRotation, double gyroAngle)
    {
        ySpeed *= speedFactor;
        xSpeed *= speedFactor;
        zRotation *= speedFactor;
        
        super.driveCartesian(ySpeed, xSpeed, zRotation, gyroAngle);
    }

    /**
     * Calculates the angle to rotate based on bearing and heading
     * @param bearing
     * @return angleToRotate
     */
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
                distanceTravelled = Math.abs(getDistanceInInches());
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

    /**
     * spins the robot to a given bearing based on navX values
     * @param bearing
     * @param speed
     * @return doneSpinning
     */
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

    public String getFrontRightMotorData()
    {
        return String.format("%6.3f,  %6.0f,  %6.3f,  %5.1f",
         frontRightMotor.get(), frontRightMotor.getEncoder().getPosition(),
          frontRightMotor.getOutputCurrent(), frontRightMotor.getEncoder().getVelocity());
    }

    public String getFrontLeftMotorData()
    {
        return String.format("%6.3f,  %6.0f,  %6.3f,  %5.1f",
         frontLeftMotor.get(), frontLeftMotor.getEncoder().getPosition(),
         frontLeftMotor.getOutputCurrent(), frontLeftMotor.getEncoder().getVelocity());
    }

    public String getBackRightMotorData()
    {
        return String.format("%6.3f,  %6.0f,  %6.3f,  %5.1f",
         backRightMotor.get(), backRightMotor.getEncoder().getPosition(),
         backRightMotor.getOutputCurrent(), backRightMotor.getEncoder().getVelocity());
    }

    public String getBackLeftMotorData()
    {
        return String.format("%6.3f,  %6.0f,  %6.3f,  %5.1f",
         backLeftMotor.get(), backLeftMotor.getEncoder().getPosition(),
         backLeftMotor.getOutputCurrent(), backLeftMotor.getEncoder().getVelocity());
    }

    public String getOmniWheelData()
    {
        return String.format("%6d,  %8.2f,  %6d,  %8.2f", -leftEncoder.get(), getLeftDistanceInInches(), rightEncoder.get(), getRightDistanceInInches());
    }

    public String getNavXData()
    {
        return String.format("%7.2f", navX.getYaw());
    }

    @Override
    public String toString()
    {
        return String.format(" FR: %.2f, FL: %.2f, BR: %.2f, BL: %.2f, Yaw: %.2f",
                frontRightMotor.getEncoder().getVelocity(), frontLeftMotor.getEncoder().getVelocity(),
                backRightMotor.getEncoder().getVelocity(), backLeftMotor.getEncoder().getVelocity(),
                getHeadingInDegrees());
    }

    //constants class for drivetrain
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

        public static final int LEFT_ENCODER_CHANNEL_A = 18;
        public static final int LEFT_ENCODER_CHANNEL_B = 16;
        public static final int RIGHT_ENCODER_CHANNEL_A = 14;
        public static final int RIGHT_ENCODER_CHANNEL_B = 15;

        public static final double ENCODER_TICKS_PER_INCH = (360.0 * 4.0) / (3.25 * Math.PI);

    }
}
