/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.components;

import frc.components.Arm;
import frc.components.Elevator;
import frc.components.Arm.Constants.ArmPosition;
import frc.components.Arm.Constants.Position;

/**
 * Add your docs here.
 */
public class ElevatorAndArm 
{
    private int elevatorPotValue;
    private int armPotValue;
    private Elevator.Constants.ElevatorPosition targetElevatorPosition = Elevator.Constants.ElevatorPosition.kNone;
    private Arm.Constants.Position targetArmPosition = Arm.Constants.Position.kNone;
    private static int floorElevatorPosition = 100;
    private static int horizontalArmPosition = 100;

    private boolean setElevatorStartingPosition = true;
    private boolean setArmStartingPosition = true;
    private double startingElevatorPosition;
    private double startingArmPosition;

    private Arm arm = Arm.getInstance();
    private Elevator elevator = Elevator.getInstance();

    private static ElevatorAndArm instance = new ElevatorAndArm();

    private ElevatorAndArm()
    {
        System.out.println(this.getClass().getName() + ": Started Constructing");
        System.out.println(this.getClass().getName() + ": Finished Constructing");
    }

    public static ElevatorAndArm getInstance()
    {
        return (instance);
    }

    public void setElevatorTargetPosition(Elevator.Constants.ElevatorPosition targetPosition)
    {
        targetElevatorPosition = targetPosition;
    }

    /**
     * Returns the current target position of the elevator
     * 
     * @return the target position the elevator is attempting to move to
     */
    public Elevator.Constants.ElevatorPosition getTargetElevatorPosition()
    {
       return targetElevatorPosition;
    }

    public void setArmTargetPosition(Arm.Constants.Position targetPosition)

    {
        targetArmPosition = targetPosition;
    }

    /**
     * Returns the current target position of the elevator
     * 
     * @return the target position the elevator is attempting to move to
     */
    public Arm.Constants.Position getTargetArmPosition()
    {
       return targetArmPosition;
    }

    public void moveTo()
    {
        elevatorPotValue = elevator.getPotValue();
        //System.out.println(elevator + "Target Elevator: " + targetElevatorPosition);
        //System.out.println(arm + "Arm Elevator: " + targetArmPosition);
        armPotValue = arm.getPotValue();
        horizontalArmPosition = arm.getArmPositionPotValue(Arm.Constants.ArmPosition.kHorizontalArmPosition);
        floorElevatorPosition = elevator.getElevatorPositionPotValues(Elevator.Constants.ElevatorPosition.kFloor);

        if(targetElevatorPosition != Elevator.Constants.ElevatorPosition.kNone)
        {
            // if(armPotValue > horizontalArmPosition + 200)
            // {
            //     elevator.stopElevator();
            //     elevator.setIsMoving(true);
            // }
            //else 
            if(elevatorPotValue < (elevator.getElevatorPositionPotValues(targetElevatorPosition) - elevator.getElevatorPositionPotValues(Elevator.Constants.ElevatorPosition.kThreshold)))
            {
                elevator.raiseElevator(scaleElevatorMovement());
            }
            else if(elevatorPotValue > (elevator.getElevatorPositionPotValues(targetElevatorPosition) + elevator.getElevatorPositionPotValues(Elevator.Constants.ElevatorPosition.kThreshold)))
            {
                elevator.lowerElevator(Constants.ELEVATOR_DOWN_SCALE_FACTOR * scaleElevatorMovement());
                //elevator.lowerElevator(0.4 * Constants.ELEVATOR_DOWN_SCALE_FACTOR);
                // elevator.lowerElevator(-0.3);
                // if(arm.getWristPosition(targetArmPosition) == Arm.Constants.WristPosition.kWristDown)
                // {
                //     if(elevatorPotValue > initialElevatorPosition || armPotValue < arm.getHorizontalArmPosition())
                //     {
                //         elevator.lowerElevator(scaleElevatorMovement());
                //     }
                //     else
                //     {
                //         elevator.stopElevator(scaleElevatorMovement());
                //     }
                // }
                // else
                // {       
                //     elevator.lowerElevator(scaleElevatorMovement());
                // }
            }
            else
            {
                //elevator.stopElevator();
                elevator.holdElevator();
                targetElevatorPosition = Elevator.Constants.ElevatorPosition.kNone;
            }
        }
        else
        {
            setElevatorStartingPosition = true;
        }

        if(targetArmPosition != Arm.Constants.Position.kNone)
        {
            if(arm.getWristPosition(targetArmPosition) == Arm.Constants.WristPosition.kWristDown)
            {
                arm.setIsWristMoving(true);
                arm.moveWristDown();

                if(arm.isWristDown())
                { 
                    targetArmPosition = arm.setWristPosition(targetArmPosition, Arm.Constants.WristPosition.kWristNone);
                    arm.setIsWristMoving(false);
                }
            }
            else if(arm.getWristPosition(targetArmPosition) == Arm.Constants.WristPosition.kWristUp)
            {
                arm.setIsWristMoving(true);
                arm.moveWristUp();

                if (arm.isWristUp())
                {
                    targetArmPosition = arm.setWristPosition(targetArmPosition, Arm.Constants.WristPosition.kWristNone);
                    arm.setIsWristMoving(false);
                }
                // if (elevatorPotValue > initialElevatorPosition - 50
                //      && armPotValue < horizontalArmPosition + 50) // Check logic
                // {
                //     arm.moveWristUp();

                //     if (arm.isWristUp())
                //     {
                //         targetArmPosition = arm.setWristPosition(targetArmPosition, Arm.Constants.WristPosition.kWristNone);
                //         arm.setIsWristMoving(false);
                //     }
                // }
            }

            if(armPotValue > arm.getArmPositionPotValue(targetArmPosition) + 10)// && targetArmPosition.getArmPosition() != Arm.Constants.ArmPosition.kArmNone)
            {
                arm.moveArmUp(scaleArmMovement());
            }
            else if(armPotValue < arm.getArmPositionPotValue(targetArmPosition) - 10)// && targetArmPosition.getArmPosition() != Arm.Constants.ArmPosition.kArmNone)
            {
                arm.moveArmDown(Constants.ARM_DOWN_SCALE_FACTOR * scaleArmMovement());

                // if(arm.getWristPosition(targetArmPosition) == Arm.Constants.WristPosition.kWristDown && !arm.isWristDown())
                // {
                //     if(elevatorPotValue > initialElevatorPosition || armPotValue < horizontalArmPosition)
                //     {
                //         arm.moveArmDown(scaleArmMovement());
                //     }
                //     else
                //     {
                //         arm.stopArm();
                //     }
                // }
                // else
                // {
                //     arm.moveArmDown(scaleArmMovement());
                // }

            }
            else
            {
                arm.stopArm();
                //targetArmPosition.setArmPosition(Arm.Constants.ArmPosition.kArmNone); 
            }

            if(arm.isWristMoving() == false && arm.isArmMoving() == false)
            {
                targetArmPosition = Position.kNone;
            }
        }
        else
        {
            setArmStartingPosition = true;
        }
    }

