package frc.sensors;

import edu.wpi.first.wpilibj.AnalogInput;

/**
 * Ultrasonic distance sensor class
 * @author Julien Thrum and Darryl Wong
 *
 */
public class Sonar extends AnalogInput
{
	/**
	 * Sonar class constructor
	 * @param port The port the sensor is plugged into.
	 */
	public Sonar(int port)
	{
        super(port);
        System.out.println(this.getClass().getName() + ": Started Constructing");
        System.out.println(this.getClass().getName() + ": Finished Constructing");
	}
	
	/**
	 * Returns the distance (in inches) measured by the the sensor.
	 * @return Distance (in inches).
	 */
	public double getInches()
	{
		return this.getVoltage() * Constants.VOLTAGE_TO_INCHES;	
	}
	
	public static class Constants
	{
		public static final double VOLTAGE_TO_INCHES = 40.2969293756;	// Constant found online to convert voltage from ultrasonic sensor to inches
	}
}
