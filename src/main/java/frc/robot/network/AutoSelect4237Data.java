package frc.robot.network;

public class AutoSelect4237Data
{
	private String selectedPosition = "None";
	private String planA = "Auto Line";
	private String planB = "Auto Line";
	private String planC = "Auto Line";
	private boolean grabSecondCube = false;
	private boolean holdSecondCube = false;
	private boolean placeSecondCubeInSwitch = false;
	private boolean placeSecondCubeInScale = false;
	private boolean holdCubeForOppositeScale = false;
	
	public String getSelectedPosition()
	{
		return this.selectedPosition;
	}
	
	public String getPlanA()
	{
		return this.planA;
	}
	
	public String getPlanB()
	{
		return this.planB;
	}
	
	public String getPlanC()
	{
		return this.planC;
	}
	
	public boolean getGrabSecondCube()
	{
		return this.grabSecondCube;
	}
	
	public boolean getHoldSecondCube()
	{
		return this.holdSecondCube;
	}
	
	public boolean getPlaceSecondCubeInSwitch()
	{
		return this.placeSecondCubeInSwitch;
	}
	
	public boolean getPlaceSecondCubeInScale()
	{
		return this.placeSecondCubeInScale;
	}
	
	public boolean getHoldCubeForOppositeScale()
	{
		return this.holdCubeForOppositeScale;
	}
}
