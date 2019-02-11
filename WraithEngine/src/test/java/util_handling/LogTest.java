package util_handling;

import java.time.LocalTime;
import org.junit.Assert;
import org.junit.Test;
import net.whg.we.utils.logging.Log;
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
	}

	@Test
	public void logProperty_toString()
	{
		LogProperty property = new LogProperty();

		property.setTimeStamp(LocalTime.of(10, 8, 6));
		property.setThreadName("main");
		property.setMessage("Test Message");
		property.setSeverity(Log.WARN);

		Assert.assertEquals(property.toString(), "[10:08:06][Warn][main] Test Message");
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
				"{abc=def, Message=Test Message, Time=10:08:06, Severity=Warn, Thread=main}");
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
}
