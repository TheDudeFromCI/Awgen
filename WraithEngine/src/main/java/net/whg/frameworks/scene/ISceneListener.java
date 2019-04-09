package net.whg.frameworks.scene;

import net.whg.frameworks.event.Listener;

/**
 * Listens for scene update events.
 *
 * @author TheDudeFromCI
 */
public interface ISceneListener extends Listener
{
	void onSceneHierarchyChanged(SceneHierarchyChangedEvent event);
}
