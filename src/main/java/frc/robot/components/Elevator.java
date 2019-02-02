/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.components;

import frc.robot.control.DriverXbox;
import frc.robot.control.OperatorXbox;
import frc.robot.control.Xbox;
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
		
    }

    private void raiseElevator()
    {
        masterElevatorMotor.set(Constants.ELEVATOR_SPEED);
    }

    private void lowerElevator()
    {
        masterElevatorMotor.set(-Constants.ELEVATOR_SPEED);
    }

    private void stopElevator()
    {
        masterElevatorMotor.set(0);
    }

    public double getPosition()
	{
		return masterElevatorMotor.getSelectedSensorPosition(0);
    }
    
    private void moveTo()
    {

    }   
  

    private void teleop()
    {
        floorPanelButton = operatorXbox.getRawButton(0);
        floorCargoButton = operatorXbox.getRawButton(1);

        bottomHatchButton = operatorXbox.getRawButton(2);
        centerHatchButton = operatorXbox.getRawButton(3);
        topHatchButton = operatorXbox.getRawButton(4);

        bottomPortButton = operatorXbox.getRawButton(5);
        centerPortButton = operatorXbox.getRawButton(6);
        topPortButton = operatorXbox.getRawButton(7);

        if(floorPanelButton)
        {

        }
        else if(floorCargoButton)
        {

        }
        else if(bottomHatchButton)
        {

        }
        else if(centerHatchButton)
        {

        }
        else if(topHatchButton)
        {

        }
        else if(bottomPortButton)
        {

        }
        else if(centerPortButton)
        {

        }
        else if(topPortButton)
        {

        }

    }

    private void autonomous()
    {

    }

    private void test()
    {

    }

    public static class Constants
    {
        private enum ElevatorPosition
		{
            kMinHeight(0),
            kMaxHeight(60),
            kFloorHatch(5),
            kFloorCargo(5),
            kCargoShipPort(15),
            kThreshold(5),

            kBottomHatch(10),      // 1 ft 7 inches to center
            kCenterHatch(30),      // 3 ft 11 inches to center
            kTopHatch(50),         // 6 ft 3 inches to center

            kBottomPort(20),       // 2 ft 3.5 inches to center
            KCenterPort(40),       // 4 ft 7.5 inches to center
            KTopPort(60);          // 6 ft 11.5 inches to center

            


            private final int position;

            ElevatorPosition(int value)
            {
                this.position = value;
            }
        }

        

        

        public static final int MASTER_ELEVATOR_MOTOR_PORT = 1;
        public static final int SLAVE_ELEVATOR_MOTOR_PORT = 2;

        public static final double ELEVATOR_SPEED = 1.0;

        //public static final int BOTTOM
    }
}
