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
import frc.control.Xbox;
import frc.control.ButtonBoard;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

/**
 * Add your docs here.
 */
public class Arm
{
    private DoubleSolenoid armSolenoid = new DoubleSolenoid(Constants.ARM_SOLENOID_PORT_1,
            Constants.ARM_SOLENOID_PORT_2);
    private DoubleSolenoid wristSolenoid = new DoubleSolenoid(Constants.WRIST_SOLENOID_PORT_1,
            Constants.WRIST_SOLENOID_PORT_2);
    private DoubleSolenoid grabberSolenoid = new DoubleSolenoid(Constants.GRABBER_SOLENOID_PORT_1,
            Constants.GRABBER_SOLENOID_PORT_2);

    private WPI_TalonSRX masterIntakeRoller = new WPI_TalonSRX(Constants.ROLLER_TALON_ID);
    private WPI_VictorSPX slaveIntakeRoller = new WPI_VictorSPX(Constants.ROLLER_VICTOR_ID);
    private boolean armPosition; // true is up false is down
    private boolean wristPosition; // true is up false is down
    private boolean hatchPanelPosition; // true for expanded false for contracted

    private static DriverXbox driverXbox = DriverXbox.getInstance();
    private static ButtonBoard buttonBoard = ButtonBoard.getInstance();

    private static Arm instance = new Arm();

    private Arm()
    {
        slaveIntakeRoller.follow(masterIntakeRoller);

    }

    public static Arm getInstance()
    {
        return (instance);
    }

    public boolean getHatchPanelPosition()
    {
        return (hatchPanelPosition);
    }

    public boolean getArmPosition()
    {
        return (armPosition);
    }

    public boolean getWristPosition()
    {
        return (wristPosition);
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
        masterIntakeRoller.set(speed);
    }

    public void ejectCargo(double speed)
    {
        speed = -Math.abs(speed);
        masterIntakeRoller.set(speed);
    }

    public void stopCargo()
    {
        masterIntakeRoller.set(0);
    }

    public void grabHatchPanel()
    {
        grabberSolenoid.set(Value.kForward);
        hatchPanelPosition = true;
    }

    public void releaseHatchPanel()
    {
        grabberSolenoid.set(Value.kReverse);
        hatchPanelPosition = false;
    }

    public void teleop()
    {
        boolean floorButton = buttonBoard.getRawButton(ButtonBoard.Constants.FLOOR_BUTTON);
        boolean cargoShipCargoButton = buttonBoard.getRawButton(ButtonBoard.Constants.CARGO_SHIP_CARGO_BUTTON);

        boolean bottomHatchButton = buttonBoard.getRawButton(ButtonBoard.Constants.BOTTOM_HATCH_BUTTON);
        boolean centerHatchButton = buttonBoard.getRawButton(ButtonBoard.Constants.CENTER_HATCH_BUTTON);
        boolean topHatchButton = buttonBoard.getRawButton(ButtonBoard.Constants.TOP_HATCH_BUTTON);

        boolean bottomCargoButton = buttonBoard.getRawButton(ButtonBoard.Constants.BOTTOM_CARGO_BUTTON);
        boolean centerCargoButton = buttonBoard.getRawButton(ButtonBoard.Constants.CENTER_CARGO_BUTTON);
        boolean topCargoButton = buttonBoard.getRawButton(ButtonBoard.Constants.TOP_CARGO_BUTTON);

        boolean hatchPanelButton = driverXbox.getRawButton(Xbox.Constants.A_BUTTON);
        boolean cargoInButton = driverXbox.getRawButton(Xbox.Constants.LEFT_BUMPER);
        boolean cargoOutButton = driverXbox.getRawButton(Xbox.Constants.RIGHT_BUMPER);

        if(floorButton)
        {
            moveWristDown();
            moveArmDown();
        }

        else if(cargoShipCargoButton)
        {
            moveWristUp();
            moveArmDown();
        }
        else if(bottomHatchButton)
        {
            moveWristDown();
            moveArmDown();
        }
        else if(centerHatchButton)
        {
            moveWristDown();
            moveArmDown();
        }
        else if(topHatchButton)
        {
            moveWristDown();
            moveArmUp();
        }
        else if(bottomCargoButton)
        {
            moveWristUp();
            moveArmDown();
        }
        else if(centerCargoButton)
        {
            moveWristUp();
            moveArmDown();
        }
        else if(topCargoButton)
        {
            moveWristUp();
            moveArmUp();
        }


        if (hatchPanelButton)
        {
            if (getHatchPanelPosition() == true)
            {
                releaseHatchPanel();
            }
            else
            {
                grabHatchPanel();
            }
        }


        if (cargoInButton)
        {
            ejectCargo(0.5);
        }
        else if (cargoOutButton)
        {
            intakeCargo(0.5);
        }
        else
        {
            stopCargo();
        }
    }

    @Override
    public String toString()
    {
        return String.format(
                "ArmPosition: %s, HPP: %s, WristPosition: %s, (true is up/expanded false is down/contracted",
                getArmPosition(), getHatchPanelPosition(), getWristPosition());
    }

    public static class Constants
    {
        public static enum ArmPosition
        {

        }
        public static enum WristPosition
        {

        }
        public static enum Position
		{
            kFloorPanel(Value.kReverse, Value.kReverse, "Floor Panel"),
            kFloorCargo(Value.kForward, Value.kReverse, "Floor Cargo"),
            kCargoShipCargo(Value.kReverse, Value.kReverse, "Cargo Ship Cargo"),

            kBottomHatch(Value.kReverse, Value.kReverse, "Bottom Hatch"),      // 1 ft 7 inches to center
            kCenterHatch(Value.kReverse, Value.kReverse, "Center Hatch"),      // 3 ft 11 inches to center
            kTopHatch(Value.kReverse, Value.kForward, "Top Hatch"),         // 6 ft 3 inches to center

            kBottomCargo(Value.kForward, Value.kForward, "Bottom Cargo"),       // 2 ft 3.5 inches to center
            kCenterCargo(Value.kForward, Value.kForward, "Center Cargo"),       // 4 ft 7.5 inches to center
            kTopCargo(Value.kForward, Value.kForward, "Top Cargo");          // 6 ft 11.5 inches to center

            private final DoubleSolenoid.Value armValue;
            private final DoubleSolenoid.Value wristValue;
            private final String name;

            private Position(DoubleSolenoid.Value wristValue, DoubleSolenoid.Value armValue, String name)
            {
                this.wristValue = wristValue;
                this.armValue = armValue;
                this.name = name;
            }

            @Override
            public String toString()
            {
                return name;
            }
        }

        public static final int ARM_SOLENOID_PORT_1 = 0;
        public static final int ARM_SOLENOID_PORT_2 = 1;
        public static final int WRIST_SOLENOID_PORT_1 = 2;
        public static final int WRIST_SOLENOID_PORT_2 = 3;
        public static final int GRABBER_SOLENOID_PORT_1 = 4;
        public static final int GRABBER_SOLENOID_PORT_2 = 5;
        public static final int ROLLER_TALON_ID = 10;
        public static final int ROLLER_VICTOR_ID = 11;
    }

}
