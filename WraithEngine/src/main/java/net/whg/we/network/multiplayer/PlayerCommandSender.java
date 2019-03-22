package net.whg.we.network.multiplayer;

import net.whg.we.command.CommandConsole;
import net.whg.we.command.CommandSender;
import net.whg.we.command.VariableKeyring;
import net.whg.we.command.console.Console;
import net.whg.we.command.console.ConsoleListener;
import net.whg.we.command.console.LineChangedEvent;
import net.whg.we.command.console.ScrollPosChanged;
import net.whg.we.ui.terminal.TerminalKeyring;
import net.whg.we.utils.logging.Log;

public class PlayerCommandSender implements CommandSender
{
    private Console _console;
    private VariableKeyring _variables;

    public PlayerCommandSender(OnlinePlayer player)
    {
        _console = new Console(512);
        _variables = new TerminalKeyring();

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
                // TODO Send scroll pos adjustment to player.
            }

            @Override
            public void onLineChanged(LineChangedEvent event)
            {
                Log.infof("> %s", event.getText());

                // TODO Send line changed adjustment to player.
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
