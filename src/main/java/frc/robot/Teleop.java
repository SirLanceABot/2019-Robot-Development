
package frc.robot;

import frc.components.Arm;
import frc.components.Elevator;
import frc.components.Climber;
import frc.components.Drivetrain;
import frc.components.ElevatorAndArm;
import frc.components.ElevatorSystem;
import frc.components.Lights;
import frc.components.Intake;
import frc.components.Wrist;
import frc.components.NewArm;
import frc.components.Carriage;
import frc.components.Grabber;
import frc.control.Xbox;
import frc.control.DriverXbox;
import frc.control.OperatorXbox;
import frc.control.ButtonBoard;
import frc.visionForWhiteTape.CameraProcess;
import frc.visionForWhiteTape.TargetData;
import frc.visionForWhiteTape.CameraProcess.rotate;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;

/**
 * Add your docs here.
 */
public class Teleop
{
    public static class Constants
    {
        private static final double ROTATION_SPEED = 0.5;
        private static final double STRAFE_SPEED = 0.5;
        private static final double CLIMBER_DRIVE_SPEED = 0.2;
        private static final double BALL_STALL_CURRENT = 0.5;
        private static final double CURRENT_LIMIT = 15.0;
        private static final double RUN_TIME = 0.5;

    }

//    private Arm arm = Arm.getInstance();
//    private Elevator elevator = Elevator.getInstance();
//    private ElevatorAndArm elevatorAndArm = ElevatorAndArm.getInstance();
    private ElevatorSystem elevatorSystem = ElevatorSystem.getInstance();
    private CameraProcess vision = CameraProcess.getInstance();

    private Intake intake = Intake.getInstance();
    private DriverXbox driverXbox = DriverXbox.getInstance();
    private OperatorXbox operatorXbox = OperatorXbox.getInstance();
    private ButtonBoard buttonBoard = ButtonBoard.getInstance();
    private Climber climber = Climber.getInstance();
    private SlabShuffleboard shuffleboard = SlabShuffleboard.getInstance();
    private Drivetrain drivetrain = Drivetrain.getInstance();
    private Lights lights = Lights.getInstance();
 
    private NewArm newArm = NewArm.getInstance();
    private Carriage carriage = Carriage.getInstance();
    private Grabber grabber = Grabber.getInstance();
    private Wrist wrist = Wrist.getInstance();

    private boolean armButton;
    private boolean armButtonReleased;
    private boolean elevatorButton;
    private boolean elevatorButtonReleased;
    private double buttonBoardYAxis;
    private double buttonBoardXAxis;

    private boolean floorButton;
    private boolean cargoShipCargoButton;

    private boolean bottomHatchButton;
    private boolean centerHatchButton;
    private boolean topHatchButton;

    private boolean bottomCargoButton;
    private boolean centerCargoButton;
    private boolean topCargoButton;

    // need to change the buttons to pressed not holds
    private boolean aButton;
    private boolean bButton;
    private boolean xButton;
    private boolean yButton;

    private double leftTrigger;
    private double rightTrigger;
    private boolean backButton;

    private boolean inButtonHeld = driverXbox.getRawButton(Xbox.Constants.LEFT_BUMPER);
    private boolean inButtonPressed = driverXbox.getRawButtonPressed(Xbox.Constants.LEFT_BUMPER);
    private boolean outButton = driverXbox.getRawButton(Xbox.Constants.RIGHT_BUMPER);
    private double motorCurrent = intake.getIntakeAmperage();

    private double[] rightAxes = driverXbox.getScaledAxes(Xbox.Constants.RIGHT_STICK_AXES,
            Xbox.Constants.PolynomialDrive.kCubicDrive);
    private double rightXAxis = rightAxes[0];

    private double[] leftAxes = driverXbox.getScaledAxes(Xbox.Constants.LEFT_STICK_AXES,
            Xbox.Constants.PolynomialDrive.kCubicDrive);
    private double leftXAxis = leftAxes[0];
    private double leftYAxis = leftAxes[1];

    enum IntakeState
    {
        // kOff turns either way off, kIntake is intake at 100%, kHold is intake at 10%,
        // kEject is eject at 100%
        kOff, kIntake, kHold, kEject;
    }

    private static Teleop instance = new Teleop();
    private TargetData targetData;
    private boolean firstTimeOverAmpLimit = true;
    private boolean rightTriggerPressed = false;
    private int rumbleTimerStart = 1;
    private IntakeState stateOfIntake = IntakeState.kOff;
    private final Timer startupTimer = new Timer();
    private final Timer rumbleTimer = new Timer();

    private Teleop()
    {
        System.out.println(this.getClass().getName() + ": Started Constructing");
        System.out.println(this.getClass().getName() + ": Finished Constructing");
    }

    public static Teleop getInstance()
    {
        return (instance);
    }

