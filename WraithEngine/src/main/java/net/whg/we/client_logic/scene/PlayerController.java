package net.whg.we.client_logic.scene;

import net.whg.we.client_logic.rendering.Camera;

public interface PlayerController
{
	void updateFrame();

	Camera getCamera();
}
