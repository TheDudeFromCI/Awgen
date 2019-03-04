package net.whg.we.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class DefaultTCPChannel implements TCPChannel
{
	private Socket _socket;

	public DefaultTCPChannel(Socket socket)
	{
		_socket = socket;
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

}
