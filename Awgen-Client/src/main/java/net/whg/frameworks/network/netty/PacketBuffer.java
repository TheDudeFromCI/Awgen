package net.whg.frameworks.network.netty;

import java.nio.charset.StandardCharsets;
import io.netty.buffer.ByteBuf;
import net.whg.frameworks.logging.Log;

public class PacketBuffer
{
	private int _packetSize;
	private int _packetNameSize;
	private String _packetName;
	private int _state;
	private ByteBuf _byteBuffer;

	public PacketBuffer(ByteBuf byteBuffer)
	{
		_byteBuffer = byteBuffer;
	}

	public void addBytes(ByteBuf buf)
	{
		Log.tracef("Read incoming bytes. (%db)", buf.readableBytes());

		_byteBuffer.writeBytes(buf);
		buf.release();
	}

	public boolean hasNextPacket()
	{
		if (_state == 0)
		{
			if (_byteBuffer.readableBytes() >= 4)
			{
				_packetSize = _byteBuffer.readInt();
				_state++;
			}
		}

		if (_state == 1)
		{
			if (_byteBuffer.readableBytes() >= 1)
			{
				_packetNameSize = _byteBuffer.readUnsignedByte();
				_state++;
			}
		}

		if (_state == 2)
		{
			if (_byteBuffer.readableBytes() >= _packetNameSize)
			{
				_packetName = (String) _byteBuffer.readCharSequence(_packetNameSize,
						StandardCharsets.UTF_8);
				_state++;
			}
		}

		if (_state == 3)
		{
			_state = 0;
			return true;
		}

		return false;
	}

	public void close()
	{
		_byteBuffer.release();
	}

	public ByteBuf getByteBuffer()
	{
		return _byteBuffer;
	}

	public int getPacketSize()
	{
		return _packetSize;
	}

	public String getPacketName()
	{
		return _packetName;
	}
}
