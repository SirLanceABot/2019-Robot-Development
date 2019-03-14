
package frc.components;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

/**
 * Add your docs here.
 */
public class Grabber
{
    public static class Constants
    {
        public static final int GRABBER_SOLENOID_EXTEND = 4;
        public static final int GRABBER_SOLENOID_RETRACT = 5;
    }

    

    private DoubleSolenoid grabberSolenoid = new DoubleSolenoid(Constants.GRABBER_SOLENOID_EXTEND,
            Constants.GRABBER_SOLENOID_RETRACT); // On the blue solenoid holder
    private Timer grabberTimer = new Timer();
    private boolean isGrabberRetracted = true;
    private boolean isGrabberMoving = false;

    private static Grabber instance = new Grabber();

    private Grabber()
    {
        System.out.println(this.getClass().getName() + ": Started Constructing");
        System.out.println(this.getClass().getName() + ": Finished Constructing");
    }

    public static Grabber getInstance()
    {
        return instance;
    }

    public void retractGrabber()
    {
        grabberSolenoid.set(Value.kReverse);
    }

    public void extendGrabber()
    {
        grabberSolenoid.set(Value.kForward);
    }

    public void toggleHatchPanel()
    {
        if (isGrabberRetracted)
        {
            extendGrabber();
            isGrabberRetracted = false;
            System.out.println("The grabber is extended");
        }
        else
        {
            retractGrabber();
            isGrabberRetracted = true;
            System.out.println("The grabber is retracted");
        }
    }

    public double getRumbleTimer()
    {
        return grabberTimer.get();
    }

    public String getGrabberSolenoidData()
    {
        return grabberSolenoid.get().toString();
    }

    /**
     * @return the isGrabberMoving, whether the grabber is moving or not
     */
    public boolean isGrabberMoving()
    {
        return isGrabberMoving;
    }

    /**
     * returns the grabbers position
     * 
     * @return kReverse is retracted, kForward is extended
     */
    public DoubleSolenoid.Value getGrabberPosition()
    {
        return grabberSolenoid.get();
    }
    
}
