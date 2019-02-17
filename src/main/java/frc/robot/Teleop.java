/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.components.Arm;
import frc.components.Elevator;
import frc.components.Climber;
import frc.components.Drivetrain;
import frc.control.Xbox;
import frc.control.DriverXbox;
import frc.control.OperatorXbox;
import frc.control.ButtonBoard;
import frc.components.ElevatorAndArm;

/**
 * Add your docs here.
 */
public class Teleop 
{    
    private Arm arm = Arm.getInstance();
    private Elevator elevator = Elevator.getInstance();
    private ElevatorAndArm elevatorAndArm = ElevatorAndArm.getInstance();

    private DriverXbox driverXbox = DriverXbox.getInstance();
    private OperatorXbox operatorXbox = OperatorXbox.getInstance();
    private ButtonBoard buttonBoard = ButtonBoard.getInstance();
    private Climber climber = Climber.getInstance();
    private SlabShuffleboard shuffleboard = SlabShuffleboard.getInstance();
    private Drivetrain drivetrain = Drivetrain.getInstance();



    private static Teleop instance = new Teleop();
    

    private Teleop()
    {

    }

    public static Teleop getInstance()
    {
        return(instance);
    }

    public void teleopInit()
    {
        SlabShuffleboard.PregameSetupTabData pregame = shuffleboard.getPregameSetupTabData(); 
        arm.setRobotType(pregame.robotType);
        elevator.setRobotType(pregame.robotType);
    }

