/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.components;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.control.DriverXbox;
import frc.control.Xbox;
import frc.components.Arm.Constants.Position;
import frc.control.ButtonBoard;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.motorcontrol.NeutralMode;

/**
 * Add your docs here.
 */
public class Arm
{
    private DoubleSolenoid armSolenoid = new DoubleSolenoid(Constants.ARM_SOLENOID_PORT_1,
            Constants.ARM_SOLENOID_PORT_2);
    private DoubleSolenoid wristSolenoid = new DoubleSolenoid(Constants.WRIST_SOLENOID_PORT_1,
            Constants.WRIST_SOLENOID_PORT_2);
    private DoubleSolenoid grabberSolenoid = new DoubleSolenoid(Constants.GRABBER_SOLENOID_PORT_1,
            Constants.GRABBER_SOLENOID_PORT_2);

    private WPI_TalonSRX armMotor = new WPI_TalonSRX(Constants.ARM_MOTOR_ID);

    private DigitalInput wristUpLimitSwitch = new DigitalInput(0);
    private DigitalInput wristDownLimitSwitch = new DigitalInput(1);

    private Constants.Position targetPosition =  Constants.Position.kNone;

    private WPI_TalonSRX masterIntakeRoller = new WPI_TalonSRX(Constants.ROLLER_TALON_ID);
    private WPI_VictorSPX slaveIntakeRoller = new WPI_VictorSPX(Constants.ROLLER_VICTOR_ID);
    private boolean armPosition; // true is up false is down
    private boolean wristPosition; // true is up false is down
    private boolean hatchPanelPosition; // true for expanded false for contracted
    private boolean isWristMoving;
    private boolean isArmMoving;
    private int potValue = 0;
    private static DriverXbox driverXbox = DriverXbox.getInstance();
    private static ButtonBoard buttonBoard = ButtonBoard.getInstance();

    private static int horizontalArmPosition = 0;
    private static int armRaisedPosition = 0;

    private static Arm instance = new Arm();

    private Arm()
    {
        masterIntakeRoller.configFactoryDefault();
        slaveIntakeRoller.configFactoryDefault();
        armMotor.configFactoryDefault();

        armMotor.setNeutralMode(NeutralMode.Brake);
        masterIntakeRoller.setNeutralMode(NeutralMode.Brake);
        slaveIntakeRoller.setNeutralMode(NeutralMode.Brake);

        slaveIntakeRoller.follow(masterIntakeRoller);
        armMotor.configSelectedFeedbackSensor(com.ctre.phoenix.motorcontrol.FeedbackDevice.Analog, 0, 0);
    }

    /**
     * This function returns the instance of the Arm to be used
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
        armMotor.set(.5);
    }

    /**
     * this function moves the arm down at a speed of -.5
     */
    public void moveArmDown()
    {
        armMotor.set(-.5);
    }

    /**
     * this function will stop the movement of the arm
     */
    public void stopArm()
    {
        armMotor.set(0);
    }

    /**
     * this will set the solenoid controlling wrist movement
     * to move forward, which moves the wrist Up
     */
    public void moveWristUp()
    {
        wristSolenoid.set(Value.kForward);
        wristPosition = true;
    }

    /**
     * this function sets the solenoid contrlling the wrist movemet
     * to move backwards, which will move the wrist down
     */
    public void moveWristDown()
    {
        wristSolenoid.set(Value.kReverse);
        wristPosition = false;
    }

    /**
     * this function will sets the motors that
     * control cargo intake/ejection to intake
     * cargo at a set speed
     * @param speed
     */
    public void intakeCargo(double speed)
    {
        speed = Math.abs(speed);
        masterIntakeRoller.set(speed);
    }

    /**
     * this function will set the motors that
     * control cargo intake/ejectin to a eject that
     * cargo at a set speed.
     * @param speed
     */
    public void ejectCargo(double speed)
    {
        speed = -Math.abs(speed);
        masterIntakeRoller.set(speed);
    }

    /**
     * this function sets the motors that control the cargo
     * intake/ejection to 0 so they stop rotating.
     */
    public void stopCargo()
    {
        masterIntakeRoller.set(0);
    }

    /**
     * this function sets the solenoid that
     * expands the HPP
     */
    public void grabHatchPanel()
    {
        grabberSolenoid.set(Value.kForward);
        hatchPanelPosition = true;
    }

    /**
     * this function sets the solenoid that 
     * contracts the HPP 
     */
    public void releaseHatchPanel()
    {
        grabberSolenoid.set(Value.kReverse);
        hatchPanelPosition = false;
    }


    /**
     * this function gets the potentiometer value that
     * moniters the arms position
     * @return returns the potentiometer value for the arm
     */
    public int getPotValue()
    {
        return armMotor.getSelectedSensorPosition(0);
    }

    /**
     * this function sets the target position to be used later
     * in the program
     * @param targetPosition have to send the target position
     */
    public void setTargetPosition(Constants.Position targetPosition)
    {
        this.targetPosition = targetPosition;
    }

    public static void setRobotType(boolean isCompetitionBot)
    {
        if(isCompetitionBot)
        {
            horizontalArmPosition = Constants.COMPETITION_HORIZONTAL_ARM_POSITION;
            armRaisedPosition = Constants.COMPETITION_ARM_RAISED_POSITION;
        }
        else
        {
            horizontalArmPosition = Constants.PRACTICE_HORIZONTAL_ARM_POSITION;
            armRaisedPosition = Constants.PRACTICE_ARM_RAISED_POSITION;
        }
    }

    public int getArmRaisedPosition()
    {
        return armRaisedPosition;
    }