    public double scaleElevatorMovement()
    {

        double currentPotValue = elevator.getPotValue();
        if(setElevatorStartingPosition)
        {
            startingElevatorPosition = currentPotValue;
            setElevatorStartingPosition = false;
        }
        
        double endingPosition = elevator.getElevatorPositionPotValues(targetElevatorPosition);
        double distanceToTravel = Math.abs(endingPosition - startingElevatorPosition);
        double startingDistance = distanceToTravel / 10.0;
        double stoppingDistance = distanceToTravel - startingDistance;
        double distanceTraveled = Math.abs(startingElevatorPosition - currentPotValue);

        if(distanceTraveled < startingDistance)
        {
            return ((Constants.ELEVATOR_MAX_SPEED - Constants.ELEVATOR_STARTING_SPEED) / startingDistance) * distanceTraveled + Constants.ELEVATOR_STARTING_SPEED;
        }
        else if(distanceTraveled >= startingDistance && distanceTraveled <= stoppingDistance)
        {
            return Constants.ELEVATOR_MAX_SPEED;
        }
        else if(distanceTraveled > stoppingDistance)
        {
            return ((Constants.ELEVATOR_MAX_SPEED - Constants.ELEVATOR_STARTING_SPEED) / (distanceToTravel - stoppingDistance)) * (distanceToTravel - distanceTraveled) + Constants.ELEVATOR_STARTING_SPEED;
        }
        else
            return 0.0;
    }

    public double scaleArmMovement()
    {

        double currentPotValue = arm.getPotValue();
        if(setArmStartingPosition)
        {
            startingArmPosition = currentPotValue;
            setArmStartingPosition = false;
        }
        
        double endingPosition = arm.getArmPositionPotValue(targetArmPosition);
        double distanceToTravel = Math.abs(endingPosition - startingArmPosition);
        double startingDistance = distanceToTravel / 10.0;
        double stoppingDistance = distanceToTravel - startingDistance;
        double distanceTraveled = Math.abs(startingArmPosition - currentPotValue);

        if(distanceTraveled < startingDistance)
        {
            return ((Constants.ARM_MAX_SPEED - Constants.ARM_STARTING_SPEED) / startingDistance) * distanceTraveled + Constants.ARM_STARTING_SPEED;
        }
        else if(distanceTraveled >= startingDistance && distanceTraveled <= stoppingDistance)
        {
            return Constants.ARM_MAX_SPEED;
        }
        else if(distanceTraveled > stoppingDistance)
        {
            return ((Constants.ARM_MAX_SPEED - Constants.ARM_STARTING_SPEED) / (distanceToTravel - stoppingDistance)) * (distanceToTravel - distanceTraveled) + Constants.ARM_STARTING_SPEED;
        }
        else
            return 0.0;
    }

    public static class Constants
    {
        public final static double ARM_MAX_SPEED = 1.0;
        public final static double ARM_STARTING_SPEED = 0.3;
        public final static double ARM_DOWN_SCALE_FACTOR = 0.85;

        public final static double ELEVATOR_MAX_SPEED = 1.0;
        public final static double ELEVATOR_STARTING_SPEED = 0.3;
        public final static double ELEVATOR_DOWN_SCALE_FACTOR = 0.85;

    }
}
