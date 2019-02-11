package net.whg.we.utils.logging;

import java.io.PrintWriter;

public class LogPrintWriterOut implements LogOutput
{
	private PrintWriter _out;

	public LogPrintWriterOut(PrintWriter out)
	{
		_out = out;
	}

	@Override
	public void println(String line)
	{
		_out.println(line);
	}
}
