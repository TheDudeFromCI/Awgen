package net.whg.we.network.server;

import java.io.IOException;
import java.net.SocketException;
import net.whg.we.network.PacketFactory;
import net.whg.we.network.PacketPool;
import net.whg.we.network.PacketProcessor;
import net.whg.we.network.PacketProtocol;
import net.whg.we.network.TCPChannel;
import net.whg.we.utils.logging.Log;

public class ServerClientAcceptor implements Runnable
{
	private TCPSocket _serverSocket;
	private int _port;
	private PacketFactory _packetFactory;
	private PacketProcessor _packetProcessor;

	public ServerClientAcceptor(TCPSocket serverSocket, int port, PacketFactory packetFactory,
			PacketProcessor packetProcessor)
	{
		_serverSocket = serverSocket;
		_port = port;
		_packetFactory = packetFactory;
		_packetProcessor = packetProcessor;
	}

	@Override
	public void run()
	{
		try
		{
			PacketPool packetPool = new PacketPool();

			_serverSocket.open(_port);
			Log.trace("Initialized server thread.");

			while (true)
			{
				Log.trace("Waiting for connection.");

				try
				{
					TCPChannel socket = _serverSocket.nextChannel();

					Log.infof("A client has connected to the server. IP: %s", socket.getIP());

					new ClientConnection(socket, new PacketProtocol(packetPool, _packetFactory,
							_packetProcessor, socket));
				}
				catch (SocketException e)
				{
					Log.info("Server socket has been forcefully closed.");
					break;
				}
				catch (Exception e)
				{
					Log.errorf("There has been an internal error in the server socket!", e);
				}
			}
		}
		catch (Exception e)
		{
			Log.fatalf("There has been a fatal error while opening the server socket!", e);
		}
		finally
		{
			try
			{
				_serverSocket.close();
			}
			catch (IOException e)
			{
				Log.errorf("Failed to properly close server socket!", e);
			}
		}
	}
}
