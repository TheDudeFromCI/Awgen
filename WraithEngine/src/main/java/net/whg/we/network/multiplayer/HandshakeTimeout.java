package net.whg.we.network.multiplayer;

import net.whg.we.network.server.ClientConnection;

public class HandshakeTimeout
{
    public static final long DEFAULT_CONNECT_TIME = 7000;
    private long _startTime;
    private long _waitTime;
    private ClientConnection _client;

    public HandshakeTimeout(ClientConnection client, long waitTime)
    {
        _startTime = System.currentTimeMillis();
        _waitTime = waitTime;
        _client = client;
    }

    public boolean isTimeUp()
    {
        return System.currentTimeMillis() - _waitTime >= _startTime;
    }

    public ClientConnection getClient()
    {
        return _client;
    }
}