    public void init()
    {
        SlabShuffleboard.PregameSetupTabData pregame = shuffleboard.getPregameSetupTabData();
        newArm.setRobotType(pregame.robotType);
        carriage.setRobotType(pregame.robotType);
        newArm.setMotorSpeedFactor(pregame.motorSpeed);
        carriage.setMotorSpeedFactor(pregame.motorSpeed);
        drivetrain.setMotorSpeedFactor(pregame.motorSpeed);
        System.out.println(pregame.robotType);

        elevatorSystem.setArmTargetPosition(NewArm.Constants.NewArmPosition.kNotMoving);
        elevatorSystem.setCarriageTargetPosition(Carriage.Constants.CarriagePosition.kNone);
        elevatorSystem.setWristTargetPosition(Wrist.Constants.WristPosition.kWristNone);
    }

    @Deprecated
    public void teleop()
    {
        // boolean armButton =
        // buttonBoard.getRawButton(ButtonBoard.Constants.ARM_BUTTON);
        // boolean armButtonReleased =
        // buttonBoard.getRawButtonReleased(ButtonBoard.Constants.ARM_BUTTON);
        // boolean elevatorButton =
        // buttonBoard.getRawButton(ButtonBoard.Constants.ELEVATOR_BUTTON);
        // boolean elevatorButtonReleased =
        // buttonBoard.getRawButtonReleased(ButtonBoard.Constants.ELEVATOR_BUTTON);
        // double buttonBoardYAxis =
        // buttonBoard.getRawAxis(ButtonBoard.Constants.Y_AXIS);
        // double buttonBoardXAxis =
        // buttonBoard.getRawAxis(ButtonBoard.Constants.X_AXIS);

        // boolean floorButton =
        // buttonBoard.getRawButtonPressed(ButtonBoard.Constants.FLOOR_BUTTON);
        // boolean cargoShipPortButton =
        // buttonBoard.getRawButtonPressed(ButtonBoard.Constants.CARGO_SHIP_CARGO_BUTTON);

        // boolean bottomHatchButton =
        // buttonBoard.getRawButtonPressed(ButtonBoard.Constants.BOTTOM_HATCH_BUTTON);
        // boolean centerHatchButton =
        // buttonBoard.getRawButtonPressed(ButtonBoard.Constants.CENTER_HATCH_BUTTON);
        // boolean topHatchButton =
        // buttonBoard.getRawButtonPressed(ButtonBoard.Constants.TOP_HATCH_BUTTON);

        // boolean bottomCargoButton =
        // buttonBoard.getRawButtonPressed(ButtonBoard.Constants.BOTTOM_CARGO_BUTTON);
        // boolean centerCargoButton =
        // buttonBoard.getRawButtonPressed(ButtonBoard.Constants.CENTER_CARGO_BUTTON);
        // boolean topCargoButton =
        // buttonBoard.getRawButtonPressed(ButtonBoard.Constants.TOP_CARGO_BUTTON);

        // //need to change the buttons to pressed not holds
        // boolean aButtonPressed =
        // driverXbox.getRawButtonPressed(Xbox.Constants.A_BUTTON); // Extend climber
        // elevator
        // boolean bButton = driverXbox.getRawButton(Xbox.Constants.B_BUTTON); //
        // Retract climber elevator
        // boolean xButton = driverXbox.getRawButtonPressed(Xbox.Constants.X_BUTTON); //
        // Release pin solenoid
        // boolean yButton = driverXbox.getRawButton(Xbox.Constants.Y_BUTTON); //
        // Retract pin solenoid

        // double leftTrigger = driverXbox.getRawAxis(Xbox.Constants.LEFT_TRIGGER_AXIS);
        // double rightTrigger =
        // driverXbox.getRawAxis(Xbox.Constants.RIGHT_TRIGGER_AXIS);
        // boolean backButton =
        // driverXbox.getRawButtonPressed(Xbox.Constants.BACK_BUTTON);

        // boolean operatorLeftBumper =
        // operatorXbox.getRawButtonPressed(Xbox.Constants.LEFT_BUMPER);
        // boolean operatorRightBumper =
        // operatorXbox.getRawButtonPressed(Xbox.Constants.RIGHT_BUMPER);
        // boolean operatorXButton = operatorXbox.getRawButton(Xbox.Constants.X_BUTTON);
        // boolean operatorAButtonPressed =
        // operatorXbox.getRawButtonPressed(Xbox.Constants.A_BUTTON);
        // boolean operatorBButtonPressed =
        // operatorXbox.getRawButtonPressed(Xbox.Constants.B_BUTTON);
        // boolean operatorYButton = operatorXbox.getRawButton(Xbox.Constants.Y_BUTTON);
        // boolean operatorYButtonReleased =
        // operatorXbox.getRawButtonReleased(Xbox.Constants.Y_BUTTON);

        // double motorCurrent = arm.getIntakeAmperage();
        // if (operatorYButton)
        // {
        // if (operatorLeftBumper)
        // {
        // elevator.lowerElevator();
        // }
        // else if (operatorRightBumper)
        // {
        // elevator.raiseElevator();
        // }
        // else
        // {
        // elevator.stopElevator();
        // }
        // if (operatorXButton)
        // {
        // arm.moveArmUp();
        // }
        // else if (operatorAButton)
        // {
        // arm.moveArmDown();
        // }
        // else
        // {
        // arm.stopArm();
        // }

        // System.out.println(arm);
        // //System.out.println(arm);
        // }

        // if(operatorLeftBumper)
        // {
        // arm.moveWristDown();
        // }
        // else if(operatorRightBumper)
        // {
        // arm.moveWristUp();
        // }

        // if(leftTrigger > 0.7)
        // {
        // if(bButton)
        // {
        // climber.extendLegs(0.5);
        // }
        // else if(yButton)
        // {
        // climber.retractLegs(0.2);
        // }
        // else if(climber.getAmperage() > 2.0) //test to find the amps pulled when
        // climbing and holding
        // {
        // climber.holdLegs(0);//set to the right number
        // }
        // else if(climber.getAmperage() <= 2.0)
        // {
        // climber.stopLegs();
        // }
        // }
        // else
        // {
        // climber.stopLegs();
        // }

        // if(rightTrigger > 0.3)
        // {
        // if(!rightTriggerPressed)
        // {
        // rightTriggerPressed = true;
        // arm.toggleHatchPanel();
        // System.out.println("RIght Trigger Pressed");
        // }
        // }
        // else
        // {
        // rightTriggerPressed = false;
        // }

        // if(arm.getGrabberPosition() == Value.kReverse && arm.getRumbleTimer() > 5.0)
        // {
        // driverXbox.setRumble(RumbleType.kRightRumble, 0.5);
        // }
        // else
        // {
        // driverXbox.setRumble(RumbleType.kRightRumble, 0.0);
        // }

        // if(armButton || elevatorButton)
        // {
        // elevatorAndArm.setArmTargetPosition(Arm.Constants.Position.kNone);
        // elevatorAndArm.setElevatorTargetPosition(Elevator.Constants.ElevatorPosition.kNone);

        // if(armButton)
        // {
        // if(buttonBoardYAxis == 1)
        // {
        // arm.moveArmUp(0.35);
        // }
        // else if(buttonBoardYAxis == -1)
        // {
        // arm.moveArmDown(-0.35);
        // }
        // else if(buttonBoardXAxis == 1)
        // {
        // arm.moveArmUp(1.0);
        // }
        // else if(buttonBoardXAxis == -1)
        // {
        // arm.moveArmDown(-0.8);
        // }
        // else
        // {
        // arm.stopArm();
        // }
        // }
        // if(elevatorButton)
        // {
        // if(buttonBoardYAxis == 1)
        // {
        // elevator.raiseElevator(0.35);
        // }
        // else if(buttonBoardYAxis == -1)
        // {
        // elevator.lowerElevator(-0.35);
        // }
        // else if(buttonBoardXAxis == 1)
        // {
        // elevator.raiseElevator(0.8);
        // }
        // else if(buttonBoardXAxis == -1)
        // {
        // elevator.lowerElevator(-0.8);
        // }
        // else
        // {
        // elevator.holdElevator();
        // }
        // }
        // }
        // else
        // {
        // if (floorButton)
        // {
        // elevatorAndArm.setElevatorTargetPosition(Elevator.Constants.ElevatorPosition.kFloor);
        // elevatorAndArm.setArmTargetPosition(Arm.Constants.Position.kFloor);
        // }
        // else if (cargoShipPortButton)
        // {
        // elevatorAndArm.setElevatorTargetPosition(Elevator.Constants.ElevatorPosition.kCargoShipCargo);
        // elevatorAndArm.setArmTargetPosition(Arm.Constants.Position.kCargoShipCargo);
        // }
        // else if (bottomHatchButton)
        // {
        // elevatorAndArm.setElevatorTargetPosition(Elevator.Constants.ElevatorPosition.kBottomHatch);
        // elevatorAndArm.setArmTargetPosition(Arm.Constants.Position.kBottomHatch);
        // }
        // else if (centerHatchButton)
        // {
        // elevatorAndArm.setElevatorTargetPosition(Elevator.Constants.ElevatorPosition.kCenterHatch);
        // elevatorAndArm.setArmTargetPosition(Arm.Constants.Position.kCenterHatch);
        // }
        // else if (topHatchButton)
        // {
        // elevatorAndArm.setElevatorTargetPosition(Elevator.Constants.ElevatorPosition.kTopHatch);
        // elevatorAndArm.setArmTargetPosition(Arm.Constants.Position.kTopHatch);
        // }
        // else if (bottomCargoButton)
        // {
        // elevatorAndArm.setElevatorTargetPosition(Elevator.Constants.ElevatorPosition.kBottomCargo);
        // elevatorAndArm.setArmTargetPosition(Arm.Constants.Position.kBottomCargo);
        // }
        // else if (centerCargoButton)
        // {
        // elevatorAndArm.setElevatorTargetPosition(Elevator.Constants.ElevatorPosition.kCenterCargo);
        // elevatorAndArm.setArmTargetPosition(Arm.Constants.Position.kCenterCargo);
        // }
        // else if (topCargoButton)
        // {
        // elevatorAndArm.setElevatorTargetPosition(Elevator.Constants.ElevatorPosition.kTopCargo);
        // elevatorAndArm.setArmTargetPosition(Arm.Constants.Position.kTopCargo);
        // }

        // elevatorAndArm.moveTo();
        // }

        // if (elevatorButtonReleased)
        // {
        // elevatorAndArm.setArmTargetPosition(Arm.Constants.Position.kNone);
        // elevatorAndArm.setElevatorTargetPosition(Elevator.Constants.ElevatorPosition.kNone);
        // //elevator.stopElevator();
        // elevator.holdElevator();
        // arm.stopArm();
        // elevator.setIsMoving(false);
        // }
        // if (armButtonReleased)
        // {
        // elevatorAndArm.setArmTargetPosition(Arm.Constants.Position.kNone);
        // elevatorAndArm.setElevatorTargetPosition(Elevator.Constants.ElevatorPosition.kNone);
        // elevator.holdElevator();
        // arm.stopArm();
        // arm.setIsArmMoving(false);
        // }

        // if (leftTrigger > 0.3)
        // {
        // if (xButton && climber.getEncoder() < Climber.Constants.MAX_CLIMBER_HEIGHT)
        // {
        // climber.extendLegs(0.5);
        // }
        // else if (yButton && climber.getEncoder() >
        // Climber.Constants.MIN_CLIMBER_HEIGHT)
        // {
        // climber.retractLegs(0.5);
        // }
        // else
        // {
        // climber.stopLegs();
        // }

        // if (xButton)
        // {
        // climber.ejectPin();
        // }
        // // have way to reset pin solenoid
        // }

        // double[] rightAxes =
        // driverXbox.getScaledAxes(Xbox.Constants.RIGHT_STICK_AXES,
        // Xbox.Constants.PolynomialDrive.kCubicDrive);
        // double rightXAxis = rightAxes[0];

        // double[] leftAxes = driverXbox.getScaledAxes(Xbox.Constants.LEFT_STICK_AXES,
        // Xbox.Constants.PolynomialDrive.kCubicDrive);
        // double leftXAxis = leftAxes[0];
        // double leftYAxis = leftAxes[1];

        // // System.out.println("X Axis:" + leftXAxis);
        // // System.out.println("Y Axis:" + -leftYAxis);

        // if (driverXbox.getRawButtonPressed(Xbox.Constants.START_BUTTON))
        // {
        // drivetrain.toggleDriveInFieldOriented();
        // if(drivetrain.getDriveInFieldOriented())
        // {
        // System.out.println("Field Oriented");
        // }
        // else
        // {
        // System.out.println("Not Field Oriented");
        // }
        // }

        // if (drivetrain.getDriveInFieldOriented())
        // {
        // drivetrain.driveCartesian(leftXAxis, leftYAxis, rightXAxis,
        // drivetrain.getFieldOrientedHeading());
        // }
        // else
        // {
        // drivetrain.driveCartesian(leftXAxis, leftYAxis, rightXAxis);
        // }

        // if (backButton)
        // {
        // arm.toggleArmOverride();
        // }

        // System.out.println(elevator);
        // System.out.println(arm);

        // if(operatorXButton)
        // {
        // lights.turnLightsOn();
        // }
        // else
        // {
        // lights.turnLightsOff();
        // }

        // if (aButtonPressed)
        // {
        //     drivetrain.moveOmniWheel();
        // }

        intakeControl();
    }

