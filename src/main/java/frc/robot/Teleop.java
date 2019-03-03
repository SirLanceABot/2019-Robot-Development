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
import frc.components.Lights;
import frc.visionForWhiteTape.CameraProcess;
import frc.visionForWhiteTape.TargetData;
import frc.visionForWhiteTape.CameraProcess.rotate;

import javax.lang.model.util.ElementScanner6;

import edu.wpi.first.wpilibj.Timer;

/**
 * Add your docs here.
 */
public class Teleop
{
    enum IntakeState
    {
        kOff, kIntake, kHold, kEject; // kOff turns either way off, kIntake is intake at 100%, kHold is intake at 10%,
                                      // kEject is eject at 100%
    }
    
    private IntakeState stateOfIntake = IntakeState.kOff;
    private final Timer startupTimer = new Timer();
    private Arm arm = Arm.getInstance();
    private Elevator elevator = Elevator.getInstance();
    private ElevatorAndArm elevatorAndArm = ElevatorAndArm.getInstance();
    private CameraProcess vision = CameraProcess.getInstance();

    private DriverXbox driverXbox = DriverXbox.getInstance();
    private OperatorXbox operatorXbox = OperatorXbox.getInstance();
    private ButtonBoard buttonBoard = ButtonBoard.getInstance();
    private Climber climber = Climber.getInstance();
    private SlabShuffleboard shuffleboard = SlabShuffleboard.getInstance();
    private Drivetrain drivetrain = Drivetrain.getInstance();
    private Lights lights = Lights.getInstance();

    private static Teleop instance = new Teleop();
    private TargetData targetData;
    private boolean firstTimeOverAmpLimit = true;

    private Teleop()
    {
        System.out.println(this.getClass().getName() + ": Started Constructing");
        System.out.println(this.getClass().getName() + ": Finished Constructing");
    }

    public static Teleop getInstance()
    {
        return (instance);
    }

    public void teleopInit()
    {
        SlabShuffleboard.PregameSetupTabData pregame = shuffleboard.getPregameSetupTabData();
        arm.setRobotType(pregame.robotType);
        elevator.setRobotType(pregame.robotType);
        arm.setMotorSpeedFactor(pregame.motorSpeed);
        elevator.setMotorSpeedFactor(pregame.motorSpeed);
        drivetrain.setMotorSpeedFactor(pregame.motorSpeed);

        elevatorAndArm.setArmTargetPosition(Arm.Constants.Position.kNone);
        elevatorAndArm.setElevatorTargetPosition(Elevator.Constants.ElevatorPosition.kNone);
    }

