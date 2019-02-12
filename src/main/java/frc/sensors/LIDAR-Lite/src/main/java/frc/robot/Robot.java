package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot {

	private LIDAR_Lite mLIDAR;

	@Override
	public void robotInit()
	{
		mLIDAR = new LIDAR_Lite(Constants.LIDAR.PORT, Constants.LIDAR.ADDRESS);
	}

	@Override
	public void teleopInit()
	{
		System.out.println("Selected LIDAR_Lite Test");
	}

	@Override
	public void teleopPeriodic()
	{
		//System.out.println(mLIDAR.GetLaserPower());


		System.out.println("Distance reading: " + mLIDAR.GetDistance());
		
		
	}
}

// 0x2c = 215 cm
// 0x6d = 215 cm
// 0xff = 230 cm 
// 0x3e8 = 230 cm