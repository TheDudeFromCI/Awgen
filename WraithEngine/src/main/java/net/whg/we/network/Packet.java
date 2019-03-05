package net.whg.we.network;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import net.whg.we.utils.Poolable;

public class Packet implements Poolable
{
	public static final int MAX_PACKET_SIZE = 4096;

	private byte[] _bytes = new byte[MAX_PACKET_SIZE];
	private PacketType _packetType;
	private Map<String, Object> _packetData = new HashMap<>();
	private TCPChannel _sender;

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
			return -1;

		return _packetType.encode(_bytes, _packetData);
	}

	public boolean decode(int length)
	{
		if (_packetType == null)
			return false;

		_packetData.clear();
		_packetType.decode(_bytes, length, _packetData);
		return true;
	}

	public void process()
	{
		if (_packetType != null)
			_packetType.process(_packetData);
	}

	public TCPChannel getSender()
	{
		return _sender;
	}

	public void setSender(TCPChannel sender)
	{
		_sender = sender;
	}

	public Map<String, Object> getData()
	{
		return _packetData;
	}
}
