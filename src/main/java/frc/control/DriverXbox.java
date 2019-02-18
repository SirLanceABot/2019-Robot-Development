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
	
	public static class Constants
	{
		public static int PORT = 0;
	}
}
