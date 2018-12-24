package net.whg.we.utils;

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

	private static HashMap<String, Integer> _indent = new HashMap<>();
	private static int _logLevel = INFO;

	/**
	 * Gets the current indent value for the log. Indent levels are specific to a
	 * thread, and are stored by the name of the thread.
	 *
	 * @return The current indent value.
	 */
	public static int getIndentLevel()
	{
		String thread = Thread.currentThread().getName();
		return _indent.get(thread);
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
		_indent.put(thread, indent);
	}

	/**
	 * Increases the indent for future log entrys by one. Indent levels are specific
	 * to a thread, and are stored by the name of the thread.
	 */
	public static void indent()
	{
		String thread = Thread.currentThread().getName();

		int indent = _indent.getOrDefault(thread, 0) + 1;
		_indent.put(thread, indent);
	}

	/**
	 * Decreases the indent for future log entrys by one. If the value is already at
	 * zero, nothing happens. Indent levels are specific to a thread, and are stored
	 * by the name of the thread.
	 */
	public static void unindent()
	{
		String thread = Thread.currentThread().getName();

		int indent = _indent.getOrDefault(thread, 0) - 1;

		if (indent < 0)
			indent = 0;

		_indent.put(thread, indent);
	}

	/**
	 * Resets the indent level for future log entrys to default. (Zero) Indent
	 * levels are specific to a thread, and are stored by the name of the thread.
	 */
	public static void resetIndent()
	{
		String thread = Thread.currentThread().getName();
		_indent.put(thread, 0);
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
		return _logLevel;
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
		if (_logLevel < TRACE || _logLevel > FATAL)
			throw new IllegalArgumentException("Argument is not a valid log level!");

		_logLevel = logLevel;
	}

	/**
	 * Logs a message with the priority level of trace.
	 *
	 * @param message
	 *            - The message to log.
	 */
	public static void trace(String message)
	{
		if (_logLevel > TRACE)
			return;

		push(format("TRACE", message));
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
		if (_logLevel > TRACE)
			return;

		push(format("TRACE", String.format(message, args)));
	}

	/**
	 * Logs a message with the priority level of debug.
	 *
	 * @param message
	 *            - The message to log.
	 */
	public static void debug(String message)
	{
		if (_logLevel > DEBUG)
			return;

		push(format("DEBUG", message));
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
		if (_logLevel > DEBUG)
			return;

		push(format("DEBUG", String.format(message, args)));
	}

	/**
	 * Logs a message with the priority level of info.
	 *
	 * @param message
	 *            - The message to log.
	 */
	public static void info(String message)
	{
		if (_logLevel > INFO)
			return;

		push(format("INFO", message));
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
		if (_logLevel > INFO)
			return;

		push(format("INFO", String.format(message, args)));
	}

	/**
	 * Logs a message with the priority level of warn.
	 *
	 * @param message
	 *            - The message to log.
	 */
	public static void warn(String message)
	{
		if (_logLevel > WARN)
			return;

		push(format("WARN", message));
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
		if (_logLevel > WARN)
			return;

		push(format("WARN", String.format(message, args)));
	}

	/**
	 * Logs a message with the priority level of error.
	 *
	 * @param message
	 *            - The message to log.
	 */
	public static void error(String message)
	{
		if (_logLevel > ERROR)
			return;

		push(format("ERROR", message));
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
		if (_logLevel > ERROR)
			return;

		push(format("ERROR", String.format(message, args)));
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
		if (_logLevel > ERROR)
			return;

		push(format("ERROR", String.format(message, args)));
		push(format("ERROR", "Exception Thrown: " + exception.toString()));

		for (StackTraceElement st : exception.getStackTrace())
			push(format("ERROR", "  at " + st.toString()));
	}

	/**
	 * Logs a message with the priority level of fatal.
	 *
	 * @param message
	 *            - The message to log.
	 */
	public static void fatal(String message)
	{
		push(format("FATAL", message));
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
		push(format("FATAL", String.format(message, args)));
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
		push(format("FATAL", String.format(message, args)));
		push(format("FATAL", "Exception Thrown: " + exception.toString()));

		for (StackTraceElement st : exception.getStackTrace())
			push(format("FATAL", "  at " + st.toString()));
	}

	private synchronized static void push(String message)
	{
		System.out.println(message);
	}

	private static String format(String type, String message)
	{
		LocalTime time = LocalTime.now();

		String format;

		String thread = Thread.currentThread().getName();
		int indent = _indent.getOrDefault(thread, 0);

		if (indent > 0)
		{
			int spaces = indent * SPACES_PER_INDENT + message.length();
			format = "[%02d:%02d:%02d][%-5s][%s] %" + spaces + "s";
		}
		else
			format = "[%02d:%02d:%02d][%-5s][%s] %s";

		return String.format(format, time.getHour(), time.getMinute(), time.getSecond(), type,
				thread, message);
	}
}
