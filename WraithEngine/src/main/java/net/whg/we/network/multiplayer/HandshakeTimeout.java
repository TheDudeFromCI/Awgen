package net.whg.we.network.multiplayer;

import net.whg.we.network.Connection;

public class HandshakeTimeout
{
	public static final long DEFAULT_CONNECT_TIME = 60000;
	private long _startTime;
	private long _waitTime;
	private Connection _client;

	public HandshakeTimeout(Connection client, long waitTime)
	{
		_startTime = System.currentTimeMillis();
		_waitTime = waitTime;
		_client = client;
	}

	public boolean isTimeUp()
	{
		return System.currentTimeMillis() - _waitTime >= _startTime;
	}

	public Connection getClient()
	{
		return _client;
	}
}
