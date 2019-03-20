package frc.components;


import frc.robot.SlabShuffleboard;
import frc.util.MotorConstants;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import java.util.Arrays;
// import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
// import com.ctre.phoenix.motorcontrol.InvertType;
// import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
// import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;

/**
 * Add your docs here.
 */
public class NewArm
{
    public static class Constants
    {
        public static enum NewArmPosition
        {
            kFloorArmPosition(0), kHorizontalArmPosition(1), kMiddleArmPosition(2), kTopArmPosition(3),
            // need to set these
            kSafePosition(
                    3), kStartingPosition, kMovingUp, kMovingDown, /* kThreshold(4), */ kNotMoving(), kManualOverride;

            private int value;

            private NewArmPosition(int value)
            {
                this.value = value;
            }

            private NewArmPosition()
            {

            }
        }

        public static final int ARM_MOTOR_ID = 9;
        public static final int ARM_THRESHOLD = 5;

        // 0: Floor
        // 1: Horizontal
        // 2: Middle
        // 3: Top
        // 4: Threshold
        // Comp Bot: starting position is 101
        // 0 1 2 3 4 5
        public static final int[] COMPETITION_ARM_POSITION_POT_VALUES = { 635, 567, 438, 202, 5, -1 };
        public static final int[] PRACTICE_ARM_POSITION_POT_VALUES = { 628, 572, 480, 230, 5, -1 };
    }

    public static enum NewArmState
    {
            kFloorArmPosition(0), kHorizontalArmPosition(1), kMiddleArmPosition(2), kTopArmPosition(3),
            // need to set these
            kSafePosition(
                    3), kStartingPosition, kMovingUp, kMovingDown, /* kThreshold(4), */ kNotMoving(), kManualOverride;

            private int value;

            private NewArmState(int value)
            {
                this.value = value;
            }

            private NewArmState()
            {

            }
    }
    private WPI_TalonSRX armMotor = new WPI_TalonSRX(Constants.ARM_MOTOR_ID);
    private double speedFactor = 1.0;
    private boolean isArmMoving = false;
    private boolean enableArmSoftLimit = true;
    private NewArmState currentArmState = NewArmState.kStartingPosition;
    private NewArmState targetArmState = NewArmState.kStartingPosition;

    /**
     * Returns the pot value of the given position
     * 
     * <p>
     * 0: Floor
     * <p>
     * 1: Horizontal
     * <p>
     * 2: Middle
     * <p>
     * 3: Top
     * 
     */
    private static int[] armPositionPotValues = Constants.COMPETITION_ARM_POSITION_POT_VALUES;

    // private SlabShuffleboard shuffleboard = SlabShuffleboard.getInstance();

    private static NewArm instance = new NewArm();

    private NewArm()
    {
        System.out.println(this.getClass().getName() + ": Started Constructing");

        armMotor.configFactoryDefault();

        armMotor.setNeutralMode(NeutralMode.Brake);
        armMotor.configPeakCurrentLimit(
                MotorConstants.getMotorStallCurrent(MotorConstants.Constants.MotorType.k9015Motor, 0.3));
        armMotor.configPeakCurrentDuration(MotorConstants.Constants.PEAK_CURRENT_DURATION);
        armMotor.configContinuousCurrentLimit(MotorConstants.Constants.CONTINOUS_CURRENT_LIMIT);
        armMotor.configOpenloopRamp(MotorConstants.Constants.OPEN_LOOP_RAMP);
        armMotor.enableCurrentLimit(true);

        armMotor.configSelectedFeedbackSensor(FeedbackDevice.Analog);
        armMotor.setSensorPhase(false);
        armMotor.configReverseSoftLimitThreshold(getArmPositionPotValue(Constants.NewArmPosition.kTopArmPosition));
        armMotor.configForwardSoftLimitThreshold(getArmPositionPotValue(Constants.NewArmPosition.kFloorArmPosition));
        armMotor.configForwardSoftLimitEnable(true);
        armMotor.configReverseSoftLimitEnable(true);

        System.out.println(this.getClass().getName() + ": Finished Constructing");
    }

    /**
     * This function returns the instance of the Arm to be used
     * 
     * @return instance of the arm that is going to be used
     */
    public static NewArm getInstance()
    {
        return instance;
    }

    /**
     * this function moves the arm up at a speed of .5
     */
    public void moveArmUp()
    {
        // armMotor.set(speedFactor * -0.5);
        moveArmUp(-0.5);
    }

