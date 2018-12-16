package net.whg.we.window;

public interface Window
{
	public void setName(String name);

	public boolean canChangeName();

	public String getName();

	public void setResizable(boolean resizable);

	public boolean canChangeResizable();

	public boolean isResizable();

	public void setVSync(boolean vSync);

	public boolean canChangeVSync();

	public boolean isVSync();

	public void setWindowSize(int width, int height);

	public boolean canChangeWindowSize();

	public int getWidth();

	public int getHeight();

	public void buildWindow();

	public void disposeWindow();
}
