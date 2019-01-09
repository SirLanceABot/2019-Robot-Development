package frc.robot.components;

import edu.wpi.first.wpilibj.DigitalOutput;

/**
 * Class to represent a Light Ring on the robot.
 * Used for illuminating either the floor or a vision target.
 */
public class LightRing extends DigitalOutput
{
	/**
	 * Constructor for the LightRing class
	 * @param port The port in which the light ring is connected.
	 */
	public LightRing(int port)
	{
		super(port);
	}
}
