package net.whg.we.utils.logging;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import net.whg.we.utils.Poolable;
import net.whg.we.utils.Log;

/**
 * This class represents a single log statement that is sent to the logger, along with all Other
 * log information that should be attached to it.
 */
public class LogProperty implements Poolable
{
	public static final String TIME_PROPERTY = "Time";
	public static final String THREAD_PROPERTY = "Thread";
	public static final String MESSAGE_PROPERTY = "Message";
	public static final String SEVERITY_PROPERTY = "Severity";

	private Map<String,String> _properties = new HashMap<>();
	private StringBuilder sb = new StringBuilder();

	public LogProperty()
	{
		init();
	}

	@Override
	public void init()
	{
		setTimeStamp(LocalTime.now());
		setThreadName(Thread.currentThread().getName());
		setMessage("");
		setSeverity(Log.INFO);
	}

	@Override
	public void dispose()
	{
		_properties.clear();
	}

	public int getPropertyCount()
	{
		return _properties.size();
	}

	public void setProperty(String property, String value)
	{
		_properties.put(property, value);
	}

	public String getProperty(String property)
	{
		return _properties.get(property);
	}

	public void setTimeStamp(LocalTime time)
	{
		setProperty(TIME_PROPERTY, String.format("%02d:%02d:%02d", time.getHour(),
			time.getMinute(), time.getSecond()));
	}

	public String getTimeStamp()
	{
		return getProperty(TIME_PROPERTY);
	}

	public void setThreadName(String thread)
	{
		setProperty(THREAD_PROPERTY, thread);
	}

	public String getThreadName()
	{
		return getProperty(THREAD_PROPERTY);
	}

	public void setMessage(String message)
	{
		setProperty(MESSAGE_PROPERTY, message);
	}

	public void setMessage(String message, Object... args)
	{
		setMessage(String.format(message, args));
	}

	public String getMessage()
	{
		return getProperty(MESSAGE_PROPERTY);
	}

	public void setSeverity(int severity)
	{
		String s;

		switch (severity)
		{
			case Log.TRACE:
				s = "Trace";
				break;

			case Log.DEBUG:
				s = "Debug";
				break;

			case Log.INFO:
				s = "Info";
				break;

			case Log.WARN:
				s = "Warn";
				break;

			case Log.ERROR:
				s = "Error";
				break;

			case Log.FATAL:
				s = "Fatal";
				break;

			default:
				throw new IllegalArgumentException("Unknown severity level!");
		}

		setProperty(SEVERITY_PROPERTY, s);
	}

	public String getSeverity()
	{
		return getProperty(SEVERITY_PROPERTY);
	}

	public void clearProperty(String property)
	{
		_properties.remove(property);
	}

	@Override
	public String toString()
	{
		sb.setLength(0);

		sb.append('[').append(getTimeStamp()).append(']');
		sb.append('[').append(getSeverity()).append(']');
		sb.append('[').append(getThreadName()).append(']');
		sb.append(' ').append(getMessage());

		return sb.toString();
	}

	public String toMapString()
	{
		return _properties.toString();
	}
}
