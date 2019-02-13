package net.whg.we.ui;

public interface UIComponent
{
	Transform2D getTransform();

	void init();

	void update();

	void updateFrame();

	void render();

	void dispose();

	boolean isDisposed();
}
