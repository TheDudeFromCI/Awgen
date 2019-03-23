package net.whg.we.client_logic.ui;

import net.whg.we.client_logic.ui.font.Cursor;

public class BasicCursor implements Cursor
{
	private int _caretX;
	private int _caretY;
	private boolean _insert;

	@Override
	public void setVisible(boolean visible)
	{
	}

	@Override
	public int getCaretX()
	{
		return _caretX;
	}

	@Override
	public int getCaretY()
	{
		return _caretY;
	}

	@Override
	public void setCaretPos(int x, int y)
	{
		_caretX = x;
		_caretY = y;
	}

	@Override
	public void setInsertMode(boolean insert)
	{
		_insert = insert;
	}

	@Override
	public boolean getInsertMode()
	{
		return _insert;
	}
}
