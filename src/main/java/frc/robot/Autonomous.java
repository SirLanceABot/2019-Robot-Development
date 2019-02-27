
package frc.robot;

import frc.components.Drivetrain;
import frc.robot.SlabShuffleboard;
import frc.robot.SlabShuffleboard.GamePiece;
import frc.robot.SlabShuffleboard.Objective;
import frc.robot.SlabShuffleboard.PregameSetupTabData;
import frc.robot.SlabShuffleboard.RocketHatch;
import frc.robot.SlabShuffleboard.StartingLocation;
import frc.components.Arm;
import frc.components.Elevator;
import frc.components.Arm.Constants.Position;
import frc.components.Drivetrain.Constants.OmniEncoder;
import frc.components.Elevator.Constants.ElevatorPosition;
import frc.components.ElevatorAndArm;
import frc.visionForWhiteTape.CameraProcess;
import frc.visionForWhiteTape.TargetData;
import frc.visionForWhiteTape.CameraProcess.rotate;

import java.awt.Point;
import java.util.ArrayList;

import javax.xml.stream.util.StreamReaderDelegate;

import edu.wpi.first.wpilibj.Timer;

/**
 * TODO: methodize some driving stuff for reuse move some variables to the top - done
 * as instance variables - done
 * 
 * create function to use the vision code - done
 * 
 * Need to add the timer to the placement of cargo or HP make the drivetopoint- done

 * function more of a state machine - done
 * 
 * add variable motor speeds
 */
public class Autonomous
{
    private SlabShuffleboard shuffleboard = SlabShuffleboard.getInstance();
    private Drivetrain drivetrain = Drivetrain.getInstance();
    private Arm arm = Arm.getInstance();
    private Elevator elevator = Elevator.getInstance();
    private ElevatorAndArm elevatorAndArm = ElevatorAndArm.getInstance();
    private CameraProcess vision = CameraProcess.getInstance();
    private Timer timer = new Timer();
    private boolean isBackingUp = true;
    private boolean continueToNextTask = false;
    private boolean donePlacing = false;
    private int drivingStep = -1;
    private int driveDistance;
    private int angle;
    private TargetData targetData;        
    private int step = 0; // move to top
    private int pointCounter = 0; // moveto top
    private double motorSpeedFactor;

    boolean isDoneDriving = false; // move to top
    boolean isDoneSpinning = false; // move to top

    private static Autonomous instance = new Autonomous();

    private Autonomous()
    {
        System.out.println(this.getClass().getName() + ": Started Constructing");
        System.out.println(this.getClass().getName() + ": Finished Constructing");
    }

    public static Autonomous getInstance()
    {
        return instance;
    }

    public void autoInit()
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

    ArrayList<Point> pathing = new ArrayList<Point>();

    private PregameSetupTabData pregameSetupTabData;

