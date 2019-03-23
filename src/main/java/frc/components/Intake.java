
package frc.components;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Timer;

/**
 * Add your docs here.
 */
public class Intake
{
    public static class Constants
    {
        public static final int INTAKE_PORT = 12;
        public static final double BALL_STALL_CURRENT = 0.5;
        public static final double CURRENT_LIMIT = 15.0;
        public static final double RUN_TIME = 0.5;
    }

    public enum IntakeState
    {
        // kOff turns either way off, kIntake is intake at 100%, kHold is intake at 10%,
        // kEject is eject at 100%
        kOff, kIntaking, kHold, kEject;
    }

    private IntakeState stateOfIntake;
    private Timer intakeTimer = new Timer();
    private WPI_TalonSRX intakeRoller = new WPI_TalonSRX(Constants.INTAKE_PORT);
    private boolean firstTimeOverAmpLimit = true;
    private IntakeState currentState = IntakeState.kHold;
    private IntakeState targetState = IntakeState.kHold;

    private static Intake instance = new Intake();

    private Intake()
    {
        System.out.println(this.getClass().getName() + ": Started Constructing");
        intakeRoller.configFactoryDefault();
        System.out.println(this.getClass().getName() + ": Finished Constructing");
    }

    public static Intake getInstance()
    {
        return instance;
    }

    public void intakeCargo()
    {
        intakeRoller.set(0.75);
    }

    public void ejectCargo()
    {
        intakeRoller.set(-0.75);
    }

    public void stopCargo()
    {
        intakeRoller.set(0.0);
    }

    public void holdCargo()
    {
        intakeRoller.set(0.2);
    }

    public double getIntakeAmperage()
    {
        return intakeRoller.getOutputCurrent();
    }

    public String getIntakeRollerMotorData()
    {
        return String.format("%6.3f,  %6d,  %6.3f,  %5.1f", intakeRoller.get(),
                intakeRoller.getSelectedSensorPosition(), intakeRoller.getOutputCurrent(),
                intakeRoller.getTemperature() * (9.0 / 5.0) + 32.0);
    }

    public void setState(Intake.IntakeState targetState)
    {
        this.targetState = targetState;
    }

    public IntakeState intakeControl()
    {
        double motorCurrent = getIntakeAmperage();
        switch (currentState)
        {
        case kEject:
            firstTimeOverAmpLimit = true;
            switch (targetState)
            {
            case kEject:
                ejectCargo();
                currentState = IntakeState.kEject;
                break;
            case kIntaking:
                intakeCargo();
                currentState = IntakeState.kIntaking;
                break;
            case kHold:
                holdCargo();
                currentState = IntakeState.kHold;
                break;
            case kOff:
                stopCargo();
                currentState = IntakeState.kOff;
                break;
            }
            break;

        case kIntaking:
            if (motorCurrent > Constants.CURRENT_LIMIT)
            {
                if(firstTimeOverAmpLimit)
                {
                    intakeTimer.reset();
                    intakeTimer.start();
                    intakeCargo();
                    currentState = IntakeState.kIntaking;
                    firstTimeOverAmpLimit = false;
                }
                else if(intakeTimer.get() > Constants.RUN_TIME)
                {
                    holdCargo();
                    currentState = IntakeState.kHold;
                    intakeTimer.stop();
                    intakeTimer.reset();
                }
            }           
            else
            {
                switch (targetState)
                {
                case kEject:
                    ejectCargo();
                    currentState = IntakeState.kEject;
                    break;
                case kIntaking:
                    intakeCargo();
                    currentState = IntakeState.kIntaking;
                    break;
                case kHold:
                    holdCargo();
                    currentState = IntakeState.kHold;
                    break;
                case kOff:
                    stopCargo();
                    currentState = IntakeState.kOff;
                    break;
                }
            }
            break;
        case kHold:
            firstTimeOverAmpLimit = true;
            switch (targetState)
            {
            case kEject:
                ejectCargo();
                currentState = IntakeState.kEject;
                break;
            case kIntaking:
                holdCargo();
                currentState = IntakeState.kHold;
                break;
            case kHold:
                holdCargo();
                currentState = IntakeState.kHold;
                break;
            case kOff:
                if(motorCurrent > 0.5)
                {
                    holdCargo();
                    currentState = IntakeState.kHold;
                }
                else
                {
                    stopCargo();
                    currentState = IntakeState.kOff;
                }
                break;
            }
            break;
        case kOff:
            firstTimeOverAmpLimit = true;
            switch (targetState)
            {
            case kEject:
                ejectCargo();
                currentState = IntakeState.kEject;
                break;
            case kIntaking:
                intakeCargo();
                currentState = IntakeState.kIntaking;
                break;
            case kHold:
                holdCargo();
                currentState = IntakeState.kHold;
                break;
            case kOff:
                stopCargo();
                currentState = IntakeState.kOff;
                break;
            }
            break;
        }

        return currentState;
    }

    //@deprecated
    public void cargoControl(boolean inButtonHeld, boolean outButton, boolean inButtonPressed)
    {
        double motorCurrent = getIntakeAmperage();
        switch (stateOfIntake)
        {
        case kOff:

            if (inButtonHeld)
            {
                stateOfIntake = IntakeState.kIntaking;
            }
            else if (outButton)
            {
                stateOfIntake = IntakeState.kEject;
            }
            else
            {
                stateOfIntake = IntakeState.kOff;
            }
            break;
        case kIntaking:
            if (motorCurrent < Constants.CURRENT_LIMIT)
            {
                if (inButtonHeld)
                {
                    firstTimeOverAmpLimit = true;
                    stateOfIntake = IntakeState.kIntaking;
                }
                if (!inButtonHeld)
                {
                    firstTimeOverAmpLimit = true;
                    stateOfIntake = IntakeState.kOff;
                }
            }
            else
            {
                if (inButtonHeld && firstTimeOverAmpLimit)
                {
                    intakeTimer.reset();
                    intakeTimer.start();
                    firstTimeOverAmpLimit = false;
                    stateOfIntake = IntakeState.kIntaking;
                }
                else if (inButtonHeld && intakeTimer.get() > Constants.RUN_TIME)
                {
                    firstTimeOverAmpLimit = true;
                    stateOfIntake = IntakeState.kHold;
                }
                else if (!inButtonHeld)
                {
                    firstTimeOverAmpLimit = true;
                    stateOfIntake = IntakeState.kHold;
                }
            }
            break;
        case kHold:
            if (outButton)
            {
                stateOfIntake = IntakeState.kEject;
            }
            else if (inButtonPressed)
            {
                stateOfIntake = IntakeState.kIntaking;
            }
            else if (motorCurrent > Constants.BALL_STALL_CURRENT)
            {
                stateOfIntake = IntakeState.kHold;
            }
            else if (motorCurrent < Constants.BALL_STALL_CURRENT)
            {
                stateOfIntake = IntakeState.kOff;
            }
            break;
        case kEject:
            if (outButton)
            {
                stateOfIntake = IntakeState.kEject;
            }
            else if (inButtonHeld)
            {
                stateOfIntake = IntakeState.kIntaking;
            }
            else
            {
                stateOfIntake = IntakeState.kOff;
            }
            break;
        }
    }
}
