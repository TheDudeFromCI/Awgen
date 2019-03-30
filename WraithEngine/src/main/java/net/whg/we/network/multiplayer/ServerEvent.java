package net.whg.we.network.multiplayer;

import net.whg.we.event.EventCallerBase;
import net.whg.we.network.netty.UserConnection;
import net.whg.we.scene.ServerGameState;

public class ServerEvent extends EventCallerBase<ServerListener>
{
	private static final int SERVER_STARTED_EVENT = 0;
	private static final int SERVER_FAILED_TO_START_EVENT = 1;
	private static final int CLIENT_CONNECTED_EVENT = 2;
	private static final int CLIENT_DISCONNECTED_EVENT = 3;
	private static final int SERVER_STOPPED_EVENT = 4;

	private ServerGameState _gameState;

	public void setGameState(ServerGameState gameState)
	{
		_gameState = gameState;
	}

	public ServerGameState getGameState()
	{
		return _gameState;
	}

	public void onServerStarted()
	{
		callEvent(SERVER_STARTED_EVENT);
	}

	public void onServerFailedToStart(int port)
	{
		callEvent(SERVER_FAILED_TO_START_EVENT, port);
	}

	public void onServerStopped()
	{
		callEvent(SERVER_STOPPED_EVENT);
	}

	public void onUserConnected(UserConnection connection)
	{
		callEvent(CLIENT_CONNECTED_EVENT, connection);
	}

	public void onUserDisconnected(UserConnection connection)
	{
		callEvent(CLIENT_DISCONNECTED_EVENT, connection);
	}

	@Override
	protected void runEvent(ServerListener listener, int e, Object arg)
	{
		switch (e)
		{
			case SERVER_STARTED_EVENT:
				listener.onServerStarted(_gameState);
				return;

			case SERVER_FAILED_TO_START_EVENT:
				listener.onServerFailedToStart(_gameState, (int) arg);
				return;

			case CLIENT_CONNECTED_EVENT:
				listener.onClientConnected(_gameState, (UserConnection) arg);
				return;

			case CLIENT_DISCONNECTED_EVENT:
				listener.onClientDisconnected(_gameState, (UserConnection) arg);
				return;

			case SERVER_STOPPED_EVENT:
				listener.onServerStopped(_gameState);
				return;

			default:
				throw new IllegalArgumentException("Unknown event id! " + e);
		}
	}
}
