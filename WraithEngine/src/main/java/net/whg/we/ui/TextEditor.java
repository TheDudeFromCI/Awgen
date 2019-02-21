package net.whg.we.ui;

import net.whg.we.ui.font.Cursor;
import net.whg.we.ui.font.TextSelection;

public class TextEditor
{
	private TextProcessor _processor;
	private TextHolder _holder;
	private Cursor _cursor;
	private TextSelection _selection;
	private boolean _singleLine;

	public TextEditor(TextHolder holder, Cursor cursor, TextSelection selection)
	{
		_holder = holder;
		_cursor = cursor;
		_selection = selection;
		_processor = new TextProcessor();
	}

	public void setSingleLine(boolean singleLine)
	{
		_singleLine = singleLine;
	}

	public boolean getSingleLine()
	{
		return _singleLine;
	}

	public boolean validChar(char c)
	{
		return _holder.getFont().getGlyph(c) != null;
	}

	public void deleteSelection()
	{
		if (!_selection.hasSelection())
			return;

		int startLine = _processor.getLineByIndex(_selection.selStart());
		int startPos = _processor.getPositionByIndex(_selection.selStart());
		int count = _selection.selEnd() - _selection.selStart() + 1;

		_processor.deleteInlcudeLines(startLine, startPos, count);
		_selection.clearSelection();
		curXY(startPos, startLine);
	}

	private void startSelection(int origin)
	{
		_selection.setSelection(origin, origin, origin);
	}

	private int curX()
	{
		return _cursor.getCaretX();
	}

	private int curY()
	{
		return _cursor.getCaretY();
	}

	private int selO()
	{
		return _selection.selOrigin();
	}

	private void curX(int x)
	{
		_cursor.setCaretPos(x, _cursor.getCaretY());
	}

	private void curY(int y)
	{
		_cursor.setCaretPos(_cursor.getCaretX(), y);
	}

	private void curXY(int x, int y)
	{
		_cursor.setCaretPos(x, y);
	}

	private int len(int line)
	{
		return _processor.getLineLength(line);
	}

	private boolean insert()
	{
		return _cursor.getInsertMode();
	}

	private boolean hasSel()
	{
		return _selection.hasSelection();
	}

	private void checkSelectionStart(boolean shift)
	{
		if (shift && !hasSel())
			startSelection(_processor.caretPosition(curY(), curX()));
	}

	private void checkSelectionEnd(boolean shift)
	{
		if (shift)
		{
			int car = _processor.caretPosition(curY(), curX());
			_selection.setSelection(Math.min(selO(), car), selO(), Math.max(selO(), car));
		}
		else
			_selection.clearSelection();
	}

	public void typeCharacter(TypedKeyInput input)
	{
		switch (input.extraKey)
		{
			case TypedKeyInput.NO_KEY:
			{
				if (!validChar(input.key))
					return;

				if (insert() && !hasSel() && curX() < len(curY()))
				{
					_processor.deleteInlcudeLines(curY(), curX(), 1);
					_processor.insert(curY(), curX(), Character.toString(input.key));
					curX(curX() + 1);
				}
				else
				{
					if (hasSel())
						deleteSelection();

					_processor.insert(curY(), curX(), Character.toString(input.key));
					curX(curX() + 1);
				}

				updateText();
				break;
			}

			case TypedKeyInput.BACKSPACE_KEY:
			{
				if (!hasSel())
				{
					if (curX() > 0 || curY() > 0)
					{
						if (curX() == 0)
							curXY(len(curY() - 1), curY() - 1);
						else
							curX(curX() - 1);

						_processor.deleteInlcudeLines(curY(), curX(), 1);
						updateText();
					}
				}
				else
				{
					deleteSelection();
					updateText();
				}
				break;
			}

			case TypedKeyInput.DELETE_KEY:
			{
				if (!hasSel())
				{
					_processor.deleteInlcudeLines(curY(), curX(), 1);
					updateText();
				}
				else
				{
					deleteSelection();
					updateText();
				}
				break;
			}

			case TypedKeyInput.LEFT_KEY:
			{
				checkSelectionStart(input.shift);

				if (curX() > 0)
					_cursor.setCaretPos(curX() - 1, curY());
				else if (curY() > 0)
					curXY(len(curY() - 1), curY() - 1);

				checkSelectionEnd(input.shift);
				break;
			}

			case TypedKeyInput.RIGHT_KEY:
			{
				checkSelectionStart(input.shift);

				if (curX() < len(curY()))
					curX(curX() + 1);
				else if (curY() < _processor.getLineCount() - 1)
					curXY(0, curY() + 1);

				checkSelectionEnd(input.shift);
				break;
			}

			case TypedKeyInput.HOME_KEY:
			{
				checkSelectionStart(input.shift);

				curX(0);

				checkSelectionEnd(input.shift);
				break;
			}

			case TypedKeyInput.END_KEY:
			{
				checkSelectionStart(input.shift);

				curX(len(curY()));

				checkSelectionEnd(input.shift);
				break;
			}

			case TypedKeyInput.UP_KEY:
			{
				checkSelectionStart(input.shift);

				if (curY() > 0)
				{
					curY(curY() - 1);
					curX(Math.min(curX(), len(curY())));
				}

				checkSelectionEnd(input.shift);
				break;
			}

			case TypedKeyInput.DOWN_KEY:
			{
				checkSelectionStart(input.shift);

				if (curY() < _processor.getLineCount())
				{
					curY(curY() + 1);
					curX(Math.min(curX(), len(curY())));
				}

				checkSelectionEnd(input.shift);
				break;
			}

			case TypedKeyInput.ENTER_KEY:
			{
				if (!_singleLine)
				{
					if (hasSel())
						deleteSelection();

					_processor.insert(curY(), curX(), "\n");
					curXY(0, curY() + 1);

					updateText();
				}

				break;
			}

			case TypedKeyInput.INSERT_KEY:
			{
				_cursor.setInsertMode(!_cursor.getInsertMode());
				break;
			}
		}
	}

	private void updateText()
	{
		_holder.setText(_processor.toString());
	}

	public TextHolder getTextHolder()
	{
		return _holder;
	}

	public Cursor getCursor()
	{
		return _cursor;
	}

	public TextSelection getSelection()
	{
		return _selection;
	}

	public int getLineCount()
	{
		return _processor.getLineCount();
	}

	public int lineLength(int line)
	{
		return _processor.getLineLength(line);
	}

	public void clear()
	{
		_processor.set("");
		updateText();
		_selection.clearSelection();
	}
}
