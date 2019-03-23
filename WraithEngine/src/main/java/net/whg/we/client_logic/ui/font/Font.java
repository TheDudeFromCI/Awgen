package net.whg.we.client_logic.ui.font;

public class Font
{
	private Glyph[] _glyphs;

	public Font(Glyph[] glyphs)
	{
		_glyphs = glyphs;
	}

	public Glyph[] getGlyphs()
	{
		return _glyphs;
	}

	public Glyph getGlyph(char c)
	{
		for (int i = 0; i < _glyphs.length; i++)
			if (_glyphs[i].getCharacter() == c)
				return _glyphs[i];
		return null;
	}

	public int countUsedSymbols(String text)
	{
		int used = 0;

		for (int i = 0; i < text.length(); i++)
			if (getGlyph(text.charAt(i)) != null)
				used++;

		return used;
	}
}
