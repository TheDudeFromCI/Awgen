package net.whg.frameworks.logging;

import java.io.PrintWriter;
import java.util.HashMap;
import net.whg.frameworks.util.ObjectPool;

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
	private LogOutput _out;
	private ObjectPool<LogProperty> _logPropertyPool = new ObjectPool<LogProperty>()
	{
		@Override
		protected LogProperty build()
		{
			return new LogProperty();
		}
	};

	private static Log getInstance()
	{
		if (_instance == null)
			_instance = new Log(new LogPrintWriterOut(new PrintWriter(System.out)));

		return _instance;
	}

	public static void setOutput(LogOutput out)
	{
		getInstance()._out = out;
	}

	public static LogOutput getOutput()
	{
		return getInstance()._out;
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
		return getInstance()._indent.getOrDefault(thread, 0);
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

	public static LogProperty get()
	{
		return getInstance()._logPropertyPool.get();
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

		format(get().setSeverity(TRACE).setMessage(message));
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

		format(get().setSeverity(TRACE).setMessage(message, args));
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

		format(get().setSeverity(DEBUG).setMessage(message));
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

		format(get().setSeverity(DEBUG).setMessage(message, args));
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

		format(get().setSeverity(INFO).setMessage(message));
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

		format(get().setSeverity(INFO).setMessage(message, args));
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

		format(get().setSeverity(WARN).setMessage(message));
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

		format(get().setSeverity(WARN).setMessage(message, args));
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

		format(get().setSeverity(ERROR).setMessage(message));
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

		format(get().setSeverity(ERROR).setMessage(String.format(message, args)));
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

		StringBuilder sb = new StringBuilder();
		sb.append("Exception Thrown: ").append(exception.toString()).append('\n');
		for (StackTraceElement st : exception.getStackTrace())
			sb.append("  at ").append(st.toString()).append('\n');

		format(get().setSeverity(ERROR)
				.setMessage(String.format(message, args), exception.toString())
				.setProperty("Exception", sb.toString()));
	}

	/**
	 * Logs a message with the priority level of fatal.
	 *
	 * @param message
	 *            - The message to log.
	 */
	public static void fatal(String message)
	{
		format(get().setSeverity(FATAL).setMessage(message));
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
		format(get().setSeverity(FATAL).setMessage(String.format(message, args)));
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
		StringBuilder sb = new StringBuilder();
		sb.append("Exception Thrown: ").append(exception.toString()).append('\n');
		for (StackTraceElement st : exception.getStackTrace())
			sb.append("  at ").append(st.toString()).append('\n');

		format(get().setSeverity(FATAL).setMessage(String.format(message, args))
				.setProperty("Exception", sb.toString()));
	}

	public static void log(LogProperty property)
	{
		format(property);
	}

	private synchronized static void push(LogProperty property)
	{
		getInstance()._out.println(property);
	}

	private static void format(LogProperty property)
	{
		property.setIndent(getIndentLevel());

		push(property);
		getInstance()._logPropertyPool.put(property);
	}

	public static void dispose()
	{
		_instance = null;
	}

	public Log(LogOutput out)
	{
		_out = out;
	}
}
