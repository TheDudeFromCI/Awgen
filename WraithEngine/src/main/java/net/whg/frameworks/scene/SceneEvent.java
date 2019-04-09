package net.whg.frameworks.scene;

import net.whg.frameworks.event.EventCallerBase;

public class SceneEvent extends EventCallerBase<ISceneListener>
{
	private static final int SCENE_HIERARCHY_CHANGED = 0;

	public void onSceneHierarchyChanged(SceneHierarchyChangedEvent event)
	{
		callEvent(SCENE_HIERARCHY_CHANGED, event, true);
	}

	@Override
	protected void runEvent(ISceneListener listener, int index, Object arg)
	{
		switch (index)
		{
			case SCENE_HIERARCHY_CHANGED:
				listener.onSceneHierarchyChanged((SceneHierarchyChangedEvent) arg);
				return;

			default:
				throw new RuntimeException("Unknown event type! " + index);
		}
	}
}
