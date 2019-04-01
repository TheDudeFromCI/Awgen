package net.whg.frameworks.command;

public interface CommandSender
{
	CommandConsole getConsole();

	VariableKeyring getVariableKeyring();

	String getUsername();

	void println(String message);
}
