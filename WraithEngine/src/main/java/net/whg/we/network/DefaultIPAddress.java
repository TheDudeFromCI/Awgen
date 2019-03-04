package net.whg.we.network;

import java.net.InetAddress;

public class DefaultIPAddress implements IPAddress
{
	private InetAddress _ip;

	public DefaultIPAddress(InetAddress ip)
	{
		_ip = ip;
	}

	public InetAddress asInetAddress()
	{
		return _ip;
	}

	@Override
	public String toString()
	{
		return _ip.toString();
	}
}
