package net.whg.we.network.packet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import net.whg.we.network.ChannelProtocol;
import net.whg.we.network.TCPChannel;
import net.whg.we.utils.logging.Log;

public class PacketProtocol implements ChannelProtocol
{
	private BufferedInputStream _in;
	private BufferedOutputStream _out;
	private PacketPool _packetPool;
	private PacketFactory _packetFactory;
	private PacketListener _packetListener;
	private TCPChannel _sender;
	private boolean _closed;

	/**
	 * Creates a new instance of the packet protocol.
	 *
	 * @param packetPool
	 *                           - The pool to pull new packet instances from.
	 * @param packetFactory
	 *                           - A factory for returning packet type encoders
	 *                           and decoders.
	 * @param packetListener
	 *                           - A listener for when packets are sent or
	 *                           recieved.
	 */
	public PacketProtocol(PacketPool packetPool, PacketFactory packetFactory,
			PacketListener packetListener)
	{
		_packetPool = packetPool;
		_packetFactory = packetFactory;
		_packetListener = packetListener;
	}

	public TCPChannel getSender()
	{
		return _sender;
	}

	@Override
	public void close() throws IOException
	{
		if (isClosed())
			return;

		_in.close();
		_out.close();
		_closed = true;
	}

	@Override
	public boolean isClosed()
	{
		return _closed;
	}

	public void sendPacket(Packet packet) throws IOException
	{
		if (_closed)
			throw new IllegalStateException("Streams already closed!");

		if (packet == null)
			return;

		if (packet.getPacketType() == null)
		{
			Log.warn("Tried to send a packet with no packet type!");
			return;
		}

		Log.tracef("Attempting to send packet. Type: %s",
				packet.getPacketType().getTypePath());

		_packetListener.onPacketSent(packet);

		byte[] nameBytes = packet.getPacketType().getTypePath()
				.getBytes(StandardCharsets.UTF_8);
		if (nameBytes.length > 255)
			throw new IllegalArgumentException(
					"Packet type name may not exceed 255 bytes!");

		int length = packet.encode();

		synchronized (_out)
		{
			_out.write(length >> 8);
			_out.write(length);

			_out.write(nameBytes.length);
			_out.write(nameBytes);

			_out.write(packet.getBytes(), 0, length);
			_out.flush();
		}
	}

	@Override
	public void init(InputStream in, OutputStream out, TCPChannel sender)
	{
		_in = new BufferedInputStream(in);
		_out = new BufferedOutputStream(out);
		_sender = sender;
		_closed = false;
	}

	@Override
	public void next() throws IOException
	{
		if (_closed)
			throw new IllegalStateException("Streams already closed!");

		Packet packet;
		int packetSize;
		byte[] nameBytes;

		synchronized (_in)
		{
			packetSize = (_in.read() & 0xFF) << 8;
			packetSize |= _in.read() & 0xFF;

			int packetTypeBytes = _in.read() & 0xFF;
			nameBytes = new byte[packetTypeBytes];
			_in.read(nameBytes);

			packet = _packetPool.get();
			_in.read(packet.getBytes(), 0, packetSize);
		}

		String name = new String(nameBytes, StandardCharsets.UTF_8);
		packet.setPacketType(_packetFactory.findPacketType(name));
		packet.setSender(_sender);

		if (packet.getPacketType() == null)
			Log.tracef("Recived packet of unknown type: '%s'", name);
		else
			Log.tracef("Attempting to recive packet. Type: %s",
					packet.getPacketType().getTypePath());

		if (packet.decode(packetSize))
			_packetListener.onPacketRecieved(packet);
		else
		{
			Log.warnf("Recived unknown packet type '%s', from %s.", name,
					_sender.getIP());
			_packetPool.put(packet);
		}
	}

	@Override
	public void onDisconnected()
	{
	}

	public PacketFactory getPacketFactory()
	{
		return _packetFactory;
	}

	public PacketPool getPacketPool()
	{
		return _packetPool;
	}

	public PacketListener getListener()
	{
		return _packetListener;
	}
}
