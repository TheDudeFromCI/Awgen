package util_handling;

import java.io.PrintWriter;
import java.time.LocalTime;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import net.whg.we.utils.logging.Log;
import net.whg.we.utils.logging.LogPrintWriterOut;
import net.whg.we.utils.logging.LogProperty;

public class LogTest
{
	@Test
	public void logProperty_disposing()
	{
		LogProperty property = new LogProperty();
		Assert.assertNotEquals(property.getPropertyCount(), 0);

		property.dispose();

		Assert.assertEquals(property.getPropertyCount(), 0);
	}

	@Test
	public void logProperty_setProperty()
	{
		LogProperty property = new LogProperty();
		property.setProperty("1", "abc");

		String val = property.getProperty("1");

		Assert.assertEquals(val, "abc");
	}

	@Test
	public void logProperty_getNullProperty()
	{
		LogProperty property = new LogProperty();
		Assert.assertNull(property.getProperty("1"));
	}

	@Test
	public void logProperty_timestamp()
	{
		LogProperty property = new LogProperty();
		LocalTime time = LocalTime.of(14, 4, 16);

		property.setTimeStamp(time);

		Assert.assertEquals(property.getTimeStamp(), "14:04:16");
	}

	@Test
	public void logProperty_threadName()
	{
		LogProperty property = new LogProperty();

		property.setThreadName("main");

		Assert.assertEquals(property.getThreadName(), "main");
	}

	@Test
	public void logProperty_message()
	{
		LogProperty property = new LogProperty();

		property.setMessage("Test Message");

		Assert.assertEquals(property.getMessage(), "Test Message");
	}

	@Test
	public void logProperty_message_format()
	{
		LogProperty property = new LogProperty();

		property.setMessage("Test Message, %s: %d", "Value", 3);

		Assert.assertEquals(property.getMessage(), "Test Message, Value: 3");
	}

	@Test
	public void logProperty_clear_property()
	{
		LogProperty property = new LogProperty();

		property.setProperty("Color", "Red");
		property.clearProperty("Color");

		Assert.assertNull(property.getProperty("Color"));
	}

	@Test
	public void logProperty_clearMainProperties()
	{
		LogProperty property = new LogProperty();

		property.clearProperty(LogProperty.MESSAGE_PROPERTY);
		property.clearProperty(LogProperty.SEVERITY_PROPERTY);
		property.clearProperty(LogProperty.THREAD_PROPERTY);
		property.clearProperty(LogProperty.TIME_PROPERTY);
		property.clearProperty(LogProperty.INDENT_PROPERTY);

		Assert.assertNotNull(property.getProperty(LogProperty.MESSAGE_PROPERTY));
		Assert.assertNotNull(property.getProperty(LogProperty.SEVERITY_PROPERTY));
		Assert.assertNotNull(property.getProperty(LogProperty.THREAD_PROPERTY));
		Assert.assertNotNull(property.getProperty(LogProperty.TIME_PROPERTY));
		Assert.assertNotNull(property.getProperty(LogProperty.INDENT_PROPERTY));
	}

	@Test
	public void logProperty_severity()
	{
		LogProperty property = new LogProperty();

		property.setSeverity(Log.TRACE);
		Assert.assertEquals(property.getSeverity(), "Trace");

		property.setSeverity(Log.DEBUG);
		Assert.assertEquals(property.getSeverity(), "Debug");

		property.setSeverity(Log.INFO);
		Assert.assertEquals(property.getSeverity(), "Info");

		property.setSeverity(Log.WARN);
		Assert.assertEquals(property.getSeverity(), "Warn");

		property.setSeverity(Log.ERROR);
		Assert.assertEquals(property.getSeverity(), "Error");

		property.setSeverity(Log.FATAL);
		Assert.assertEquals(property.getSeverity(), "Fatal");
	}

	@Test(expected = IllegalArgumentException.class)
	public void logProperty_wrongSeverity()
	{
		LogProperty property = new LogProperty();
		property.setSeverity(-1);
	}

	@Test
	public void logProperty_init()
	{
		LogProperty property = new LogProperty();

		Assert.assertNotNull(property.getTimeStamp());
		Assert.assertNotNull(property.getThreadName());
		Assert.assertNotNull(property.getMessage());
		Assert.assertNotNull(property.getSeverity());
		Assert.assertNotNull(property.getIndent());
	}

