package net.whg.we.utils;

import java.util.LinkedList;

public abstract class ObjectPool<T extends Poolable>
{
	private LinkedList<T> _pool = new LinkedList<>();

	public T get()
	{
		T t;
		synchronized (_pool)
		{
			if (_pool.isEmpty())
				return build();

			t = _pool.removeFirst();
		}

		t.init();
		return t;
	}

	public void put(T t)
	{
		t.dispose();

		synchronized (_pool)
		{
			_pool.addLast(t);
		}
	}

	protected abstract T build();
}