    public void setMotorSpeeds()
    {
        switch(pregameSetupTabData.motorSpeed)
        {
            case k100percent:
                motorSpeedFactor = 1.00;
                break;
            case k75percent:
                motorSpeedFactor = 0.75;
                break;
            case k50percent:
                motorSpeedFactor = 0.50;
                break;
            case k25percent:
                motorSpeedFactor = 0.25;
                break;
        }
    }
    /**
     * this function is meant to be called before adding any points based on the
     * pregame info it will intialize the first point to where the robot is starting
     */

    
    public void taskOnePathing()
    {
        switch (pregameSetupTabData.startingLocation)
        {
        case kLeft:
            pathing.add(new Point(Constants.LEFT_STARTING_POSITION_X, Constants.LEFT_STARTING_POSITION_Y));
            pathing.add(new Point(Constants.LEFT_STARTING_POSITION_X, Constants.LEFT_STARTING_POSITION_Y + 36));
            break;
        case kRight:
            pathing.add(new Point(Constants.RIGHT_STARTING_POSITION_X, Constants.RIGHT_STARTING_POSITION_Y));
            pathing.add(new Point(Constants.RIGHT_STARTING_POSITION_X, Constants.RIGHT_STARTING_POSITION_Y + 36));
            break;
        case kCenter:
            pathing.add(new Point(Constants.CENTER_STARTING_POSITION_X, Constants.CENTER_STARTING_POSITION_Y));
            pathing.add(new Point(Constants.CENTER_STARTING_POSITION_X, Constants.CENTER_STARTING_POSITION_Y + 36));
            break;
        case kNone:
            break;
        }

        switch (pregameSetupTabData.task1Objective)
        {
        case kRocket:
            if (pregameSetupTabData.task1RocketHatch == RocketHatch.kFront)
                pathing.add(new Point(leftOrRight() * 200, 300));
            if (pregameSetupTabData.task1RocketHatch == RocketHatch.kBack)
                pathing.add(new Point(leftOrRight() * 200, 340));
            break;
        case kCargoShip:
            pathing.add(new Point(leftOrRight() * 100, 80));
            pathing.add(new Point(leftOrRight() * 100, 200));

            switch (pregameSetupTabData.task1CargoShip)
            {
            case kSideNear:
                pathing.add(new Point(leftOrRight() * 100, 210));
                break;

            case kSideMiddle:
                pathing.add(new Point(leftOrRight() * 100, 240));
                break;

            case kSideFar:
                pathing.add(new Point(leftOrRight() * 100, 280));
                break;
            }
            break;

        case kNothing:
            break;

        }
    }

    /*
    public boolean taskOnePlacement()
    {
        if (pregameSetupTabData.task1Objective == Objective.kRocket)
        {
            switch (pregameSetupTabData.task1GamePiece)
            {
            case kHatchPanel:
                switch (pregameSetupTabData.task1RocketLevel)
                {
                case kBottom:
                    placeObject(ElevatorPosition.kBottomHatch, Position.kBottomHatch);
                    break;
                case kMiddle:
                    placeObject(ElevatorPosition.kCenterHatch, Position.kCenterHatch);
                    break;
                case kTop:
                    placeObject(ElevatorPosition.kTopHatch, Position.kTopHatch);
                    break;
                }
                break;
            case kCargo:
                switch (pregameSetupTabData.task1RocketLevel)
                {

                case kBottom:
                    placeObject(ElevatorPosition.kBottomCargo, Position.kBottomCargo);
                    break;
                case kMiddle:
                    placeObject(ElevatorPosition.kCenterCargo, Position.kCenterCargo);
                    break;
                case kTop:
                    placeObject(ElevatorPosition.kTopCargo, Position.kTopCargo);
                    break;
                }
                break;

            case kNone:
                break;

            }
        }
        else if (pregameSetupTabData.task1Objective == Objective.kCargoShip)
        {
            switch (pregameSetupTabData.task1GamePiece)
            {
            case kCargo:
                placeObject(ElevatorPosition.kCargoShipCargo, Position.kCargoShipCargo);
                break;

            case kHatchPanel:
                placeObject(ElevatorPosition.kCargoShipCargo, Position.kCargoShipCargo); // is there an enum for cargo
                                                                                         // ship hatch panel???
                break;

            }
        }
        return continueToNextTask;
    }
    */
    /*
    public boolean placeObject(Elevator.Constants.ElevatorPosition elevatorPosition, Arm.Constants.Position armPosition)
    {

        boolean elevatorDoneMoving;
        boolean armDoneMoving;
        boolean doneDriving;
        elevatorDoneMoving = moveElevator(elevatorPosition);
        armDoneMoving = moveArm(armPosition);
        if (elevatorDoneMoving && armDoneMoving)
        {
            doneDriving = drivetrain.driveDistanceInInches(18, .7, (int) drivetrain.getHeadingInDegrees(), 3,
                    OmniEncoder.kBoth);
            if (doneDriving)
            {
                if (pregameSetupTabData.task1GamePiece == GamePiece.kHatchPanel)
                {
                    arm.releaseHatchPanel(); // add the timer function
                }
                else if (pregameSetupTabData.task1GamePiece == GamePiece.kCargo)
                {
                    arm.ejectCargo(.9);
                }
            }
            isBackingUp = drivetrain.driveDistanceInInches(18, -.7, (int) drivetrain.getHeadingInDegrees(), 3,
                    OmniEncoder.kBoth);
            if (!isBackingUp)
            {
                continueToNextTask = true;
                return continueToNextTask;
            }
        }
        return false;
    }
    */

