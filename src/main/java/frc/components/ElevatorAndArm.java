/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.components;

import frc.components.Arm;
import frc.components.Elevator;
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
    private static int initialElevatorPosition = 100;
    private static int horizontalArmPosition = 100;

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
        System.out.println(arm + "Arm Elevator: " + targetArmPosition);
        armPotValue = arm.getPotValue();
        horizontalArmPosition = arm.getHorizontalArmPosition();
        initialElevatorPosition = elevator.getInitialElevatorPosition();

        if(targetElevatorPosition != Elevator.Constants.ElevatorPosition.kNone)
        {
            // if(armPotValue < horizontalArmPosition + 200)
            // {
            //     elevator.stopElevator();
            //     elevator.setIsMoving(true);
            // }
            //else 
            if(elevatorPotValue < (elevator.getPositionValue(targetElevatorPosition) - elevator.getPositionValue(Elevator.Constants.ElevatorPosition.kThreshold)))
            {
                elevator.raiseElevator();
                elevator.setIsMoving(true);
            }
            else if(elevatorPotValue > (elevator.getPositionValue(targetElevatorPosition) + elevator.getPositionValue(Elevator.Constants.ElevatorPosition.kThreshold)))
            {
                elevator.setIsMoving(true);
                elevator.lowerElevator();
                // if(arm.getWristPosition(targetArmPosition) == Arm.Constants.WristPosition.kWristDown)
                // {
                //     if(elevatorPotValue > initialElevatorPosition || armPotValue > arm.getHorizontalArmPosition())
                //     {
                //         elevator.lowerElevator();
                //     }
                //     else
                //     {
                //         elevator.stopElevator();
                //     }
                // }
                // else
                // {       
                //     elevator.lowerElevator();
                // }
            }
            else
            {
                elevator.stopElevator();
                targetElevatorPosition = Elevator.Constants.ElevatorPosition.kNone;
                elevator.setIsMoving(false);
            }
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
                arm.setIsArmMoving(true);
                arm.moveWristUp();

                if (arm.isWristUp())
                {
                    targetArmPosition = arm.setWristPosition(targetArmPosition, Arm.Constants.WristPosition.kWristNone);
                    arm.setIsWristMoving(false);
                }
                // if (elevatorPotValue > initialElevatorPosition - 50
                //      && armPotValue > horizontalArmPosition - 50)
                // {
                //     arm.moveWristUp();

                //     if (arm.isWristUp())
                //     {
                //         targetArmPosition = arm.setWristPosition(targetArmPosition, Arm.Constants.WristPosition.kWristNone);
                //         arm.setIsWristMoving(false);
                //     }
                // }
            }

            if(armPotValue < arm.getTargetPositionArmPositionValue(targetArmPosition))
            {
                arm.moveArmUp();
                arm.setIsArmMoving(true);
            }
            else if(armPotValue > arm.getTargetPositionArmPositionValue(targetArmPosition))
            {
                arm.setIsArmMoving(true);
                arm.moveArmDown();

                // if(arm.getWristPosition(targetArmPosition) == Arm.Constants.WristPosition.kWristDown && !arm.isWristDown())
                // {
                //     if(elevatorPotValue > initialElevatorPosition || armPotValue > horizontalArmPosition)
                //     {
                //         arm.moveArmDown();
                //     }
                //     else
                //     {
                //         arm.stopArm();
                //     }
                // }
                // else
                // {
                //     arm.moveArmDown();
                // }

            }
            else
            {
                arm.stopArm();
                arm.setIsArmMoving(false);
            }

            if(arm.isWristMoving() == false && arm.isArmMoving() == false)
            {
                targetArmPosition = Position.kNone;
            }
        }
    }
}
