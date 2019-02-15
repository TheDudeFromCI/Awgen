package net.whg.we.ui.font;

import net.whg.we.rendering.Material;
import net.whg.we.rendering.Mesh;
import net.whg.we.ui.UIImage;

public class UICursor extends UIImage implements Cursor
{
	private int _caretX;
	private int _caretY;
	private UIString _uiString;
	private boolean _insert;

	public UICursor(Mesh mesh, Material material, UIString uiString)
	{
		super(mesh, material);
		_uiString = uiString;

		getTransform().setPosition(0f, 0.5f);
		getTransform().setSize(1f / 12f, 1f);
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
		if (_caretX == x && _caretY == y)
			return;

		_caretX = x;
		_caretY = y;

		updateCaretPos();
	}

	@Override
	public void setInsertMode(boolean insert)
	{
		_insert = insert;

		if (_insert)
			getTransform().setSize(_uiString.getMonoWidth(), 1f);
		else
			getTransform().setSize(1f / 12f, 1f);
	}

	public void updateCaretPos()
	{
		float x = 0f;
		float y = 0.5f;

		int lineX = 0;
		int lineY = 0;
		for (int i = 0; i < _uiString.getText().length(); i++)
		{
			if (lineX == _caretX && lineY == _caretY)
				break;

			char c = _uiString.getText().charAt(i);
			if (c == '\n')
			{
				if (lineY == _caretY)
					break;

				lineX = 0;
				lineY++;

				x = 0f;
				y -= 1f;
				continue;
			}

			Glyph g = _uiString.getFont().getGlyph(c);
			if (g == null)
				continue;

			lineX++;
			x += g.getWidth();
		}

		if (_insert)
			x += _uiString.getMonoWidth() / 2f;

		getTransform().setPosition(x, y);
	}
}
