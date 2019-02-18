package net.whg.we.ui;

import net.whg.we.utils.Poolable;

public class TypedKeyInput implements Poolable
{
	public static final int NO_KEY = -1;
	public static final int BACKSPACE_KEY = 0;
	public static final int LEFT_KEY = 1;
	public static final int RIGHT_KEY = 2;
	public static final int HOME_KEY = 3;
	public static final int END_KEY = 4;
	public static final int UP_KEY = 5;
	public static final int DOWN_KEY = 6;
	public static final int ENTER_KEY = 7;
	public static final int DELETE_KEY = 8;
	public static final int INSERT_KEY = 9;
	public static final int TAB_KEY = 10;

	public char key;
	public boolean shift;
	public boolean control;
	public boolean alt;
	public boolean sup;
	public int extraKey;

	public TypedKeyInput()
	{
		init();
	}

	@Override
	public void init()
	{
		key = '\u0000';
		shift = false;
		control = false;
		alt = false;
		sup = false;
		extraKey = NO_KEY;
	}

	@Override
	public void dispose()
	{
	}
}