	@Test(expected = IllegalArgumentException.class)
	public void logProperty_setNegativeIndent()
	{
		new LogProperty().setIndent(-1);
	}

	@Test
	public void logProperty_toString()
	{
		LogProperty property = new LogProperty();

		property.setTimeStamp(LocalTime.of(10, 8, 6));
		property.setThreadName("main");
		property.setMessage("Test Message");
		property.setSeverity(Log.WARN);

		Assert.assertEquals("[10:08:06][Warn][main] Test Message", property.toString());
	}

	@Test
	public void logProperty_toString_Lines()
	{
		LogProperty property = new LogProperty();

		property.setTimeStamp(LocalTime.of(10, 8, 6));
		property.setThreadName("main");
		property.setMessage("Test Message\nLine2");
		property.setSeverity(Log.WARN);

		Assert.assertEquals("[10:08:06][Warn][main] Test Message\n[10:08:06][Warn][main] Line2",
				property.toString());
	}

	@Test
	public void logProperty_toString_Indent()
	{
		LogProperty property = new LogProperty();

		property.setTimeStamp(LocalTime.of(10, 8, 6));
		property.setThreadName("main");
		property.setMessage("Test Message");
		property.setSeverity(Log.WARN);
		property.setIndent(1);

		Assert.assertEquals(2, Log.SPACES_PER_INDENT);

		Assert.assertEquals("[10:08:06][Warn][main]   Test Message", property.toString());
	}

	@Test
	public void logProperty_toString_BigIndent()
	{
		LogProperty property = new LogProperty();

		property.setTimeStamp(LocalTime.of(10, 8, 6));
		property.setThreadName("main");
		property.setMessage("Test Message");
		property.setSeverity(Log.WARN);
		property.setIndent(5);

		Assert.assertEquals(2, Log.SPACES_PER_INDENT);

		Assert.assertEquals("[10:08:06][Warn][main]           Test Message", property.toString());
	}

	@Test
	public void logProperty_toString_Indent_Lines()
	{
		LogProperty property = new LogProperty();

		property.setTimeStamp(LocalTime.of(10, 8, 6));
		property.setThreadName("main");
		property.setMessage("Test Message\nLine2");
		property.setSeverity(Log.WARN);
		property.setIndent(1);

		Assert.assertEquals(2, Log.SPACES_PER_INDENT);

		Assert.assertEquals("[10:08:06][Warn][main]   Test Message\n[10:08:06][Warn][main]   Line2",
				property.toString());
	}

	@Test
	public void logProperty_toMapString()
	{
		LogProperty property = new LogProperty();

		property.setTimeStamp(LocalTime.of(10, 8, 6));
		property.setThreadName("main");
		property.setMessage("Test Message");
		property.setSeverity(Log.WARN);
		property.setProperty("abc", "def");

		Assert.assertEquals(property.toMapString(),
				"{Indent=0, abc=def, Message=Test Message, Time=10:08:06, Severity=Warn, Thread=main}");
	}

	@Test
	public void log_setLogLevel()
	{
		Log.dispose();
		Log.setLogLevel(Log.DEBUG);

		Assert.assertEquals(Log.DEBUG, Log.getLogLevel());
	}

	@Test(expected = IllegalArgumentException.class)
	public void log_setNegativeLogLevel()
	{
		Log.dispose();
		Log.setLogLevel(-1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void log_setLargeLogLevel()
	{
		Log.dispose();
		Log.setLogLevel(Log.FATAL + 1);
	}

	@Test
	public void log_levels_are_continous()
	{
		Assert.assertEquals(0, Log.TRACE);
		Assert.assertEquals(1, Log.DEBUG);
		Assert.assertEquals(2, Log.INFO);
		Assert.assertEquals(3, Log.WARN);
		Assert.assertEquals(4, Log.ERROR);
		Assert.assertEquals(5, Log.FATAL);
	}

	@Test
	public void log_setOutput_PrintWriter()
	{
		PrintWriter pw = Mockito.mock(PrintWriter.class);
		Log.setOutput(new LogPrintWriterOut(pw));

		Log.info("Hello");

		Mockito.verify(pw).println(Mockito.anyString());
	}
}
