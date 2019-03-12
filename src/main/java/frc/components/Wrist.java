
package frc.components;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

/**
 * Add your docs here.
 */
public class Wrist 
{
    public static class Constants
    {
        private static final int WRIST_SOLENOID_PORT_1 = 0;
        private static final int WRIST_SOLENOID_PORT_2 = 1;
    }

    private DoubleSolenoid wristSolenoid = new DoubleSolenoid(Constants.WRIST_SOLENOID_PORT_1,
    Constants.WRIST_SOLENOID_PORT_2); // On the blue solenoid holder

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
}
