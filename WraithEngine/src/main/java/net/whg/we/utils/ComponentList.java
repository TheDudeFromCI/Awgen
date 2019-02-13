package net.whg.we.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This object represents a list where objects are not added or removed until
 * the end of a frame. This object also prevents an object to be put in twice.
 * Note: because objects are not added or removed from this list until the end
 * of a frame (by calling #{@link #endFrame()}), the size of the list will
 * appear unchanged, as well as all other normal operations such as iteration
 * and sublists. Only some methods are specifically overriden, namely
 * {@link #add(Object)} and {@link #remove(Object)}.
 *
 * @author TheDudeFromCI
 * @param <T>
 *            - The type of object this list contains.
 */
public class ComponentList<T> extends ArrayList<T>
{
	private static final long serialVersionUID = -6194959716842919143L;

	private List<T> _toAdd = new LinkedList<>();
	private List<T> _toRemove = new LinkedList<>();
	private List<Runnable> _componentActions = new LinkedList<>();

	public void endFrame()
	{
		addAll(_toAdd);
		removeAll(_toRemove);

		_toAdd.clear();
		_toRemove.clear();

		while (!_componentActions.isEmpty())
			_componentActions.remove(0).run();
	}

	@Override
	public boolean add(T e)
	{
		if (super.contains(e) || _toAdd.contains(e))
			return true;

		_toAdd.add(e);
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object o)
	{
		_toRemove.add((T) o);
		return true;
	}

	public void addInstant(T e)
	{
		super.add(e);

		_toAdd.remove(e);
		_toRemove.remove(e);
	}

	public void removeInstant(T t)
	{
		super.remove(t);

		_toAdd.remove(t);
		_toRemove.remove(t);
	}

	public void clearPending()
	{
		clear();
		_toAdd.clear();
		_toRemove.clear();
		_componentActions.clear();
	}

	public boolean containPending(Object o)
	{
		return (contains(o) || _toAdd.contains(o)) && !_toRemove.contains(o);
	}

	public void preformComponentAction(Runnable action)
	{
		_componentActions.add(action);
	}
}
