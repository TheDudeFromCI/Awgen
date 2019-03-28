package net.whg.we.network.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.whg.we.network.packet.Packet;
import net.whg.we.network.packet.PacketManagerHandler;
import net.whg.we.network.packet.PacketType;
import net.whg.we.utils.logging.Log;

public class PacketDecoder extends ChannelInboundHandlerAdapter
{
	private PacketBuffer _packetBuffer;
	private UserConnection _userConnection;
	private PacketManagerHandler _packetManager;

	public PacketDecoder(UserConnection userConnection, PacketManagerHandler packetManager)
	{
		_userConnection = userConnection;
		_packetManager = packetManager;
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception
	{
		_packetBuffer = new PacketBuffer(ctx.alloc().buffer(Packet.MAX_PACKET_SIZE + 260));
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		_packetBuffer.addBytes((ByteBuf) msg);

		while (_packetBuffer.hasNextPacket())
		{
			int packetSize = _packetBuffer.getPacketSize();
			String packetName = _packetBuffer.getPacketName();
			PacketType type = _packetManager.factory().findPacketType(packetName);

			Log.tracef("Recieved packet '%s' (%db)", packetName, packetSize);

			if (type == null)
			{
				Log.warnf("Recived packet of unknown type: '%s'", packetName);
				_packetBuffer.getByteBuffer().skipBytes(packetSize);
				return;
			}

			Packet packet = _packetManager.pool().get();
			_packetBuffer.getByteBuffer().readBytes(packet.getBytes(), 0, packetSize);
			packet.setPacketType(type);
			packet.setSender(_userConnection);

			if (packet.decode(packetSize))
				_packetManager.processor().onPacketRecieved(packet);
			else
				_packetManager.pool().put(packet);
		}
	}
}
