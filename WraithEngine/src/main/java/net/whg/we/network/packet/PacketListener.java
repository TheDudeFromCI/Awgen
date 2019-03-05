package net.whg.we.network.packet;

public interface PacketListener
{
	void onPacketSent(Packet packet);

	void onPacketRecieved(Packet packet);
}
