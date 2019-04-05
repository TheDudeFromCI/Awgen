package net.whg.we.packets;

import java.nio.charset.StandardCharsets;
import net.whg.frameworks.logging.Log;
import net.whg.frameworks.network.packet.Packet;
import net.whg.frameworks.network.packet.PacketHandler;
import net.whg.frameworks.network.packet.PacketType;
import net.whg.frameworks.util.ByteReader;
import net.whg.frameworks.util.ByteWriter;
import net.whg.we.client_logic.scene.WindowedGameLoop;
import net.whg.we.client_logic.ui.terminal.ConsoleOutput;
import net.whg.we.scene.GameState;

public class TerminalOutputPacket implements PacketType
{
	private static final int SET_LINE_TYPE = 0;
	private static final int SCROLL_POS_TYPE = 1;

	public void build_SetLine(Packet packet, int lineIndex, String text)
	{
		packet.getData().put("type", SET_LINE_TYPE);
		packet.getData().put("line_index", lineIndex);
		packet.getData().put("text", text);
	}

	public void build_ScollPos(Packet packet, int scrollPos)
	{
		packet.getData().put("type", SCROLL_POS_TYPE);
		packet.getData().put("pos", scrollPos);
	}

	@Override
	public String getTypePath()
	{
		return "common.terminal.in";
	}

	@Override
	public int encode(Packet packet)
	{
		ByteWriter out = packet.getByteWriter();

		int type = (int) packet.getData().get("type");
		out.writeByte(type);

		if (type == SET_LINE_TYPE)
		{
			int lineIndex = (int) packet.getData().get("line_index");
			String text = (String) packet.getData().get("text");

			out.writeInt(lineIndex);
			out.writeString(text, StandardCharsets.UTF_16);
		}
		else
		{
			int scrollPos = (int) packet.getData().get("pos");
			out.writeInt(scrollPos);
		}

		return out.getPos();
	}

	@Override
	public void decode(Packet packet)
	{
		ByteReader in = packet.getByteReader();

		int type = in.getByte();
		packet.getData().put("type", type);

		if (type == SET_LINE_TYPE)
		{
			int lineIndex = in.getInt();
			String text = in.getString(StandardCharsets.UTF_16);

			packet.getData().put("line_index", lineIndex);
			packet.getData().put("text", text);
		}
		else
		{
			int scrollPos = in.getInt();
			packet.getData().put("pos", scrollPos);
		}
	}

	@Override
	public void process(Packet packet, PacketHandler handler)
	{
		if (!handler.isClient())
		{
			Log.warnf("Client %s has attempted to send %s packet!", packet.getSender().getIP(),
					getTypePath());
			return;
		}

		GameState gameState = handler.getGameState();
		WindowedGameLoop gameLoop = (WindowedGameLoop) gameState.getGameLoop();
		ConsoleOutput console = gameLoop.getTerminal().getConsoleOutput();

		int type = (int) packet.getData().get("type");

		if (type == SET_LINE_TYPE)
		{
			int line = (int) packet.getData().get("line_index");
			String text = (String) packet.getData().get("text");

			Log.tracef("Setting console line %d to '%s'.", line, text);
			console.setLine(line, text);
		}
		else
		{
			int scrollPos = (int) packet.getData().get("pos");
			console.setScroll(scrollPos);
		}
	}
}
