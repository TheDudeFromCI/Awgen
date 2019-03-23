package net.whg.we.server_logic.connect;

import net.whg.we.server_logic.command.CommandConsole;
import net.whg.we.server_logic.command.CommandSender;
import net.whg.we.server_logic.command.VariableKeyring;
import net.whg.we.server_logic.command.console.Console;
import net.whg.we.server_logic.command.console.ConsoleListener;
import net.whg.we.server_logic.command.console.DefaultKeyring;
import net.whg.we.server_logic.command.console.LineChangedEvent;
import net.whg.we.server_logic.command.console.ScrollPosChanged;
import net.whg.we.packets.TerminalOutputPacket;
import net.whg.we.network.packet.Packet;

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
                ((TerminalOutputPacket) packet.getPacketType())
                        .build_ScollPos(packet, event.getLine());
                player.sendPacket(packet);
            }

            @Override
            public void onLineChanged(LineChangedEvent event)
            {
                Packet packet = player.newPacket("common.terminal.in");
                ((TerminalOutputPacket) packet.getPacketType()).build_SetLine(
                        packet, event.getLine(), event.getText());
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
}