    public void teleop()
    {     
        boolean cargoInButton = buttonBoard.getRawButton(ButtonBoard.Constants.SLOW_SPEED_BUTTON);
        boolean cargoOutButton = buttonBoard.getRawButton(ButtonBoard.Constants.FAST_SPEED_BUTTON);

        boolean floorButton = buttonBoard.getRawButtonPressed(ButtonBoard.Constants.FLOOR_BUTTON);
        boolean cargoShipPortButton = buttonBoard.getRawButtonPressed(ButtonBoard.Constants.CARGO_SHIP_CARGO_BUTTON);

        boolean bottomHatchButton = buttonBoard.getRawButtonPressed(ButtonBoard.Constants.BOTTOM_HATCH_BUTTON);
        boolean centerHatchButton = buttonBoard.getRawButtonPressed(ButtonBoard.Constants.CENTER_HATCH_BUTTON);
        boolean topHatchButton = buttonBoard.getRawButtonPressed(ButtonBoard.Constants.TOP_HATCH_BUTTON);

        boolean bottomCargoButton = buttonBoard.getRawButtonPressed(ButtonBoard.Constants.BOTTOM_CARGO_BUTTON);
        boolean centerCargoButton = buttonBoard.getRawButtonPressed(ButtonBoard.Constants.CENTER_CARGO_BUTTON);
        boolean topCargoButton = buttonBoard.getRawButtonPressed(ButtonBoard.Constants.TOP_CARGO_BUTTON);

        boolean aButton = driverXbox.getRawButton(Xbox.Constants.A_BUTTON);                 // Extend climber elevator
        boolean bButton = driverXbox.getRawButton(Xbox.Constants.B_BUTTON);                 // Retract climber elevator
        boolean xButton = driverXbox.getRawButtonPressed(Xbox.Constants.X_BUTTON);          // Release pin solenoid
        boolean yButton = driverXbox.getRawButtonPressed(Xbox.Constants.Y_BUTTON);          // Retract pin solenoid
        boolean rightBumper = driverXbox.getRawButtonPressed(Xbox.Constants.RIGHT_BUMPER);    // Hold button in order to access climbing
        

        boolean operatorLeftBumper = operatorXbox.getRawButton(Xbox.Constants.LEFT_BUMPER);
        boolean operatorRightBumper = operatorXbox.getRawButton(Xbox.Constants.RIGHT_BUMPER);
        boolean operatorXButton = operatorXbox.getRawButton(Xbox.Constants.X_BUTTON);
        boolean operatorAButton = operatorXbox.getRawButton(Xbox.Constants.A_BUTTON);

        if(operatorLeftBumper)
        {
            elevator.lowerElevator();
        }
        else if(operatorRightBumper)
        {
            elevator.raiseElevator();
        }
        else
        {
            elevator.stopElevator();
        }
        if(operatorXButton)
        {
            arm.moveWristUp();
        }
        else if(operatorAButton)
        {
            arm.moveWristDown();
        }
        else
        {
            arm.stopArm();
        }


        if(floorButton)
        {
            elevatorAndArm.setElevatorTargetPosition(Elevator.Constants.ElevatorPosition.kFloor);
            elevatorAndArm.setArmTargetPosition(Arm.Constants.Position.kFloor);
        }
        else if(cargoShipPortButton)
        {
            elevatorAndArm.setElevatorTargetPosition(Elevator.Constants.ElevatorPosition.kCargoShipCargo);
            elevatorAndArm.setArmTargetPosition(Arm.Constants.Position.kCargoShipCargo);
        }
        else if(bottomHatchButton)
        {
            elevatorAndArm.setElevatorTargetPosition(Elevator.Constants.ElevatorPosition.kBottomHatch);
            elevatorAndArm.setArmTargetPosition(Arm.Constants.Position.kBottomHatch);
        }
        else if(centerHatchButton)
        {
            elevatorAndArm.setElevatorTargetPosition(Elevator.Constants.ElevatorPosition.kCenterHatch);
            elevatorAndArm.setArmTargetPosition(Arm.Constants.Position.kCenterHatch);
        }
        else if(topHatchButton)
        {
            elevatorAndArm.setElevatorTargetPosition(Elevator.Constants.ElevatorPosition.kTopHatch);
            elevatorAndArm.setArmTargetPosition(Arm.Constants.Position.kTopHatch);
        }
        else if(bottomCargoButton)
        {
            elevatorAndArm.setElevatorTargetPosition(Elevator.Constants.ElevatorPosition.kBottomCargo);
            elevatorAndArm.setArmTargetPosition(Arm.Constants.Position.kBottomCargo);
        }
        else if(centerCargoButton)
        {
            elevatorAndArm.setElevatorTargetPosition(Elevator.Constants.ElevatorPosition.kCenterCargo);
            elevatorAndArm.setArmTargetPosition(Arm.Constants.Position.kCenterCargo);
        }
        else if(topCargoButton)
        {
            elevatorAndArm.setElevatorTargetPosition(Elevator.Constants.ElevatorPosition.kTopCargo);
            elevatorAndArm.setArmTargetPosition(Arm.Constants.Position.kTopCargo);
        }

        elevatorAndArm.moveTo();
        
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
        
        if(rightBumper)
        {
            if(xButton && climber.getEncoder() < Climber.Constants.MAX_CLIMBER_HEIGHT)
            {
                climber.extendLegs(0.5);
            }
            else if(yButton && climber.getEncoder() > Climber.Constants.MIN_CLIMBER_HEIGHT)
            {
                climber.retractLegs(0.5);
            }
            else
            {
                climber.stopLegs();
            }

            if(xButton)
            {
                climber.ejectPin();
            }
            // have way to reset pin solenoid
        }

        double[] rightAxes = driverXbox.getScaledAxes(Xbox.Constants.RIGHT_STICK_AXES, Xbox.Constants.PolynomialDrive.kCubicDrive);
        double rightXAxis = rightAxes[0];

        double[] leftAxes = driverXbox.getScaledAxes(Xbox.Constants.LEFT_STICK_AXES, Xbox.Constants.PolynomialDrive.kCubicDrive);
        double leftXAxis = leftAxes[0];
        double leftYAxis = leftAxes[1];

        System.out.println("X Axis:" + leftXAxis);
        System.out.println("Y Axis:" + -leftYAxis);

        if(driverXbox.getRawButtonPressed(Xbox.Constants.START_BUTTON))
        {
            drivetrain.toggleDriveInFieldOriented();
        }

		if (drivetrain.getDriveInFieldOriented())
		{
            drivetrain.driveCartesian(leftXAxis, leftYAxis, rightXAxis, drivetrain.getFieldOrientedHeading());
		}
		else
		{
			drivetrain.driveCartesian(leftXAxis, leftYAxis, rightXAxis);
        }

        if(aButton)
        {
            //omniwheel up/down 
        }
    }

    public static class Constants
    {

    }
}




