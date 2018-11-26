package whg.WraithEngine.core;

public class Time
{
	private static float _time;
	private static float _delta;
	private static long _startMs = -1;
	
	public static void updateTime()
	{
		long time = System.nanoTime();

		if (_startMs == -1)
		{
			_startMs = time;
			_time = 0f;
			_delta = 0f;
			return;
		}
		
		long passed = time - _startMs;
		_startMs = time;
		
		_delta = (float)(passed / 1000000000.0);
		_time += _delta;
	}
	
	public static float time()
	{
		return _time;
	}
	
	public static float deltaTime()
	{
		return _delta;
	}
}
