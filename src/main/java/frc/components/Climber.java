
package frc.components;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.util.MotorConstants;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;

import com.ctre.phoenix.motorcontrol.NeutralMode;


/**
 * Add your docs here.
 */
public class Climber 
{
    public static class Constants
    {
        public static final int SOLENOID_PORT_1 = 2; 
        public static final int SOLENOID_PORT_2 = 3;
        public static final int CLIMBER_TALON_PORT = 14;
        public static final int CLIMBER_VICTOR_PORT = 15;
        public static final int CLIMBER_DRIVE_PORT = 13;
        public static final int MAX_CLIMBER_HEIGHT = 500;
        public static final int MIN_CLIMBER_HEIGHT = -5;
    }
    private DoubleSolenoid pinSolenoid = new DoubleSolenoid(Constants.SOLENOID_PORT_1, Constants.SOLENOID_PORT_2); // // On the white solenoid holder
    private WPI_TalonSRX masterLegMotor =  new WPI_TalonSRX(Constants.CLIMBER_TALON_PORT);
    private WPI_VictorSPX slaveLegMotor = new WPI_VictorSPX(Constants.CLIMBER_VICTOR_PORT);
    private WPI_VictorSPX forwardClimbDrive = new WPI_VictorSPX(Constants.CLIMBER_DRIVE_PORT);

    private static Climber instance = new Climber();

    private Climber()
    {
        System.out.println(this.getClass().getName() + ": Started Constructing");
        
        masterLegMotor.configFactoryDefault();
        slaveLegMotor.configFactoryDefault();
        forwardClimbDrive.configFactoryDefault();

        masterLegMotor.setNeutralMode(NeutralMode.Brake);
        slaveLegMotor.setNeutralMode(NeutralMode.Brake);

        masterLegMotor.configPeakCurrentLimit(MotorConstants.getMotorStallCurrent(MotorConstants.Constants.MotorType.kRedLine, 0.5));
        masterLegMotor.configPeakCurrentDuration(MotorConstants.Constants.PEAK_CURRENT_DURATION);
        masterLegMotor.configContinuousCurrentLimit(MotorConstants.Constants.CONTINOUS_CURRENT_LIMIT);
        masterLegMotor.configOpenloopRamp(MotorConstants.Constants.OPEN_LOOP_RAMP);
        masterLegMotor.enableCurrentLimit(true);

        masterLegMotor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector,LimitSwitchNormal.NormallyOpen);
        masterLegMotor.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);
        masterLegMotor.configSelectedFeedbackSensor(com.ctre.phoenix.motorcontrol.FeedbackDevice.QuadEncoder, 0, 0);

        forwardClimbDrive.setInverted(true);

        masterLegMotor.configClearPositionOnLimitR(true, 0);

        slaveLegMotor.follow(masterLegMotor);

        System.out.println(this.getClass().getName() + ": Finished Constructing");
    }

    public static Climber getInstance()
    {
        return(instance);
    }

    public void ejectPin()
    {
        pinSolenoid.set(Value.kForward);
    }

    public void resetPin()
    {
        pinSolenoid.set(Value.kReverse);
    }

    public void extendLegs(double speed)
    {
        speed = -Math.abs(speed);
        masterLegMotor.set(speed);
    }

    public void retractLegs(double speed) 
    {
        speed = Math.abs(speed);
        masterLegMotor.set(speed);
    }

    public void holdLegs(double speed)
    {
        extendLegs(speed);
    }
    public void stopLegs()
    {
        masterLegMotor.set(0);
    }

    public void driveForward(double speed)
    {
        forwardClimbDrive.set(Math.abs(speed));
    }
    
    public double getAmperage()
    {
        return masterLegMotor.getOutputCurrent();
    }
    public int getEncoder()
    {
        return masterLegMotor.getSelectedSensorPosition();
    }

    public void resetEncoderPosition()
    {
        masterLegMotor.setSelectedSensorPosition(0);
    }

    public void teleop()
    {
    }

    public String getMasterLegMotorData()
    {
        return String.format("%6.3f,  %6d,  %6.3f,  %5.1f",
            masterLegMotor.get(), masterLegMotor.getSelectedSensorPosition(),
            masterLegMotor.getOutputCurrent(), masterLegMotor.getTemperature() * (9.0 / 5.0) + 32.0);
    }

    public String getPinSolenoidData()
    {
        return pinSolenoid.get().toString();
    }

    @Override
    public String toString()
    {
        return String.format("Encoder: %d  Motor Current: %.2f", getEncoder(), masterLegMotor.getOutputCurrent());
    }

}
