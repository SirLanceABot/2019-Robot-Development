package frc.robot;

import frc.robot.components.Drivetrain;

import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot
{
    private Drivetrain drivetrain = Drivetrain.getInstance();
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
        drivetrain.teleop();
    }
}