    public void periodic()
    {
        buttonBoardControl();
        driverXboxControl();
        drivetrainControl();
        carriageControl();
        armControl();
        wristControl();
        elevatorSystem.moveTo();
        //elevatorAndArmControl();
        intakeControl();
        //intake.cargoControl(inButtonHeld, outButton, inButtonPressed);
        grabberControl();
        climberControl();
        overrideArmLimits();
    }

    public void overrideArmLimits()
    {
        if (backButton)
        {
            newArm.toggleArmOverride();
        }
    }

    public void drivetrainControl()
    {

        // System.out.println("X Axis:" + leftXAxis);
        // System.out.println("Y Axis:" + -leftYAxis);

        if (driverXbox.getRawButtonPressed(Xbox.Constants.START_BUTTON))
        {
            drivetrain.toggleDriveInFieldOriented();
            if (drivetrain.getDriveInFieldOriented())
            {
                System.out.println("Field Oriented");
            }
            else
            {
                System.out.println("Not Field Oriented");
            }
        }

        if(leftTrigger > 0.7)
        {
            if(aButton)
            {
                drivetrain.driveCartesian(0, Constants.CLIMBER_DRIVE_SPEED, 0);
            }
            else
            {
                drivetrain.driveCartesian(0, 0, 0);
            }
        }
        else if (drivetrain.getDriveInFieldOriented())
        {
            drivetrain.driveCartesian(leftXAxis, leftYAxis, rightXAxis, drivetrain.getFieldOrientedHeading());
        }
        else
        {
            drivetrain.driveCartesian(leftXAxis, leftYAxis, rightXAxis);
        }
    }

