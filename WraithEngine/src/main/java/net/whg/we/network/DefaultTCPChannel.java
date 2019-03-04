package net.whg.we.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class DefaultTCPChannel implements TCPChannel
{
	private Socket _socket;
	private boolean _isClient;

	public DefaultTCPChannel(Socket socket, boolean isClient)
	{
		_socket = socket;
		_isClient = isClient;
	}

	@Override
	public void close() throws IOException
	{
		try
		{
			_socket.close();
		}
		finally
		{
			_socket = null;
		}
	}

	@Override
	public String getIPString()
	{
		return _socket.getInetAddress().toString();
	}

	@Override
	public OutputStream getOutputStream() throws IOException
	{
		return _socket.getOutputStream();
	}

	@Override
	public InputStream getInputStream() throws IOException
	{
		return _socket.getInputStream();
	}

	@Override
	public boolean isClient()
	{
		return _isClient;
	}
}
