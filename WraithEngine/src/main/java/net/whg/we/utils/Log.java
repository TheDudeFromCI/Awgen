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
	 * Logs a message with the priority level of trace.
	 * 
	 * @param message
	 *            - The message to log.
	 */
	public static void trace(String message)
	{
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
	public static void errorf(String message, Throwable exception,
			Object... args)
	{
		// TODO Print stack trace
		System.out.println(format("ERROR", String.format(message, args)));
		System.out.println(format("ERROR", exception.getMessage()));
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
	public static void fatalf(String message, Throwable exception,
			Object... args)
	{
		// TODO Print stack trace
		System.out.println(format("FATAL", String.format(message, args)));
		System.out.println(format("FATAL", exception.getMessage()));
	}

	private static String format(String type, String message)
	{
		LocalTime time = LocalTime.now();

		return String.format("[%02d:%02d:%02d][%-5s] %s", time.getHour(),
				time.getMinute(), time.getSecond(), type, message);
	}
}
