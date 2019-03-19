
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

    public enum GrabberState
    {
        kRetracted, kExtendedNoRumble, kExtendedRumble;
    }

    private DoubleSolenoid grabberSolenoid = new DoubleSolenoid(Constants.GRABBER_SOLENOID_EXTEND,
            Constants.GRABBER_SOLENOID_RETRACT); // On the blue solenoid holder
    private Timer grabberTimer = new Timer();
    private boolean isGrabberRetracted = true;
    private boolean isGrabberMoving = false;
    private GrabberState currentGrabberState = GrabberState.kRetracted;
    private GrabberState targetGrabberState = GrabberState.kRetracted;
    private boolean firstTimeExtended = true;

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

    public void setState(GrabberState targetState)
    {
        targetGrabberState = targetState;
    }

    public GrabberState grabberControl()
    {
        switch (currentGrabberState)
        {
        case kRetracted:
            if (targetGrabberState == GrabberState.kExtendedNoRumble)
            {
                extendGrabber();
                currentGrabberState = GrabberState.kExtendedNoRumble;
            }
            else if (targetGrabberState == GrabberState.kRetracted)
            {
                currentGrabberState = GrabberState.kRetracted;
            }
            break;
        case kExtendedNoRumble:
            if(firstTimeExtended)
            {
                grabberTimer.reset();
                grabberTimer.start();
                firstTimeExtended = false;
                currentGrabberState = GrabberState.kExtendedNoRumble;
            }
            else if (targetGrabberState == GrabberState.kExtendedNoRumble && grabberTimer.get() <= 5.0)
            {
                currentGrabberState = GrabberState.kExtendedNoRumble;
            }
            else if (targetGrabberState == GrabberState.kExtendedNoRumble && grabberTimer.get() > 5.0)
            {
                currentGrabberState = GrabberState.kExtendedRumble;
                targetGrabberState = GrabberState.kExtendedRumble;
                grabberTimer.stop();
                grabberTimer.reset();
                firstTimeExtended = true;
            }
            else if (targetGrabberState == GrabberState.kRetracted)
            {
                retractGrabber();
                grabberTimer.stop();
                grabberTimer.reset();
                firstTimeExtended = true;
                currentGrabberState = GrabberState.kRetracted;
            }
            break;
        case kExtendedRumble:
            if(targetGrabberState == GrabberState.kExtendedNoRumble)
            {
                currentGrabberState = GrabberState.kExtendedRumble;
            }
            else if(targetGrabberState == GrabberState.kExtendedRumble)
            {
                currentGrabberState = GrabberState.kExtendedRumble;
            }
            else if(targetGrabberState == GrabberState.kRetracted)
            {              
                currentGrabberState = GrabberState.kRetracted;
            }
        }
        
        return currentGrabberState;
    }
}
