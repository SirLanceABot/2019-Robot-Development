package frc.control;

public class DriverXbox extends Xbox
{
    private static DriverXbox instance = new DriverXbox(Constants.PORT);
    
	public static DriverXbox getInstance()
	{
		return instance;
	}
	
	private DriverXbox(int port)
	{
        super(port);
        System.out.println(this.getClass().getName() + ": Started Constructing");
        System.out.println(this.getClass().getName() + ": Finished Constructing");
    }
    
    // public String getABXYButtonData()
    // {
    //     String a = "";
    //     String b = "";
    //     String x = "";
    //     String y = "";

    //     if(instance.getRawButton(Xbox.Constants.A_BUTTON))
    //         a = "A";
    //     if(instance.getRawButton(Xbox.Constants.B_BUTTON))
    //         b = "B";
    //     if(instance.getRawButton(Xbox.Constants.X_BUTTON))
    //         x = "X";
    //     if(instance.getRawButton(Xbox.Constants.Y_BUTTON))
    //         y = "Y";

    //     return a + b + x + y;
    // }

    // public String getFrontControlsData()
    // {
    //     String start = "";
    //     String back = "";
    //     String leftStick = "";
    //     String rightStick = "";

    //     if(instance.getRawButton(Xbox.Constants.START_BUTTON))
    //         start = "Start";
    //     if(instance.getRawButton(Xbox.Constants.BACK_BUTTON))
    //         back = "Back";
    //     if(instance.getRawButton(Xbox.Constants.LEFT_STICK_BUTTON))
    //         leftStick = "LT";
    //     if(instance.getRawButton(Xbox.Constants.RIGHT_STICK_BUTTON))
    //         rightStick = "RT";

    //     return start + back + leftStick + rightStick;
    // }

    // public String getRearControlsData()
    // {
    //     String rightBumper = "";
    //     String leftBumper = "";

    //     if(instance.getRawButton(Xbox.Constants.RIGHT_BUMPER))
    //         rightBumper = "RB";
    //     if(instance.getRawButton(Xbox.Constants.LEFT_BUMPER))
    //         leftBumper = "LB";

    //     return rightBumper + leftBumper + String.format("%5.3f, %5.3f",
    //      instance.getRawAxis(Xbox.Constants.LEFT_TRIGGER_AXIS), instance.getRawAxis(Xbox.Constants.RIGHT_TRIGGER_AXIS));
    // }

    // public String getLeftRightStickData()
    // {
    //     return String.format("%5.3f, %5.3f, %5.3f, %5.3f",
    //     instance.getRawAxis(Xbox.Constants.LEFT_STICK_X_AXIS), instance.getRawAxis(Xbox.Constants.LEFT_STICK_Y_AXIS),
    //      instance.getRawAxis(Xbox.Constants.RIGHT_STICK_X_AXIS), instance.getRawAxis(Xbox.Constants.RIGHT_STICK_Y_AXIS));
    // }

    
	
	public static class Constants
	{
		public static int PORT = 0;
	}
}