    // public void elevatorAndArmControl()
    // {
    //     if (armButton || elevatorButton)
    //     {
    //         elevatorAndArm.setArmTargetPosition(Arm.Constants.Position.kNone);
    //         elevatorAndArm.setElevatorTargetPosition(Elevator.Constants.ElevatorPosition.kNone);

    //         if (armButton)
    //         {
    //             if (buttonBoardYAxis == 1)
    //             {
    //                 arm.moveArmUp(0.35);
    //             }
    //             else if (buttonBoardYAxis == -1)
    //             {
    //                 arm.moveArmDown(-0.35);
    //             }
    //             else if (buttonBoardXAxis == 1)
    //             {
    //                 arm.moveArmUp(0.8);
    //             }
    //             else if (buttonBoardXAxis == -1)
    //             {
    //                 arm.moveArmDown(-0.8);
    //             }
    //             else
    //             {
    //                 arm.stopArm();
    //             }
    //         }
    //         if (elevatorButton)
    //         {
    //             if (buttonBoardYAxis == 1)
    //             {
    //                 elevator.raiseElevator(0.35);
    //             }
    //             else if (buttonBoardYAxis == -1)
    //             {
    //                 elevator.lowerElevator(-0.35);
    //             }
    //             else if (buttonBoardXAxis == 1)
    //             {
    //                 elevator.raiseElevator(0.8);
    //             }
    //             else if (buttonBoardXAxis == -1)
    //             {
    //                 elevator.lowerElevator(-0.8);
    //             }
    //             else
    //             {
    //                 elevator.holdElevator();
    //             }
    //         }
    //     }
    //     else
    //     {
    //         if (floorButton)
    //         {
    //             elevatorAndArm.setElevatorTargetPosition(Elevator.Constants.ElevatorPosition.kFloor);
    //             elevatorAndArm.setArmTargetPosition(Arm.Constants.Position.kFloor);
    //         }
    //         else if (cargoShipCargoButton)
    //         {
    //             elevatorAndArm.setElevatorTargetPosition(Elevator.Constants.ElevatorPosition.kCargoShipCargo);
    //             elevatorAndArm.setArmTargetPosition(Arm.Constants.Position.kCargoShipCargo);
    //         }
    //         else if (bottomHatchButton)
    //         {
    //             elevatorAndArm.setElevatorTargetPosition(Elevator.Constants.ElevatorPosition.kBottomHatch);
    //             elevatorAndArm.setArmTargetPosition(Arm.Constants.Position.kBottomHatch);
    //         }
    //         else if (centerHatchButton)
    //         {
    //             elevatorAndArm.setElevatorTargetPosition(Elevator.Constants.ElevatorPosition.kCenterHatch);
    //             elevatorAndArm.setArmTargetPosition(Arm.Constants.Position.kCenterHatch);
    //         }
    //         else if (topHatchButton)
    //         {
    //             elevatorAndArm.setElevatorTargetPosition(Elevator.Constants.ElevatorPosition.kTopHatch);
    //             elevatorAndArm.setArmTargetPosition(Arm.Constants.Position.kTopHatch);
    //         }
    //         else if (bottomCargoButton)
    //         {
    //             elevatorAndArm.setElevatorTargetPosition(Elevator.Constants.ElevatorPosition.kBottomCargo);
    //             elevatorAndArm.setArmTargetPosition(Arm.Constants.Position.kBottomCargo);
    //         }
    //         else if (centerCargoButton)
    //         {
    //             elevatorAndArm.setElevatorTargetPosition(Elevator.Constants.ElevatorPosition.kCenterCargo);
    //             elevatorAndArm.setArmTargetPosition(Arm.Constants.Position.kCenterCargo);
    //         }
    //         else if (topCargoButton)
    //         {
    //             elevatorAndArm.setElevatorTargetPosition(Elevator.Constants.ElevatorPosition.kTopCargo);
    //             elevatorAndArm.setArmTargetPosition(Arm.Constants.Position.kTopCargo);
    //         }