    public void teleop()
    {
        boolean armButton = buttonBoard.getRawButton(ButtonBoard.Constants.ARM_BUTTON);
        boolean armButtonReleased = buttonBoard.getRawButtonReleased(ButtonBoard.Constants.ARM_BUTTON);
        boolean elevatorButton = buttonBoard.getRawButton(ButtonBoard.Constants.ELEVATOR_BUTTON);
        boolean elevatorButtonReleased = buttonBoard.getRawButtonReleased(ButtonBoard.Constants.ELEVATOR_BUTTON);
        double buttonBoardYAxis = buttonBoard.getRawAxis(ButtonBoard.Constants.Y_AXIS);
        double buttonBoardXAxis = buttonBoard.getRawAxis(ButtonBoard.Constants.X_AXIS);


        boolean floorButton = buttonBoard.getRawButtonPressed(ButtonBoard.Constants.FLOOR_BUTTON);
        boolean cargoShipPortButton = buttonBoard.getRawButtonPressed(ButtonBoard.Constants.CARGO_SHIP_CARGO_BUTTON);

        boolean bottomHatchButton = buttonBoard.getRawButtonPressed(ButtonBoard.Constants.BOTTOM_HATCH_BUTTON);
        boolean centerHatchButton = buttonBoard.getRawButtonPressed(ButtonBoard.Constants.CENTER_HATCH_BUTTON);
        boolean topHatchButton = buttonBoard.getRawButtonPressed(ButtonBoard.Constants.TOP_HATCH_BUTTON);

        boolean bottomCargoButton = buttonBoard.getRawButtonPressed(ButtonBoard.Constants.BOTTOM_CARGO_BUTTON);
        boolean centerCargoButton = buttonBoard.getRawButtonPressed(ButtonBoard.Constants.CENTER_CARGO_BUTTON);
        boolean topCargoButton = buttonBoard.getRawButtonPressed(ButtonBoard.Constants.TOP_CARGO_BUTTON);

        //need to change the buttons to pressed not holds
        boolean aButtonPressed = driverXbox.getRawButtonPressed(Xbox.Constants.A_BUTTON); // Extend climber elevator
        boolean bButton = driverXbox.getRawButton(Xbox.Constants.B_BUTTON); // Retract climber elevator
        boolean xButton = driverXbox.getRawButtonPressed(Xbox.Constants.X_BUTTON); // Release pin solenoid
        boolean yButton = driverXbox.getRawButtonPressed(Xbox.Constants.Y_BUTTON); // Retract pin solenoid
        boolean rightBumper = driverXbox.getRawButtonPressed(Xbox.Constants.RIGHT_BUMPER); // Hold button in order to
                                                                                           // access climbing
        boolean leftBumper = driverXbox.getRawButton(Xbox.Constants.LEFT_BUMPER); //hybrid auto switch

        boolean operatorLeftBumper = operatorXbox.getRawButtonPressed(Xbox.Constants.LEFT_BUMPER);
        boolean operatorRightBumper = operatorXbox.getRawButtonPressed(Xbox.Constants.RIGHT_BUMPER);
        boolean operatorXButton = operatorXbox.getRawButton(Xbox.Constants.X_BUTTON);
        boolean operatorAButtonPressed = operatorXbox.getRawButtonPressed(Xbox.Constants.A_BUTTON);
        boolean operatorBButtonPressed = operatorXbox.getRawButtonPressed(Xbox.Constants.B_BUTTON);
        boolean operatorYButton = operatorXbox.getRawButton(Xbox.Constants.Y_BUTTON);
        boolean operatorYButtonReleased = operatorXbox.getRawButtonReleased(Xbox.Constants.Y_BUTTON);

        double motorCurrent = arm.getIntakeAmperage();
        // if (operatorYButton)
        // {
        //     if (operatorLeftBumper)
        //     {
        //         elevator.lowerElevator();
        //     }
        //     else if (operatorRightBumper)
        //     {
        //         elevator.raiseElevator();
        //     }
        //     else
        //     {
        //         elevator.stopElevator();
        //     }
        //     if (operatorXButton)
        //     {
        //         arm.moveArmUp();
        //     }
        //     else if (operatorAButton)
        //     {
        //         arm.moveArmDown();
        //     }
        //     else
        //     {
        //         arm.stopArm();
        //     }

        //     System.out.println(arm);
        //     //System.out.println(arm);
        // }

        if(operatorLeftBumper)
        {
            arm.moveWristDown();
        }
        else if(operatorRightBumper)
        {
            arm.moveWristUp();
        }
        if(operatorAButtonPressed)
        {
            arm.grabHatchPanel();
        }
        else if(operatorBButtonPressed)
        {
            arm.releaseHatchPanel();
        }

        if(armButton || elevatorButton)
        {
            elevatorAndArm.setArmTargetPosition(Arm.Constants.Position.kNone);
            elevatorAndArm.setElevatorTargetPosition(Elevator.Constants.ElevatorPosition.kNone);

            if(armButton)
            {
                if(buttonBoardYAxis == 1)
                {
                    arm.moveArmUp(0.35);
                }
                else if(buttonBoardYAxis == -1)
                {
                    arm.moveArmDown(-0.35);
                }
                else if(buttonBoardXAxis == 1)
                {
                    arm.moveArmUp(0.8);
                }
                else if(buttonBoardXAxis == -1)
                {
                    arm.moveArmDown(-0.8);
                }
                else
                {
                    arm.stopArm();
                }
            }
            if(elevatorButton)
            {
                if(buttonBoardYAxis == 1)
                {
                    elevator.raiseElevator(0.35);
                }
                else if(buttonBoardYAxis == -1)
                {
                    elevator.lowerElevator(-0.35);
                }
                else if(buttonBoardXAxis == 1)
                {
                    elevator.raiseElevator(0.8);
                }
                else if(buttonBoardXAxis == -1)
                {
                    elevator.lowerElevator(-0.8);
                }
                else
                {
                    elevator.stopElevator();
                }
            }

            //System.out.println(arm);
            // System.out.println(arm);
        }
        else
        {
            if (floorButton)
            {
                elevatorAndArm.setElevatorTargetPosition(Elevator.Constants.ElevatorPosition.kFloor);
                elevatorAndArm.setArmTargetPosition(Arm.Constants.Position.kFloor);
            }
            else if (cargoShipPortButton)
            {
                elevatorAndArm.setElevatorTargetPosition(Elevator.Constants.ElevatorPosition.kCargoShipCargo);
                elevatorAndArm.setArmTargetPosition(Arm.Constants.Position.kCargoShipCargo);
            }
            else if (bottomHatchButton)
            {
                elevatorAndArm.setElevatorTargetPosition(Elevator.Constants.ElevatorPosition.kBottomHatch);
                elevatorAndArm.setArmTargetPosition(Arm.Constants.Position.kBottomHatch);
            }
            else if (centerHatchButton)
            {
                elevatorAndArm.setElevatorTargetPosition(Elevator.Constants.ElevatorPosition.kCenterHatch);
                elevatorAndArm.setArmTargetPosition(Arm.Constants.Position.kCenterHatch);
            }
            else if (topHatchButton)
            {
                elevatorAndArm.setElevatorTargetPosition(Elevator.Constants.ElevatorPosition.kTopHatch);
                elevatorAndArm.setArmTargetPosition(Arm.Constants.Position.kTopHatch);
            }
            else if (bottomCargoButton)
            {
                elevatorAndArm.setElevatorTargetPosition(Elevator.Constants.ElevatorPosition.kBottomCargo);
                elevatorAndArm.setArmTargetPosition(Arm.Constants.Position.kBottomCargo);
                System.out.println("Bottom Cargo Button");
            }
            else if (centerCargoButton)
            {
                elevatorAndArm.setElevatorTargetPosition(Elevator.Constants.ElevatorPosition.kCenterCargo);
                elevatorAndArm.setArmTargetPosition(Arm.Constants.Position.kCenterCargo);
            }
            else if (topCargoButton)
            {
                elevatorAndArm.setElevatorTargetPosition(Elevator.Constants.ElevatorPosition.kTopCargo);
                elevatorAndArm.setArmTargetPosition(Arm.Constants.Position.kTopCargo);
            }

            elevatorAndArm.moveTo();
        }

        if (elevatorButtonReleased)
        {
            elevatorAndArm.setArmTargetPosition(Arm.Constants.Position.kNone);
            elevatorAndArm.setElevatorTargetPosition(Elevator.Constants.ElevatorPosition.kNone);
            elevator.stopElevator();
        }
        if (armButtonReleased)
        {
            elevatorAndArm.setArmTargetPosition(Arm.Constants.Position.kNone);
            elevatorAndArm.setElevatorTargetPosition(Elevator.Constants.ElevatorPosition.kNone);
            arm.stopArm();
        }

        // if (rightBumper)
        // {
        //     if (xButton && climber.getEncoder() < Climber.Constants.MAX_CLIMBER_HEIGHT)
        //     {
        //         climber.extendLegs(0.5);
        //     }
        //     else if (yButton && climber.getEncoder() > Climber.Constants.MIN_CLIMBER_HEIGHT)
        //     {
        //         climber.retractLegs(0.5);
        //     }
        //     else
        //     {
        //         climber.stopLegs();
        //     }

        //     if (xButton)
        //     {
        //         climber.ejectPin();
        //     }
        //     // have way to reset pin solenoid
        // }

        double[] rightAxes = driverXbox.getScaledAxes(Xbox.Constants.RIGHT_STICK_AXES,
                Xbox.Constants.PolynomialDrive.kCubicDrive);
        double rightXAxis = rightAxes[0];

        double[] leftAxes = driverXbox.getScaledAxes(Xbox.Constants.LEFT_STICK_AXES,
                Xbox.Constants.PolynomialDrive.kCubicDrive);
        double leftXAxis = leftAxes[0];
        double leftYAxis = leftAxes[1];

        // System.out.println("X Axis:" + leftXAxis);
        // System.out.println("Y Axis:" + -leftYAxis);

        if (driverXbox.getRawButtonPressed(Xbox.Constants.START_BUTTON))
        {
            drivetrain.toggleDriveInFieldOriented();
            System.out.println(drivetrain.getDriveInFieldOriented());
        }

        if (drivetrain.getDriveInFieldOriented())
        {
            drivetrain.driveCartesian(leftXAxis, leftYAxis, rightXAxis, drivetrain.getFieldOrientedHeading());
        }
        else
        {
            drivetrain.driveCartesian(leftXAxis, leftYAxis, rightXAxis);
        }

        if (driverXbox.getRawButtonPressed(Xbox.Constants.BACK_BUTTON))
        {
            drivetrain.resetNavX();
        }

        // System.out.println(elevator);
        //System.out.println(arm);  

        if(operatorXButton)
        {
            lights.turnLightsOn();
        }
        else
        {
            lights.turnLightsOff();
        }

        if (aButtonPressed)
        {
            drivetrain.moveOmniWheel();
        }
    //   System.out.println(drivetrain.getLeftServo() + " " + drivetrain.getRightServo());

        //System.out.println(drivetrain.getNavXData());

        intakeStateMachine();
    }

