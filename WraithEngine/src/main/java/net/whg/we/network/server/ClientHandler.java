package net.whg.we.network.server;

public class ClientHandler implements ConnectionListener
{
	private ConnectedClientList _clientList;

	public ClientHandler(ConnectedClientList clientList)
	{
		_clientList = clientList;
	}

	@Override
	public void onConnected(ClientConnection connection)
	{
		_clientList.addClient(connection);
	}

	@Override
	public void onOutgoingPacket(ClientConnection connection, byte[] bytes, int pos, int length)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onIncomingPacket(ClientConnection connection, byte[] bytes, int length)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onDisconnected(ClientConnection connection)
	{
		_clientList.removeClient(connection);
	}
}
