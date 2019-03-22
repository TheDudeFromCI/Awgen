package net.whg.we.network.multiplayer;

import net.whg.we.command.CommandConsole;
import net.whg.we.command.CommandSender;
import net.whg.we.command.VariableKeyring;
import net.whg.we.command.console.Console;
import net.whg.we.command.console.ConsoleListener;
import net.whg.we.command.console.DefaultKeyring;
import net.whg.we.command.console.LineChangedEvent;
import net.whg.we.command.console.ScrollPosChanged;
import net.whg.we.packets.TerminalOutputPacket;
import net.whg.we.network.packet.Packet;

public class PlayerCommandSender implements CommandSender
{
    private Console _console;
    private VariableKeyring _variables;

    public PlayerCommandSender(OnlinePlayer player)
    {
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
}