    //         elevatorAndArm.moveTo();
    //     }

    //     if (elevatorButtonReleased)
    //     {
    //         elevatorAndArm.setArmTargetPosition(Arm.Constants.Position.kNone);
    //         elevatorAndArm.setElevatorTargetPosition(Elevator.Constants.ElevatorPosition.kNone);
    //         elevator.holdElevator();
    //         arm.stopArm();
    //         elevator.setIsMoving(false);
    //     }
    //     if (armButtonReleased)
    //     {
    //         elevatorAndArm.setArmTargetPosition(Arm.Constants.Position.kNone);
    //         elevatorAndArm.setElevatorTargetPosition(Elevator.Constants.ElevatorPosition.kNone);
    //         elevator.holdElevator();
    //         arm.stopArm();
    //         arm.setIsArmMoving(false);
    //     }
    // }

    public void carriageControl()
    {
        if (armButton || elevatorButton)
        {
            elevatorSystem.setArmTargetPosition(NewArm.Constants.NewArmPosition.kNotMoving);
            elevatorSystem.setCarriageTargetPosition(Carriage.Constants.CarriagePosition.kNone);
            elevatorSystem.setWristTargetPosition(Wrist.Constants.WristPosition.kWristNone);

            if(elevatorButton)
            {
                if (buttonBoardYAxis == 1)
                {
                    carriage.raiseCarriage(0.35);
                }
                else if (buttonBoardYAxis == -1)
                {
                    carriage.lowerCarriage(-0.35);
                }
                else if (buttonBoardXAxis == 1)
                {
                    carriage.raiseCarriage(0.8);
                }
                else if (buttonBoardXAxis == -1)
                {
                    carriage.lowerCarriage(-0.8);
                }
                else
                {
                    carriage.holdCarriage();
                }
            }
        }
        else
        {
            if (floorButton)
            {
                elevatorSystem.setCarriageTargetPosition(Carriage.Constants.CarriagePosition.kFloor);
            }
            else if (cargoShipCargoButton)
            {
                elevatorSystem.setCarriageTargetPosition(Carriage.Constants.CarriagePosition.kCargoShipCargo);
            }
            else if (bottomHatchButton)
            {
                elevatorSystem.setCarriageTargetPosition(Carriage.Constants.CarriagePosition.kBottomHatch);
            }
            else if (centerHatchButton)
            {
                elevatorSystem.setCarriageTargetPosition(Carriage.Constants.CarriagePosition.kCenterHatch);
            }
            else if (topHatchButton)
            {
                elevatorSystem.setCarriageTargetPosition(Carriage.Constants.CarriagePosition.kTopHatch);
            }
            else if (bottomCargoButton)
            {
                elevatorSystem.setCarriageTargetPosition(Carriage.Constants.CarriagePosition.kBottomCargo);
            }
            else if (centerCargoButton)
            {
                elevatorSystem.setCarriageTargetPosition(Carriage.Constants.CarriagePosition.kCenterCargo);
            }
            else if (topCargoButton)
            {
                elevatorSystem.setCarriageTargetPosition(Carriage.Constants.CarriagePosition.kTopCargo);
            }
        }

        if (elevatorButtonReleased)
        {
            elevatorSystem.setArmTargetPosition(NewArm.Constants.NewArmPosition.kNotMoving);
            elevatorSystem.setCarriageTargetPosition(Carriage.Constants.CarriagePosition.kNone);
            carriage.holdCarriage();
            newArm.stopArm();
            carriage.setIsMoving(false);
        }
    }

