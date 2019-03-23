package net.whg.we.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

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
	public void close() throws IOException
	{
		if (isClosed())
			return;

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
	public OutputStream getOutputStream() throws IOException
	{
		if (isClosed())
			return null;

		return _socket.getOutputStream();
	}

	@Override
	public InputStream getInputStream() throws IOException
	{
		if (isClosed())
			return null;

		return _socket.getInputStream();
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
		return _socket == null || _socket.isClosed();
	}
}