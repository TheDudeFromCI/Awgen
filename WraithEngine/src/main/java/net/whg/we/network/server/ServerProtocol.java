package net.whg.we.network.server;

import net.whg.we.network.ChannelProtocol;
import net.whg.we.network.TCPChannel;

public interface ServerProtocol
{
	ChannelProtocol openChannelProtocol(TCPChannel channel);
}
