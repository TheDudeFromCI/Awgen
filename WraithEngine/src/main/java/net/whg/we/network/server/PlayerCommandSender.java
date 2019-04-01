package net.whg.we.network.server;

import net.whg.frameworks.command.CommandConsole;
import net.whg.frameworks.command.CommandSender;
import net.whg.frameworks.command.Console;
import net.whg.frameworks.command.ConsoleListener;
import net.whg.frameworks.command.DefaultKeyring;
import net.whg.frameworks.command.LineChangedEvent;
import net.whg.frameworks.command.ScrollPosChanged;
import net.whg.frameworks.command.VariableKeyring;
import net.whg.we.network.packet.Packet;
import net.whg.we.packets.TerminalOutputPacket;

public class PlayerCommandSender implements CommandSender
{
	private Console _console;
	private VariableKeyring _variables;
	private OnlinePlayer _player;

	public PlayerCommandSender(OnlinePlayer player)
	{
		_player = player;
		_console = new Console();
		_variables = new DefaultKeyring();

		_console.getEvent().addListener(new ConsoleListener()
		{
			@Override
			public int getPriority()
			{
				return 0;
			}

			@Override
			public void onScrollPosChanged(ScrollPosChanged event)
			{
				Packet packet = player.newPacket("common.terminal.in");
				((TerminalOutputPacket) packet.getPacketType()).build_ScollPos(packet,
						event.getLine());
				player.sendPacket(packet);
			}

			@Override
			public void onLineChanged(LineChangedEvent event)
			{
				Packet packet = player.newPacket("common.terminal.in");
				((TerminalOutputPacket) packet.getPacketType()).build_SetLine(packet,
						event.getLine(), event.getText());
				player.sendPacket(packet);
			}
		});
	}

	@Override
	public CommandConsole getConsole()
	{
		return _console;
	}

	@Override
	public VariableKeyring getVariableKeyring()
	{
		return _variables;
	}

	@Override
	public String getUsername()
	{
		return _player.getUsername();
	}

	@Override
	public void println(String message)
	{
		_console.println(message);
	}

	public OnlinePlayer getPlayer()
	{
		return _player;
	}
}
