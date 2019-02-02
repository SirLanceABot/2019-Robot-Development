/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.components;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.control.DriverXbox;
import frc.control.OperatorXbox;
import frc.control.Xbox;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;



/**
 * Add your docs here.
 */
public class Arm {
    private DoubleSolenoid armSolenoid = new DoubleSolenoid(Constants.ARM_SOLENOID_PORT_1, Constants.ARM_SOLENOID_PORT_2);
    private DoubleSolenoid wristSolenoid = new DoubleSolenoid(Constants.WRIST_SOLENOID_PORT_1,Constants.WRIST_SOLENOID_PORT_2);
    private DoubleSolenoid grabberSolenoid = new DoubleSolenoid(Constants.GRABBER_SOLENOID_PORT_1, Constants.GRABBER_SOLENOID_PORT_2);
    
    private WPI_TalonSRX horizontalRoller = new WPI_TalonSRX(Constants.ROLLER_TALON_ID); 
    private WPI_VictorSPX horizontalRollerSlave = new WPI_VictorSPX(Constants.ROLLER_VICTOR_ID); 
    private boolean armPosition; //true is up false is down
    private boolean wristPosition; //true is up false is down
    private boolean hatchPanelPosition; //true for expanded false for contracted

    private static DriverXbox controller = DriverXbox.getInstance();

    private static Arm instance = new Arm();

    private Arm()
    {
        horizontalRollerSlave.follow(horizontalRoller);

    }

    public static Arm getInstance()
    {
        return(instance);
    }

    public boolean getHatchPanelPosition()
    {
        return(hatchPanelPosition);
    }
    public boolean getArmPosition()
    {
        return(armPosition);
    }

    public boolean getWristPosition()
    {
        return(wristPosition);
    }

    public void moveArmUp()
    {
        armSolenoid.set(Value.kForward);
        armPosition = true;
    }

    public void moveArmDown()
    {
        armSolenoid.set(Value.kReverse);
        armPosition = false;
    }

    public void moveWristUp()
    {
        wristSolenoid.set(Value.kForward);
        wristPosition = true;
    }

    public void moveWristDown()
    {
        wristSolenoid.set(Value.kReverse);
        wristPosition = false; 
    }

    public void intakeCargo(double speed)
    {
        speed = Math.abs(speed);
        horizontalRoller.set(speed);
    }

    public void ejectCargo(double speed)
    {
        speed = -Math.abs(speed);
        horizontalRoller.set(speed);
    }

    public void stopCargo()
    {
        horizontalRoller.set(0);
    }
    public void expandHatchPanelPlug()
    {
        grabberSolenoid.set(Value.kForward);
        hatchPanelPosition = true;
    }
    
    public void contractHatchPanelPlug()
    {
        grabberSolenoid.set(Value.kReverse);
        hatchPanelPosition = false;
    }

    
    public void teleop()
    {
        boolean armButton = controller.getRawButton(Constants.ARM_BUTTON_ID);
        boolean wristButton = controller.getRawButton(Constants.WRIST_BUTTON_ID);
        boolean cargoInButton = controller.getRawButton(Constants.CARGO_BUTTON_IN_ID);
        boolean cargoOutButton = controller.getRawButton(Constants.CARGO_BUTTON_OUT_ID);
        boolean hatchPanelButton = controller.getRawButton(Constants.WRIST_BUTTON_ID);

        if(armButton)
        {
            if(getArmPosition() == true)
            {
                moveArmDown();
            }
            else
            {
                moveArmUp();
            }
        }

        if(wristButton)
        {
            if(getWristPosition() == true)
            {
                moveWristDown();
            }
            else
            {
                moveWristUp();
            }
        }

        if(hatchPanelButton)
        {
            if(getHatchPanelPosition() == true)
            {
                contractHatchPanelPlug();
            }
            else
            {
                expandHatchPanelPlug();
            }
        }

        if(cargoInButton)
        {
            ejectCargo(0.5);
        }

        else if(cargoOutButton)
        {
            intakeCargo(0.5);
        }

        else
        {
            stopCargo();
        }
    }
    

    public String toString()
    {
        return String.format("ArmPosition: %s, HPP: %s, WristPosition: %s, (true is up/expanded false is down/contracted",
        getArmPosition(), getHatchPanelPosition(), getWristPosition());
    }


    public static class Constants
    {
        public static final int ARM_SOLENOID_PORT_1 = 0;
        public static final int ARM_SOLENOID_PORT_2 = 1;
        public static final int WRIST_SOLENOID_PORT_1 = 2;
        public static final int WRIST_SOLENOID_PORT_2 = 3;
        public static final int GRABBER_SOLENOID_PORT_1 = 4;
        public static final int GRABBER_SOLENOID_PORT_2 = 5;
        public static final int ROLLER_TALON_ID = 10;
        public static final int ROLLER_VICTOR_ID =  11;
        public static final int ARM_BUTTON_ID = 0;
        public static final int WRIST_BUTTON_ID = 0;
        public static final int CARGO_BUTTON_IN_ID = 0;
        public static final int CARGO_BUTTON_OUT_ID = 0;
        public static final int HATCH_PANEL_BUTTON_ID = 0;
    }

}

