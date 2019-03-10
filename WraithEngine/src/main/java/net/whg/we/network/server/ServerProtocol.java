package net.whg.we.network.server;

import java.io.IOException;
import net.whg.we.network.TCPChannel;

public interface ServerProtocol
{
	ClientConnection openChannelProtocol(TCPChannel channel) throws IOException;
}
