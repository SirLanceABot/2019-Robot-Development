package frc.robot.control;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Xbox controller class
 * 
 * @author Mark Washington
 *
 */
public abstract class Xbox extends Joystick
{
    /**
     * Xbox constructor
     * 
     * @param port
     */
    Xbox(int port)
    {
        super(port);
    }

    @Override
    public double getRawAxis(int axis)
    {
        double value = Math.pow(super.getRawAxis(axis), 3);

        if (Math.abs(value) <= Constants.AXIS_DEADZONE)
        {
            value = 0.0;
        }

        if (axis == Constants.LEFT_STICK_Y_AXIS || axis == Constants.RIGHT_STICK_Y_AXIS)
        {
            value = -value;
        }

        return value;
    }

    /**
     * Constants class for Xbox
     * 
     * @author Mark and Yash
     *
     */
    public static class Constants
    {
        public static final int A_BUTTON = 1;
        public static final int B_BUTTON = 2;
        public static final int X_BUTTON = 3;
        public static final int Y_BUTTON = 4;
        public static final int LEFT_BUMPER = 5;
        public static final int RIGHT_BUMPER = 6;
        public static final int BACK_BUTTON = 7;
        public static final int START_BUTTON = 8;
        public static final int LEFT_STICK_BUTTON = 9;
        public static final int RIGHT_STICK_BUTTON = 10;

        public static final int LEFT_STICK_X_AXIS = 0;
        public static final int LEFT_STICK_Y_AXIS = 1;
        public static final int LEFT_TRIGGER_AXIS = 2;
        public static final int RIGHT_TRIGGER_AXIS = 3;
        public static final int RIGHT_STICK_X_AXIS = 4;
        public static final int RIGHT_STICK_Y_AXIS = 5;

        public static final double AXIS_DEADZONE = 0.2;
    }
}
