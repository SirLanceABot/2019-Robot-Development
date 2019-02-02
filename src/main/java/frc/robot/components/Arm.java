/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.components;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.robot.control.DriverXbox;
import frc.robot.control.OperatorXbox;
import frc.robot.control.Xbox;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;



/**
 * Add your docs here.
 */
public class Arm {
    private DoubleSolenoid armSolenoid = new DoubleSolenoid(Constants.ARMSOLENOIDPORT1, Constants.ARMSOLENOIDPORT2);
    private DoubleSolenoid wristSolenoid = new DoubleSolenoid(Constants.WRISTSOLENOIDPORT1,Constants.WRISTSOLENOIDPORT2);
    private DoubleSolenoid grabberSolenoid = new DoubleSolenoid(Constants.GRABBERSOLENOIDPORT1, Constants.GRABBERSOLENOIDPORT2);
    
    private WPI_TalonSRX horizontalRoller = new WPI_TalonSRX(Constants.ROLLERTALONID); 
    private WPI_VictorSPX horizontalRollerSlave = new WPI_VictorSPX(Constants.ROLLERVICTORID); 
    private boolean armPosition; //true is up false is down
    private boolean wristPosition; //true is up false is down
    private boolean hatchPanelPosition; //true for expanded false for contracted

    private static DriverXbox controller = DriverXbox.getInstance();

    private static Arm instance = new Arm();

    private Arm()
    {
        horizontalRollerSlave.follow(horizontalRoller);

    }

    private static Arm getInstance()
    {
        return(instance);
    }

    private boolean getHatchPanelPosition()
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
        double armButton = controller.getRawAxis(1);
        double wristButton = controller.getRawAxis(2);
        double cargoButton = controller.getRawAxis(3);
        double hatchPanelButton = controller.getRawAxis(4);

        if(armButton == 1)
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

        if(wristButton == 1)
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

        if(hatchPanelButton == 1)
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


        
    }
    




    public static class Constants
    {
        public static final int ARMSOLENOIDPORT1 = 0;
        public static final int ARMSOLENOIDPORT2 = 1;
        public static final int WRISTSOLENOIDPORT1 = 2;
        public static final int WRISTSOLENOIDPORT2 = 3;
        public static final int GRABBERSOLENOIDPORT1 = 4;
        public static final int GRABBERSOLENOIDPORT2 = 5;
        public static final int ROLLERTALONID = 10;
        public static final int ROLLERVICTORID =  11;

    }

}

