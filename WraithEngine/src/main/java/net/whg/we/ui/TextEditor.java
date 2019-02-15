package net.whg.we.ui;

import net.whg.we.ui.font.Cursor;
import net.whg.we.ui.font.TextSelection;
import net.whg.we.ui.font.UIString;
import net.whg.we.utils.Input;
import net.whg.we.utils.Input.TypedKey;
import net.whg.we.utils.Time;

public class TextEditor
{
	private UIString _textOut;
	private Cursor _cursor;
	private TextSelection _sel;
	private String[] _lines;
	private int _caretX;
	private int _caretY;
	private int _selStart = -1;
	private int _selOrigin = -1;
	private int _selEnd = -1;

	public TextEditor(UIString textOut)
	{
		_textOut = textOut;
		_cursor = textOut.getCursor();
		_sel = textOut.getTextSelection();

		_lines = new String[1];
		_lines[0] = "";
	}

	private int caretIndex()
	{
		return caretIndex(_caretX, _caretY);
	}

	private int caretIndex(int x, int y)
	{
		int index = x;

		for (int i = 0; i < y; i++)
			index += _lines[i].length() + 1;

		return index;
	}

	private String textAsString()
	{
		String s = "";

		for (int i = 0; i < _lines.length; i++)
		{
			if (i > 0)
				s += '\n';

			s += _lines[i];
		}

		return s;
	}