    public boolean taskOneFinalSpin()
    {
        boolean isDoneSpinning = false;
        switch (pregameSetupTabData.task1Objective)
        {
        case kRocket:
            if (pregameSetupTabData.task1RocketHatch == RocketHatch.kBack)
            {
                isDoneSpinning = drivetrain.spinToBearing(120 * leftOrRight(), Constants.ROTATION_SPEED * motorSpeedFactor);
            }
            // possibly add the front rocket
            break;
        case kCargoShip:
            isDoneSpinning = drivetrain.spinToBearing(90 * -leftOrRight(), Constants.ROTATION_SPEED * motorSpeedFactor);
        break;
        }

        return isDoneSpinning;
    }
    
    /*
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
    */

    /**
     * this function moves the elevator to the position specified
     * 
     * @return this function returns true if the elevator is not moving and false if
     *         it is still moving
     */
    /*
    public boolean moveElevator(Elevator.Constants.ElevatorPosition position)
    {
        elevatorAndArm.setElevatorTargetPosition(position);
        if (!elevator.isMoving())
            return true;
        else
            return false;
    }
    */

    /**
     * this function will move the arm
     * 
     * @return gives back boolean true is done moving, false if it is still moving
     */
    /*
    public boolean moveArm(Arm.Constants.Position position)
    {
        boolean isArmMoving = arm.isArmMoving();
        boolean isWristMoving = arm.isWristMoving();
        elevatorAndArm.setArmTargetPosition(position);
        if (!isWristMoving && !isArmMoving)
        {
            return true;
        }
        else
            return false;

    }
    */
    
    /**
     * this function will add another point to drive to in INCHES
     */

    public void addPoint(int x, int y)
    {
        pathing.add(new Point(x, y));
    }

    /**
     * this will calculate the magnitude at which the robot will need to travel at
     * 
     * @param counter
     * @return
     */
    public double calculateMagnitude(int counter)
    {
        double xInitial, xFinal, xDifference;
        double yInitial, yFinal, yDifference;
        Point initialLocation;
        Point finalLocation;

        // get the point where you are and the point where you need to go
        initialLocation = pathing.get(counter);
        finalLocation = pathing.get(counter + 1);
        // get the x and y values of the two points
        xInitial = initialLocation.getX();
        yInitial = initialLocation.getY();
        xFinal = finalLocation.getX();
        yFinal = finalLocation.getY();
        xDifference = xFinal - xInitial;
        yDifference = yFinal - yInitial;
        // calculate the distance to travel
        return Math.sqrt(Math.pow(yFinal - yInitial, 2) + Math.pow(xFinal - xInitial, 2));

    }

    /**
     * this will return the angle at which the robot will need to drive at.
     * 
     * @param counter
     * @return a value frmo -pi to +pi (radians)
     */
    public double calculateAngle(int counter)
    {
        double xInitial, xFinal, xDifference;
        double yInitial, yFinal, yDifference;
        double angle;
        Point initialLocation;
        Point finalLocation;

        // get the point where you are and the point where you need to go
        initialLocation = pathing.get(counter);
        finalLocation = pathing.get(counter + 1);
        // get the x and y values of the two points
        xInitial = initialLocation.getX();
        yInitial = initialLocation.getY();
        xFinal = finalLocation.getX();
        yFinal = finalLocation.getY();
        xDifference = xFinal - xInitial;
        yDifference = yFinal - yInitial;
        // calculate the angle to travel
        return Math.atan2(xDifference, yDifference) * (180.0 / Math.PI);  //the x and y are flipped because thte
        //navX and the atan two work on different coordinate planes.
    }

