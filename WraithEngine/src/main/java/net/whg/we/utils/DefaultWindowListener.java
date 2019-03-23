package net.whg.we.utils;

import org.lwjgl.opengl.GL11;
import net.whg.we.client_logic.utils.Input;
import net.whg.we.client_logic.utils.Screen;
import net.whg.we.client_logic.window.KeyState;
import net.whg.we.client_logic.window.WindowListener;

public class DefaultWindowListener implements WindowListener
{
    @Override
    public void onWindowResized(int width, int height)
    {
        Screen.updateSize(width, height);
        // _camera.rebuildProjectionMatrix();
        GL11.glViewport(0, 0, width, height);
    }

    @Override
    public void onKey(int key, KeyState state, int mods)
    {
        Input.setKeyPressed(key, state, mods);
    }

    @Override
    public void onMouseMoved(float mouseX, float mouseY)
    {
        Input.setMousePosition(mouseX, mouseY);
    }

    @Override
    public void onType(int key, int mods)
    {
        Input.addTypedKey(key, mods);
    }
}
