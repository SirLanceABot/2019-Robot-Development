
package frc.components;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import frc.control.DriverXbox;
import frc.control.Xbox;

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

    public enum grabberState
    {
        kRetracted, kExtended, kRumble, kNoRumble, kNone;
    }
    

    private DoubleSolenoid grabberSolenoid = new DoubleSolenoid(Constants.GRABBER_SOLENOID_EXTEND,
            Constants.GRABBER_SOLENOID_RETRACT); // On the blue solenoid holder
    private Timer grabberTimer = new Timer();
    private DriverXbox driverXbox = DriverXbox.getInstance();
    private boolean isGrabberRetracted = true;
    private boolean isGrabberMoving = false;
    private grabberState currentGrabberState = grabberState.kRetracted;
    private grabberState targetGrabberState = grabberState.kNone;
    private grabberState rumbleState = grabberState.kNoRumble;

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

    public void setState(grabberState targetGrabberState)
    {
        this.targetGrabberState = targetGrabberState;
    }

    public void grabberControl()
    {
        switch(targetGrabberState)
        {
            case kExtended:
                if(currentGrabberState == grabberState.kRetracted)
                {
                    extendGrabber();
                    grabberTimer.reset();
                    grabberTimer.start();
                    currentGrabberState = grabberState.kExtended;
                }
                else if(currentGrabberState == grabberState.kExtended && grabberTimer.get() > 5.0)
                {
                    currentGrabberState = grabberState.kExtended;
                    rumbleState = grabberState.kRumble;
                }
                else if(currentGrabberState == grabberState.kNone)
                {
                    extendGrabber();
                    grabberTimer.reset();
                    grabberTimer.start();
                    currentGrabberState = grabberState.kExtended;
                }
                else if(currentGrabberState == grabberState.kExtended)
                {
                    currentGrabberState = grabberState.kExtended;
                }
                break;
            case kRetracted:
                if(currentGrabberState == grabberState.kExtended)
                {
                    retractGrabber();
                    grabberTimer.stop();
                    grabberTimer.reset();
                    currentGrabberState = grabberState.kRetracted;
                    rumbleState = grabberState.kNoRumble;
                }
                else if(currentGrabberState == grabberState.kNone)
                {
                    retractGrabber();
                    currentGrabberState = grabberState.kRetracted;
                    rumbleState = grabberState.kNoRumble;
                }
                else if(currentGrabberState == grabberState.kRetracted)
                {
                    currentGrabberState = grabberState.kRetracted;
                    rumbleState = grabberState.kNoRumble;
                }
                break;
            case kNone:
                break;
        }
    }
        public void setRumble()
        {
            if(rumbleState == grabberState.kRumble)
            {
                driverXbox.setRumble(RumbleType.kRightRumble, 0.5);;
            }
            else
            {
                driverXbox.setRumble(RumbleType.kRightRumble, 0.00);
            }

        }

}