    public void armControl()
    {
        if (armButton || elevatorButton)
        {
            elevatorSystem.setArmTargetPosition(NewArm.Constants.NewArmPosition.kNotMoving);
            elevatorSystem.setCarriageTargetPosition(Carriage.Constants.CarriagePosition.kNone);
            elevatorSystem.setWristTargetPosition(Wrist.Constants.WristPosition.kWristNone);

            if(armButton)
            {
                if (buttonBoardYAxis == 1)
                {
                    newArm.moveArmUp(0.35);
                }
                else if (buttonBoardYAxis == -1)
                {
                    newArm.moveArmDown(-0.35);
                }
                else if (buttonBoardXAxis == 1)
                {
                    newArm.moveArmUp(0.8);
                }
                else if (buttonBoardXAxis == -1)
                {
                    newArm.moveArmDown(-0.8);
                }
                else
                {
                    newArm.stopArm();
                }
            }
        }
        else
        {
            if (floorButton)
            {
                elevatorSystem.setArmTargetPosition(NewArm.Constants.NewArmPosition.kFloorArmPosition);
            }
            else if (cargoShipCargoButton)
            {
                elevatorSystem.setArmTargetPosition(NewArm.Constants.NewArmPosition.kHorizontalArmPosition);
            }
            else if (bottomHatchButton)
            {
                elevatorSystem.setArmTargetPosition(NewArm.Constants.NewArmPosition.kFloorArmPosition);
            }
            else if (centerHatchButton)
            {
                elevatorSystem.setArmTargetPosition(NewArm.Constants.NewArmPosition.kHorizontalArmPosition);
            }
            else if (topHatchButton)
            {
                elevatorSystem.setArmTargetPosition(NewArm.Constants.NewArmPosition.kMiddleArmPosition);
            }
            else if (bottomCargoButton)
            {
                elevatorSystem.setArmTargetPosition(NewArm.Constants.NewArmPosition.kHorizontalArmPosition);
            }
            else if (centerCargoButton)
            {
                elevatorSystem.setArmTargetPosition(NewArm.Constants.NewArmPosition.kHorizontalArmPosition);
            }
            else if (topCargoButton)
            {
                elevatorSystem.setArmTargetPosition(NewArm.Constants.NewArmPosition.kMiddleArmPosition);
            }
        }

        if (armButtonReleased)
        {
            elevatorSystem.setArmTargetPosition(NewArm.Constants.NewArmPosition.kNotMoving);
            elevatorSystem.setCarriageTargetPosition(Carriage.Constants.CarriagePosition.kNone);
            carriage.holdCarriage();
            newArm.stopArm();
            carriage.setIsMoving(false);
        }
    }

