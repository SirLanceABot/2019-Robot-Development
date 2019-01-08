package frc.robot.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import frc.robot.vision.VisionData;

import com.esotericsoftware.jsonbeans.Json;

import edu.wpi.first.wpilibj.Timer;

/**
 * Class for receiving data from the Raspberry Pi
 * REMEMBER: UDP has no return address 
 * @author Mark Washington, Ben Puzycki, Darryl Wrong
 */
public class RaspberryPiReceiver extends Thread	
{
	private DatagramSocket rxsocket = new DatagramSocket(Constants.PORT);
	private DatagramPacket packet = null;
	private String data = "{}";
	private Json json = new Json();

	private static RaspberryPiReceiver instance;

	static //Static block to get around the constructor throwing IOException
	{
		try	
		{
			instance = new RaspberryPiReceiver(); 
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	/**
	 * Returns singleton instance of RaspberryPiReceiver
	 * @return Instance of RaspberryPiReceiver
	 */
	public static RaspberryPiReceiver getInstance()
	{
		return instance;
	}

	private RaspberryPiReceiver() throws IOException
	{
		
	}

	/**
	 * Method that runs when thread is started
	 */
	public void run()
	{
		while (!this.interrupted())
		{
			try
			{
				byte[] buffer = new byte[Constants.PACKETSIZE];
				this.packet = new DatagramPacket(buffer, buffer.length);
				try
				{
					this.rxsocket.receive(packet);
					setRawData(new String(packet.getData(), 0, packet.getLength()));
					
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Gets the most current data received by the Raspberry Pi.
	 * @return Data from Raspberry Pi.
	 */
	public synchronized String getRawData()
	{
		return this.data;
	}

	public synchronized void setRawData(String data)
	{
		this.data = data;
	}

	/**
	 * Constants class for constants related to RaspberryPiReceiver
	 * @author Mark
	 */
	public static class Constants
	{
		private static final int PACKETSIZE = 4096;
		private static final int PORT = 5802;
	}
}
