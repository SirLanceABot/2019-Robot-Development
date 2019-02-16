
package frc.robot;

import frc.components.Drivetrain;
import frc.robot.SlabShuffleboard;
import frc.robot.SlabShuffleboard.Objective;
import frc.robot.SlabShuffleboard.PregameSetupTabData;
import frc.robot.SlabShuffleboard.RocketHatch;
import frc.robot.SlabShuffleboard.StartingLocation;
import frc.components.Arm;
import frc.components.Elevator;


import java.awt.Point;
import java.util.ArrayList;
import edu.wpi.first.wpilibj.Timer;

public class Autonomous
{
    private SlabShuffleboard shuffleboard = SlabShuffleboard.getInstance();
    private Drivetrain drivetrain = Drivetrain.getInstance();
    private Arm arm = Arm.getInstance();
    private Elevator elevator = Elevator.getInstance();
    private Timer timer = new Timer();
    private boolean donePlacing = false;
    ArrayList<Point> pathing = new ArrayList<Point>();

    private PregameSetupTabData pregameSetupTabData;

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

    public boolean taskOnePlacement()
    {
        boolean elevatorDoneMoving;
        boolean armDoneMoving;
        boolean doneDriving;
        boolean isBackingUp = true;
        boolean continueToNextTask = false;

        if (pregameSetupTabData.task1Objective == Objective.kRocket)
        {
            switch (pregameSetupTabData.task1GamePiece)
            {
            case kHatchPanel:
                switch (pregameSetupTabData.task1RocketLevel)
                {

                case kBottom:
                    elevatorDoneMoving = moveElevator(Elevator.Constants.ElevatorPosition.kBottomHatch);
                    armDoneMoving = moveArm(Arm.Constants.Position.kBottomHatch);
                    if (elevatorDoneMoving && armDoneMoving)
                    {
                        doneDriving = drivetrain.driveDistanceInInchesLeftSide(18, .7,
                                (int) drivetrain.getHeadingInDegrees(), 3);
                        if (doneDriving)
                        {
                            arm.releaseHatchPanel(); // add the timer function

                        }
                        isBackingUp = drivetrain.driveDistanceInInchesLeftSide(18, -.7,
                                (int) drivetrain.getHeadingInDegrees(), 3);
                        if (!isBackingUp)
                        {
                            continueToNextTask = true;
                            return continueToNextTask;
                        }
                    }
                    break;
                ////////////////////////////////////////////////////////////////////////////////////
                case kMiddle:
                    elevatorDoneMoving = moveElevator(Elevator.Constants.ElevatorPosition.kCenterHatch);
                    armDoneMoving = moveArm(Arm.Constants.Position.kCenterHatch);
                    if (elevatorDoneMoving && armDoneMoving)
                    {
                        doneDriving = drivetrain.driveDistanceInInchesLeftSide(18, .7,
                                (int) drivetrain.getHeadingInDegrees(), 3);
                        if (doneDriving)
                        {
                            arm.releaseHatchPanel(); // add the timer function
                        }
                        isBackingUp = drivetrain.driveDistanceInInchesLeftSide(18, -.7,
                                (int) drivetrain.getHeadingInDegrees(), 3);
                        if (!isBackingUp)
                        {
                            continueToNextTask = true;
                            return continueToNextTask;
                        }
                    }
                    break;
                ///////////////////////////////////////////////////////////////////////
                case kTop:
                    elevatorDoneMoving = moveElevator(Elevator.Constants.ElevatorPosition.kTopHatch);
                    armDoneMoving = moveArm(Arm.Constants.Position.kTopHatch);
                    if (elevatorDoneMoving && armDoneMoving)
                    {
                        doneDriving = drivetrain.driveDistanceInInchesLeftSide(18, .7,
                                (int) drivetrain.getHeadingInDegrees(), 3);
                        if (doneDriving)
                        {
                            arm.releaseHatchPanel(); // add the timer function
                        }
                        isBackingUp = drivetrain.driveDistanceInInchesLeftSide(18, -.7,
                                (int) drivetrain.getHeadingInDegrees(), 3);
                        if (!isBackingUp)
                        {
                            continueToNextTask = true;
                            return continueToNextTask;
                        }
                    }
                    break;
                //////////////////////////////////////////////////////////////////////
                }
                break;

            case kCargo:

                switch (pregameSetupTabData.task1RocketLevel)
                {

                case kBottom:
                    elevatorDoneMoving = moveElevator(Elevator.Constants.ElevatorPosition.kBottomCargo);
                    armDoneMoving = moveArm(Arm.Constants.Position.kBottomCargo);
                    if (elevatorDoneMoving && armDoneMoving)
                    {
                        doneDriving = drivetrain.driveDistanceInInchesLeftSide(18, .7,
                                (int) drivetrain.getHeadingInDegrees(), 3);
                        if (doneDriving)
                        {
                            arm.ejectCargo(.75); // add the timer function
                        }
                        isBackingUp = drivetrain.driveDistanceInInchesLeftSide(18, -.7,
                                (int) drivetrain.getHeadingInDegrees(), 3);
                    }
                    if (!isBackingUp)
                    {
                        continueToNextTask = true;
                        break;
                    }
                    ////////////////////////////////////////////////////////////////////////////////////
                case kMiddle:
                    elevatorDoneMoving = moveElevator(Elevator.Constants.ElevatorPosition.kCenterCargo);
                    armDoneMoving = moveArm(Arm.Constants.Position.kCenterCargo);
                    if (elevatorDoneMoving && armDoneMoving)
                    {
                        doneDriving = drivetrain.driveDistanceInInchesLeftSide(18, .7,
                                (int) drivetrain.getHeadingInDegrees(), 3);
                        if (doneDriving)
                        {
                            arm.ejectCargo(.75); // add the timer function
                        }
                        isBackingUp = drivetrain.driveDistanceInInchesLeftSide(18, -.7,
                                (int) drivetrain.getHeadingInDegrees(), 3);
                    }
                    if (!isBackingUp)
                    {
                        continueToNextTask = true;
                        break;
                    }
                    ///////////////////////////////////////////////////////////////////////
                case kTop:
                    elevatorDoneMoving = moveElevator(Elevator.Constants.ElevatorPosition.kTopCargo);
                    armDoneMoving = moveArm(Arm.Constants.Position.kTopCargo);
                    if (elevatorDoneMoving && armDoneMoving)
                    {
                        doneDriving = drivetrain.driveDistanceInInchesLeftSide(18, .7,
                                (int) drivetrain.getHeadingInDegrees(), 3);
                        if (doneDriving)
                        {
                            arm.ejectCargo(.75); // add the timer function
                        }
                        isBackingUp = drivetrain.driveDistanceInInchesLeftSide(18, -.7,
                                (int) drivetrain.getHeadingInDegrees(), 3);
                    }
                    if (!isBackingUp)
                    {
                        continueToNextTask = true;
                        break;
                    }
                    //////////////////////////////////////////////////////////////////////
                }
                break;

            case kNone:
                break;

            }
        }
        if (pregameSetupTabData.task1Objective == Objective.kCargoShip)
        {
            switch (pregameSetupTabData.task1GamePiece)
            {
            case kCargo:
                elevatorDoneMoving = moveElevator(Elevator.Constants.ElevatorPosition.kCargoShipCargo);
                armDoneMoving = moveArm(Arm.Constants.Position.kCargoShipCargo);
                if (elevatorDoneMoving && armDoneMoving)
                {
                    doneDriving = drivetrain.driveDistanceInInchesLeftSide(18, .7,
                            (int) drivetrain.getHeadingInDegrees(), 3);
                    if (doneDriving)
                    {
                        arm.ejectCargo(.75); // add the timer function

                    }
                    isBackingUp = drivetrain.driveDistanceInInchesLeftSide(18, -.7,
                            (int) drivetrain.getHeadingInDegrees(), 3);
                    if (!isBackingUp)
                    {
                        continueToNextTask = true;
                        return continueToNextTask;

                    }
                }
                break;

            case kHatchPanel:
                elevatorDoneMoving = moveElevator(Elevator.Constants.ElevatorPosition.kCargoShipCargo);
                armDoneMoving = moveArm(Arm.Constants.Position.kCargoShipCargo);
                if (elevatorDoneMoving && armDoneMoving)
                {
                    doneDriving = drivetrain.driveDistanceInInchesLeftSide(18, .7,
                            (int) drivetrain.getHeadingInDegrees(), 3);
                    if (doneDriving)
                    {
                        arm.releaseHatchPanel(); // add the timer function

                    }
                    isBackingUp = drivetrain.driveDistanceInInchesLeftSide(18, -.7,
                            (int) drivetrain.getHeadingInDegrees(), 3);
                    if (!isBackingUp)
                    {
                        continueToNextTask = true;
                        return continueToNextTask;

                    }
                }
                break;

            }
        }
        return continueToNextTask;
    }