    /**
     * this will take the point you are on, and drive you to the next one in the
     * arrayList
     * 
     * @param pointCounter
     * @param stepper
     * @return boolean true if done false if still moving.
     * 
     */
    public boolean driveToPoint(int pointCounter)
    {
        switch (drivingStep)
        {
        case 0: // this case will just calculate the values needed
            driveDistance = (int) calculateMagnitude(pointCounter);
            angle = (int) calculateAngle(pointCounter);
            System.out.println("Heading : " + drivetrain.getHeadingInDegrees() +  " Angle: " + calculateAngle(pointCounter) + " Magnitude: " + calculateMagnitude(pointCounter));
            drivingStep++;
            if(drivingStep == 1)
            {
                System.out.println("Moving on to step 1");
            }
            break;
        case 1: // this step will rotate the robot if needed
            //System.out.println("Heading : " + drivetrain.getHeadingInDegrees() +  " Angle: " + calculateAngle(pointCounter) + " Magnitude: " + calculateMagnitude(pointCounter));
            if ((angle < drivetrain.getHeadingInDegrees() - 3) || (angle > drivetrain.getHeadingInDegrees() + 3)) 
            {
                isDoneSpinning = drivetrain.spinToBearing(angle, 0.3);
            }
            else
            {
                isDoneSpinning = true;
            }
            if (isDoneSpinning)
           
            {
                drivingStep++;
                System.out.println("Moving On to step 2");
            }
            break;
        case 2: // this step will moving the robot if needed
            isDoneDriving = drivetrain.driveDistanceInInches(driveDistance, 0.25 , angle, 5, OmniEncoder.kLeft);
            //System.out.println("Encoder Value: " + drivetrain.getAvgDistanceInInches() + "   navX Value: " + drivetrain.getHeadingInDegrees());
            if (isDoneDriving)
            {
                drivingStep++;
                System.out.println("Moving On To Default");
            }
            break;
        default: // this case will intialize the values needed.
            drivingStep = 0;
            isDoneDriving = false;
            isDoneSpinning = false;
            break;
        }
        return isDoneDriving;

    }

    /**
     * this function sets a value to use for right or left calculation
     * 
     * @return -1 if left, +1 if right
     */
    public int leftOrRight()
    {
        if (pregameSetupTabData.startingLocation == StartingLocation.kLeft)
        {
            return -1;
        }
        else if (pregameSetupTabData.startingLocation == StartingLocation.kRight)
            ;
        {
            return 1;
        }
    }

    public boolean executeTasks()
    {
        boolean isAllDone = false;
        boolean isDone;
        switch (step)
        {
        case 0:
            //taskOnePathing();
            addPoint(0, 0);
            addPoint(0, 24);
            addPoint(48, 72);
            addPoint(108, 72);
            step++;
            break;
        case 1:
            isDone = driveToPoint(pointCounter);
            if (isDone)
                pointCounter++;
            if (pointCounter > pathing.size() - 2)
                isAllDone = true;
            break;
        case 2:
            //isDone = taskOneFinalSpin();
            //if (isDone)
                //step++;
            break;
        case 3: 
            //isDone = whiteLineAlignment();
            //if(isDone)
                //step++;
            break;
        case 4:
            //isDone = taskOnePlacement();
            //if (isDone)
                //step++;
            break;
        
        }
        return isAllDone;
    }


    public static class Constants
    {
        private static final int LEFT_STARTING_POSITION_X = 1;
        private static final int LEFT_STARTING_POSITION_Y = 1;
        private static final int RIGHT_STARTING_POSITION_X = 1;
        private static final int RIGHT_STARTING_POSITION_Y = 1;
        private static final int CENTER_STARTING_POSITION_X = 1;
        private static final int CENTER_STARTING_POSITION_Y = 1;

        private static final double ROTATION_SPEED = 0.5;
        private static final double STRAFE_SPEED = 0.5;
    }
}