package net.whg.we.network;

public interface PacketListener
{
	void onPacketSent(Packet packet);

	void onPacketRecieved(Packet packet);
}
