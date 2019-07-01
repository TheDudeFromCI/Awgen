package net.whg.we.client_logic.window;

import net.whg.we.client_logic.rendering.Graphics;

interface Window
{
    void setQueuedWindow(QueuedWindow window);

    void setName(String name);

    void setResizable(boolean resizable);

    void setVSync(boolean vSync);

    void setSize(int width, int height);

    void buildWindow();

    void requestClose();

    void disposeWindow();

    boolean endFrame();

    void updateWindow();

    void initGraphics(Graphics graphics);

    void setCursorEnabled(boolean cursorEnabled);
}
