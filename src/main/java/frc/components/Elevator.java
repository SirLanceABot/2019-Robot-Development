/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.components;

import frc.components.Elevator.Constants.ElevatorPosition;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import frc.robot.SlabShuffleboard;
import frc.components.Arm;

import com.ctre.phoenix.motorcontrol.NeutralMode;

/**
 * Add your docs here.
 */
public class Elevator
{
    private WPI_TalonSRX masterElevatorMotor = new WPI_TalonSRX(Constants.MASTER_ELEVATOR_MOTOR_PORT);
    private WPI_VictorSPX slaveElevatorMotor = new WPI_VictorSPX(Constants.SLAVE_ELEVATOR_MOTOR_PORT);


    private boolean isMoving = false;
    private Constants.ElevatorPosition targetPosition = Constants.ElevatorPosition.kNone;
    private int elevatorPotValue;
    private int armPotValue;
    private static int initialElevatorPosition = 100;

   
    // private Arm arm = Arm.getInstance();
    private static Elevator instance = new Elevator();

    public static Elevator getInstance()
	{
		return instance;
    }
    
    private Elevator()
	{
        masterElevatorMotor.configFactoryDefault();
        slaveElevatorMotor.configFactoryDefault();

        masterElevatorMotor.setNeutralMode(NeutralMode.Brake);
        slaveElevatorMotor.setNeutralMode(NeutralMode.Brake);

        masterElevatorMotor.configSelectedFeedbackSensor(com.ctre.phoenix.motorcontrol.FeedbackDevice.Analog, 0, 0);

        masterElevatorMotor.configReverseSoftLimitThreshold(Constants.ElevatorPosition.kMinHeight.position, 0);
        masterElevatorMotor.configForwardSoftLimitThreshold(Constants.ElevatorPosition.kMaxHeight.position, 0);
        
        masterElevatorMotor.configForwardSoftLimitEnable(true, 0);
        masterElevatorMotor.configReverseSoftLimitEnable(true, 0);

        masterElevatorMotor.configPeakCurrentLimit(Constants.PEAK_CURRENT_LIMIT);
        masterElevatorMotor.configPeakCurrentDuration(Constants.PEAK_CURRENT_DURATION);
        masterElevatorMotor.configContinuousCurrentLimit(Constants.CONTINOUS_CURRENT_LIMIT);
        masterElevatorMotor.configOpenloopRamp(Constants.OPEN_LOOP_RAMP);
        masterElevatorMotor.enableCurrentLimit(true);
        
        slaveElevatorMotor.follow(masterElevatorMotor);
    }

    /**
     * Raises the Elevator at a constant speed
     */
    public void raiseElevator()
    {
        masterElevatorMotor.set(Constants.ELEVATOR_SPEED);
    }

    /**
     * Lowers the Elevator at a constant speed
     */
    public void lowerElevator()
    {
        masterElevatorMotor.set(-Constants.ELEVATOR_SPEED);
    }

    /**
     * Stops the Elevator motor
     */
    public void stopElevator()
    {
        masterElevatorMotor.set(0);
    }

    /**
     * Returns the value of the potentiometer
     * 
     * @return Value of Potentiometer
     */
    public int getPotValue()
	{
		return masterElevatorMotor.getSelectedSensorPosition(0);
    }

    /**
     * Returns a boolean that says whether or not the elevator is moving
     * 
     * @return whether the elevator is moving or not
     */
    public boolean isMoving()
    {
        return isMoving;
    }

    /**
     * Sets the target position for the elevator
     * 
     * @param targetPosition the location the elevator is attempting to move to
     */
    public void setTargetPosition(ElevatorPosition targetPosition)
    {
        this.targetPosition = targetPosition;
    }

    /**
     * Returns the current target position of the elevator
     * 
     * @return the target position the elevator is attempting to move to
     */
    public ElevatorPosition getTargetPosition()
    {
       return targetPosition;
    }

    public void setRobotType(SlabShuffleboard.RobotType robotType)
    {
        if(robotType == SlabShuffleboard.RobotType.kCompetition)
        {
            initialElevatorPosition = Constants.COMPETITION_INITIAL_ELEVATOR_POSITION;
        }
        else
        {
            initialElevatorPosition = Constants.PRACTICE_INITIAL_ELEVATOR_POSITION;
        }
    }

    public int getInitialElevatorPosition()
    {
        return initialElevatorPosition;
    }

    private static int inchesToTicks(double inches)
    {
        return (int)((1.00/8.3) * inches * 100); 
    }
    
