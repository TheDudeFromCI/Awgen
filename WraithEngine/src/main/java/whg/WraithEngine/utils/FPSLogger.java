package whg.WraithEngine.utils;

import whg.WraithEngine.core.Time;

public class FPSLogger
{
	private float _lastLog = 0f;
	private int _frames;

	public float getFramerate()
	{
		return 1f / Time.deltaTime();
	}
	
	public void logFramerate()
	{
		_frames++;
		if (Time.time() - _lastLog < 1f)
			return;
		
		float average = (Time.time() - _lastLog) / _frames;
		_lastLog = Time.time();
		_frames = 0;
		
		float fps = 1f / average;
		System.out.printf("FPS: %.3f\n", fps);
	}
}
