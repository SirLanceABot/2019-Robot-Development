
package frc.components;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

/**
 * Add your docs here.
 */
public class Grabber
{
    public static class Constants
    {
        private static final int GRABBER_SOLENOID_PORT_1 = 0;
        private static final int GRABBER_SOLENOID_PORT_2 = 1;
    }

    private DoubleSolenoid grabberSolenoid = new DoubleSolenoid(Constants.GRABBER_SOLENOID_PORT_1,
    Constants.GRABBER_SOLENOID_PORT_2); // On the blue solenoid holder

    public void retractGrabber()
    {
        grabberSolenoid.set(Value.kReverse);
    }

    public void ExtendGrabber()
    {
        grabberSolenoid.set(Value.kForward);
    }

    /**
     * returns the grabbers position
     * @return kReverse is retracted, kForward is extended
     */
    public DoubleSolenoid.Value getgrabberPosition()
    {
        return grabberSolenoid.get();
    }
}
