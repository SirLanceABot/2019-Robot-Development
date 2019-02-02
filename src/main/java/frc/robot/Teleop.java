/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.components.Arm;
import frc.components.Elevator;
import frc.control.OperatorXbox;

/**
 * Add your docs here.
 */
public class Teleop 
{
    
    private static Arm arm = Arm.getInstance();
    private static Elevator elevator = Elevator.getInstance();

    private static Teleop instance = new Teleop();
    OperatorXbox operatorXbox = OperatorXbox.getInstance();
    private Teleop()
    {

    }

    public Teleop getInstance()
    {
        return(instance);
    }

    public void teleop()
    {     
        boolean cargoInButton = operatorXbox.getRawButton(9);
        boolean cargoOutButton = operatorXbox.getRawButton(10);

        boolean floorPanelButton = operatorXbox.getRawButton(1);
        boolean floorCargoButton = operatorXbox.getRawButton(2);

        boolean bottomHatchButton = operatorXbox.getRawButton(3);
        boolean centerHatchButton = operatorXbox.getRawButton(4);
        boolean topHatchButton = operatorXbox.getRawButton(5);

        boolean bottomPortButton = operatorXbox.getRawButton(6);
        boolean centerPortButton = operatorXbox.getRawButton(7);
        boolean topPortButton = operatorXbox.getRawButton(8);
        
        
        if(floorPanelButton)
        {
            elevator.setTargetPosition(Elevator.Constants.ElevatorPosition.kFloorPanel);
            arm.moveWristDown();
            arm.moveArmDown();
        }

        else if(floorCargoButton)
        {
            elevator.setTargetPosition(Elevator.Constants.ElevatorPosition.kFloorCargo);
            arm.moveWristUp();
            arm.moveArmDown();
        }
        else if(bottomHatchButton)
        {
            elevator.setTargetPosition(Elevator.Constants.ElevatorPosition.kBottomHatch);
            arm.moveWristDown();
            arm.moveArmDown();
        }
        else if(centerHatchButton)
        {
            elevator.setTargetPosition(Elevator.Constants.ElevatorPosition.kCenterHatch);
            arm.moveWristDown();
            arm.moveArmDown();
        }
        else if(topHatchButton)
        {
            elevator.setTargetPosition(Elevator.Constants.ElevatorPosition.kTopHatch);
            arm.moveWristDown();
            arm.moveArmUp();
        }
        else if(bottomPortButton)
        {
            elevator.setTargetPosition(Elevator.Constants.ElevatorPosition.kBottomPort);
            arm.moveWristUp();
            arm.moveArmDown();
        }
        else if(centerPortButton)
        {
            elevator.setTargetPosition(Elevator.Constants.ElevatorPosition.kBottomPort);
            arm.moveWristUp();
            arm.moveArmDown();
        }
        else if(topPortButton)
        {
            elevator.setTargetPosition(Elevator.Constants.ElevatorPosition.kTopPort);
            arm.moveWristUp();
            arm.moveArmUp();
        }

        elevator.moveTo();
        
        if(cargoInButton)
        {
            arm.ejectCargo(0.5);
        }
    
        else if(cargoOutButton)
        {
            arm.intakeCargo(0.5);
        }
    
        else
        {
            arm.stopCargo();
        }
        
    

    }
    

    public static class Constants
    {

    }
}




