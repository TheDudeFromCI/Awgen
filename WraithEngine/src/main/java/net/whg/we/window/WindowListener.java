package net.whg.we.window;

public interface WindowListener
{
	public void onWindowResized(int width, int height);

	public void onKey(int key, KeyState action, int mods);

	public void onMouseMoved(float mouseX, float mouseY);
}
