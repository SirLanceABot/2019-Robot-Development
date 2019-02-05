/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.components.Arm;
import frc.components.Elevator;
import frc.control.DriverXbox;
import frc.control.ButtonBoard;

/**
 * Add your docs here.
 */
public class Teleop 
{    
    private Arm arm = Arm.getInstance();
    private Elevator elevator = Elevator.getInstance();

    private DriverXbox driverXbox = DriverXbox.getInstance();
    private ButtonBoard buttonBoard = ButtonBoard.getInstance();

    private static Teleop instance = new Teleop();
    

    private Teleop()
    {

    }

    public Teleop getInstance()
    {
        return(instance);
    }

    public void teleop()
    {     
        boolean cargoInButton = driverXbox.getRawButton(9);
        boolean cargoOutButton = driverXbox.getRawButton(10);

        boolean floorButton = buttonBoard.getRawButtonPressed(ButtonBoard.Constants.FLOOR_BUTTON);
        boolean cargoShipPortButton = buttonBoard.getRawButtonPressed(ButtonBoard.Constants.CARGO_SHIP_CARGO_BUTTON);

        boolean bottomHatchButton = buttonBoard.getRawButtonPressed(ButtonBoard.Constants.BOTTOM_HATCH_BUTTON);
        boolean centerHatchButton = buttonBoard.getRawButtonPressed(ButtonBoard.Constants.CENTER_HATCH_BUTTON);
        boolean topHatchButton = buttonBoard.getRawButtonPressed(ButtonBoard.Constants.TOP_HATCH_BUTTON);

        boolean bottomCargoButton = buttonBoard.getRawButtonPressed(ButtonBoard.Constants.BOTTOM_CARGO_BUTTON);
        boolean centerCargoButton = buttonBoard.getRawButtonPressed(ButtonBoard.Constants.CENTER_CARGO_BUTTON);
        boolean topCargoButton = buttonBoard.getRawButtonPressed(ButtonBoard.Constants.TOP_CARGO_BUTTON);
        
        
        if(floorButton)
        {
            elevator.setTargetPosition(Elevator.Constants.ElevatorPosition.kFloor);
            arm.setTargetPosition(Arm.Constants.Position.kFloor);
        }
        else if(cargoShipPortButton)
        {
            elevator.setTargetPosition(Elevator.Constants.ElevatorPosition.kCargoShipCargo);
            arm.setTargetPosition(Arm.Constants.Position.kCargoShipCargo);
        }
        else if(bottomHatchButton)
        {
            elevator.setTargetPosition(Elevator.Constants.ElevatorPosition.kBottomHatch);
            arm.setTargetPosition(Arm.Constants.Position.kBottomHatch);
        }
        else if(centerHatchButton)
        {
            elevator.setTargetPosition(Elevator.Constants.ElevatorPosition.kCenterHatch);
            arm.setTargetPosition(Arm.Constants.Position.kCenterHatch);
        }
        else if(topHatchButton)
        {
            elevator.setTargetPosition(Elevator.Constants.ElevatorPosition.kTopHatch);
            arm.setTargetPosition(Arm.Constants.Position.kTopHatch);
        }
        else if(bottomCargoButton)
        {
            elevator.setTargetPosition(Elevator.Constants.ElevatorPosition.kBottomCargo);
            arm.setTargetPosition(Arm.Constants.Position.kBottomCargo);
        }
        else if(centerCargoButton)
        {
            elevator.setTargetPosition(Elevator.Constants.ElevatorPosition.kCenterCargo);
            arm.setTargetPosition(Arm.Constants.Position.kCenterCargo);
        }
        else if(topCargoButton)
        {
            elevator.setTargetPosition(Elevator.Constants.ElevatorPosition.kTopCargo);
            arm.setTargetPosition(Arm.Constants.Position.kTopCargo);
        }

        elevator.moveTo();
        arm.moveTo();
        
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




