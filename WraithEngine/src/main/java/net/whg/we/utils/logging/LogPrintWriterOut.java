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
	public void println(LogProperty property)
	{
		_out.println(property.toString());

		String exception = property.getProperty("Exception");
		if (exception != null)
			_out.println(exception);

		_out.flush();
	}

	public PrintWriter getPrintWriter()
	{
		return _out;
	}
}
