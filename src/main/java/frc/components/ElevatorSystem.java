/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.components;

import frc.components.NewArm;
import frc.components.Carriage;
import frc.components.Grabber;
import frc.components.Intake;
import frc.components.Wrist;
import frc.components.Arm.Constants.ArmPosition;
import frc.components.NewArm.Constants.NewArmPosition;
import frc.components.Wrist.Constants.WristPosition;

/**
 * Add your docs here.
 */
public class ElevatorSystem 
{
    private int carriagePotValue;
    private int armPotValue;
    private Carriage.Constants.CarriagePosition targetCarriagePosition = Carriage.Constants.CarriagePosition.kNone;
    private NewArm.Constants.NewArmPosition targetArmPosition = NewArm.Constants.NewArmPosition.kArmNone;
    private Wrist.Constants.WristPosition targetWristPosition = Wrist.Constants.WristPosition.kWristNone;
    private static int floorElevatorPosition = 100;
    private static int horizontalArmPosition = 100;

    private boolean setCarriageStartingPosition = true;
    private boolean setArmStartingPosition = true;
    private double startingCarriagePosition;
    private double startingArmPosition;

    private NewArm newArm = NewArm.getInstance();
    private Carriage carriage = Carriage.getInstance();
    private Grabber grabber = Grabber.getInstance();
    private Wrist wrist = Wrist.getInstance();
    private Intake intake = Intake.getInstance();

    private static ElevatorSystem instance = new ElevatorSystem();

    private ElevatorSystem()
    {
        System.out.println(this.getClass().getName() + ": Started Constructing");
        System.out.println(this.getClass().getName() + ": Finished Constructing");
    }

    public static ElevatorSystem getInstance()
    {
        return instance;
    }

    public void setCarriageTargetPosition(Carriage.Constants.CarriagePosition targetPosition)
    {
        targetCarriagePosition = targetPosition;
    }

    /**
     * Returns the current target position of the carriage
     * 
     * @return the target position the carriage is attempting to move to
     */
    public Carriage.Constants.CarriagePosition getTargetCarriagePosition()
    {
       return targetCarriagePosition;
    }

    public void setArmTargetPosition(NewArm.Constants.NewArmPosition targetPosition)
    {
        targetArmPosition = targetPosition;
    }

    /**
     * Returns the current target position of the carriage
     * 
     * @return the target position the carriage is attempting to move to
     */
    public NewArm.Constants.NewArmPosition getTargetArmPosition()
    {
       return targetArmPosition;
    }

    public void setWristTargetPosition(Wrist.Constants.WristPosition wristPosition)
    {
        targetWristPosition = wristPosition;
    }

    public Wrist.Constants.WristPosition getWristTargetPosition()
    {
        return targetWristPosition;
    }