    public void wristControl()
    {
        if (armButton || elevatorButton)
        {

        }
        else
        {
            if (floorButton)
            {
                elevatorSystem.setWristTargetPosition(Wrist.Constants.WristPosition.kWristDown);
            }
            else if (cargoShipCargoButton)
            {
                elevatorSystem.setWristTargetPosition(Wrist.Constants.WristPosition.kWristDown);
            }
            else if (bottomHatchButton)
            {
                elevatorSystem.setWristTargetPosition(Wrist.Constants.WristPosition.kWristUp);
            }
            else if (centerHatchButton)
            {
                elevatorSystem.setWristTargetPosition(Wrist.Constants.WristPosition.kWristUp);
            }
            else if (topHatchButton)
            {
                elevatorSystem.setWristTargetPosition(Wrist.Constants.WristPosition.kWristUp);
            }
            else if (bottomCargoButton)
            {
                elevatorSystem.setWristTargetPosition(Wrist.Constants.WristPosition.kWristDown);
            }
            else if (centerCargoButton)
            {
                elevatorSystem.setWristTargetPosition(Wrist.Constants.WristPosition.kWristDown);
            }
            else if (topCargoButton)
            {
                elevatorSystem.setWristTargetPosition(Wrist.Constants.WristPosition.kWristDown);
            }
        }
    }


    public void grabberControl()
    {
        if (rightTrigger > 0.3)
        {
            if (!rightTriggerPressed)
            {
                rightTriggerPressed = true;
                grabber.toggleHatchPanel();
                System.out.println("Right Trigger Pressed");
            }
        }
        else
        {
            rightTriggerPressed = false;
        }

        if (grabber.getGrabberPosition() == Value.kReverse && grabber.getRumbleTimer() > 5.0)
        {
            driverXbox.setRumble(RumbleType.kRightRumble, 0.5);
        }
        else
        {
            driverXbox.setRumble(RumbleType.kRightRumble, 0.0);
        }
    }

    public void climberControl()
    {
        if (leftTrigger > 0.7)
        {
            if (bButton)
            {
                climber.extendLegs(0.5);
            }
            else if (yButton)
            {
                climber.retractLegs(0.2);
            }
            else if (climber.getAmperage() > 2.0) // test to find the amps pulled when climbing and holding
            {
                climber.holdLegs(0.1);// set to the right number
            }
            else if (climber.getAmperage() <= 2.0)
            {
                climber.stopLegs();
            }

            if(aButton)
            {
                climber.driveForward(Constants.CLIMBER_DRIVE_SPEED);
            }
            else
            {
                climber.driveForward(0.0);
            }

        }
        else
        {
            climber.stopLegs();
            climber.driveForward(0.0);
        }
    }

