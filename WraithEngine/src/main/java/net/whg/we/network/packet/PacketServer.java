package net.whg.we.network.packet;

import net.whg.we.network.packet.PacketFactory;
import net.whg.we.network.packet.PacketPool;
import net.whg.we.network.packet.PacketProcessor;
import net.whg.we.network.packet.PacketServerProtocol;
import net.whg.we.network.server.DefaultServer;
import net.whg.we.network.server.ServerProtocol;

public class PacketServer extends DefaultServer
{
    private static ServerProtocol buildPacketServerProtocol(PacketFactory factory)
    {
        PacketPool pool = new PacketPool();
        PacketProcessor processor = new PacketProcessor(pool);
        return new PacketServerProtocol(factory, processor, pool);
    }
    
    protected PacketServerProtocol _packetServerProtocol;

    public PacketServer(PacketFactory factory)
	{
        super(buildPacketServerProtocol(factory));
        _packetServerProtocol = (PacketServerProtocol) _protocol;
    }
    
    public PacketPool getPacketPool()
    {
        return _packetServerProtocol.getPacketPool();
    }

    public PacketProcessor getPacketProcessor()
    {
        return _packetServerProtocol.getPacketProcessor();
    }

    public PacketFactory getPacketFactory()
    {
        return _packetServerProtocol.getPacketFactory();
    }

    public void handlePackets()
    {
        getPacketProcessor().handlePackets();
    }
}