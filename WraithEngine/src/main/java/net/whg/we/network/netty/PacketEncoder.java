package net.whg.we.network.netty;

import java.nio.charset.StandardCharsets;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.whg.we.network.packet.Packet;
import net.whg.we.utils.logging.Log;

public class PacketEncoder extends MessageToByteEncoder<Packet>
{
	@Override
	protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) throws Exception
	{
		msg.encode();

		int packetSize = msg.getPacketSize();
		String packetName = msg.getPacketType().getTypePath();
		int packetNameSize = packetName.length();

		if (packetSize <= 0)
		{
			Log.warn("Attempted to send empty packet!");
			return;
		}

		Log.tracef("Sending packet '%s' (%db)", packetName, packetSize);

		out.writeInt(packetSize);
		out.writeByte(packetNameSize);
		out.writeCharSequence(packetName, StandardCharsets.UTF_8);
		out.writeBytes(msg.getBytes(), 0, packetSize);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
	{
		Log.errorf("An error has occured while encoding packet!", cause);
		ctx.close();
	}
}