    /**
     * moves arm up at given speed
     * 
     * @param speed
     */
    public void moveArmUp(double speed)
    {
        armMotor.set(speedFactor * -Math.abs(speed));
        setIsArmMoving(true);
    }

    public void manualOverride(double speed)
    {
        if (speed > 0.0)
        {
            moveArmUp(speed);
        }
        else
        {
            moveArmDown(speed);
        }

        currentArmState = NewArmState.kManualOverride;
    }

    /**
     * this function moves the arm down at a speed of -.5
     */
    public void moveArmDown()
    {
        // armMotor.set(speedFactor * 0.25);
        moveArmDown(0.25);
    }

    /**
     * moves arm down at given speed
     * 
     * @param speed
     */
    public void moveArmDown(double speed)
    {
        armMotor.set(speedFactor * Math.abs(speed));
        setIsArmMoving(true);
    }

    /**
     * this function will stop the movement of the arm
     */
    public void stopArm()
    {
        setIsArmMoving(false);
        armMotor.set(0);
    }

    public void holdArm()
    {
        moveArmUp(0.1);
    }

    public double getArmCurrent()
    {
        return armMotor.getOutputCurrent();
    }

    public void toggleArmOverride()
    {
        enableArmSoftLimit = !enableArmSoftLimit;
        armMotor.overrideSoftLimitsEnable(enableArmSoftLimit);
        System.out.println("Arm enable soft limits: " + enableArmSoftLimit);
    }

    /**
     * this function gets the potentiometer value that moniters the arms position
     * 
     * @return returns the potentiometer value for the arm
     */
    public int getPotValue()
    {
        return armMotor.getSelectedSensorPosition(); // Subtract a number from this to flip the directions (Down is low
                                                     // num, up is high num)
    }

    /**
     * sets the robot we are using (competition or practice)
     * 
     * @param robotType
     */
    public void setRobotType(SlabShuffleboard.RobotType robotType)
    {
        if (robotType == SlabShuffleboard.RobotType.kCompetition)
        {
            armPositionPotValues = Constants.COMPETITION_ARM_POSITION_POT_VALUES;
        }
        else
        {
            armPositionPotValues = Constants.PRACTICE_ARM_POSITION_POT_VALUES;
        }

        System.out.println(
                this.getClass().getName() + ": armPositionPotValues = " + Arrays.toString(armPositionPotValues));
        armMotor.configReverseSoftLimitThreshold(getArmPositionPotValue(Constants.NewArmPosition.kTopArmPosition));
        armMotor.configForwardSoftLimitThreshold(getArmPositionPotValue(Constants.NewArmPosition.kFloorArmPosition));
    }

    public void setMotorSpeedFactor(SlabShuffleboard.MotorSpeed speedFactor)
    {
        this.speedFactor = speedFactor.value;
    }

    /**
     * Returns the pot value of the given position
     * 
     * @param position
     *                     which position to return:
     *                     <p>
     *                     0: Floor
     *                     <p>
     *                     1: Horizontal
     *                     <p>
     *                     2: Middle
     *                     <p>
     *                     3: Top
     * 
     * @return pot value of the selected position
     */
    public int getArmPositionPotValue(Constants.NewArmPosition position)
    {
        return armPositionPotValues[position.value];
    }

    /**
     * checks if the arm is moving
     */
    public boolean isArmMoving()
    {
        return isArmMoving;
    }

    /**
     * sets if the arm is moving
     */
    public void setIsArmMoving(boolean isArmMoving)
    {
        this.isArmMoving = isArmMoving;
    }

    public String getArmMotorData()
    {
        return String.format("%6.3f,  %6d,  %6.3f,  %5.1f", armMotor.get(), armMotor.getSelectedSensorPosition(),
                armMotor.getOutputCurrent(), armMotor.getTemperature() * (9.0 / 5.0) + 32.0);
    }

    public void setState(NewArmState position)
    {
        targetArmState = position;
    }

    public NewArmState getState()
    {
        return currentArmState;
    }

