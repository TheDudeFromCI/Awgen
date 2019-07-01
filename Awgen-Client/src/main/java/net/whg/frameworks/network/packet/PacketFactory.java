package net.whg.frameworks.network.packet;

/**
 * An interface for locating packet types based on their type path.
 *
 * @author TheDudeFromCI
 */
public interface PacketFactory
{
	/**
	 * Gets the packet type based on the requested type path. This function may be
	 * called from any thread. This factory is assumed to always return the same
	 * decoder instance instead of creating new instances.
	 *
	 * @param typePath
	 *            - The type path of the packet type to find.
	 * @return The packet type with the given type path, or null if none is found.
	 */
	PacketType findPacketType(String typePath);
}
