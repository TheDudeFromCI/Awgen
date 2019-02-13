package net.whg.we.ui;

import org.joml.Vector2f;

public interface UIComponent
{
	Vector2f getPosition();

	Vector2f getSize();

	void init();

	void update();

	void updateFrame();

	void render();

	void dispose();

	boolean isDisposed();
}
