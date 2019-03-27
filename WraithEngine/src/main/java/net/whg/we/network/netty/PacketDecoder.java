package net.whg.we.network.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.whg.we.network.packet.Packet;
import net.whg.we.network.packet.PacketFactory;
import net.whg.we.network.packet.PacketListener;
import net.whg.we.network.packet.PacketPool;
import net.whg.we.network.packet.PacketType;
import net.whg.we.utils.logging.Log;

public class PacketDecoder extends ChannelInboundHandlerAdapter
{
	private PacketPool _packetPool;
	private PacketFactory _packetFactory;
	private PacketListener _packetListener;
	private PacketBuffer _packetBuffer;
	private UserConnection _userConnection;

	public PacketDecoder(PacketPool packetPool, PacketFactory packetFactory,
			PacketListener packetListener, UserConnection userConnection)
	{
		_packetPool = packetPool;
		_packetFactory = packetFactory;
		_packetListener = packetListener;
		_userConnection = userConnection;
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
			PacketType type = _packetFactory.findPacketType(packetName);

			Log.tracef("Recieved packet '%s' (%db)", packetName, packetSize);

			if (type == null)
			{
				Log.warnf("Recived packet of unknown type: '%s'", packetName);
				_packetBuffer.getByteBuffer().skipBytes(packetSize);
				return;
			}

			Packet packet = _packetPool.get();
			_packetBuffer.getByteBuffer().readBytes(packet.getBytes(), 0, packetSize);
			packet.setPacketType(type);
			packet.setSender(_userConnection);

			if (packet.decode(packetSize))
				_packetListener.onPacketRecieved(packet);
			else
				_packetPool.put(packet);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
	{
		Log.errorf("An error has occured while decoding packet!", cause);
		ctx.close();
	}
}