    //////////////////////////////////////////////////////////////////////////////////////
    public void setStates()
    {
        //this goes into teleop
    }
    public void executeStateMachines()
    {

    }
    //////////////////////////////////////////////////////////////////////////////////////
    public void moveTo()
    {
        carriagePotValue = carriage.getPotValue();
        //System.out.println(carriage + "Target Carriage: " + targetElevatorPosition);
        //System.out.println(arm + "Arm: " + targetArmPosition);
        armPotValue = newArm.getPotValue();
        horizontalArmPosition = newArm.getArmPositionPotValue(NewArm.Constants.NewArmPosition.kHorizontalArmPosition);
        floorElevatorPosition = carriage.getCarriagePositionPotValues(Carriage.Constants.CarriagePosition.kFloor);

        if(targetCarriagePosition != Carriage.Constants.CarriagePosition.kNone)
        {
            // if(armPotValue > horizontalArmPosition + 200)
            // {
            //     carriage.stopCarriage();
            //     carriage.setIsMoving(true);
            // }
            //else 
            if(carriagePotValue < (carriage.getCarriagePositionPotValues(targetCarriagePosition) - carriage.getCarriagePositionPotValues(Carriage.Constants.CarriagePosition.kThreshold)))
            {
                carriage.raiseCarriage(scaleCarriageMovement());
            }
            else if(carriagePotValue > (carriage.getCarriagePositionPotValues(targetCarriagePosition) + carriage.getCarriagePositionPotValues(Carriage.Constants.CarriagePosition.kThreshold)))
            {
                carriage.lowerCarriage(Constants.CARRIAGE_DOWN_SCALE_FACTOR * scaleCarriageMovement());
       
                // if(targetWristPosition == Wrist.Constants.WristPosition.kWristDown)
                // {
                //     if(carriagePotValue > initialCarriagePosition || armPotValue < newArm.getHorizontalArmPosition())
                //     {
                //         carriage.lowerCarriage(Constants.CARRIAGE_DOWN_SCALE_FACTOR * scaleCarriageMovement());
                //     }
                //     else
                //     {
                //         carriage.stopCarriage(Constants.CARRIAGE_DOWN_SCALE_FACTOR * scaleCarriageMovement());
                //     }
                // }
                // else
                // {       
                //     carriage.lowerCarriage(Constants.CARRIAGE_DOWN_SCALE_FACTOR * scaleCarriageMovement());
                // }
            }
            else
            {
                //carriage.stopCarriage();
                carriage.holdCarriage();
                targetCarriagePosition = Carriage.Constants.CarriagePosition.kNone;
            }
        }
        else
        {
            setCarriageStartingPosition = true;
        }

        if(targetArmPosition != NewArm.Constants.NewArmPosition.kArmNone)
        {
            if(targetWristPosition == Wrist.Constants.WristPosition.kWristDown)
            {
                wrist.setIsWristMoving(true);
                wrist.moveWristDown();

                if(wrist.isWristDown())
                { 
                    targetWristPosition = Wrist.Constants.WristPosition.kWristNone;
                    wrist.setIsWristMoving(false);
                }
            }
            else if(targetWristPosition == Wrist.Constants.WristPosition.kWristUp)
            {
                wrist.setIsWristMoving(true);
                wrist.moveWristUp();

                if (wrist.isWristUp())
                {
                    targetWristPosition = Wrist.Constants.WristPosition.kWristNone;
                    wrist.setIsWristMoving(false);
                }
                // if (carriagePotValue > initialCarriagePosition - 50
                //      && armPotValue < horizontalArmPosition + 50) // Check logic
                // {
                //     wrist.moveWristUp();

                //     if (wrist.isWristUp())
                //     {
                //         targetWristPosition = Arm.Constants.WristPosition.kWristNone;
                //         wrist.setIsWristMoving(false);
                //     }
                // }
            }

            if(armPotValue > newArm.getArmPositionPotValue(targetArmPosition) + 10)// && targetArmPosition.getArmPosition() != NewArm.Constants.NewArmPosition.kArmNone)
            {
                newArm.moveArmUp(scaleArmMovement());
            }
            else if(armPotValue < newArm.getArmPositionPotValue(targetArmPosition) - 10)// && targetArmPosition.getArmPosition() != NewArm.Constants.NewArmPosition.kArmNone)
            {
                newArm.moveArmDown(Constants.ARM_DOWN_SCALE_FACTOR * scaleArmMovement());

                // if(targetWristPosition == Wrist.Constants.WristPosition.kWristDown && !wrist.isWristDown())
                // {
                //     if(carriagePotValue > initialCarriagePosition || armPotValue < horizontalArmPosition)
                //     {
                //         newArm.moveArmDown(scaleArmMovement());
                //     }
                //     else
                //     {
                //         newArm.stopArm();
                //     }
                // }
                // else
                // {
                //     newArm.moveArmDown(scaleArmMovement());
                // }

            }
            else
            {
                newArm.stopArm();
                //targetArmPosition = NewArm.Constants.NewArmPosition.kArmNone; 
            }

            if(wrist.isWristMoving() == false && newArm.isArmMoving() == false)
            {
                targetArmPosition = NewArm.Constants.NewArmPosition.kArmNone;
                targetWristPosition = Wrist.Constants.WristPosition.kWristNone;
            }
        }
        else
        {
            setArmStartingPosition = true;
        }
    }

    public double scaleCarriageMovement()
    {

        double currentPotValue = carriage.getPotValue();
        if(setCarriageStartingPosition)
        {
            startingCarriagePosition = currentPotValue;
            setCarriageStartingPosition = false;
        }
        
        double endingPosition = carriage.getCarriagePositionPotValues(targetCarriagePosition);
        double distanceToTravel = Math.abs(endingPosition - startingCarriagePosition);
        double startingDistance = distanceToTravel / 10.0;
        double stoppingDistance = distanceToTravel - startingDistance;
        double distanceTraveled = Math.abs(startingCarriagePosition - currentPotValue);

        if(distanceTraveled < startingDistance)
        {
            return ((Constants.CARRIAGE_MAX_SPEED - Constants.CARRIAGE_STARTING_SPEED) / startingDistance) * distanceTraveled + Constants.CARRIAGE_STARTING_SPEED;
        }
        else if(distanceTraveled >= startingDistance && distanceTraveled <= stoppingDistance)
        {
            return Constants.CARRIAGE_MAX_SPEED;
        }
        else if(distanceTraveled > stoppingDistance)
        {
            return ((Constants.CARRIAGE_MAX_SPEED - Constants.CARRIAGE_STARTING_SPEED) / (distanceToTravel - stoppingDistance)) * (distanceToTravel - distanceTraveled) + Constants.CARRIAGE_STARTING_SPEED;
        }
        else
            return 0.0;
    }

    public double scaleArmMovement()
    {

        double currentPotValue = newArm.getPotValue();
        if(setArmStartingPosition)
        {
            startingArmPosition = currentPotValue;
            setArmStartingPosition = false;
        }
        
        double endingPosition = newArm.getArmPositionPotValue(targetArmPosition);
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

        public final static double CARRIAGE_MAX_SPEED = 1.0;
        public final static double CARRIAGE_STARTING_SPEED = 0.3;
        public final static double CARRIAGE_DOWN_SCALE_FACTOR = 0.85;

    }
}
