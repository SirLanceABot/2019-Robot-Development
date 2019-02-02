/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.components;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.control.DriverXbox;
import frc.robot.control.OperatorXbox;
import frc.robot.control.Xbox;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;


/**
 * Add your docs here.
 */
public class Climber 
{
    private Solenoid pinSolenoid = new Solenoid(Constants.FOOT_SOLENOID_PORT);
    private WPI_TalonSRX masterDrive =  new WPI_TalonSRX(Constants.CLIMBER_TALON_PORT);
    private WPI_VictorSPX followerDrive = new WPI_VictorSPX(Constants.CLIMBER_VICTOR_PORT);

    private static Climber instance = new Climber();

    OperatorXbox operatorXbox = OperatorXbox.getInstance();
    private Climber()
    {
        pinSolenoid.set(false);
        masterDrive.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector,LimitSwitchNormal.NormallyOpen);
        masterDrive.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);
        followerDrive.follow(masterDrive);
    }

    public static Climber getInstance()
    {
        return(instance);
    }

    private void ejectPin()
    {
        pinSolenoid.set(true);
    }

    private void extendLegs(double speed)
    {
        speed = Math.abs(speed);
        masterDrive.set(speed);
    }

    private void retractLegs(double speed) 
    {
        speed = -Math.abs(speed);
        masterDrive.set(speed);
    }

    public void teleop()
    {
        boolean legsExtendButton = operatorXbox.getRawButton(Constants.EXTEND_LEGS_BUTTON);
        boolean legsRetractButton = operatorXbox.getRawButton(Constants.RETRACT_LEGS_BUTTON);
        boolean pushPin = operatorXbox.getRawButtonPressed(Constants.PUSH_PIN_BUTTON);
        boolean climbVerification = operatorXbox.getRawButton(Constants.CLIMB_VERIFICATION);

        if(climbVerification)
        {
            if(legsExtendButton)
            {
                extendLegs(0.5);
            }

            if(legsRetractButton)
            {
                retractLegs(0.5);
            }

            if(pushPin)
            {
                ejectPin();
            }
          }
    }


    


    public static class Constants
    {
        private static final int FOOT_SOLENOID_PORT = 0;
        private static final int CLIMBER_TALON_PORT = 0;
        private static final int CLIMBER_VICTOR_PORT = 0;
        private static final int EXTEND_LEGS_BUTTON = 0;
        private static final int RETRACT_LEGS_BUTTON = 0;
        private static final int PUSH_PIN_BUTTON = 0;
        private static final int CLIMB_VERIFICATION = 0;
    }
}
