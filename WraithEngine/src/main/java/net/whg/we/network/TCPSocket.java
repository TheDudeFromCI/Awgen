package net.whg.we.network;

import java.io.Closeable;
import java.io.IOException;

public interface TCPSocket extends Closeable
{
	void open(int port) throws IOException;

	TCPChannel nextChannel() throws IOException;
}
