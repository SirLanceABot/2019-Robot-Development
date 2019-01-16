package frc.robot.vision;

import com.esotericsoftware.jsonbeans.Json;

import frc.robot.network.RaspberryPiReceiver;

public class Vision
{	
//	private VideoSink cameraServer = CameraServer.getInstance().getServer();
	
	private VisionData visionData;
	private Json json = new Json();
	private RaspberryPiReceiver raspberryPiReceiver = RaspberryPiReceiver.getInstance();

	private String[] visionTargetIDs = {};
	private int numTargets = -1;
	private int offset = -1;
	private int midpointX = -1;
	private int largestHeight = -1;
	
	private static Vision instance = new Vision();
	public static Vision getInstance()
	{
		return instance;
	}
	
	private Vision()
	{
		
	}
	
	public void update()
	{
		visionData = json.fromJson(VisionData.class, raspberryPiReceiver.getRawData());
		visionData.getData().keySet().toArray(visionTargetIDs);
		this.numTargets = visionTargetIDs.length;
		this.midpointX = (visionData.getData().get(visionTargetIDs[0]).midpointX + visionData.getData().get(visionTargetIDs[1]).midpointX) / 2;
		this.offset = Constants.CENTER - midpointX;
		this.largestHeight = visionData.getData().get(visionTargetIDs[0]).getHeight();
	}
	
	public int getMidpointX()
	{
		return this.midpointX;
	}
	
	/**
	 * Gets the height of the tallest target found.
	 * Make sure to call the update() method before running this.
	 * @return Height of the tallest target found.
	 */
	public int getLargestHeight()
	{
		return this.largestHeight;
	}

	public double getTurnSpeed()
	{
		double kP = -0.00069;
		double controllerMin = 0.22;
		double controllerMax = 0.7;

		double speed = 0.0;
		
		if (numTargets > 0)
		{
			if (Math.abs(offset) <= Constants.ACCEPTABLE_OFFSET)
			{
				speed = 0.0;
			}
			else
			{
				speed = kP * offset;
				if (speed >= 0.0)
				{
					speed = Math.min(speed, controllerMax);
					speed = Math.max(speed, controllerMin);
				}
				else
				{
					speed = Math.max(speed, -controllerMax);
					speed = Math.min(speed, -controllerMin);
				}
			}
		}
		else
		{
			speed = 0.0;
		}
		return speed;
	}

	public void increaseExposure()
	{
//		camera.setExposureManual(13);
	}

	public void decreaseExposure()
	{
//		camera.setExposureManual(0);
	}

	public static class Constants
	{
		public static final int CENTER = 160;
		public static final int FINAL_ALIGNMENT_POINT = 250;
		public static final int CLOSE_TO_TARGET_HEIGHT = 55;
		public static final int CLOSE_TO_TARGET_DISTANCE_SONAR = 5;
		public static final int ACCEPTABLE_OFFSET = 6;
	}
}
