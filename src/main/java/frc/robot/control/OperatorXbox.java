package frc.robot.control;

/**
 * Xbox class for the operator
 * @author Mark
 *
 */
public class OperatorXbox extends Xbox
{
	private static OperatorXbox instance = new OperatorXbox(Constants.PORT);
	public static OperatorXbox getInstance()
	{
		return instance;
	}
	
	private OperatorXbox(int port)
	{
		super(port);
	}
	
	public static class Constants
	{
		public static int PORT = 1;
	}
}
