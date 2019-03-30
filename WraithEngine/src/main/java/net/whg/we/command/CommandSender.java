package net.whg.we.command;

public interface CommandSender
{
	CommandConsole getConsole();

	VariableKeyring getVariableKeyring();

	String getUsername();

	void println(String message);
}