	private void stringToText(String s)
	{
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

	private void deleteSelection()
	{
		String s = textAsString();
		s = s.substring(0, _selStart) + s.substring(_selEnd, s.length());
		stringToText(s);

		_caretX = 0;
		_caretY = 0;
		for (int i = 0; i < s.length(); i++)
		{
			if (i == _selStart)
				break;

			if (s.charAt(i) == '\n')
			{
				_caretX = 0;
				_caretY++;
				continue;
			}

			_caretX++;
		}

		_selStart = _selEnd = _selOrigin = -1;
	}

	public void updateFrame()
	{
		boolean insertMode = _cursor.getInsertMode();

		boolean changedText = false;
		for (TypedKey key : Input.getTypedKeys())
		{
			if (key.extraKey == Input.NO_KEY)
			{
				if (_textOut.getFont().getGlyph(key.key) == null)
					continue;

				if (insertMode && _selStart == -1 && _caretX < _lines[_caretY].length())
				{
					_lines[_caretY] = _lines[_caretY].substring(0, _caretX) + key.key
							+ _lines[_caretY].substring(_caretX + 1, _lines[_caretY].length());
					_caretX++;
				}
				else
				{
					if (_selStart != -1)
						deleteSelection();

					_lines[_caretY] = _lines[_caretY].substring(0, _caretX) + key.key
							+ _lines[_caretY].substring(_caretX, _lines[_caretY].length());
					_caretX++;
				}

				changedText = true;
			}
			else if (key.extraKey == Input.BACKSPACE_KEY)
			{
				if (_selStart == -1)
				{
					if (_caretX > 0 || _caretY > 0)
					{
						if (_caretX == 0)
						{
							_caretY--;
							_caretX = _lines[_caretY].length();

							_lines[_caretY] += _lines[_caretY + 1];

							String[] newLines = new String[_lines.length - 1];
							for (int i = 0; i <= _caretY; i++)
								newLines[i] = _lines[i];

							for (int i = _caretY + 1; i < newLines.length; i++)
								newLines[i] = _lines[i + 1];

							_lines = newLines;
						}
						else
						{
							_lines[_caretY] = _lines[_caretY].substring(0, _caretX - 1)
									+ _lines[_caretY].substring(_caretX, _lines[_caretY].length());
							_caretX--;
						}

						_selStart = _selEnd = -1;

						changedText = true;
					}
				}
				else
				{
					deleteSelection();
					changedText = true;
				}
			}
			else if (key.extraKey == Input.DELETE_KEY)
			{
				if (_selStart == -1)
				{
					if (_caretX < _lines[_caretY].length() || _caretY < _lines.length - 1)
					{
						if (_caretX == _lines[_caretY].length())
						{
							_lines[_caretY] += _lines[_caretY + 1];

							String[] newLines = new String[_lines.length - 1];
							for (int i = 0; i <= _caretY; i++)
								newLines[i] = _lines[i];

							for (int i = _caretY + 1; i < newLines.length; i++)
								newLines[i] = _lines[i + 1];

							_lines = newLines;
						}
						else
						{
							_lines[_caretY] =
									_lines[_caretY].substring(0, _caretX) + _lines[_caretY]
											.substring(_caretX + 1, _lines[_caretY].length());
						}

						_selStart = _selEnd = -1;

						changedText = true;
					}
				}
				else
				{
					deleteSelection();
					changedText = true;
				}
			}
			else if (key.extraKey == Input.LEFT_KEY)
			{
				if (key.shift && _selStart == -1)
					_selStart = _selEnd = _selOrigin = caretIndex();

				if (_caretX > 0)
				{
					_caretX--;
				}
				else if (_caretY > 0)
				{
					_caretY--;
					_caretX = _lines[_caretY].length();
				}

				if (key.shift)
				{
					int car = caretIndex();
					_selEnd = Math.max(_selOrigin, car);
					_selStart = Math.min(_selOrigin, car);
				}
				else
					_selStart = _selEnd = _selOrigin = -1;
			}
			else if (key.extraKey == Input.RIGHT_KEY)
			{
				if (key.shift && _selStart == -1)
					_selStart = _selEnd = _selOrigin = caretIndex();

				if (_caretX < _lines[_caretY].length())
				{
					_caretX++;
				}
				else if (_caretY < _lines.length - 1)
				{
					_caretX = 0;
					_caretY++;
				}

				if (key.shift)
				{
					int car = caretIndex();
					_selEnd = Math.max(_selOrigin, car);
					_selStart = Math.min(_selOrigin, car);
				}
				else
					_selStart = _selEnd = _selOrigin = -1;
			}
			else if (key.extraKey == Input.HOME_KEY)
			{
				if (key.shift && _selStart == -1)
					_selStart = _selEnd = _selOrigin = caretIndex();

				_caretX = 0;

				if (key.shift)
				{
					int car = caretIndex();
					_selEnd = Math.max(_selOrigin, car);
					_selStart = Math.min(_selOrigin, car);
				}
				else
					_selStart = _selEnd = _selOrigin = -1;
			}
			else if (key.extraKey == Input.END_KEY)
			{
				if (key.shift && _selStart == -1)
					_selStart = _selEnd = _selOrigin = caretIndex();

				_caretX = _lines[_caretY].length();

				if (key.shift)
				{
					int car = caretIndex();
					_selEnd = Math.max(_selOrigin, car);
					_selStart = Math.min(_selOrigin, car);
				}
				else
					_selStart = _selEnd = _selOrigin = -1;
			}
			else if (key.extraKey == Input.UP_KEY)
			{
				if (key.shift && _selStart == -1)
					_selStart = _selEnd = _selOrigin = caretIndex();

				if (_caretY > 0)
				{
					_caretY--;
					_caretX = Math.min(_caretX, _lines[_caretY].length());

					if (key.shift)
					{
						int car = caretIndex();
						_selEnd = Math.max(_selOrigin, car);
						_selStart = Math.min(_selOrigin, car);
					}
				}
				else
					_selStart = _selEnd = _selOrigin = -1;
			}
			else if (key.extraKey == Input.DOWN_KEY)
			{
				if (key.shift && _selStart == -1)
					_selStart = _selEnd = _selOrigin = caretIndex();

				if (_caretY < _lines.length - 1)
				{
					_caretY++;
					_caretX = Math.min(_caretX, _lines[_caretY].length());

					if (key.shift)
					{
						int car = caretIndex();
						_selEnd = Math.max(_selOrigin, car);
						_selStart = Math.min(_selOrigin, car);
					}
				}
				else
					_selStart = _selEnd = _selOrigin = -1;
			}
			else if (key.extraKey == Input.ENTER_KEY)
			{
				if (_selStart != -1)
					deleteSelection();

				String[] newLines = new String[_lines.length + 1];

				for (int i = 0; i < _caretY; i++)
					newLines[i] = _lines[i];

				newLines[_caretY] = _lines[_caretY].substring(0, _caretX);
				newLines[_caretY + 1] =
						_lines[_caretY].substring(_caretX, _lines[_caretY].length());

				for (int i = _caretY + 1; i < _lines.length; i++)
					newLines[i + 1] = _lines[i];

				_lines = newLines;

				_caretX = 0;
				_caretY++;

				_selStart = _selEnd = -1;

				changedText = true;
			}
			else if (key.extraKey == Input.INSERT_KEY)
				_cursor.setInsertMode(!insertMode);
		}

		if (changedText)
			_textOut.setText(textAsString());

		_cursor.setCaretPos(_caretX, _caretY);
		_sel.setSelection(_selStart, _selEnd);

		_cursor.setVisible(Time.time() % 0.666f < 0.333f);
	}
}
