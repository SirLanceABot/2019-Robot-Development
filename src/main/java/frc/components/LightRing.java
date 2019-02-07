package frc.components;

import edu.wpi.first.wpilibj.DigitalOutput;

/**
 * Class to represent a Light Ring on the robot.
 * Used for illuminating either the floor or a vision target.
 */
public class LightRing extends DigitalOutput
{

    DigitalOutput light1 = new DigitalOutput(Constants.LIGHT1PORT);

	/**
	 * Constructor for the LightRing class
	 * @param port The port in which the light ring is connected.
	 */
	public LightRing(int port)
	{
		super(port);
    }
    
    public void turnLightsOn()
    {
        light1.set(true);
    }

    public void turnLightsOff()
    {
        light1.set(false);
    }

    public static class Constants
    {
        public static final int LIGHT1PORT = 0;
    }
}
