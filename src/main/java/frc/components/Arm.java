/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.components;

import frc.robot.SlabShuffleboard;
import frc.util.MotorConstants;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;


/**
 * Add your docs here.
 */
public class Arm
{
    private DoubleSolenoid wristSolenoid = new DoubleSolenoid(Constants.WRIST_SOLENOID_PORT_1,
            Constants.WRIST_SOLENOID_PORT_2); // On the blue solenoid holder
    private DoubleSolenoid grabberSolenoid = new DoubleSolenoid(Constants.GRABBER_SOLENOID_PORT_1,
            Constants.GRABBER_SOLENOID_PORT_2); // On the orange solenoid holder

    private WPI_TalonSRX armMotor = new WPI_TalonSRX(Constants.ARM_MOTOR_ID);


    private double speedFactor = 1.0;
    private Timer grabberTimer = new Timer();


    private WPI_TalonSRX intakeRoller = new WPI_TalonSRX(Constants.ROLLER_TALON_ID);

    private boolean isGrabberMoving = false;
    private boolean isWristMoving;
    private boolean isArmMoving;

    /**
     * Returns the pot value of the given position
     * 
     * <p> 0: Floor
     * <p> 1: Horizontal
     * <p> 2: Middle
     * <p> 3: Top
     * 
     */
    private static int[] armPositionPotValues = {1671, 1758, 1900, 1965};;

    private SlabShuffleboard shuffleboard = SlabShuffleboard.getInstance();

    private static Arm instance = new Arm();

