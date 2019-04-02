package net.whg.frameworks.util;

public class SimpleObjectPool<T extends Poolable> extends ObjectPool<T>
{
	private Class<T> _classReference;

	public SimpleObjectPool(Class<T> classReference)
	{
		_classReference = classReference;
	}

	@Override
	protected T build()
	{
		try
		{
			return _classReference.newInstance();
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}

		return null;
	}
}
