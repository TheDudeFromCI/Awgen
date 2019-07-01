package net.whg.we.client_logic.ui;

import net.whg.we.legacy.Transform2D;

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
