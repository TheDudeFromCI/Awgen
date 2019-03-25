package net.whg.we.network.server;

import net.whg.we.network.ChannelProtocol;

public interface ServerProtocol
{
	ChannelProtocol createProtocolInstance();
}
