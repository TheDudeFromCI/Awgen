package net.whg.we.scene;

import net.whg.we.event.Listener;

public interface UpdateListener extends Listener
{
	void init();

	void update();

	void updateFrame();

	void render();

	void dispose();
}
