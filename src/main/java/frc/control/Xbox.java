package frc.control;


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

    private double getSign(double input)
    {
        return input == 0.0 ? 0.0 : Math.abs(input) / input;
    }

    private double getLinearAxis(double input)
    {
        return maximumAxisValue / (1.0 - axisDeadzone) * (input - axisDeadzone * getSign(input));
    }

    private double getQuadraticAxis(double input)
    {
        return maximumAxisValue / Math.pow(1.0 - axisDeadzone, 2) * getSign(input) * Math.pow(input - axisDeadzone * getSign(input), 2);
    }

    private double getCubicAxis(double input)
    {
        return maximumAxisValue / Math.pow(1.0 - axisDeadzone, 3) * Math.pow(input - axisDeadzone * getSign(input), 3);
    }

    public double[] getScaledAxes(int axes, Constants.PolynomialDrive driveMode)
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
            scalarRatio = Math.abs(yAxis / xAxis);
            xAxis = xSign * magnitude;
            yAxis = ySign * magnitude * scalarRatio;
        }

        else if(Math.abs(xAxis) < Math.abs(yAxis))
        {
            scalarRatio = Math.abs(xAxis / yAxis);
            yAxis = ySign * magnitude;
            xAxis = xSign * magnitude * scalarRatio;
        }
        
        else  
        {
            yAxis = ySign * magnitude;
            xAxis = xSign * magnitude;
        }

        if(driveMode == Constants.PolynomialDrive.kLinearDrive)
        {
            xAxis = getLinearAxis(xAxis);
            yAxis = getLinearAxis(yAxis);
        }
        else if(driveMode == Constants.PolynomialDrive.kQuadraticDrive)
        {
            xAxis = getQuadraticAxis(xAxis);
            yAxis = getQuadraticAxis(yAxis);
        }
        else if(driveMode == Constants.PolynomialDrive.kCubicDrive)
        {
            xAxis = getCubicAxis(xAxis);
            yAxis = getCubicAxis(yAxis);
        }

        double[] scaledAxes = {xAxis, yAxis};

        
        return scaledAxes;
    }

    public void configMaximumAxisValue(double value)
    {
        maximumAxisValue = value;
    }

    public void configAxisDeadzone(double value)
    {
        axisDeadzone = value;
    }

    /**
     * Constants class for Xbox
     * 
     * @author Mark and Yash
     *
     */
    public static class Constants
    {
        public static enum PolynomialDrive
		{

            kLinearDrive(1, "Linear"),
            kQuadraticDrive(2, "Quadratic"),
            kCubicDrive(3, "Cubic");

            int power;
            String name;

            private PolynomialDrive(int power, String name)
            {
                this.power = power;
                this.name = name;
            }

            @Override
            public String toString()
            {
                return name;
            }
        }

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