    private Arm()
    {
        System.out.println(this.getClass().getName() + ": Started Constructing");
        intakeRoller.configFactoryDefault();

        armMotor.configFactoryDefault();

        armMotor.setNeutralMode(NeutralMode.Brake);
        intakeRoller.setNeutralMode(NeutralMode.Brake);

        armMotor.configPeakCurrentLimit(MotorConstants.getMotorStallCurrent(MotorConstants.Constants.MotorType.k9015Motor, 0.3));
        intakeRoller.configPeakCurrentDuration(MotorConstants.Constants.PEAK_CURRENT_DURATION);
        intakeRoller.configContinuousCurrentLimit(MotorConstants.Constants.CONTINOUS_CURRENT_LIMIT);
        intakeRoller.configOpenloopRamp(MotorConstants.Constants.OPEN_LOOP_RAMP);
        intakeRoller.enableCurrentLimit(true);

        intakeRoller.configPeakCurrentLimit(MotorConstants.getMotorStallCurrent(MotorConstants.Constants.MotorType.kBagMotor, 0.3));
        intakeRoller.configPeakCurrentDuration(MotorConstants.Constants.PEAK_CURRENT_DURATION);
        intakeRoller.configContinuousCurrentLimit(MotorConstants.Constants.CONTINOUS_CURRENT_LIMIT);
        intakeRoller.configOpenloopRamp(MotorConstants.Constants.OPEN_LOOP_RAMP);
        intakeRoller.enableCurrentLimit(true);

        armMotor.setSensorPhase(true);
        armMotor.setInverted(InvertType.InvertMotorOutput);

        armMotor.configForwardLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled);
        armMotor.configReverseLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled);
 
        armMotor.configSetParameter(ParamEnum.eFeedbackNotContinuous, 1, 0, 0, 0);

        armMotor.configSelectedFeedbackSensor(FeedbackDevice.Analog);

        
    
        System.out.println(this.getClass().getName() + ": Finished Constructing");
    }

    /**
     * This function returns the instance of the Arm to be used
     * 
     * @return instance of the arm that is going to be used
     */
    public static Arm getInstance()
    {
        return (instance);
    }

    /**
     * this function moves the arm up at a speed of .5
     */
    public void moveArmUp()
    {
        armMotor.set(speedFactor * .5);
    }

    /**
     * moves arm up at given speed
     * @param speed
     */
    public void moveArmUp(double speed)
    {
        armMotor.set(speedFactor * Math.abs(speed));
    }

    /**
     * this function moves the arm down at a speed of -.5
     */
    public void moveArmDown()
    {
        armMotor.set(speedFactor * -.25);
    }

    public double getArmCurrent()
    {
        return armMotor.getOutputCurrent();
    }

     /**
     * moves arm down at given speed
     * @param speed
     */
    public void moveArmDown(double speed)
    {
        armMotor.set(speedFactor * -Math.abs(speed));
    }

    /**
     * this function will stop the movement of the arm
     */
    public void stopArm()
    {
        armMotor.set(0);
    }

    /**
     * this will set the solenoid controlling wrist movement to move forward, which
     * moves the wrist Up
     */
    public void moveWristUp()
    {
        wristSolenoid.set(Value.kForward);
    }

    /**
     * this function sets the solenoid contrlling the wrist movemet to move
     * backwards, which will move the wrist down
     */
    public void moveWristDown()
    {
        wristSolenoid.set(Value.kReverse);
    }

    /**
     * this function will sets the motors that control cargo intake/ejection to
     * intake cargo at a set speed
     * 
     * @param speed
     */
    public void intakeCargo(double speed)
    {
        speed = Math.abs(speed);
        intakeRoller.set(speed);
    }

    /**
     * this function will set the motors that control cargo intake/ejectin to a
     * eject that cargo at a set speed.
     * 
     * @param speed
     */
    public void ejectCargo(double speed)
    {
        speed = -Math.abs(speed);
        intakeRoller.set(speed);
    }

    /**
     * this function sets the motors that control the cargo intake/ejection to 0 so
     * they stop rotating.
     */
    public void stopCargo()
    {
        intakeRoller.set(0);
    }

    /**
     * this function sets the solenoid that expands the HPP
     */
    public void grabHatchPanel()
    {
        if(!isGrabberMoving)
        {
            grabberTimer.reset();
            grabberTimer.start();
            grabberSolenoid.set(Value.kForward);
            isGrabberMoving = true;
        }
        
        if(grabberTimer.get() > 0.5)
        {
            isGrabberMoving = false;
        }
    }

    /**
     * this function sets the solenoid that contracts the HPP
     */
    public void releaseHatchPanel()
    {
        if(!isGrabberMoving)
        {
            grabberTimer.reset();
            grabberTimer.start();
            grabberSolenoid.set(Value.kReverse);
            isGrabberMoving = true;
        }
        
        if(grabberTimer.get() > 0.5)
        {
            isGrabberMoving = false;
        }
    }

    /**
     * @return the isGrabberMoving, whether the grabber is moving or not
     */
    public boolean isGrabberMoving()
    {
        return isGrabberMoving;
    }

    /**
     * this function gets the potentiometer value that moniters the arms position
     * 
     * @return returns the potentiometer value for the arm
     */
    public int getPotValue()
    {
        return armMotor.getSelectedSensorPosition();   // Subtract a number from this to flip the directions (Down is low num, up is high num)
    }

    /**
     * this function sets the target position to be used later in the program
     * 
     * @param targetPosition have to send the target position
     */


    /**
     * gets the wrist position
     * @param position
     * @return position of the wrist
     */
    public Constants.WristPosition getWristPosition(Constants.Position position)
    {
        return position.wristPosition;
    }

    /**
     * gets the target position for the arm
     * @param position
     * @return the target arm position
     */
    public int getTargetPositionArmPositionValue(Constants.Position position)
    {
        return position.armPosition.armPosition;
    }

    /**
     * sets the position for the wrist in the position enum
     */
    public Constants.Position setWristPosition(Constants.Position position, Constants.WristPosition wristPosition)
    {
        position.wristPosition = wristPosition;

        return position;
    }

    /**
     * sets the robot we are using (competition or practice)
     * @param robotType
     */
    public void setRobotType(SlabShuffleboard.RobotType robotType)
    {
        if (robotType == SlabShuffleboard.RobotType.kCompetition)
        {
            armPositionPotValues = Constants.COMPETITION_ARM_POSITION_POT_VALUES;
        }
        else
        {
            armPositionPotValues = Constants.PRACTICE_ARM_POSITION_POT_VALUES;
        }
    }

    public void setMotorSpeedFactor(SlabShuffleboard.MotorSpeed speedFactor)
    {
        this.speedFactor = speedFactor.value;
    }

    /**
     * Returns the pot value of the given position
     * 
     * @param position which position to return:
     * <p> 0: Floor
     * <p> 1: Horizontal
     * <p> 2: Middle
     * <p> 3: Top
     * 
     * @return pot value of the selected position
     */
    public int getArmPositionPotValue(int position)
    {
        return armPositionPotValues[position];
    }

    /**
     * checks if the wrist is moving
     */
    public boolean isWristMoving()
    {
        return isWristMoving;
    }

    /**
     * sets if the wrist is moving
     */
    public void setIsWristMoving(boolean isWristMoving)
    {
        this.isWristMoving = isWristMoving;
    }

    /**
     * checks if the arm is moving
     */
    public boolean isArmMoving()
    {
        return isArmMoving;
    }

    /**
     * sets if the arm is moving
     */
    public void setIsArmMoving(boolean isArmMoving)
    {
        this.isArmMoving = isArmMoving;
    }

    public double getIntakeAmperage()
    {
        return intakeRoller.getOutputCurrent();
    }

    /**
     * checks if the wrist is down
     */
    public boolean isWristDown()
    {
        return armMotor.getSensorCollection().isRevLimitSwitchClosed();
    }

    /**
     * checks if the wrist is up
     */
    public boolean isWristUp()
    {
        return armMotor.getSensorCollection().isFwdLimitSwitchClosed();
    }

    @Deprecated
    /**
     * converts angle value to pot ticks (bad math by darryl)
     */
    private static int angleToTicks(double angle)
    {
        return (int)((500.0 * angle / 360.0) / (1000.0 + (500.0 * angle / 360.0)) * 1024.0);
    }


    public void teleop()
    {

    }

    public String getArmMotorData()
    {
        return String.format("%6.3f,  %6d,  %6.3f,  %5.1f",
         armMotor.get(), armMotor.getSelectedSensorPosition(),
         armMotor.getOutputCurrent(), armMotor.getTemperature() * (9.0 / 5.0) + 32.0);
    }

    public String getIntakeRollerMotorData()
    {
        return String.format("%6.3f,  %6d,  %6.3f,  %5.1f",
         intakeRoller.get(), intakeRoller.getSelectedSensorPosition(),
         intakeRoller.getOutputCurrent(), intakeRoller.getTemperature() * (9.0 / 5.0) + 32.0);
    }

    public String getWristSolenoidData()
    {
        return wristSolenoid.get().toString();
    }

    public String getGrabberSolenoidData()
    {
        return grabberSolenoid.get().toString();
    }

    @Override
    public String toString()
    {
        return String.format("Arm Pot Value: " + getPotValue());
    }

    public static class Constants
    {
        public static enum ArmPosition
        {
            kFloorArmPosition(armPositionPotValues[0], "Floor Arm Position"), 
            kHorizontalArmPosition(armPositionPotValues[1], "Horizontal Arm Position"), 
            kMiddleArmPosition(armPositionPotValues[2], "Middle Arm Position"),
            kTopArmPosition(armPositionPotValues[3], "Top Arm Position"),
            kArmNone(-1, "Arm to None");

            private int armPosition;
            private final String name;

            private ArmPosition(int armPosition, String name)
            {
                this.armPosition = armPosition;
                this.name = name;
            }

            @Override
            public String toString()
            {
                return (name);
            }
        }

        public static enum WristPosition
        {
            kWristDown(DoubleSolenoid.Value.kReverse, "Wrist Down"), 
            kWristUp(DoubleSolenoid.Value.kForward, "Wrist Up"), 
            kWristNone(DoubleSolenoid.Value.kOff, "Wrist None");

            private DoubleSolenoid.Value wristPosition;
            private final String name;

            private WristPosition(DoubleSolenoid.Value wristPosition, String name)
            {
                this.wristPosition = wristPosition;
                this.name = name;
            }

            @Override
            public String toString()
            {
                return (name);
            }
        }

        public static enum Position
        {
            kFloor(Constants.WristPosition.kWristDown, Constants.ArmPosition.kFloorArmPosition, "Floor"),              
            kCargoShipCargo(Constants.WristPosition.kWristDown, Constants.ArmPosition.kHorizontalArmPosition, "Cargo Ship Cargo"),
            kBottomHatch(Constants.WristPosition.kWristUp, Constants.ArmPosition.kHorizontalArmPosition, "Bottom Hatch"), // 1 ft 7 inches to center
            kCenterHatch(Constants.WristPosition.kWristUp, Constants.ArmPosition.kHorizontalArmPosition, "Center Hatch"), // 3 ft 11 inches to center
            kTopHatch(Constants.WristPosition.kWristUp, Constants.ArmPosition.kMiddleArmPosition, "Top Hatch"), // 6 ft 3 inches to center
            kBottomCargo(Constants.WristPosition.kWristDown, Constants.ArmPosition.kHorizontalArmPosition, "Bottom Cargo"), // 2 ft 3.5 inches to center
            kCenterCargo(Constants.WristPosition.kWristDown, Constants.ArmPosition.kHorizontalArmPosition, "Center Cargo"), // 4 ft 7.5 inches to center
            kTopCargo(Constants.WristPosition.kWristDown, Constants.ArmPosition.kMiddleArmPosition, "Top Cargo"), // 6 ft 11.5 inches to center
            kDrive(Constants.WristPosition.kWristUp, Constants.ArmPosition.kMiddleArmPosition, "Driving"), 
            kNone(Constants.WristPosition.kWristNone, Constants.ArmPosition.kArmNone, "No Target");

            private Constants.ArmPosition armPosition;
            private Constants.WristPosition wristPosition;
            private final String name;

            private Position(Constants.WristPosition wristPosition, Constants.ArmPosition armPosition, String name)
            {
                this.wristPosition = wristPosition;
                this.armPosition = armPosition;
                this.name = name;
            }

            @Override
            public String toString()
            {
                return name;
            }
        }

        public static final int HORIZONTAL_TO_FLOOR = angleToTicks(-30); // -30 degrees
        public static final int HORIZONTAL_TO_MIDDLE = angleToTicks(45); // 45 degrees
        public static final int HORIZONTAL_TO_TOP = angleToTicks(90); // 90 degrees

        public static final int WRIST_SOLENOID_PORT_1 = 4;
        public static final int WRIST_SOLENOID_PORT_2 = 5;
        public static final int GRABBER_SOLENOID_PORT_1 = 2;
        public static final int GRABBER_SOLENOID_PORT_2 = 3;
        public static final int ROLLER_TALON_ID = 12;
        public static final int ARM_MOTOR_ID = 9;

        public static final int ARM_THRESHOLD = 5;


        // 0: Floor
        // 1: Horizontal
        // 2: Middle
        // 3: Top
        public static final int[] COMPETITION_ARM_POSITION_POT_VALUES = {670, 597, 502, 283};
        public static final int[] PRACTICE_ARM_POSITION_POT_VALUES = {628, 572, 452, 212};


    }

}
