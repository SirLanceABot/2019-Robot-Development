package frc.robot.vision;

import java.util.HashMap;

public class VisionData
{	
	private String idNum;
	private HashMap<String, VisionTarget> data;
	
	public VisionData()
	{

	}

	/**
	 * Returns the current idNum of the instance of VisionData.
	 * @return The current idNum.
	 */
	public synchronized String getIdNum()
	{
		return this.idNum;
	}

	/**
	 * Returns the current data (vision targets and their numerical IDs).
	 * @return The current data.
	 */
	public synchronized HashMap<String, VisionTarget> getData()
	{
		return data;
	}

	/**
	 * Prints the current data in an easy-to-read form.
	 */
	public void debugOutput()
	{
		System.out.println("idNum: " + getIdNum());
		for (String i : data.keySet())
		{
			System.out.println("Target " + i);
			System.out.println("\tHeight:    " + data.get(i).getHeight());
			System.out.println("\tMidPointX: " + data.get(i).getMidpointX() + "\n");
		}
		System.out.println();
	}
}
