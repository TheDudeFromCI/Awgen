package whg.WraithEngine.core;

public class Screen
{
	private static int _width = 640;
	private static int _height = 480;

	public static void updateSize(int width, int height)
	{
		_width = width;
		_height = height;
	}

	public static int width()
	{
		return _width;
	}

	public static int height()
	{
		return _height;
	}

	public static float aspect()
	{
		return (float) _width / _height;
	}
}