    /**
     * state machine for control of the arm.
     */
    public void newArmControl()
    {
        int currentPotValue = getPotValue();
        switch (currentArmState)
        {
        case kFloorArmPosition:
            switch (targetArmState)
            {
            case kFloorArmPosition:
                currentArmState = NewArmState.kFloorArmPosition;
                break;
            case kHorizontalArmPosition:
                moveArmUp();
                currentArmState = NewArmState.kMovingUp;
                break;
            case kMiddleArmPosition:
                moveArmUp();
                currentArmState = NewArmState.kMovingUp;
                break;
            case kTopArmPosition:
                moveArmUp();
                currentArmState = NewArmState.kMovingUp;
                break;
            case kSafePosition:
                moveArmUp();
                currentArmState = NewArmState.kMovingUp;
                break;
            case kStartingPosition:
                moveArmUp();
                currentArmState = NewArmState.kMovingUp;
                break;
            case kMovingDown:
                holdArm();
                currentArmState = NewArmState.kNotMoving;
                break;
            case kMovingUp:
                moveArmUp();
                currentArmState = NewArmState.kMovingUp;
                break;
            case kNotMoving:
                holdArm();
                currentArmState = NewArmState.kNotMoving;
            case kManualOverride:
                currentArmState = NewArmState.kManualOverride;
                break;
            }
            break;
        case kHorizontalArmPosition:
            switch (targetArmState)
            {
            case kFloorArmPosition:
                moveArmDown();
                currentArmState = NewArmState.kMovingDown;
                break;
            case kHorizontalArmPosition:
                currentArmState = NewArmState.kHorizontalArmPosition;
                break;
            case kMiddleArmPosition:
                moveArmUp();
                currentArmState = NewArmState.kMovingUp;
                break;
            case kTopArmPosition:
                moveArmUp();
                currentArmState = NewArmState.kMovingUp;
                break;
            case kSafePosition:
                moveArmUp();
                currentArmState = NewArmState.kMovingUp;
                break;
            case kStartingPosition:
                moveArmUp();
                currentArmState = NewArmState.kMovingUp;
                break;
            case kMovingDown:
                holdArm();
                currentArmState = NewArmState.kNotMoving;
                break;
            case kMovingUp:
                moveArmUp();
                currentArmState = NewArmState.kMovingUp;
                break;
            case kNotMoving:
                moveArmDown();
                currentArmState = NewArmState.kMovingDown;
            case kManualOverride:
                currentArmState = NewArmState.kManualOverride;
                break;
            }
            break;
        case kMiddleArmPosition:
            switch (targetArmState)
            {
            case kFloorArmPosition:
                moveArmDown();
                currentArmState = NewArmState.kMovingDown;
                break;
            case kHorizontalArmPosition:
                moveArmDown();
                currentArmState = NewArmState.kMovingDown;
                break;
            case kMiddleArmPosition:
                currentArmState = NewArmState.kMiddleArmPosition;
                break;
            case kTopArmPosition:
                moveArmUp();
                currentArmState = NewArmState.kMovingUp;
                break;
            case kSafePosition:
                moveArmUp();
                currentArmState = NewArmState.kMovingUp;
                break;
            case kStartingPosition:
                moveArmUp();
                currentArmState = NewArmState.kMovingUp;
                break;
            case kMovingDown:
                moveArmDown();
                currentArmState = NewArmState.kMovingDown;
                break;
            case kMovingUp:
                moveArmUp();
                currentArmState = NewArmState.kMovingUp;
                break;
            case kNotMoving:
                holdArm();
                currentArmState = NewArmState.kNotMoving;
            case kManualOverride:
                currentArmState = NewArmState.kManualOverride;
                break;
            }
            break;
        case kTopArmPosition:
            switch (targetArmState)
            {
            case kFloorArmPosition:
                moveArmDown();
                currentArmState = NewArmState.kMovingDown;
                break;
            case kHorizontalArmPosition:
                moveArmDown();
                currentArmState = NewArmState.kMovingDown;
                break;
            case kMiddleArmPosition:
                moveArmDown();
                currentArmState = NewArmState.kMovingDown;
                break;
            case kTopArmPosition:
                currentArmState = NewArmState.kTopArmPosition;
                break;
            case kSafePosition:
                moveArmDown();
                currentArmState = NewArmState.kMovingDown;
                break;
            case kStartingPosition:
                moveArmDown();
                currentArmState = NewArmState.kMovingDown;
                break;
            case kMovingDown:
                holdArm();
                currentArmState = NewArmState.kNotMoving;
                break;
            case kMovingUp:
                holdArm();
                currentArmState = NewArmState.kNotMoving;
                break;
            case kNotMoving:
                holdArm();
                currentArmState = NewArmState.kNotMoving;
                break;
            case kManualOverride:
                currentArmState = NewArmState.kManualOverride;
                break;
            }
            break;
        case kSafePosition:
            switch (targetArmState)
            {
            case kFloorArmPosition:
                moveArmDown();
                currentArmState = NewArmState.kMovingDown;
                break;
            case kHorizontalArmPosition:
                moveArmDown();
                currentArmState = NewArmState.kMovingDown;
                break;
            case kMiddleArmPosition:
                moveArmDown();
                currentArmState = NewArmState.kMovingDown;
                break;
            case kTopArmPosition:
                moveArmUp();
                currentArmState = NewArmState.kMovingUp;
                break;
            case kSafePosition:
                currentArmState = NewArmState.kSafePosition;
                break;
            case kStartingPosition:
                moveArmUp();
                currentArmState = NewArmState.kStartingPosition;
                break;
            case kMovingDown:
                holdArm();
                currentArmState = NewArmState.kNotMoving;
                break;
            case kMovingUp:
                holdArm();
                currentArmState = NewArmState.kNotMoving;
                break;
            case kNotMoving:
                holdArm();
                currentArmState = NewArmState.kNotMoving;
                break;
            case kManualOverride:
                currentArmState = NewArmState.kManualOverride;
                break;
            }
            break;
        case kStartingPosition:
            switch (targetArmState)
            {
            case kFloorArmPosition:
                moveArmDown();
                currentArmState = NewArmState.kMovingDown;
                break;
            case kHorizontalArmPosition:
                moveArmDown();
                currentArmState = NewArmState.kMovingDown;
                break;
            case kMiddleArmPosition:
                moveArmDown();
                currentArmState = NewArmState.kMovingDown;
                break;
            case kTopArmPosition:
                moveArmDown();
                currentArmState = NewArmState.kMovingDown;
                break;
            case kSafePosition:
                moveArmDown();
                currentArmState = NewArmState.kMovingDown;
                break;
            case kStartingPosition:
                moveArmDown();
                currentArmState = NewArmState.kMovingDown;
                break;
            case kMovingDown:
                holdArm();
                currentArmState = NewArmState.kMovingDown;
                break;
            case kMovingUp:
                holdArm();
                currentArmState = NewArmState.kNotMoving;
                break;
            case kNotMoving:
                holdArm();
                currentArmState = NewArmState.kNotMoving;
                break;
            case kManualOverride:
                currentArmState = NewArmState.kManualOverride;
                break;
            }
            break;

        case kMovingDown:
            if (currentPotValue > (armPositionPotValues[targetArmState.value] + Constants.ARM_THRESHOLD))
            {
                moveArmUp();
                currentArmState = NewArmState.kMovingUp;
            }
            else if (currentPotValue < (armPositionPotValues[targetArmState.value] - Constants.ARM_THRESHOLD))
            {
                moveArmDown();
                currentArmState = NewArmState.kMovingDown;
            }
            else
            {
                holdArm();
                currentArmState = targetArmState;
            }
            break;

        case kMovingUp:
            if (currentPotValue > (armPositionPotValues[targetArmState.value] + Constants.ARM_THRESHOLD))
            {
                moveArmUp();
                currentArmState = NewArmState.kMovingUp;
            }
            else if (currentPotValue < (armPositionPotValues[targetArmState.value] - Constants.ARM_THRESHOLD))
            {
                moveArmDown();
                currentArmState = NewArmState.kMovingDown;
            }
            else
            {
                holdArm();
                currentArmState = targetArmState;
            }
            break;
        case kNotMoving:
            holdArm();
            currentArmState = NewArmState.kNotMoving;
            break;
        case kManualOverride:
            switch (targetArmState)
            {
            case kFloorArmPosition:
                currentArmState = NewArmState.kMovingDown;
                break;
            case kHorizontalArmPosition:
                currentArmState = NewArmState.kMovingDown;
                break;
            case kMiddleArmPosition:
                currentArmState = NewArmState.kMovingDown;
                break;
            case kTopArmPosition:
                currentArmState = NewArmState.kMovingDown;
                break;
            case kSafePosition:
                currentArmState = NewArmState.kMovingDown;
                break;
            case kStartingPosition:
                currentArmState = NewArmState.kMovingDown;
                break;
            case kMovingDown:
                currentArmState = NewArmState.kMovingDown;
                break;
            case kMovingUp:
                holdArm();
                currentArmState = NewArmState.kNotMoving;
                break;
            case kNotMoving:
                holdArm();
                currentArmState = NewArmState.kNotMoving;
                break;
            case kManualOverride:
                currentArmState = NewArmState.kManualOverride;
                break;
            }
            break;
        }
    }

    @Override
    public String toString()
    {
        return String.format("Arm Pot Value: " + getPotValue());
    }

}