    /**
     * Moves the elevator to the current set target position. 
     * Checks to see if the target position is above or below the current elevator position.
     * Raises elevator if above and lowers elevator if below.
     * 
     * Also updates the integer potValue to equal the current value of the potentiometer.
     */
    public void moveTo()
    {
        elevatorPotValue = getPotValue();
        //armPotValue = arm.getPotValue();

        if(targetPosition != ElevatorPosition.kNone)
        {
             if(false)// armPotValue < arm.getHorizontalArmPosition() + 200)
            {
                stopElevator();
                isMoving = true;
            }
            else if(elevatorPotValue < (targetPosition.position - ElevatorPosition.kThreshold.position))
            {
                raiseElevator();
                isMoving = true;
            }
            else if(elevatorPotValue > (targetPosition.position + ElevatorPosition.kThreshold.position))
            {
                isMoving = true;
                if(false)//arm.getWristTargetPosition() == Arm.Constants.WristPosition.kWristDown)
                {
                    if(false)//elevatorPotValue > initialElevatorPosition || armPotValue > arm.getHorizontalArmPosition())
                    {
                        lowerElevator();
                    }
                    else
                    {
                        stopElevator();
                    }
                }
                else
                {       
                    lowerElevator();
                }
            }
            else
            {
                stopElevator();
                targetPosition = ElevatorPosition.kNone;
                isMoving = false;
            }
        }
    }   

    @Override
    public String toString()
    {
        return String.format("Pot Value: %d", getPotValue());
    }

    public static class Constants
    {
        public static enum ElevatorPosition
		{
            kInitialHeight(initialElevatorPosition, "Initial Height"),
            kMinHeight(initialElevatorPosition + INITIAL_HEIGHT_TO_MIN_HEIGHT, "Min Height"),
            kMaxHeight(initialElevatorPosition + INITIAL_HEIGHT_TO_MAX_HEIGHT, "Max Height"),
            kFloor(initialElevatorPosition + INITIAL_HEIGHT_TO_FLOOR, "Floor"),
            kCargoShipCargo(initialElevatorPosition + INITIAL_HEIGHT_TO_CARGO_SHIP_CARGO, "Cargo Ship Cargo"),
            kThreshold(5, "Threshold"),

            kBottomHatch(initialElevatorPosition + INITIAL_HEIGHT_TO_BOTTOM_HATCH, "Bottom Hatch"),      // 1 ft 7 inches to center
            kCenterHatch(initialElevatorPosition + INITIAL_HEIGHT_TO_CENTER_HATCH, "Center Hatch"),      // 3 ft 11 inches to center
            kTopHatch(initialElevatorPosition + INITIAL_HEIGHT_TO_TOP_HATCH, "Top Hatch"),         // 6 ft 3 inches to center

            kBottomCargo(initialElevatorPosition + INITIAL_HEIGHT_TO_BOTTOM_CARGO, "Bottom Cargo"),       // 2 ft 3.5 inches to center
            kCenterCargo(initialElevatorPosition + INITIAL_HEIGHT_TO_CENTER_CARGO, "Center Cargo"),       // 4 ft 7.5 inches to center
            kTopCargo(initialElevatorPosition + INITIAL_HEIGHT_TO_TOP_CARGO, "Top Cargo"),          // 6 ft 11.5 inches to center
            kNone(-1, "None");

            
            private final int position;
            private final String name;

            private ElevatorPosition(int value, String name)
            {
                this.position = value;
                this.name = name;
            }

            @Override
            public String toString()
            {
                return name;
            }
        }
    
        public static final int INITIAL_HEIGHT_TO_MIN_HEIGHT = inchesToTicks(-10);
        public static final int INITIAL_HEIGHT_TO_MAX_HEIGHT = inchesToTicks(85);
        public static final int INITIAL_HEIGHT_TO_FLOOR = inchesToTicks(-5);
        public static final int INITIAL_HEIGHT_TO_CARGO_SHIP_CARGO = inchesToTicks(10);
        public static final int INITIAL_HEIGHT_TO_BOTTOM_HATCH = inchesToTicks(5);
        public static final int INITIAL_HEIGHT_TO_CENTER_HATCH = inchesToTicks(15);
        public static final int INITIAL_HEIGHT_TO_TOP_HATCH = inchesToTicks(25);
        public static final int INITIAL_HEIGHT_TO_BOTTOM_CARGO = inchesToTicks(10);
        public static final int INITIAL_HEIGHT_TO_CENTER_CARGO = inchesToTicks(20);
        public static final int INITIAL_HEIGHT_TO_TOP_CARGO = inchesToTicks(30);

        public static final int MASTER_ELEVATOR_MOTOR_PORT = 10;
        public static final int SLAVE_ELEVATOR_MOTOR_PORT = 11;

        public static final double ELEVATOR_SPEED = 0.25;

        public static final int PEAK_CURRENT_LIMIT = 20;        // In Amps
        public static final int PEAK_CURRENT_DURATION = 250;    // In milliseconds
        public static final int CONTINOUS_CURRENT_LIMIT = 20;   // In Amps
        public static final double OPEN_LOOP_RAMP = 0.05;       // In seconds

        public static final int COMPETITION_INITIAL_ELEVATOR_POSITION = 200;
        public static final int PRACTICE_INITIAL_ELEVATOR_POSITION = 200;
    }
}