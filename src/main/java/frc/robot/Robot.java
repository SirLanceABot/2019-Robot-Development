package frc.robot;

import frc.components.Drivetrain;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.control.DriverXbox;
import frc.control.Xbox;
import frc.control.Xbox.Constants;

public class Robot extends TimedRobot
{
    private Drivetrain drivetrain = Drivetrain.getInstance();
    private DriverXbox driverXbox = DriverXbox.getInstance();

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

    }

    @Override
    public void teleopPeriodic()
    {
        double[] scaledArray = driverXbox.getScaledAxes(Constants.LEFT_STICK_AXES);
        drivetrain.teleop();

        System.out.printf("X = %5.3f Y = %5.3f X = %5.3f Y = %5.3f \n",
                driverXbox.getRawAxis(Constants.LEFT_STICK_X_AXIS), driverXbox.getRawAxis(Constants.LEFT_STICK_Y_AXIS),
                scaledArray[0], scaledArray[1]);
    }
}