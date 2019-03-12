package frc.components;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

/**
 * Add your docs here.
 */
public class Wrist 
{
    public static class Constants
    {
        private static final int WRIST_SOLENOID_EXTEND = 0;
        private static final int WRIST_SOLENOID_RETRACT = 1;
    }

    private Wrist instance = new Wrist();
    private Wrist()
    {

    }

    private DoubleSolenoid wristSolenoid = new DoubleSolenoid(Constants.WRIST_SOLENOID_EXTEND,
    Constants.WRIST_SOLENOID_RETRACT); // On the blue solenoid holder

    private boolean isWristMoving = false;

    public static enum WristPosition
    {
        kWristDown(DoubleSolenoid.Value.kReverse), 
        kWristUp(DoubleSolenoid.Value.kForward), 
        kWristNone(DoubleSolenoid.Value.kOff);

        private DoubleSolenoid.Value value;

        private WristPosition(DoubleSolenoid.Value value)
        {
            this.value = value;
        }
    }

    public Wrist getInstance()
    {
        return instance;
    }
    public void moveWristUp()
    {
        wristSolenoid.set(Value.kForward);
    }

    public void moveWristDown()
    {
        wristSolenoid.set(Value.kReverse);
    }

    public DoubleSolenoid.Value getWristPosition()
    {
        return wristSolenoid.get();
    }

    public boolean isWristDown()
    {
        if(wristSolenoid.get() == Value.kForward)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public boolean isWristUp()
    {
        if(wristSolenoid.get() == Value.kForward)
        {
            return false;
        }
        else
        {
            return true;
        }    
    }

    public String getWristSolenoidData()
    {
        return wristSolenoid.get().toString();
    }

    /**
     * checks if the wrist is moving
     */
    public boolean isWristMoving()
    {
        return isWristMoving;
    }

    /**
     * sets if the wrist is moving
     */
    public void setIsWristMoving(boolean isWristMoving)
    {
        this.isWristMoving = isWristMoving;
    }
    // public Constants.Position setWristPosition(Constants.Position position, Constants.WristPosition wristPosition)
    // {
    //     position.wristPosition = wristPosition;

    //     return position;
    // }
}
