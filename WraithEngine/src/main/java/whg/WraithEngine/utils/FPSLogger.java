package whg.WraithEngine.utils;

import net.whg.we.utils.Log;
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
		Log.debugf("FPS Logger: %.0ffps (~%.2fms)", fps, average);
	}
}
