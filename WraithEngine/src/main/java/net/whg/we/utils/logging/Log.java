package net.whg.we.utils.logging;

import java.time.LocalTime;
import java.util.HashMap;

/**
 * Logs events to file and to console. Events are ignored if below the intended
 * priority level to decrease overhead.
 *
 * @author TheDudeFromCI
 */
public class Log
{
	/**
	 * The state value representing a trace logging level.<br>
	 * This is extremely verbose and can cause heavy overhead if enabled.
	 */
	public static final int TRACE = 0;

	/**
	 * The state value representing a debug logging level.<br>
	 * This is useful for hunting down bugs or understanding internal workings of
	 * the software, but is not recommended for release, as it may cause some
	 * overhead.
	 */
	public static final int DEBUG = 1;

	/**
	 * The state value representing a info logging level.<br>
	 * This value is the default logging level for standing software operations.
	 */
	public static final int INFO = 2;

	/**
	 * The state value representing a warn logging level.<br>
	 * This value is represents warning or potential issues that may arise later
	 * under current software operation.
	 */
	public static final int WARN = 3;

	/**
	 * The state value representing an error logging level.<br>
	 * This value represents an error which has occured.
	 */
	public static final int ERROR = 4;

	/**
	 * The state value representing an fatal logging level.<br>
	 * This value represents a fatal error which makes the the software unable to
	 * load or unable to continue operation.
	 */
	public static final int FATAL = 5;

	/**
	 * The number of spaces used to represent a tab indention.
	 */
	public static final int SPACES_PER_INDENT = 2;

	private static Log _instance;

	private HashMap<String, Integer> _indent = new HashMap<>();
	private int _logLevel = INFO;

	private static Log getInstance()
	{
		if (_instance == null)
			_instance = new Log();
		return _instance;
	}

	/**
	 * Gets the current indent value for the log. Indent levels are specific to a
	 * thread, and are stored by the name of the thread.
	 *
	 * @return The current indent value.
	 */
	public static int getIndentLevel()
	{
		String thread = Thread.currentThread().getName();
		return getInstance()._indent.get(thread);
	}

	/**
	 * Sets the indent level to a specific value. If the value is negative, the
	 * value is set to zero. Indent levels are specific to a thread, and are stored
	 * by the name of the thread.
	 *
	 * @param indent
	 *            - The new indent level.
	 */
	public static void setIndentLevel(int indent)
	{
		if (indent < 0)
			indent = 0;

		String thread = Thread.currentThread().getName();
		getInstance()._indent.put(thread, indent);
	}

	/**
	 * Increases the indent for future log entrys by one. Indent levels are specific
	 * to a thread, and are stored by the name of the thread.
	 */
	public static void indent()
	{
		String thread = Thread.currentThread().getName();

		int indent = getInstance()._indent.getOrDefault(thread, 0) + 1;
		getInstance()._indent.put(thread, indent);
	}

	/**
	 * Decreases the indent for future log entrys by one. If the value is already at
	 * zero, nothing happens. Indent levels are specific to a thread, and are stored
	 * by the name of the thread.
	 */
	public static void unindent()
	{
		String thread = Thread.currentThread().getName();

		int indent = getInstance()._indent.getOrDefault(thread, 0) - 1;

		if (indent < 0)
			indent = 0;

		getInstance()._indent.put(thread, indent);
	}

	/**
	 * Resets the indent level for future log entrys to default. (Zero) Indent
	 * levels are specific to a thread, and are stored by the name of the thread.
	 */
	public static void resetIndent()
	{
		String thread = Thread.currentThread().getName();
		getInstance()._indent.put(thread, 0);
	}

	/**
	 * Gets the current logging level being used.<br>
	 * One of:<br>
	 *
	 * <pre>
	 * TRACE, DEBUG, INFO, WARN, ERROR, FATAL
	 * </pre>
	 *
	 * @return The current logging level.
	 * @see {@link #setLogLevel(int)}
	 */
	public static int getLogLevel()
	{
		return getInstance()._logLevel;
	}

	/**
	 * Sets the current logging level to a specified value.<br>
	 * One of:<br>
	 *
	 * <pre>
	 * TRACE, DEBUG, INFO, WARN, ERROR, FATAL
	 * </pre>
	 *
	 * Values equal to, or above the current state are logged while values lower
	 * than the current state are ignored.
	 */
	public static void setLogLevel(int logLevel)
	{
		if (logLevel < TRACE || logLevel > FATAL)
			throw new IllegalArgumentException("Argument is not a valid log level!");

		getInstance()._logLevel = logLevel;
	}

	/**
	 * Logs a message with the priority level of trace.
	 *
	 * @param message
	 *            - The message to log.
	 */
	public static void trace(String message)
	{
		if (getInstance()._logLevel > TRACE)
			return;

		format("TRACE", message);
	}

	/**
	 * Logs a message with the priority level of trace, with formatting.
	 *
	 * @param message
	 *            - The message to log.
	 * @param args
	 *            - Any formatted arguments to format into the message.
	 */
	public static void tracef(String message, Object... args)
	{
		if (getInstance()._logLevel > TRACE)
			return;

		format("TRACE", String.format(message, args));
	}

	/**
	 * Logs a message with the priority level of debug.
	 *
	 * @param message
	 *            - The message to log.
	 */
	public static void debug(String message)
	{
		if (getInstance()._logLevel > DEBUG)
			return;

		format("DEBUG", message);
	}

	/**
	 * Logs a message with the priority level of debug, with formatting.
	 *
	 * @param message
	 *            - The message to log.
	 * @param args
	 *            - Any formatted arguments to format into the message.
	 */
	public static void debugf(String message, Object... args)
	{
		if (getInstance()._logLevel > DEBUG)
			return;

		format("DEBUG", String.format(message, args));
	}

