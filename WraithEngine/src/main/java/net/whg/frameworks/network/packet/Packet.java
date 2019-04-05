package net.whg.frameworks.network.packet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import net.whg.frameworks.network.netty.UserConnection;
import net.whg.frameworks.util.ByteReader;
import net.whg.frameworks.util.ByteWriter;
import net.whg.frameworks.util.Poolable;

public class Packet implements Poolable
{
	public static final int MAX_PACKET_SIZE = 4096;

	private byte[] _bytes = new byte[MAX_PACKET_SIZE];
	private PacketType _packetType;
	private Map<String, Object> _packetData = new HashMap<>();
	private UserConnection _sender;
	private int _packetSize;
	private ByteReader _byteReader = new ByteReader(_bytes);
	private ByteWriter _byteWriter = new ByteWriter(_bytes);

	@Override
	public void init()
	{
		Arrays.fill(_bytes, (byte) 0);
	}

	@Override
	public void dispose()
	{
		_packetType = null;
		_sender = null;
		_packetData.clear();
	}

	public byte[] getBytes()
	{
		return _bytes;
	}

	public PacketType getPacketType()
	{
		return _packetType;
	}

	public void setPacketType(PacketType packetType)
	{
		_packetType = packetType;
	}

	public int encode()
	{
		if (_packetType == null)
			return _packetSize = -1;

		return _packetSize = _packetType.encode(this);
	}

	public boolean decode(int length)
	{
		if (_packetType == null)
			return false;

		_packetData.clear();
		_packetType.decode(this);
		return true;
	}

	public void process(PacketHandler handler)
	{
		if (_packetType != null)
			_packetType.process(this, handler);
	}

	public UserConnection getSender()
	{
		return _sender;
	}

	public void setSender(UserConnection sender)
	{
		_sender = sender;
	}

	public Map<String, Object> getData()
	{
		return _packetData;
	}

	public int getPacketSize()
	{
		return _packetSize;
	}

	public void setPacketSize(int packetSize)
	{
		_packetSize = packetSize;
	}

	public ByteReader getByteReader()
	{
		_byteReader.setPos(0);
		return _byteReader;
	}

	public ByteWriter getByteWriter()
	{
		_byteWriter.setPos(0);
		return _byteWriter;
	}
}
