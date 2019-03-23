package net.whg.we.client_logic.ui;

public class TextProcessor
{
	private String[] _lines;
	private StringBuilder _sb = new StringBuilder();

	public TextProcessor()
	{
		_lines = new String[1];
		_lines[0] = "";
	}

	public TextProcessor(String text)
	{
		set(text);
	}

	public void set(String s)
	{
		if (s == null)
			s = "";

		int lineCount = 1;
		for (int i = 0; i < s.length(); i++)
			if (s.charAt(i) == '\n')
				lineCount++;

		String[] lines = new String[lineCount];
		for (int i = 0; i < lineCount; i++)
			lines[i] = "";

		int lineIndex = 0;
		for (int i = 0; i < s.length(); i++)
		{

			if (s.charAt(i) == '\n')
			{
				lineIndex++;
				continue;
			}

			lines[lineIndex] += s.charAt(i);
		}

		_lines = lines;
	}

	public void insert(int line, int position, String s)
	{
		if (line < 0 || line >= _lines.length)
			throw new ArrayIndexOutOfBoundsException(line);
		if (position < 0 || position > _lines[line].length())
			throw new ArrayIndexOutOfBoundsException(position);

		_lines[line] = _lines[line].substring(0, position) + s
				+ _lines[line].substring(position, _lines[line].length());

		wrapNewLineCharacters(line);
	}

	private void wrapNewLineCharacters(int line)
	{
		int newLineIndex;
		while ((newLineIndex = _lines[line].indexOf('\n')) != -1)
		{
			String leftOver = _lines[line].substring(newLineIndex + 1, _lines[line].length());
			_lines[line] = _lines[line].substring(0, newLineIndex);

			insertNewLineAt(++line);
			_lines[line] = leftOver;
		}
	}

	public void insertNewLineAt(int line)
	{
		String[] lines = new String[_lines.length + 1];

		int j = 0;
		for (int i = 0; i < lines.length; i++)
		{
			if (i == line)
			{
				lines[i] = "";
				continue;
			}
			lines[i] = _lines[j++];
		}

		_lines = lines;
	}

	public void append(int line, String s)
	{
		if (s == null)
			return;

		_lines[line] += s;

		wrapNewLineCharacters(line);
	}

	public void delete(int line, int position, int count)
	{
		if (line < 0 || line >= _lines.length)
			throw new ArrayIndexOutOfBoundsException(line);
		if (position < 0)
			throw new ArrayIndexOutOfBoundsException(position);
		if (count <= 0 || position >= _lines[line].length())
			return;

		int toRemove = position + Math.min(count, _lines[line].length() - position);
		_lines[line] = _lines[line].substring(0, position)
				+ _lines[line].substring(toRemove, _lines[line].length());
	}

	public int caretPosition(int line, int position)
	{
		if (line < 0 || line >= _lines.length)
			throw new ArrayIndexOutOfBoundsException(line);
		if (position < 0)
			throw new ArrayIndexOutOfBoundsException(position);

		if (position > _lines[line].length())
			position = _lines[line].length();

		int c = position;
		for (int i = 0; i < line; i++)
			c += _lines[i].length() + 1;

		return c;
	}

	public int getLineByIndex(int index)
	{
		if (index < 0)
			return -1;

		int c = 0;
		for (int i = 0; i < _lines.length; i++)
		{
			c += _lines[i].length();
			if (c >= index)
				return i;
		}

		return -1;
	}

	public int getPositionByIndex(int index)
	{
		if (index < 0)
			return -1;

		for (int i = 0; i < _lines.length; i++)
		{
			if (_lines[i].length() >= index)
				return index;
			index -= _lines[i].length() + 1;
		}

		return Math.min(index, _lines[_lines.length - 1].length());
	}

	public void deleteInlcudeLines(int line, int position, int count)
	{
		if (line < 0 || line >= _lines.length)
			throw new ArrayIndexOutOfBoundsException(line);
		if (position < 0)
			throw new ArrayIndexOutOfBoundsException(position);
		if (count <= 0)
			return;

		// Small optimization for same line deletions.
		if (position + count < _lines[line].length())
		{
			delete(line, position, count);
			return;
		}

		String s = toString();
		int caret = caretPosition(line, position);

		int toRemove = caret + Math.min(count, s.length() - caret);
		s = s.substring(0, caret) + s.substring(toRemove, s.length());

		set(s);
	}

	public void deleteLine(int line)
	{
		if (line < 0 || line >= _lines.length)
			throw new ArrayIndexOutOfBoundsException(line);

		if (_lines.length == 1)
		{
			_lines[0] = "";
			return;
		}

		String[] lines = new String[_lines.length - 1];

		int j = 0;
		for (int i = 0; i < _lines.length; i++)
		{
			if (i == line)
				continue;
			lines[j++] = _lines[i];
		}

		_lines = lines;
	}

	public int getLineCount()
	{
		return _lines.length;
	}

	public String getLine(int index)
	{
		return _lines[index];
	}

	public int getLineLength(int line)
	{
		return _lines[line].length();
	}

	public void appendNewLine(int line, String text)
	{
		insertNewLineAt(line);
		append(line, text);
	}

	public void setLine(int line, String text)
	{
		_lines[line] = text;
	}

	@Override
	public String toString()
	{
		_sb.setLength(0);

		for (int i = 0; i < _lines.length; i++)
		{
			if (i > 0)
				_sb.append('\n');
			_sb.append(_lines[i]);
		}

		return _sb.toString();
	}
}
