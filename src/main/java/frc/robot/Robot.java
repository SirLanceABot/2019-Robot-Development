package frc.robot;

import frc.components.Drivetrain;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.control.DriverXbox;
import frc.robot.Teleop;
import frc.control.Xbox;
import frc.control.Xbox.Constants;

public class Robot extends TimedRobot
{
    private Drivetrain drivetrain = Drivetrain.getInstance();
    private DriverXbox driverXbox = DriverXbox.getInstance();
    private Teleop teleop = Teleop.getInstance();

    private boolean isCompetitionBot = true;    // Update Arm and Elevator Constants for Pots

    public Robot()
    {

    }

    @Override
    public void autonomousInit()
    {

    }

    @Override
    public void autonomousPeriodic()
    {

    }

    @Override
    public void teleopInit()
    {
        teleop.teleopInit();
    }

    @Override
    public void teleopPeriodic()
    {
        double[] scaledArray = driverXbox.getScaledAxes(Constants.LEFT_STICK_AXES, Xbox.Constants.PolynomialDrive.kCubicDrive);
        teleop.teleop();

        System.out.printf("X = %5.3f Y = %5.3f X = %5.3f Y = %5.3f \n",
                driverXbox.getRawAxis(Constants.LEFT_STICK_X_AXIS), driverXbox.getRawAxis(Constants.LEFT_STICK_Y_AXIS),
                scaledArray[0], scaledArray[1]);
    }
}