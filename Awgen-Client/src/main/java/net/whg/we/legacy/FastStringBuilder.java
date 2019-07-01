package net.whg.we.legacy;

import net.whg.frameworks.util.Poolable;

class PoolableStringBuilder implements Poolable
{
	private StringBuilder sb = new StringBuilder();

	@Override
	public void init()
	{
	}

	@Override
	public void dispose()
	{
		sb.setLength(0);
	}

	@Override
	public String toString()
	{
		return sb.toString();
	}

	public void append(char c)
	{
		sb.append(c);
	}

	public void clear()
	{
		sb.setLength(0);
	}
}
