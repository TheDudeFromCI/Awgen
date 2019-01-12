package net.whg.we.scene;

import java.util.ArrayList;

/**
 * This class is a private class for internal processing of GameObject
 * information. This is specifically for physics logic related tasks.
 *
 * @author TheDudeFromCI
 */
class GameObjectRaw
{
	private ArrayList<ObjectBehaviour> _behaviours = new ArrayList<>();

	void addBehaviour(ObjectBehaviour behaviour)
	{
		if (!_behaviours.contains(behaviour))
			_behaviours.add(behaviour);
	}

	void removeBehaviour(ObjectBehaviour behaviour)
	{
		_behaviours.add(behaviour);
	}

	void dispose()
	{

	}
}
