/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.components;

import frc.control.DriverXbox;
import frc.control.OperatorXbox;
import frc.control.Xbox;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

/**
 * Add your docs here.
 */
public class Elevator
{
    private WPI_TalonSRX masterElevatorMotor = new WPI_TalonSRX(Constants.MASTER_ELEVATOR_MOTOR_PORT);
    private VictorSPX slaveElevatorMotor = new WPI_VictorSPX(Constants.SLAVE_ELEVATOR_MOTOR_PORT);

    private OperatorXbox operatorXbox = OperatorXbox.getInstance();
    private DriverXbox driverXbox = DriverXbox.getInstance();
    
    private boolean floorPanelButton;
    private boolean floorCargoButton;

    private boolean bottomHatchButton;
    private boolean centerHatchButton;
    private boolean topHatchButton;

    private boolean bottomPortButton;
    private boolean centerPortButton;
    private boolean topPortButton;

    private double yAxis;

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
        slaveElevatorMotor.follow(masterElevatorMotor);

        masterElevatorMotor.configReverseSoftLimitThreshold(Constants.ElevatorPosition.kMinHeight.position, 0);
        masterElevatorMotor.configForwardSoftLimitThreshold(Constants.ElevatorPosition.kMaxHeight.position, 0);
        
        masterElevatorMotor.configForwardSoftLimitEnable(true, 0);
		masterElevatorMotor.configReverseSoftLimitEnable(true, 0);
    }

    public void raiseElevator()
    {
        masterElevatorMotor.set(Constants.ELEVATOR_SPEED);
    }

    public void lowerElevator()
    {
        masterElevatorMotor.set(-Constants.ELEVATOR_SPEED);
    }

    public void stopElevator()
    {
        masterElevatorMotor.set(0);
    }

    public int getPotValue()
	{
		return masterElevatorMotor.getSelectedSensorPosition(0);
    }

    public Constants.ElevatorPosition getTargetPosition()
    {
       return targetPosition;
    }
    
    public void moveTo()
    {
        potValue = getPotValue();

        if(!targetPosition.equals(Constants.ElevatorPosition.kNone))
        {
            if(potValue < (targetPosition.position - Constants.ElevatorPosition.kThreshold.position))
            {
                raiseElevator();
                isMoving = true;
            }
            else if(potValue > (targetPosition.position + Constants.ElevatorPosition.kThreshold.position))
            {
                lowerElevator();
                isMoving = true;
            }
            else
            {
                stopElevator();
                targetPosition = Constants.ElevatorPosition.kNone;
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
        floorPanelButton = operatorXbox.getRawButton(1);
        floorCargoButton = operatorXbox.getRawButton(2);

        bottomHatchButton = operatorXbox.getRawButton(3);
        centerHatchButton = operatorXbox.getRawButton(4);
        topHatchButton = operatorXbox.getRawButton(5);

        bottomPortButton = operatorXbox.getRawButton(6);
        centerPortButton = operatorXbox.getRawButton(7);
        topPortButton = operatorXbox.getRawButton(8);

       // yAxis = -operatorXbox.getRawAxis(1);


        if(floorPanelButton)
        {
            targetPosition = Constants.ElevatorPosition.kFloorPanel;
        }
        else if(floorCargoButton)
        {
            targetPosition = Constants.ElevatorPosition.kFloorCargo;
        }
        else if(bottomHatchButton)
        {
            targetPosition = Constants.ElevatorPosition.kBottomHatch;
        }
        else if(centerHatchButton)
        {
            targetPosition = Constants.ElevatorPosition.kCenterHatch;
        }
        else if(topHatchButton)
        {
            targetPosition = Constants.ElevatorPosition.kTopHatch;
        }
        else if(bottomPortButton)
        {
            targetPosition = Constants.ElevatorPosition.kBottomPort;
        }
        else if(centerPortButton)
        {
            targetPosition = Constants.ElevatorPosition.kCenterPort;
        }
        else if(topPortButton)
        {
            targetPosition = Constants.ElevatorPosition.kTopPort;
        }

        moveTo();
    }

    public void autonomous()
    {

    }

    public void test()
    {

    }

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
            kFloorPanel(5, "Floor Panel"),
            kFloorCargo(5, "Floor Cargo"),
            kCargoShipPort(15, "Cargo Ship Port"),
            kThreshold(5, "Threshold"),

            kBottomHatch(10, "Bottom Hatch"),      // 1 ft 7 inches to center
            kCenterHatch(30, "Center Hatch"),      // 3 ft 11 inches to center
            kTopHatch(50, "Top Hatch"),         // 6 ft 3 inches to center

            kBottomPort(20, "Bottom Port"),       // 2 ft 3.5 inches to center
            kCenterPort(40, "Center Port"),       // 4 ft 7.5 inches to center
            kTopPort(60, "Top Port"),          // 6 ft 11.5 inches to center
            kNone(-1, "None");

            
            private final int position;
            private String name;

            private ElevatorPosition(int value, String name)
            {
                this.position = value;
                this.name = name;
            }

            public String toString()
            {
                return name;
            }
        }
    

        public static final int MASTER_ELEVATOR_MOTOR_PORT = 0;
        public static final int SLAVE_ELEVATOR_MOTOR_PORT = 0;

        public static final double ELEVATOR_SPEED = 1.0;
    }
}