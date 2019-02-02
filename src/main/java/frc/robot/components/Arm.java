/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.components;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.Talon;


/**
 * Add your docs here.
 */
public class Arm {
    private DoubleSolenoid armSolenoid = new DoubleSolenoid(0,1);
    private DoubleSolenoid wristSolenoid = new DoubleSolenoid(2,3);
    private DoubleSolenoid grabberSolenoid = new DoubleSolenoid(4,5);

    public static class Constants
    {
        

    }

}

