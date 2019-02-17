/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.components;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

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
    private DoubleSolenoid pinSolenoid = new DoubleSolenoid(Constants.SOLENOID_PORT_1, Constants.SOLENOID_PORT_2);
    private WPI_TalonSRX masterLegMotor =  new WPI_TalonSRX(Constants.CLIMBER_TALON_PORT);
    private WPI_VictorSPX slaveLegMotor = new WPI_VictorSPX(Constants.CLIMBER_VICTOR_PORT);

    private static Climber instance = new Climber();

    private Climber()
    {
        masterLegMotor.configFactoryDefault();
        slaveLegMotor.configFactoryDefault();

        masterLegMotor.setNeutralMode(NeutralMode.Brake);
        slaveLegMotor.setNeutralMode(NeutralMode.Brake);

        masterLegMotor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector,LimitSwitchNormal.NormallyOpen);
        masterLegMotor.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);
        masterLegMotor.configSelectedFeedbackSensor(com.ctre.phoenix.motorcontrol.FeedbackDevice.QuadEncoder, 0, 0);

        masterLegMotor.configClearPositionOnLimitR(true, 0);

        slaveLegMotor.follow(masterLegMotor);
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
        speed = Math.abs(speed);
        masterLegMotor.set(speed);
    }

    public void retractLegs(double speed) 
    {
        speed = -Math.abs(speed);
        masterLegMotor.set(speed);
    }

    public void stopLegs()
    {
        masterLegMotor.set(0);
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


    @Override
    public String toString()
    {
        return String.format("Encoder: %d  Motor Current: %.2f", getEncoder(), masterLegMotor.getOutputCurrent());
    }


    public static class Constants
    {
        public static final int SOLENOID_PORT_1 = 4;
        public static final int SOLENOID_PORT_2 = 5;
        public static final int CLIMBER_TALON_PORT = 14;
        public static final int CLIMBER_VICTOR_PORT = 15;

        public static final int MAX_CLIMBER_HEIGHT = 500;
        public static final int MIN_CLIMBER_HEIGHT = -5;
    }
}
