
package frc.components;

import frc.components.Arm;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.Timer;

/**
 * Add your docs here.
 */
public class Intake 
{
    public static class Constants
    {
        private static final int INTAKE_VICTOR_PORT = 0;
        private static final int INTAKE_TALON_PORT = 1;
        private static final double BALL_STALL_CURRENT = 0.5;
        private static final double CURRENT_LIMIT = 15.0;
        private static final double RUN_TIME = 0.5;
    }
    enum IntakeState
    {
        // kOff turns either way off, kIntake is intake at 100%, kHold is intake at 10%,
        // kEject is eject at 100%
        kOff, kIntake, kHold, kEject;
    }

    private Intake()
    {

    }

    private static Intake instance = new Intake();

    private IntakeState stateOfIntake;
    private Timer startupTimer = new Timer();
    private WPI_TalonSRX intakeRoller = new WPI_TalonSRX(Constants.INTAKE_TALON_PORT);
    private Arm arm = Arm.getInstance();
    private boolean firstTimeOverAmpLimit = true;

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

    public double getRollerAmperage()
    {
        return intakeRoller.getOutputCurrent();
    }

    public void cargoControl(boolean inButtonHeld, boolean outButton, boolean inButtonPressed)
    {
        double motorCurrent = arm.getIntakeAmperage();
        switch (stateOfIntake)
        {
        case kOff:

            if (inButtonHeld)
            {
                stateOfIntake = IntakeState.kIntake;
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
        case kIntake:
            if (motorCurrent < Constants.CURRENT_LIMIT)
            {
                if (inButtonHeld)
                {
                    firstTimeOverAmpLimit = true;
                    stateOfIntake = IntakeState.kIntake;
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
                    stateOfIntake = IntakeState.kIntake;
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
                stateOfIntake = IntakeState.kIntake;
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
                stateOfIntake = IntakeState.kIntake;
            }
            else
            {
                stateOfIntake = IntakeState.kOff;
            }
            break;
        }
    }
}
