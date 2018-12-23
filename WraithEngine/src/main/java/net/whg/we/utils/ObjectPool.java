package net.whg.we.utils;

import java.util.LinkedList;

public abstract class ObjectPool<T extends Poolable>
{
	private LinkedList<T> _pool = new LinkedList<>();

	public T get()
	{
		if (_pool.isEmpty())
			return build();

		T t = _pool.removeFirst();
		t.init();
		return t;
	}

	public void put(T t)
	{
		t.dispose();
		_pool.addLast(t);
	}

	protected abstract T build();
}
