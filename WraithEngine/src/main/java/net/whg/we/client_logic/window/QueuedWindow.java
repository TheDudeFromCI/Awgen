package net.whg.we.client_logic.window;

import java.util.LinkedList;
import net.whg.we.client_logic.rendering.Graphics;

public class QueuedWindow
{
    private Window _window;
    private LinkedList<WindowRequest> _requests = new LinkedList<>();
    private LinkedList<WindowEvent> _events = new LinkedList<>();

    private boolean _requestClose;
    private String _name;
    private boolean _isResizable;
    private boolean _vSync;
    private int _width;
    private int _height;
    private WindowListener _listener;
    private boolean _cursorEnabled = true;

    QueuedWindow(Window window)
    {
        _window = window;
        _window.setQueuedWindow(this);
    }

    void waitForEvents()
    {
        while (!_requestClose)
        {
            _window.updateWindow();

            synchronized (_requests)
            {
                while (!_requests.isEmpty())
                    _requests.removeFirst().run();
            }
        }

        _window.disposeWindow();
    }

    public void setWindowListener(WindowListener listener)
    {
        _listener = listener;
    }

    Window getWindow()
    {
        return _window;
    }

    void buildWindow()
    {
        addRequest(() ->
        {
            _window.buildWindow();
        });
    }

    public void setCursorEnabled(boolean cursorEnabled)
    {
        addRequest(() ->
        {
            _window.setCursorEnabled(cursorEnabled);
        });
    }

    void setCursorEnabledInstant(boolean cursorEnabled)
    {
        _cursorEnabled = cursorEnabled;
    }

    public boolean isCursorEnabled()
    {
        return _cursorEnabled;
    }

    public void requestClose()
    {
        addRequest(() ->
        {
            _window.requestClose();
        });
    }

    private void addRequest(WindowRequest r)
    {
        synchronized (_requests)
        {
            _requests.add(r);
        }
    }

    void addEvent(WindowEvent e)
    {
        synchronized (_events)
        {
            _events.add(e);
        }
    }

    public String getName()
    {
        return _name;
    }

    public boolean isResizable()
    {
        return _isResizable;
    }

    public boolean isVSync()
    {
        return _vSync;
    }

    public int getWidth()
    {
        return _width;
    }

    public int getHeight()
    {
        return _height;
    }

    public void setName(String name)
    {
        addRequest(() ->
        {
            _window.setName(name);
        });
    }

    void setNameInstant(String name)
    {
        _name = name;
    }

    public void setResizable(boolean resizable)
    {
        addRequest(() ->
        {
            _window.setResizable(resizable);
        });
    }

    void setResizableInstant(boolean resizable)
    {
        _isResizable = resizable;
    }

    public void setVSync(boolean vSync)
    {
        addRequest(() ->
        {
            _window.setVSync(vSync);
        });
    }

    void setVSyncInstant(boolean vSync)
    {
        _vSync = vSync;
    }

    public void setSize(int width, int height)
    {
        addRequest(() ->
        {
            _window.setSize(width, height);
        });
    }

    void setSizeInstant(int width, int height)
    {
        _width = width;
        _height = height;

        if (_listener != null)
            _listener.onWindowResized(width, height);
    }

    void onKey(int key, KeyState state, int mods)
    {
        if (_listener != null)
            _listener.onKey(key, state, mods);
    }

    void onType(int key, int mods)
    {
        if (_listener != null)
            _listener.onType(key, mods);
    }

    void onMouseMove(float mouseX, float mouseY)
    {
        if (_listener != null)
            _listener.onMouseMoved(mouseX, mouseY);
    }

    /**
     * Called at the end of each rendered frame to push the image to the monitor
     * and poll input events.
     *
     * @return True if the window has requested a close. False otherwise.
     */
    public boolean endFrame()
    {
        synchronized (_events)
        {
            while (!_events.isEmpty())
                _events.removeFirst().run();
        }

        if (_window.endFrame())
        {
            _requestClose = true;
            return true;
        }

        return false;
    }

    private void blockUntilRequestsFinish()
    {
        while (true)
        {
            try
            {
                Thread.sleep(1);
            }
            catch (InterruptedException e)
            {
            }

            synchronized (_requests)
            {
                if (_requests.isEmpty())
                    return;
            }
        }
    }

    void initGraphics(Graphics graphics)
    {
        blockUntilRequestsFinish();
        _window.initGraphics(graphics);
    }
}