    public boolean whiteLineAlignment()
    {
        targetData = vision.getTargetData();
        if (targetData.isFreshData())
        {
            CameraProcess.rotate rotation = vision.getRotateDirection(targetData);
            double rotationFactor = vision.getRotateFactor(targetData);
            CameraProcess.strafeDirection strafeDirection = vision.getStrafeDirection(targetData);
            double strafeDirectionFactor = vision.getStrafeFactor(targetData);

            // rotate first then strafe
            if (rotation != rotate.kNone)
            {
                drivetrain.driveCartesian(0.0, 0.0, Constants.ROTATION_SPEED * (rotationFactor / 90.0));
            }
            else if (strafeDirection != CameraProcess.strafeDirection.kNone)
            {
                drivetrain.driveCartesian(0.0, Constants.STRAFE_SPEED * (strafeDirectionFactor / 80.0), 0.0);
            }
            else
            {
                return true;
            }
        }
        return false;
    }

    public void intakeStateMachine() {
        boolean inButtonHeld = driverXbox.getRawButton(Xbox.Constants.RIGHT_BUMPER);
        boolean inButtonPressed = driverXbox.getRawButtonPressed(Xbox.Constants.RIGHT_BUMPER);
        boolean outButton = driverXbox.getRawButton(Xbox.Constants.LEFT_BUMPER);
        double motorCurrent = arm.getIntakeAmperage();
    
        switch (stateOfIntake) 
        {
        case kOff:
    
          if (inButtonHeld) 
          {
            stateOfIntake = IntakeState.kIntake;
          } 
          else if (outButton) 
          {
            stateOfIntake = IntakeState.kEject;
          } 
          else 
          {
            stateOfIntake = IntakeState.kOff;
          }
          break;
        case kIntake:
        if(motorCurrent < Constants.CURRENT_LIMIT)
        {
          if(inButtonHeld)
          {
            firstTimeOverAmpLimit = true;
            stateOfIntake = IntakeState.kIntake;
          }
          if(!inButtonHeld)
          {
            firstTimeOverAmpLimit = true;
            stateOfIntake = IntakeState.kOff;
          }
        }
        else
        {
          if(inButtonHeld && firstTimeOverAmpLimit)
          {
            startupTimer.reset();
            startupTimer.start();
            firstTimeOverAmpLimit = false;
            stateOfIntake = IntakeState.kIntake;
          }
          else if(inButtonHeld && startupTimer.get() > Constants.RUN_TIME)
          {
            firstTimeOverAmpLimit = true;
            stateOfIntake = IntakeState.kHold;
          }
          else if(!inButtonHeld)
          {
            firstTimeOverAmpLimit = true;
            stateOfIntake = IntakeState.kHold;
          }
        }
          break;
        case kHold:
          if(outButton)
          {
            stateOfIntake = IntakeState.kEject;
          }
          else if(inButtonPressed)
          {
            stateOfIntake = IntakeState.kIntake;
          }
          else if(motorCurrent > Constants.BALL_STALL_CURRENT)
          {
            stateOfIntake = IntakeState.kHold;
          }
          else if(motorCurrent < Constants.BALL_STALL_CURRENT)
          {
            stateOfIntake = IntakeState.kOff;
          }
          break;
        case kEject:
          if(outButton)
          {
            stateOfIntake = IntakeState.kEject;
          }
          else if(inButtonHeld)
          {
            stateOfIntake = IntakeState.kIntake;
          }
          else
          {
            stateOfIntake = IntakeState.kOff;
          }
          break;
        }
     
      
      switch(stateOfIntake)
      {
        case kOff:
          arm.stopCargo();
          break;
        case kIntake:
            arm.intakeCargo(1.0);
          break;
        case kEject:
            arm.ejectCargo(1.0);
          break;
        case kHold:
            arm.intakeCargo(0.1);
          break;
      }
    
      // System.out.println("Current: " + motorCurrent + "   State: " + stateOfIntake);
    }
    public static class Constants
    {
        private static final double ROTATION_SPEED = 0.5;
        private static final double STRAFE_SPEED = 0.5;
        private static final double BALL_STALL_CURRENT = 0.5;
        private static final double CURRENT_LIMIT = 15.0;
        private static final double RUN_TIME = 0.5;

    }
}
