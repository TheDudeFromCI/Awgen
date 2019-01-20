package net.whg.we.scene.behaviours;

import net.whg.we.scene.GameObject;
import net.whg.we.scene.ObjectBehaviour;

public class RenderBehaviour implements ObjectBehaviour
{
	private GameObject _owner;

	@Override
	public void init(GameObject owner)
	{
		_owner = owner;
	}

	@Override
	public void dispose()
	{

	}
}
