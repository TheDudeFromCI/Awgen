package net.whg.we.client_logic.utils;

import net.whg.frameworks.logging.Log;
import net.whg.we.legacy.Time;

/**
 * A quick and simple utility class that calculates the current rendering
 * framerate based on the information returned from the Time utility class.
 *
 * @author TheDudeFromCI
 */
public class FPSLogger
{
    private static float _lastLog = 0f;
    private static int _frames;

    /**
     * Gets the current framerate of the last frame.
     *
     * @return The framerate of the last frame.
     */
    public static float getFramerate()
    {
        return 1f / Time.deltaTime();
    }

    /**
     * Resets the log timer to the current frame. Useful to avoid inaccurate log
     * calculation if the rendering has been paused for a period of time.
     */
    public static void resetLogTimer()
    {
        _lastLog = Time.time();
        _frames = 0;
    }

    /**
     * Updates the current framerate and logs the average framerate to the
     * console once every second. The framerate is logged under the TRACE log
     * priority.
     */
    public static void logFramerate()
    {
        _frames++;
        if (Time.time() - _lastLog < 1f)
            return;

        float average = (Time.time() - _lastLog) / _frames;
        _lastLog = Time.time();
        _frames = 0;

        float fps = 1f / average;
        Log.tracef("FPS Logger: %.0ffps (~%.2fms)", fps, average);
    }
}
