package net.whg.we.network.packet;

import java.util.Map;

/**
 * Represents a handler for encoding and decoding packets of data.
 *
 * @author TheDudeFromCI
 */
public interface PacketType
{
	/*
	 * Get name of this packet type. Packets are matched to their encoders and
	 * decoders based on this name listed here. This value should be unique and
	 * unchanging.<br><br> When a packet is sent over a network, the type path
	 * prefixes the data of the packet. This string is encoded into UTF-8, and may
	 * not exceed 255 bytes in length.
	 */
	String getTypePath();

	/**
	 * Encodes a data map into a byte array, which can be later decoded back into
	 * the same datamap on the server or client. This is called on a private thread.
	 *
	 * @param bytes
	 *            - The byte buffer to write the bytes into.
	 * @param packetData
	 *            - The current data within the packet.
	 * @return The number of bytes written.
	 */
	int encode(byte[] bytes, Map<String, Object> packetData);

	/**
	 * Decodes a byte array into a data map to store in the packet while it is
	 * processed. This is called on a private thread.
	 *
	 * @param bytes
	 *            - The byte buffer which holds the information.
	 * @param length
	 *            - The number of bytes in the buffer which currently hold useful
	 *            information.
	 * @param packetData
	 *            - The data map to write information into.
	 */
	void decode(byte[] bytes, int length, Map<String, Object> packetData);

	/**
	 * This is called on the main thread during a physics update after a packet is
	 * recieved and decoded. References to this map should not be stored as this
	 * packet information is placed back into a pool after this packet is processed.
	 *
	 * @param packet
	 *            - The packet which has been recieved.
	 */
	void process(Packet packet);
}
