package net.whg.we.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.BufferOverflowException;
import net.whg.we.utils.logging.Log;

public class DefaultTCPChannel implements TCPChannel
{
	private Socket _socket;
	private boolean _isClient;
	private IPAddress _ip;

	public DefaultTCPChannel(Socket socket, boolean isClient)
	{
		_socket = socket;
		_isClient = isClient;
		_ip = new DefaultIPAddress(socket.getInetAddress());
	}

	@Override
	public void close()
	{
		if (isClosed())
			return;

		try
		{
			_socket.close();
		}
		catch (IOException | BufferOverflowException e)
		{
			Log.errorf("An error has occured while attempting to close this socket!", e);
		}
		finally
		{
			_socket = null;
		}
	}

	@Override
	public OutputStream getOutputStream()
	{
		if (isClosed())
			return null;

		try
		{
			return _socket.getOutputStream();
		}
		catch (Exception e)
		{
			return null;
		}
	}

	@Override
	public InputStream getInputStream()
	{
		if (isClosed())
			return null;

		try
		{
			return _socket.getInputStream();
		}
		catch (Exception e)
		{
			return null;
		}
	}

	@Override
	public boolean isClient()
	{
		return _isClient;
	}

	@Override
	public IPAddress getIP()
	{
		return _ip;
	}

	@Override
	public boolean isClosed()
	{
		if (_socket != null && _socket.isClosed())
			_socket = null;

		return _socket == null;
	}
}
