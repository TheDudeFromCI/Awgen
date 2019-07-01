package net.whg.awgen.server;

import java.util.LinkedList;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.whg.awgenshell.CommandSender;
import net.whg.awgenshell.ShellEnvironment;

public class TerminalInput implements CommandSender
{
	private Logger logger = LoggerFactory.getLogger(TerminalInput.class);

	private ShellEnvironment shell;
	private LinkedList<String> commands = new LinkedList<>();

	public TerminalInput()
	{
		shell = new ShellEnvironment(this);

		new Thread(() ->
		{
			try (Scanner scanner = new Scanner(System.in))
			{
				while (true)
				{
					String line = scanner.nextLine();

					synchronized (commands)
					{
						commands.add(line);
					}
				}
			}
		}).start();
	}

	@Override
	public String getName()
	{
		return "Server";
	}

	@Override
	public void println(String arg0)
	{
		logger.info(arg0);
	}

	public void pushCommands()
	{
		synchronized (commands)
		{
			if (!commands.isEmpty())
				shell.runCommand(commands.removeFirst());
		}
	}
}
