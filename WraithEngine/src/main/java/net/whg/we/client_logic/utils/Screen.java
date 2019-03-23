package net.whg.we.client_logic.utils;

import net.whg.we.utils.logging.Log;
import net.whg.we.client_logic.window.QueuedWindow;

/**
 * A utility class for talking to the screen.
 *
 * @author TheDudeFromCI
 */
public class Screen
{
    private static int _width = 640;
    private static int _height = 480;
    private static boolean _mouseLocked;
    private static QueuedWindow _window;

    /**
     * Sets the window to forward update events to, such as
     * {@link #setMouseLocked(boolean)}. When this window is assigned, the
     * assigned state will be passed to the window.
     *
     * @param window
     */
    public static void setWindow(QueuedWindow window)
    {
        _window = window;

        window.setCursorEnabled(!_mouseLocked);
    }

    /**
     * Changes the internal referenced size of the screen. This should only be
     * called by the window listener when a window is resized.
     *
     * @param width
     *                   - The new width of the screen.
     * @param height
     *                   - The new height of the screen.
     */
    public static void updateSize(int width, int height)
    {
        _width = width;
        _height = height;
    }

    /**
     * Checks if the mouse if currently locked to the screen.
     *
     * @return True if the mouse is locked to the screen. False otherwise.
     */
    public static boolean isMouseLocked()
    {
        return _mouseLocked;
    }

    /**
     * Attempts to lock or unlock the mouse from the screen.
     *
     * @param mouseLocked
     *                        - Whether to lock the mouse (if true) or unlock
     *                        the mouse (if false).
     */
    public static void setMouseLocked(boolean mouseLocked)
    {
        Log.tracef("Set cursor locked to %s.", mouseLocked);

        if (_window != null)
            _window.setCursorEnabled(!mouseLocked);
        _mouseLocked = mouseLocked;
    }

    /**
     * Gets the current width of the screen in pixels.
     *
     * @return The width of the screen.
     */
    public static int width()
    {
        return _width;
    }

    /**
     * Gets the current height of the screen in pixels.
     *
     * @return The height of the screen.
     */
    public static int height()
    {
        return _height;
    }

    /**
     * Gets the current aspect ratio of the screen.
     *
     * @return The aspect ration of the screen.
     */
    public static float aspect()
    {
        return (float) _width / _height;
    }
}
