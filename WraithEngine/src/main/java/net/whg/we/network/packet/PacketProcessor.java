package net.whg.we.network.packet;

import java.util.LinkedList;
import java.util.List;
import net.whg.we.utils.logging.Log;

public class PacketProcessor implements PacketListener
{
    private Object LOCK = new Object();
    private List<Packet> _packets = new LinkedList<>();
    private PacketPool _packetPool;
    private PacketHandler _handler;

    public PacketProcessor(PacketPool packetPool, PacketHandler handler)
    {
        _packetPool = packetPool;
        _handler = handler;
    }

    public void addPacket(Packet packet)
    {
        if (packet == null)
            return;

        synchronized (LOCK)
        {
            if (_packets.contains(packet))
                return;

            _packets.add(packet);
        }
    }

    public void handlePackets()
    {
        synchronized (LOCK)
        {
            for (Packet packet : _packets)
            {
                packet.process(_handler);
                _packetPool.put(packet);
            }
            _packets.clear();
        }
    }

    @Override
    public void onPacketSent(Packet packet)
    {
    }

    @Override
    public void onPacketRecieved(Packet packet)
    {
        if (Log.getLogLevel() <= Log.TRACE)
        {
            String sender = packet.getSender() == null ? "null"
                    : packet.getSender().getIP().toString();
            String type = packet.getPacketType() == null ? "null"
                    : packet.getPacketType().getTypePath();

            Log.tracef("Recieved packet from %s. Type: %s", sender, type);
        }

        addPacket(packet);
    }

    public int getPendingPackets()
    {
        synchronized (LOCK)
        {
            return _packets.size();
        }
    }
}
