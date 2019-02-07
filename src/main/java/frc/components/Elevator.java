/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.components;

import frc.components.Elevator.Constants.ElevatorPosition;
import frc.control.ButtonBoard;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

/**
 * Add your docs here.
 */
public class Elevator
{
    private WPI_TalonSRX masterElevatorMotor = new WPI_TalonSRX(Constants.MASTER_ELEVATOR_MOTOR_PORT);
    private WPI_VictorSPX slaveElevatorMotor = new WPI_VictorSPX(Constants.SLAVE_ELEVATOR_MOTOR_PORT);

    private ButtonBoard buttonBoard = ButtonBoard.getInstance();

    private boolean isMoving = false;
    private Constants.ElevatorPosition targetPosition = Constants.ElevatorPosition.kNone;
    private int potValue;

    private static Elevator instance = new Elevator();

    public static Elevator getInstance()
	{
		return instance;
    }
    
    private Elevator()
	{
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
    
    /**
     * Moves the elevator to the current set target position. 
     * Checks to see if the target position is above or below the current elevator position.
     * Raises elevator if above and lowers elevator if below.
     * 
     * Also updates the integer potValue to equal the current value of the potentiometer.
     */
    public void moveTo()
    {
        potValue = getPotValue();

        if(!targetPosition.equals(ElevatorPosition.kNone))
        {
            if(potValue < (targetPosition.position - ElevatorPosition.kThreshold.position))
            {
                raiseElevator();
                isMoving = true;
            }
            else if(potValue > (targetPosition.position + ElevatorPosition.kThreshold.position))
            {
                lowerElevator();
                isMoving = true;
            }
            else
            {
                stopElevator();
                targetPosition = ElevatorPosition.kNone;
                isMoving = false;
            }
        }
        /*else  // For manual override of elevator
        {
            masterElevatorMotor.set(yAxis);
        }*/
    }   

    public void teleop()
    {
        boolean floorButton = buttonBoard.getRawButton(ButtonBoard.Constants.FLOOR_BUTTON);
        boolean cargoShipCargoButton = buttonBoard.getRawButton(ButtonBoard.Constants.CARGO_SHIP_CARGO_BUTTON);

        boolean bottomHatchButton = buttonBoard.getRawButton(ButtonBoard.Constants.BOTTOM_HATCH_BUTTON);
        boolean centerHatchButton = buttonBoard.getRawButton(ButtonBoard.Constants.CENTER_HATCH_BUTTON);
        boolean topHatchButton = buttonBoard.getRawButton(ButtonBoard.Constants.TOP_HATCH_BUTTON);

        boolean bottomCargoButton = buttonBoard.getRawButton(ButtonBoard.Constants.BOTTOM_CARGO_BUTTON);
        boolean centerCargoButton = buttonBoard.getRawButton(ButtonBoard.Constants.CENTER_CARGO_BUTTON);
        boolean topCargoButton = buttonBoard.getRawButton(ButtonBoard.Constants.TOP_CARGO_BUTTON);

       // yAxis = -operatorXbox.getRawAxis(1);


        if(floorButton)
        {
            targetPosition = ElevatorPosition.kFloor;
        }
        else if(cargoShipCargoButton)
        {
            targetPosition = ElevatorPosition.kCargoShipCargo;
        }
        else if(bottomHatchButton)
        {
            targetPosition = ElevatorPosition.kBottomHatch;
        }
        else if(centerHatchButton)
        {
            targetPosition = ElevatorPosition.kCenterHatch;
        }
        else if(topHatchButton)
        {
            targetPosition = ElevatorPosition.kTopHatch;
        }
        else if(bottomCargoButton)
        {
            targetPosition = ElevatorPosition.kBottomCargo;
        }
        else if(centerCargoButton)
        {
            targetPosition = ElevatorPosition.kCenterCargo;
        }
        else if(topCargoButton)
        {
            targetPosition = ElevatorPosition.kTopCargo;
        }

        moveTo();
    }

    public void autonomous()
    {

    }

    public void test()
    {

    }

    @Override
    public String toString()
    {
        return String.format("Pot Value: %d    Target Position: %d    Elevator Current: %.2f", getPotValue(), targetPosition.position, masterElevatorMotor.getOutputCurrent());
    }

    public static class Constants
    {
        public static enum ElevatorPosition
		{
            kMinHeight(0, "Min Height"),
            kMaxHeight(60, "Max Height"),
            kFloor(5, "Floor"),
            kCargoShipCargo(15, "Cargo Ship Cargo"),
            kThreshold(5, "Threshold"),

            kBottomHatch(10, "Bottom Hatch"),      // 1 ft 7 inches to center
            kCenterHatch(30, "Center Hatch"),      // 3 ft 11 inches to center
            kTopHatch(50, "Top Hatch"),         // 6 ft 3 inches to center

            kBottomCargo(20, "Bottom Cargo"),       // 2 ft 3.5 inches to center
            kCenterCargo(40, "Center Cargo"),       // 4 ft 7.5 inches to center
            kTopCargo(60, "Top Cargo"),          // 6 ft 11.5 inches to center
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
    

        public static final int MASTER_ELEVATOR_MOTOR_PORT = 0;
        public static final int SLAVE_ELEVATOR_MOTOR_PORT = 0;

        public static final double ELEVATOR_SPEED = 0.25;

        public static final int PEAK_CURRENT_LIMIT = 20;        // In Amps
        public static final int PEAK_CURRENT_DURATION = 250;    // In milliseconds
        public static final int CONTINOUS_CURRENT_LIMIT = 20;   // In Amps
        public static final double OPEN_LOOP_RAMP = 0.05;       // IN seconds
    }
}