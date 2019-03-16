package frc.components;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.components.Arm.Constants.WristPosition;

/**
 * Add your docs here.
 */
public class Wrist
{
    public static class Constants
    {
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

        private static final int WRIST_SOLENOID_EXTEND = 0;
        private static final int WRIST_SOLENOID_RETRACT = 1;
        private static final int LIMIT_SWITCH_UP = 1;
        private static final int LIMIT_SWITCH_DOWN = 2;
    }

    private DoubleSolenoid wristSolenoid = new DoubleSolenoid(Constants.WRIST_SOLENOID_EXTEND, Constants.WRIST_SOLENOID_RETRACT); // On the blue solenoid holder
    private DigitalInput limitSwitchUp = new DigitalInput(Constants.LIMIT_SWITCH_UP);
    private DigitalInput limitSwitchDown = new DigitalInput(Constants.LIMIT_SWITCH_DOWN);

    private boolean isWristMoving = false;
    private WristPosition currentWristState = WristPosition.kWristUp;
    private WristPosition targetWristState = WristPosition.kWristUp;

    private static Wrist instance = new Wrist();

    private Wrist()
    {
        System.out.println(this.getClass().getName() + ": Started Constructing");
        System.out.println(this.getClass().getName() + ": Finished Constructing");
    }

    public static Wrist getInstance()
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

    public void toggleWrist()
    {
        if(isWristUp())
        {
            moveWristDown();
        }
        else
        {
            moveWristUp();
        }
    }

    public boolean isWristDown()
    {
        if (wristSolenoid.get() == Value.kReverse)
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
        if (wristSolenoid.get() == Value.kForward)
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
    // public Constants.Position setWristPosition(Constants.Position position,
    // Constants.WristPosition wristPosition)
    // {
    // position.wristPosition = wristPosition;

    // return position;
    // }

    public void setState(WristPosition position)
    {
        targetWristState = position;
    }

    public WristPosition wristControl()
    {
        switch (currentWristState)
        {
        case kWristDown:
            if (targetWristState == WristPosition.kWristUp)
            {
                moveWristUp();
                currentWristState = WristPosition.kWristUp;
            }
            else if (targetWristState == WristPosition.kWristDown)
            {
                currentWristState = WristPosition.kWristDown;
            }
            break;
        case kWristUp:
            if (targetWristState == WristPosition.kWristUp)
            {
                currentWristState = WristPosition.kWristUp;
            }
            else if (targetWristState == WristPosition.kWristDown)
            {
                moveWristDown();
                currentWristState = WristPosition.kWristDown;
            }
            break;
        }
        
        return currentWristState;
    }
}
