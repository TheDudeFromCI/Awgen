package whg.WraithEngine.utils;

import java.time.LocalTime;

public class Log
{
	public static void trace(String message)
	{
		System.out.println(format("TRACE", message));
	}

	public static void tracef(String message, Object... args)
	{
		System.out.println(format("TRACE", String.format(message, args)));
	}
	
	public static void debug(String message)
	{
		System.out.println(format("DEBUG", message));
	}

	public static void debugf(String message, Object... args)
	{
		System.out.println(format("DEBUG", String.format(message, args)));
	}
	
	public static void info(String message)
	{
		System.out.println(format("INFO", message));
	}

	public static void infof(String message, Object... args)
	{
		System.out.println(format("INFO", String.format(message, args)));
	}
	
	public static void warn(String message)
	{
		System.out.println(format("WARN", message));
	}

	public static void warnf(String message, Object... args)
	{
		System.out.println(format("WARN", String.format(message, args)));
	}
	
	public static void error(String message)
	{
		System.out.println(format("ERROR", message));
	}

	public static void errorf(String message, Object... args)
	{
		System.out.println(format("ERROR", String.format(message, args)));
	}
	
	public static void fatal(String message)
	{
		System.out.println(format("FATAL", message));
	}

	public static void fatalf(String message, Object... args)
	{
		System.out.println(format("FATAL", String.format(message, args)));
	}
	
	private static String format(String type, String message)
	{
		LocalTime time = LocalTime.now();
		
		return String.format("[%02d:%02d:%02d][%-5s] %s", time.getHour(), time.getMinute(),
				time.getSecond(), type, message);
	}
}
