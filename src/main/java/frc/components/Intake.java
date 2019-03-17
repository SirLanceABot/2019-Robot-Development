
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

    enum IntakeState
    {
        // kOff turns either way off, kIntake is intake at 100%, kHold is intake at 10%,
        // kEject is eject at 100%
        kOff, kIntaking, kHold, kEject;
    }

    private static Intake instance = new Intake();
    private IntakeState stateOfIntake;
    private Timer startupTimer = new Timer();
    private WPI_TalonSRX intakeRoller = new WPI_TalonSRX(Constants.INTAKE_PORT);
    private boolean firstTimeOverAmpLimit = true;
    private IntakeState currentState = IntakeState.kOff;
    private IntakeState targetState = IntakeState.kOff;

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
        intakeRoller.set(Math.abs(.75));
    }

    public void ejectCargo()
    {
        intakeRoller.set(-Math.abs(.75));
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

    public void cargoControl()
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
            if (motorCurrent > Constants.CURRENT_LIMIT && firstTimeOverAmpLimit == true)
            {
                startupTimer.reset();
                startupTimer.start();
                intakeCargo();
                firstTimeOverAmpLimit = false;
                currentState = IntakeState.kIntaking;
                firstTimeOverAmpLimit = false;
            }
            else if(motorCurrent > Constants.CURRENT_LIMIT && startupTimer.get() > Constants.RUN_TIME)
            {
                currentState = IntakeState.kHold;
            }
            else
            {
                switch (targetState)
                {
                case kEject:
                    ejectCargo();
                    firstTimeOverAmpLimit = true;
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
                stopCargo();
                currentState = IntakeState.kOff;
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
                    startupTimer.reset();
                    startupTimer.start();
                    firstTimeOverAmpLimit = false;
                    stateOfIntake = IntakeState.kIntaking;
                }
                else if (inButtonHeld && startupTimer.get() > Constants.RUN_TIME)
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
