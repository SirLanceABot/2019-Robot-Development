package frc.robot.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;

import com.esotericsoftware.jsonbeans.Json;

import edu.wpi.first.wpilibj.Timer;

public class AutoSelect4237 extends Thread
{
	AutoSelect4237Data data = new AutoSelect4237Data();
	
	private DatagramSocket rxsocket = new DatagramSocket(Constants.PORT);
	private DatagramPacket packet = null;
	private Json json = new Json();
	
	private static AutoSelect4237 instance;
	
	static //Static block to get around the constructor throwing IOException
	{
		try	
		{
			instance = new AutoSelect4237(); 
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static AutoSelect4237 getInstance()
	{
		return instance;
	}
	
	private AutoSelect4237() throws IOException
	{
		this.rxsocket.setSoTimeout(50);
	}
	
	@Override
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
					setData(new String(packet.getData(), 0, packet.getLength()));
				}
				catch (SocketTimeoutException e)
				{
					//System.out.println("No data received from AutoSelect4237");
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			Timer.delay(0.005);
		}
	}
	
	private synchronized void setData(String data)
	{
		System.out.println(data);
		this.data = json.fromJson(AutoSelect4237Data.class, data);
	}
	
	public synchronized AutoSelect4237Data getData()
	{
		return this.data;
	}
	
	public static class Constants
	{
		private static final int PACKETSIZE = 256;
		private static final int PORT = 5804;
	}
}