    public boolean taskOneFinalSpin()
    {
        boolean isDoneSpinning = false;
        switch (pregameSetupTabData.task1Objective)
        {
        case kRocket:
            if (pregameSetupTabData.task1RocketHatch == RocketHatch.kBack)
            {
                isDoneSpinning = drivetrain.spinToBearing(120 * leftOrRight(), .75);
            }
            //possibly add the front rocket
            break;
        case kCargoShip:
            isDoneSpinning = drivetrain.spinToBearing(90 * -leftOrRight(), .75);
            break;
        }

        return isDoneSpinning;
    }

    public boolean moveElevator(Elevator.Constants.ElevatorPosition position)
    {
        elevator.setTargetPosition(position);
        if (!elevator.isMoving())
            return true;
        else
            return false;
    }
    
    public boolean moveArm(Arm.Constants.Position position)
    {
        /*
        arm.setTargetPosition(position);
        if (Arm.isWristMoving() && Arm.isArmMoving)
        {
            return true;
        }
        else
            return false;
        */
        return false;
    }
    
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
        return Math.atan2(yDifference, xDifference) * (180.0 / Math.PI);
    }

    public boolean driveToPoint(int counter)
    //change to be like the step in the execute
    {
        int numberOfPoints = pathing.size();
        int driveDistance;
        int angle;
        boolean isDoneDriving = false; //move to top
        boolean isDoneSpinning = false; //move to top
        driveDistance = (int) calculateMagnitude(counter);
        angle = (int) calculateAngle(counter);

        if ((angle != drivetrain.getHeadingInDegrees() && !isDoneSpinning)) //add tolerance
        {
            isDoneSpinning = drivetrain.spinToBearing(angle, 0.5);
        }
        else if (!isDoneDriving)
        {
            isDoneDriving = drivetrain.driveDistanceInInchesLeftSide(driveDistance / 20, 1, 0, driveDistance / 20);//these are integer division, change it
        }
        return isDoneDriving;
    }

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

    public void executeTasks()
    {
        int step = 0; // move to top
        int pointCounter = 0; //moveto top
        boolean isDone;
        switch (step)
        {
        case 0:
            taskOnePathing();
            step++;
            break;
        case 1:
            isDone = driveToPoint(pointCounter);
            if (isDone)
                pointCounter++;
            if (pointCounter > pathing.size())
                step++;
            break;
        case 2:
            isDone = taskOneFinalSpin();
            if (isDone)
                step++;
        case 3:
            isDone = taskOnePlacement();
            if (isDone)
                step++;
            break;

        }
        // right side start, right rocket, near side hatch, bottom
    }

    public static class Constants
    {
        private static final int LEFT_STARTING_POSITION_X = 1;
        private static final int LEFT_STARTING_POSITION_Y = 1;
        private static final int RIGHT_STARTING_POSITION_X = 1;
        private static final int RIGHT_STARTING_POSITION_Y = 1;
        private static final int CENTER_STARTING_POSITION_X = 1;
        private static final int CENTER_STARTING_POSITION_Y = 1;
    }
}
