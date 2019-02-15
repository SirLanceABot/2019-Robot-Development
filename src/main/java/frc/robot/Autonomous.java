
package frc.robot;

import frc.components.Drivetrain;
import frc.robot.SlabShuffleboard;
import frc.robot.SlabShuffleboard.PregameSetupTabData;
import frc.components.Arm;
import frc.components.Elevator;
import edu.wpi.first.wpilibj.Timer;

public class Autonomous
{
    private SlabShuffleboard shuffleboard = SlabShuffleboard.getInstance();
    private Drivetrain drivetrain = Drivetrain.getInstance();
    private Arm arm = Arm.getInstance();
    private Elevator elevator = Elevator.getInstance();
    private Timer timer = new Timer();

    private PregameSetupTabData pregameSetupTabData;

    private boolean isNewPregamData = true;
    private boolean isPregame = true;
    private boolean isDoneDrivingOffPlatform = false;
    private boolean isDoneFirstRotation  = false;
    private boolean isDoneSecondRotation = false;
    private boolean isDoneTravelingToFirstDestination = false;
    private boolean isDoneDrivingToRocket = false;
    private boolean isDoneLeavingRocket = false;

    /**
     * This function gets the pregame data for the instance
     */
    public void getPregameData()
    {
        pregameSetupTabData = shuffleboard.getPregameSetupTabData();
    }

    public void rightSideRocket()
    {
        if(!isDoneDrivingOffPlatform)
        {
             isDoneDrivingOffPlatform = drivetrain.driveDistanceInInchesLeftSide(36, .5, 0, 5);
        }

        else if(!isDoneFirstRotation)
        {
            isDoneFirstRotation = drivetrain.spinToBearing(45, .3);
        }

        else if(!isDoneTravelingToFirstDestination)
        {
            isDoneTravelingToFirstDestination = drivetrain.driveDistanceInInchesLeftSide(84, .5, 0, 5);
        }

        else if(!isDoneSecondRotation)
        {
            isDoneSecondRotation = drivetrain.spinToBearing(45, .3);
        }

        else if(!isDoneDrivingToRocket)
        {
            isDoneDrivingToRocket = drivetrain.driveDistanceInInchesLeftSide(10, .5, 0, 3);
        }
        else if(true)
        {
            elevator.setTargetPosition(Elevator.Constants.ElevatorPosition.kBottomHatch);
            arm.setTargetPosition(Arm.Constants.Position.kBottomHatch);
            //need to add the timer
        }

        else if(!isDoneLeavingRocket)
        {
            isDoneLeavingRocket = drivetrain.driveDistanceInInchesLeftSide(-10, .5, 0, 3);
        }

    }
}
