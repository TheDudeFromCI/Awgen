package net.whg.we.ui;

import net.whg.we.ui.font.Cursor;
import net.whg.we.ui.font.UIString;
import net.whg.we.utils.Input;
import net.whg.we.utils.Input.TypedKey;
import net.whg.we.utils.Time;

public class TextEditor
{
	private UIString _textOut;
	private Cursor _cursor;
	private String[] _lines;
	private int _caretX;
	private int _caretY;

	public TextEditor(UIString textOut)
	{
		_textOut = textOut;
		_cursor = textOut.getCursor();

		_lines = new String[1];
		_lines[0] = "";
	}

	public void updateFrame()
	{
		boolean changedText = false;
		boolean changedCaret = false;
		for (TypedKey key : Input.getTypedKeys())
		{
			if (key.extraKey == Input.NO_KEY)
			{
				if (_textOut.getFont().getGlyph(key.key) == null)
					continue;

				_lines[_caretY] = _lines[_caretY].substring(0, _caretX) + key.key
						+ _lines[_caretY].substring(_caretX, _lines[_caretY].length());
				_caretX++;

				changedText = true;
				changedCaret = true;
			}
			else if (key.extraKey == Input.BACKSPACE_KEY)
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

					changedText = true;
					changedCaret = true;
				}
			}
			else if (key.extraKey == Input.DELETE_KEY)
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
						_lines[_caretY] = _lines[_caretY].substring(0, _caretX)
								+ _lines[_caretY].substring(_caretX + 1, _lines[_caretY].length());
					}

					changedText = true;
				}
			}
			else if (key.extraKey == Input.LEFT_KEY)
			{
				if (_caretX > 0)
				{
					_caretX--;

					changedCaret = true;
				}
				else if (_caretY > 0)
				{
					_caretY--;
					_caretX = _lines[_caretY].length();

					changedCaret = true;
				}
			}
			else if (key.extraKey == Input.RIGHT_KEY)
			{
				if (_caretX < _lines[_caretY].length())
				{
					_caretX++;

					changedCaret = true;
				}
				else if (_caretY < _lines.length - 1)
				{
					_caretX = 0;
					_caretY++;

					changedCaret = true;
				}
			}
			else if (key.extraKey == Input.HOME_KEY)
			{
				_caretX = 0;
				changedCaret = true;
			}
			else if (key.extraKey == Input.END_KEY)
			{
				_caretX = _lines[_caretY].length();
				changedCaret = true;
			}
			else if (key.extraKey == Input.UP_KEY)
			{
				if (_caretY > 0)
				{
					_caretY--;
					_caretX = Math.min(_caretX, _lines[_caretY].length());

					changedCaret = true;
				}
			}
			else if (key.extraKey == Input.DOWN_KEY)
			{
				if (_caretY < _lines.length - 1)
				{
					_caretY++;
					_caretX = Math.min(_caretX, _lines[_caretY].length());

					changedCaret = true;
				}
			}
			else if (key.extraKey == Input.ENTER_KEY)
			{
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

				changedCaret = true;
				changedText = true;
			}
		}

		if (changedText)
		{
			String s = "";

			for (int i = 0; i < _lines.length; i++)
			{
				if (i > 0)
					s += '\n';

				s += _lines[i];
			}

			_textOut.setText(s);
		}

		if (changedCaret)
			_cursor.setCaretPos(_caretX, _caretY);

		_cursor.setVisible(Time.time() % 0.666f < 0.333f);
	}
}
