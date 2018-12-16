package net.whg.we.utils;

import java.time.LocalTime;

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

	private static int _indent = 0;
	private static int _logLevel = INFO;

	/**
	 * Gets the current indent value for the log.
	 *
	 * @return The current indent value.
	 */
	public static int getIndentLevel()
	{
		return _indent;
	}

	/**
	 * Sets the indent level to a specific value. If the value is negative, the
	 * value is set to zero.
	 *
	 * @param indent
	 *            - The new indent level.
	 */
	public static void setIndentLevel(int indent)
	{
		_indent = indent;

		if (_indent < 0)
			_indent = 0;
	}

	/**
	 * Increases the indent for future log entrys by one.
	 */
	public static void indent()
	{
		_indent++;
	}

	/**
	 * Decreases the indent for future log entrys by one. If the value is already at
	 * zero, nothing happens.
	 */
	public static void unindent()
	{
		_indent--;

		if (_indent < 0)
			_indent = 0;
	}

	/**
	 * Resets the indent level for future log entrys to default. (Zero)
	 */
	public static void resetIndent()
	{
		_indent = 0;
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

		System.out.println(format("TRACE", message));
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

		System.out.println(format("TRACE", String.format(message, args)));
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

		System.out.println(format("DEBUG", message));
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

		System.out.println(format("DEBUG", String.format(message, args)));
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

		System.out.println(format("INFO", message));
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

		System.out.println(format("INFO", String.format(message, args)));
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

		System.out.println(format("WARN", message));
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

		System.out.println(format("WARN", String.format(message, args)));
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

		System.out.println(format("ERROR", message));
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

		System.out.println(format("ERROR", String.format(message, args)));
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

		System.out.println(format("ERROR", String.format(message, args)));
		System.out.println(format("ERROR", "Exception Thrown: " + exception.toString()));

		for (StackTraceElement st : exception.getStackTrace())
			System.out.println(format("ERROR", "  at " + st.toString()));
	}

	/**
	 * Logs a message with the priority level of fatal.
	 *
	 * @param message
	 *            - The message to log.
	 */
	public static void fatal(String message)
	{
		System.out.println(format("FATAL", message));
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
		System.out.println(format("FATAL", String.format(message, args)));
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
		System.out.println(format("FATAL", String.format(message, args)));
		System.out.println(format("FATAL", "Exception Thrown: " + exception.toString()));

		for (StackTraceElement st : exception.getStackTrace())
			System.out.println(format("FATAL", "  at " + st.toString()));
	}

	private static String format(String type, String message)
	{
		LocalTime time = LocalTime.now();

		String format;

		if (_indent > 0)
		{
			int spaces = _indent * SPACES_PER_INDENT + message.length();
			format = "[%02d:%02d:%02d][%-5s] %" + spaces + "s";
		}
		else
			format = "[%02d:%02d:%02d][%-5s] %s";

		return String.format(format, time.getHour(), time.getMinute(), time.getSecond(), type,
				message);
	}
}
