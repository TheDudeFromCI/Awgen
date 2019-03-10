package net.whg.we.network.packet;

import java.io.IOException;
import net.whg.we.network.packet.PacketFactory;
import net.whg.we.network.packet.PacketPool;
import net.whg.we.network.packet.PacketProcessor;
import net.whg.we.network.packet.PacketServerProtocol;
import net.whg.we.network.server.ClientConnection;
import net.whg.we.network.server.DefaultServer;
import net.whg.we.network.server.ServerProtocol;
import net.whg.we.utils.logging.Log;

public class PacketServer extends DefaultServer
{
    private static ServerProtocol
            buildPacketServerProtocol(PacketFactory factory)
    {
        PacketPool pool = new PacketPool();
        PacketProcessor processor = new PacketProcessor(pool);
        return new PacketServerProtocol(null, factory, processor, pool);
    }

    protected PacketServerProtocol _packetServerProtocol;

    public PacketServer(PacketFactory factory, int port) throws IOException
    {
        super(buildPacketServerProtocol(factory), port);
        _packetServerProtocol = (PacketServerProtocol) getProtocol();
        _packetServerProtocol.setServer(this);
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

    public Packet newPacket(String typePath)
    {
        Packet packet = getPacketPool().get();
        packet.setPacketType(getPacketFactory().findPacketType(typePath));

        return packet;
    }

    public void sendPacket(Packet packet, ClientConnection client)
    {
        PacketProtocol protocol = (PacketProtocol) client.getProtocol();

        try
        {
            protocol.sendPacket(packet);
        }
        catch (IOException e)
        {
            Log.errorf(
                    "There has been an error while attempting to send a packet!",
                    e);
            client.close();
        }

        getPacketPool().put(packet);
    }
}