    public void intakeControl()
    {
        motorCurrent = intake.getIntakeAmperage();
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
            if (motorCurrent < Constants.CURRENT_LIMIT)
            {
                if (inButtonHeld)
                {
                    firstTimeOverAmpLimit = true;
                    stateOfIntake = IntakeState.kIntake;
                }
                if (!inButtonHeld)
                {
                    firstTimeOverAmpLimit = true;
                    stateOfIntake = IntakeState.kOff;
                }
            }
            else
            {
                if (inButtonHeld && firstTimeOverAmpLimit)
                {
                    startupTimer.reset();
                    startupTimer.start();
                    firstTimeOverAmpLimit = false;
                    stateOfIntake = IntakeState.kIntake;
                }
                else if (inButtonHeld && startupTimer.get() > Constants.RUN_TIME)
                {
                    firstTimeOverAmpLimit = true;
                    stateOfIntake = IntakeState.kHold;
                }
                else if (!inButtonHeld)
                {
                    firstTimeOverAmpLimit = true;
                    stateOfIntake = IntakeState.kHold;
                }
            }
            break;
        case kHold:
            if (outButton)
            {
                stateOfIntake = IntakeState.kEject;
            }
            else if (inButtonPressed)
            {
                stateOfIntake = IntakeState.kIntake;
            }
            else if (motorCurrent > Constants.BALL_STALL_CURRENT)
            {
                stateOfIntake = IntakeState.kHold;
            }
            else if (motorCurrent < Constants.BALL_STALL_CURRENT)
            {
                stateOfIntake = IntakeState.kOff;
            }
            break;
        case kEject:
            if (outButton)
            {
                stateOfIntake = IntakeState.kEject;
            }
            else if (inButtonHeld)
            {
                stateOfIntake = IntakeState.kIntake;
            }
            else
            {
                stateOfIntake = IntakeState.kOff;
            }
            break;
        }

        switch (stateOfIntake)
        {
        case kOff:
            intake.stopCargo();
            break;
        case kIntake:
            intake.intakeCargo();
            break;
        case kEject:
            intake.ejectCargo();
            break;
        case kHold:
            intake.intakeCargo();
            break;
        }

        // System.out.println("Current: " + motorCurrent + " State: " + stateOfIntake);
    }

    public void buttonBoardControl()
    {
        armButton = buttonBoard.getRawButton(ButtonBoard.Constants.ARM_BUTTON);
        armButtonReleased = buttonBoard.getRawButtonReleased(ButtonBoard.Constants.ARM_BUTTON);
        elevatorButton = buttonBoard.getRawButton(ButtonBoard.Constants.ELEVATOR_BUTTON);
        elevatorButtonReleased = buttonBoard.getRawButtonReleased(ButtonBoard.Constants.ELEVATOR_BUTTON);
        buttonBoardYAxis = buttonBoard.getRawAxis(ButtonBoard.Constants.Y_AXIS);
        buttonBoardXAxis = buttonBoard.getRawAxis(ButtonBoard.Constants.X_AXIS);

        floorButton = buttonBoard.getRawButtonPressed(ButtonBoard.Constants.FLOOR_BUTTON);
        cargoShipCargoButton = buttonBoard.getRawButtonPressed(ButtonBoard.Constants.CARGO_SHIP_CARGO_BUTTON);

        bottomHatchButton = buttonBoard.getRawButtonPressed(ButtonBoard.Constants.BOTTOM_HATCH_BUTTON);
        centerHatchButton = buttonBoard.getRawButtonPressed(ButtonBoard.Constants.CENTER_HATCH_BUTTON);
        topHatchButton = buttonBoard.getRawButtonPressed(ButtonBoard.Constants.TOP_HATCH_BUTTON);

        bottomCargoButton = buttonBoard.getRawButtonPressed(ButtonBoard.Constants.BOTTOM_CARGO_BUTTON);
        centerCargoButton = buttonBoard.getRawButtonPressed(ButtonBoard.Constants.CENTER_CARGO_BUTTON);
        topCargoButton = buttonBoard.getRawButtonPressed(ButtonBoard.Constants.TOP_CARGO_BUTTON);
    }

    public void driverXboxControl()
    {
        rightAxes = driverXbox.getScaledAxes(Xbox.Constants.RIGHT_STICK_AXES,
                Xbox.Constants.PolynomialDrive.kCubicDrive);
        rightXAxis = rightAxes[0];

        leftAxes = driverXbox.getScaledAxes(Xbox.Constants.LEFT_STICK_AXES, Xbox.Constants.PolynomialDrive.kCubicDrive);
        leftXAxis = leftAxes[0];
        leftYAxis = leftAxes[1];

        aButton = driverXbox.getRawButton(Xbox.Constants.A_BUTTON); // Drive Climber forwards
        bButton = driverXbox.getRawButton(Xbox.Constants.B_BUTTON); // Extend Climber Legs
        xButton = driverXbox.getRawButton(Xbox.Constants.X_BUTTON); // Unused
        yButton = driverXbox.getRawButton(Xbox.Constants.Y_BUTTON); // Retract climber Legs

        leftTrigger = driverXbox.getRawAxis(Xbox.Constants.LEFT_TRIGGER_AXIS);
        rightTrigger = driverXbox.getRawAxis(Xbox.Constants.RIGHT_TRIGGER_AXIS);
        backButton = driverXbox.getRawButtonPressed(Xbox.Constants.BACK_BUTTON);

        inButtonHeld = driverXbox.getRawButton(Xbox.Constants.LEFT_BUMPER);
        inButtonPressed = driverXbox.getRawButtonPressed(Xbox.Constants.LEFT_BUMPER);
        outButton = driverXbox.getRawButton(Xbox.Constants.RIGHT_BUMPER);
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

}
