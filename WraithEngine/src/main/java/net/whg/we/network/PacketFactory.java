package net.whg.we.network;

public interface PacketFactory
{
	PacketType findPacketType(String typePath);
}
