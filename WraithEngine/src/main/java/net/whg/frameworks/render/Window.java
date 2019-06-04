package net.whg.frameworks.render;

public interface Window
{
	void init(WindowProperties properties);

	void show();

	void rebuild(WindowProperties properties);

	void destory();

	boolean isOpen();
}
