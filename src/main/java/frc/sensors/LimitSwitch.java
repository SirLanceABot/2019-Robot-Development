package frc.sensors;

import edu.wpi.first.wpilibj.DigitalInput;

/**
 * Class for limit switches
 * @author Mark Washington
 *
 */
public class LimitSwitch extends DigitalInput
{
	/**
	 * Limit switch class constructor
	 * @param port Port the limit switch is plugged into
	 */
	public LimitSwitch(int port)
	{
        super(port);
        System.out.println(this.getClass().getName() + ": Started Constructing");
        System.out.println(this.getClass().getName() + ": Finished Constructing");
	}
}