    /**
     * this function will move to the position that has been specified 
     * by the variabe targetPosition
     */
    public void moveTo()
    {
        potValue = getPotValue();

        if(!targetPosition.equals(Constants.Position.kNone))
        {
            if(potValue < targetPosition.armPosition.armPosition - Constants.ARM_THRESHOLD)
            {
                moveArmUp();
                isArmMoving = true;
            }

            else if(potValue > targetPosition.armPosition.armPosition + Constants.ARM_THRESHOLD)
            {
                moveArmDown();
                isArmMoving = true;
            }

            else
            {
                stopArm();
                isArmMoving = false;
            }

            if(targetPosition.wristPosition.wristPosition == Value.kReverse)
            {
                moveWristDown();
                isWristMoving = true;

                if(wristDownLimitSwitch.get() == false)
                {
                    isWristMoving = false;
                }
            }

            else if(targetPosition.wristPosition.wristPosition == Value.kForward)
            {
                moveWristUp();
                isWristMoving = true;

                if(wristUpLimitSwitch.get() == false)
                {
                    isWristMoving = false;
                }
            }

            if((isWristMoving == false) && (isArmMoving == false))
            {
                targetPosition = Position.kNone;
            }
        }
    }

    public void teleop()
    {
       
    }

    @Override
    public String toString()
    {
        return String.format(
                "ArmPosition: %s, HPP: %s, WristPosition: %s, (true is up/expanded false is down/contracted",
                isArmMoving, hatchPanelPosition , isWristMoving);
        // return String.format("Arm Pot Value: " + getPotValue() + "   Arm Target Position:   " + targetPosition.armPosition.armPosition + "  Wrist Moving:  " + isWristMoving);
    }

    public static class Constants
    {
        public static enum ArmPosition
        {
            kFloorArmPosition(horizontalArmPosition - 50, "Floor Arm Position"),
            kHorizontalArmPosition(horizontalArmPosition, "Horizontal Arm Position"),  
            kMiddleArmPosition(horizontalArmPosition + 50,"Middle Arm Position"),
            kTopArmPosition(horizontalArmPosition + 100, "Top Arm Position"),

            kArmNone(-1, "Arm to None");


            private final int armPosition;
            private final String name;
            private ArmPosition(int armPosition, String name)
            {
                this.armPosition = armPosition;
                this.name = name;
            }  

            @Override
            public String toString()
            {
                return(name);
            }
        }
        public static enum WristPosition
        {
            kWristDown(DoubleSolenoid.Value.kReverse, "Wrist Down"),
            kWristUp(DoubleSolenoid.Value.kForward, "Wrist Up"),
            kWristNone(DoubleSolenoid.Value.kOff, "Wrist None");

            private final DoubleSolenoid.Value wristPosition;
            private final String name;
            private WristPosition(DoubleSolenoid.Value wristPosition, String name)
            {
                this.wristPosition = wristPosition;
                this.name = name;
            }

            @Override
            public String toString()
            {
                return(name);
            }
        }
        public static enum Position
		{
            kFloor(Constants.WristPosition.kWristDown, Constants.ArmPosition.kFloorArmPosition, "Floor"),
            kCargoShipCargo(Constants.WristPosition.kWristDown, Constants.ArmPosition.kHorizontalArmPosition, "Cargo Ship Cargo"),

            kBottomHatch(Constants.WristPosition.kWristUp, Constants.ArmPosition.kHorizontalArmPosition, "Bottom Hatch"),      // 1 ft 7 inches to center
            kCenterHatch(Constants.WristPosition.kWristUp, Constants.ArmPosition.kHorizontalArmPosition, "Center Hatch"),      // 3 ft 11 inches to center
            kTopHatch(Constants.WristPosition.kWristUp, Constants.ArmPosition.kMiddleArmPosition, "Top Hatch"),         // 6 ft 3 inches to center

            kBottomCargo(Constants.WristPosition.kWristDown, Constants.ArmPosition.kHorizontalArmPosition, "Bottom Cargo"),       // 2 ft 3.5 inches to center
            kCenterCargo(Constants.WristPosition.kWristDown, Constants.ArmPosition.kHorizontalArmPosition, "Center Cargo"),       // 4 ft 7.5 inches to center
            kTopCargo(Constants.WristPosition.kWristDown,Constants.ArmPosition.kMiddleArmPosition, "Top Cargo"),          // 6 ft 11.5 inches to center

            kDrive(Constants.WristPosition.kWristUp, Constants.ArmPosition.kMiddleArmPosition, "Driving"),
            kNone(Constants.WristPosition.kWristNone, Constants.ArmPosition.kArmNone, "No Target");
            private final Constants.ArmPosition armPosition;
            private final Constants.WristPosition wristPosition;
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

        public static final int ARM_SOLENOID_PORT_1 = 4;
        public static final int ARM_SOLENOID_PORT_2 = 5;
        public static final int WRIST_SOLENOID_PORT_1 = 0;
        public static final int WRIST_SOLENOID_PORT_2 = 1;
        public static final int GRABBER_SOLENOID_PORT_1 = 2;
        public static final int GRABBER_SOLENOID_PORT_2 = 3;
        public static final int ROLLER_TALON_ID = 1;
        public static final int ROLLER_VICTOR_ID = 1;
        public static final int ARM_MOTOR_ID = 2;

        public static final int ARM_THRESHOLD = 5;

        public static final int COMPETITION_HORIZONTAL_ARM_POSITION = 0;
        public static final int PRACTICE_HORIZONTAL_ARM_POSITION = 0;

        public static final int COMPETITION_ARM_RAISED_POSITION = 200;
        public static final int PRACTICE_ARM_RAISED_POSITION = 200;
    }

}
