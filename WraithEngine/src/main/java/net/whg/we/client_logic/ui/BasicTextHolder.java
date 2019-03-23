package net.whg.we.client_logic.ui;

import net.whg.we.client_logic.ui.font.Font;

public class BasicTextHolder implements TextHolder
{
	private Font _font;
	private String _text = "";

	public BasicTextHolder(Font font)
	{
		_font = font;
	}

	@Override
	public String getText()
	{
		return _text;
	}

	@Override
	public void setText(String text)
	{
		_text = text;
	}

	@Override
	public Font getFont()
	{
		return _font;
	}
}
