package frc.control;

import javax.lang.model.util.ElementScanner6;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Xbox controller class
 * 
 * @author Mark Washington
 *
 */
public abstract class Xbox extends Joystick
{
    private double maximumAxisValue = 1.0;
    private double axisDeadzone = 0.2;


    /**
     * Xbox constructor
     * 
     * @param port
     */
    public Xbox(int port)
    {
        super(port);
    }

    @Override
    public double getRawAxis(int axis)
    {
        double value = super.getRawAxis(axis);



        if (Math.abs(value) <= Constants.AXIS_DEADZONE)
        {
            value = 0.0;
        }

        //value = Math.pow(value, 3);

        if (axis == Constants.LEFT_STICK_Y_AXIS || axis == Constants.RIGHT_STICK_Y_AXIS)
        {
            value = -value;
        }

        return value;
    }

    public double getLinearAxis(int axis)
    {
        double input = super.getRawAxis(axis);
        double newInput;

        if (axis == Constants.LEFT_STICK_Y_AXIS || axis == Constants.RIGHT_STICK_Y_AXIS)
        {
            input = -input;
        }

        if(Math.abs(input) <= axisDeadzone)
        {
            newInput = 0.0;
        }
        else
        {
            newInput = (maximumAxisValue / (1.0 - axisDeadzone)) * (input - axisDeadzone * (Math.abs(input) / input));
        }
        
        return newInput;
    }

    public double getQuadraticAxis(int axis)
    {
        double input = super.getRawAxis(axis);
        double newInput;

        if (axis == Constants.LEFT_STICK_Y_AXIS || axis == Constants.RIGHT_STICK_Y_AXIS)
        {
            input = -input;
        }

        if(Math.abs(input) <= axisDeadzone)
        {
            newInput = 0.0;
        }
        else
        {
            newInput = (maximumAxisValue / Math.pow( (1.0 - axisDeadzone), 2)) * (((Math.abs(input) / input) * Math.pow(input - (axisDeadzone * (Math.abs(input) / input)), 2)));
        }

        return newInput;
    }

    public double getCubicAxis(int axis)
    {
        double input = super.getRawAxis(axis);
        double newInput;

        if (axis == Constants.LEFT_STICK_Y_AXIS || axis == Constants.RIGHT_STICK_Y_AXIS)
        {
            input = -input;
        }

        if(Math.abs(input) <= axisDeadzone)
        {
            newInput = 0.0;
        }
        else
        {
            newInput = (maximumAxisValue / Math.pow( (1.0 - axisDeadzone), 3)) * ((Math.pow(input - (axisDeadzone * (Math.abs(input) / input)), 3)));
        }

        return newInput;
    }

    public double[] getScaledAxes(int axes)
    {   

        double xAxis = super.getRawAxis(axes);
        double yAxis = -super.getRawAxis(axes + 1);

        double magnitude = Math.min(Math.sqrt(Math.pow(xAxis, 2) + Math.pow(yAxis, 2)), 1.0);
        double scalarRatio;
        xAxis = (Math.abs(xAxis) <= axisDeadzone ? 0.0 : xAxis);
        yAxis = (Math.abs(yAxis) <= axisDeadzone ? 0.0 : yAxis);

        double xSign = (xAxis == 0.0 ? 0.0 : Math.abs(xAxis) / xAxis);
        double ySign = (yAxis == 0.0 ? 0.0 : Math.abs(yAxis) / yAxis);

        if(Math.abs(xAxis) > Math.abs(yAxis))
        {
            scalarRatio = Math.abs(yAxis) / Math.abs(xAxis);
            xAxis = xSign * magnitude;
            yAxis = ySign * magnitude * scalarRatio;
        }

        else if(Math.abs(xAxis) < Math.abs(yAxis))
        {
            scalarRatio = Math.abs(xAxis) / Math.abs(yAxis);
            yAxis = ySign * magnitude;
            xAxis = xSign * magnitude * scalarRatio;
        }
        
        else  
        {
            yAxis = ySign * magnitude;
            xAxis = xSign * magnitude;
        }
        xAxis = maximumAxisValue / Math.pow( 1.0 - axisDeadzone, 3) * Math.pow(xAxis - axisDeadzone * xSign, 3);
        yAxis = maximumAxisValue / Math.pow( 1.0 - axisDeadzone, 3) * Math.pow(yAxis - axisDeadzone * ySign, 3);
        double[] scaledAxes = {xAxis, yAxis};

        
        return scaledAxes;
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
        public static final int LEFT_STICK_AXES = 0;
        public static final int RIGHT_STICK_AXES = 4;

        public static final double AXIS_DEADZONE = 0.2;
        
    }
}
