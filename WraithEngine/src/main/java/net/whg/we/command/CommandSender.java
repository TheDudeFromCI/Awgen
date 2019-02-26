package net.whg.we.command;

public interface CommandSender
{
	CommandConsole getConsole();

	CommandList getCommandList();

	VariableKeyring getVariableKeyring();
}
