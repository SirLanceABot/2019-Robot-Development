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
import frc.control.ButtonBoard;

/**
 * Add your docs here.
 */
public class Teleop 
{
    
    private static Arm arm = Arm.getInstance();
    private static Elevator elevator = Elevator.getInstance();

    private static Teleop instance = new Teleop();
    OperatorXbox operatorXbox = OperatorXbox.getInstance();
    ButtonBoard buttonBoard = ButtonBoard.getInstance();

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

        boolean floorButton = buttonBoard.getRawButton(ButtonBoard.Constants.FLOOR_BUTTON);
        boolean cargoShipPortButton = buttonBoard.getRawButton(ButtonBoard.Constants.CARGO_SHIP_PORT_BUTTON);

        boolean bottomHatchButton = buttonBoard.getRawButton(ButtonBoard.Constants.BOTTOM_HATCH_BUTTON);
        boolean centerHatchButton = buttonBoard.getRawButton(ButtonBoard.Constants.CENTER_HATCH_BUTTON);
        boolean topHatchButton = buttonBoard.getRawButton(ButtonBoard.Constants.TOP_HATCH_BUTTON);

        boolean bottomPortButton = buttonBoard.getRawButton(ButtonBoard.Constants.BOTTOM_PORT_BUTTON);
        boolean centerPortButton = buttonBoard.getRawButton(ButtonBoard.Constants.CENTER_PORT_BUTTON);
        boolean topPortButton = buttonBoard.getRawButton(ButtonBoard.Constants.TOP_PORT_BUTTON);
        
        
        if(floorButton)
        {
            elevator.setTargetPosition(Elevator.Constants.ElevatorPosition.kFloorPanel);
            arm.moveWristDown();
            arm.moveArmDown();
        }

        else if(cargoShipPortButton)
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