	/**
	 * Logs a message with the priority level of info.
	 *
	 * @param message
	 *            - The message to log.
	 */
	public static void info(String message)
	{
		if (getInstance()._logLevel > INFO)
			return;

		format("INFO", message);
	}

	/**
	 * Logs a message with the priority level of info, with formatting.
	 *
	 * @param message
	 *            - The message to log.
	 * @param args
	 *            - Any formatted arguments to format into the message.
	 */
	public static void infof(String message, Object... args)
	{
		if (getInstance()._logLevel > INFO)
			return;

		format("INFO", String.format(message, args));
	}

	/**
	 * Logs a message with the priority level of warn.
	 *
	 * @param message
	 *            - The message to log.
	 */
	public static void warn(String message)
	{
		if (getInstance()._logLevel > WARN)
			return;

		format("WARN", message);
	}

	/**
	 * Logs a message with the priority level of warn, with formatting.
	 *
	 * @param message
	 *            - The message to log.
	 * @param args
	 *            - Any formatted arguments to format into the message.
	 */
	public static void warnf(String message, Object... args)
	{
		if (getInstance()._logLevel > WARN)
			return;

		format("WARN", String.format(message, args));
	}

	/**
	 * Logs a message with the priority level of error.
	 *
	 * @param message
	 *            - The message to log.
	 */
	public static void error(String message)
	{
		if (getInstance()._logLevel > ERROR)
			return;

		format("ERROR", "--------------");
		format("ERROR", message);
		format("ERROR", "--------------");
	}

	/**
	 * Logs a message with the priority level of error, with formatting.
	 *
	 * @param message
	 *            - The message to log.
	 * @param args
	 *            - Any formatted arguments to format into the message.
	 */
	public static void errorf(String message, Object... args)
	{
		if (getInstance()._logLevel > ERROR)
			return;

		format("ERROR", "--------------");
		format("ERROR", String.format(message, args));
		format("ERROR", "--------------");
	}

	/**
	 * Logs a message with the priority level of error, with a throwable stack
	 * trace.
	 *
	 * @param message
	 *            - The message to log.
	 * @param exception
	 *            - The exception to write.
	 * @param args
	 *            - Any formatted arguments to format into the message.
	 */
	public static void errorf(String message, Throwable exception, Object... args)
	{
		if (getInstance()._logLevel > ERROR)
			return;

		format("ERROR", "--------------");
		format("ERROR", String.format(message, args));
		format("ERROR", "Exception Thrown: " + exception.toString());

		for (StackTraceElement st : exception.getStackTrace())
			format("ERROR", "  at " + st.toString());
		format("ERROR", "--------------");
	}

	/**
	 * Logs a message with the priority level of fatal.
	 *
	 * @param message
	 *            - The message to log.
	 */
	public static void fatal(String message)
	{
		format("FATAL", "==============");
		format("FATAL", message);
		format("FATAL", "==============");
	}

	/**
	 * Logs a message with the priority level of fatal, with formatting.
	 *
	 * @param message
	 *            - The message to log.
	 * @param args
	 *            - Any formatted arguments to format into the message.
	 */
	public static void fatalf(String message, Object... args)
	{
		format("FATAL", "==============");
		format("FATAL", String.format(message, args));
		format("FATAL", "==============");
	}

	/**
	 * Logs a message with the priority level of fatal, with a throwable stack
	 * trace.
	 *
	 * @param message
	 *            - The message to log.
	 * @param exception
	 *            - The exception to write.
	 * @param args
	 *            - Any formatted arguments to format into the message.
	 */
	public static void fatalf(String message, Throwable exception, Object... args)
	{
		format("FATAL", "==============");
		format("FATAL", String.format(message, args));
		format("FATAL", "Exception Thrown: " + exception.toString());

		for (StackTraceElement st : exception.getStackTrace())
			format("FATAL", "  at " + st.toString());
		format("FATAL", "==============");
	}

	private synchronized static void push(String message)
	{
		System.out.println(message);
	}

	// String.Format can be slow and can allocate more memory. Consider switching to
	// using only string builders from the pool.
	private static void format(String type, String message)
	{
		// Gather required information
		LocalTime time = LocalTime.now();

		String format;

		String thread = Thread.currentThread().getName();
		int indent = getInstance()._indent.getOrDefault(thread, 0);

		if (indent > 0)
		{
			int spaces = indent * SPACES_PER_INDENT + message.length();
			format = "[%02d:%02d:%02d][%-5s][%s] %" + spaces + "s"; // This statement seems like an
																	// extra memory allocation
		}
		else
			format = "[%02d:%02d:%02d][%-5s][%s] %s";

		// Count number of lines
		int lines = 1;
		for (int i = 0; i < message.length(); i++)
			if (message.charAt(i) == '\n')
				lines++;

		// If only a single line, push instantly
		if (lines == 1)
		{
			push(String.format(format, time.getHour(), time.getMinute(), time.getSecond(), type,
					thread, message));
			return;
		}

		// If multiple lines, push as we solve them.
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < message.length(); i++)
		{
			char c = message.charAt(i);
			if (c == '\n')
			{
				push(String.format(format, time.getHour(), time.getMinute(), time.getSecond(), type,
						thread, sb.toString()));
				sb.setLength(0);
			}
			else
				sb.append(c);
		}

		// Push the last line and cleanup.
		push(String.format(format, time.getHour(), time.getMinute(), time.getSecond(), type, thread,
				sb.toString()));
	}

	public static void dispose()
	{
		_instance = null;
	}
}
