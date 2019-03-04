package net.whg.we.network.client;

import java.io.IOException;
import java.net.UnknownHostException;

public interface Client
{
	void connect(String ip, int port) throws UnknownHostException, IOException;

	void close() throws IOException;

	boolean isClosed();
}
