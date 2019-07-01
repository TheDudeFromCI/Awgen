package net.whg.we.client_logic.ui;

import net.whg.we.client_logic.ui.font.TextSelection;

public class BasicTextSelection implements TextSelection
{
	private int _selStart = -1;
	private int _selOrigin = -1;
	private int _selEnd = -1;

	@Override
	public int selStart()
	{
		return _selStart;
	}

	@Override
	public int selOrigin()
	{
		return _selOrigin;
	}

	@Override
	public int selEnd()
	{
		return _selEnd;
	}

	@Override
	public void setSelection(int start, int origin, int end)
	{
		_selStart = start;
		_selOrigin = origin;
		_selEnd = end;
	}

	@Override
	public boolean hasSelection()
	{
		return _selStart != -1;
	}

	@Override
	public void clearSelection()
	{
		_selStart = -1;
	}
}
