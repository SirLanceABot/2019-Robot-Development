package frc.components;

import edu.wpi.first.wpilibj.DigitalOutput;

/**
 * Class to represent a Light Ring on the robot.
 * Used for illuminating either the floor or a vision target.
 */
public class Lights extends DigitalOutput
{

    private static Lights instance = new Lights(Constants.LIGHT_PORT);

	/**
	 * Constructor for the LightRing class
	 * @param port The port in which the light ring is connected.
	 */
	private Lights(int port)
	{
        super(port);
        System.out.println(this.getClass().getName() + ": Started Constructing");
        System.out.println(this.getClass().getName() + ": Finished Constructing");
    }

    public static Lights getInstance()
    {
        return instance;
    }
    
    /**
     * Turn the light bar and light rings on.
     */
    public void turnLightsOn()
    {
        set(true);
    }

    /**
     * Turn the light bar and light rings off.
     */
    public void turnLightsOff()
    {
        set(false);
    }

    public static class Constants
    {
        public static final int LIGHT_PORT = 0;
    }
}
