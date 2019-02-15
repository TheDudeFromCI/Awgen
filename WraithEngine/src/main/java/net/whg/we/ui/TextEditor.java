package net.whg.we.ui;

import net.whg.we.ui.font.Cursor;
import net.whg.we.ui.font.UIString;
import net.whg.we.utils.Input;
import net.whg.we.utils.Input.TypedKey;
import net.whg.we.utils.Time;

public class TextEditor
{
	private UIString _text;
	private Cursor _cursor;

	public TextEditor(UIString text)
	{
		_text = text;
		_cursor = text.getCursor();
	}

	public void updateFrame()
	{
		String text = _text.getText();
		for (TypedKey key : Input.getTypedKeys())
		{
			if (key.extraKey == Input.NO_KEY)
			{
				text = text.substring(0, _cursor.getCaretX()) + key.key
						+ text.substring(_cursor.getCaretX(), text.length());
				_cursor.setCaretPos(_cursor.getCaretX() + 1, _cursor.getCaretY());
			}
			else if (key.extraKey == Input.BACKSPACE_KEY)
			{
				if (_cursor.getCaretX() > 0)
				{
					text = text.substring(0, _cursor.getCaretX() - 1)
							+ text.substring(_cursor.getCaretX(), text.length());
					_cursor.setCaretPos(_cursor.getCaretX() - 1, _cursor.getCaretY());
				}
			}
			else if (key.extraKey == Input.LEFT_KEY)
			{
				if (_cursor.getCaretX() > 0)
					_cursor.setCaretPos(_cursor.getCaretX() - 1, _cursor.getCaretY());
			}
			else if (key.extraKey == Input.RIGHT_KEY)
			{
				if (_cursor.getCaretX() < text.length())
					_cursor.setCaretPos(_cursor.getCaretX() + 1, _cursor.getCaretY());
			}
			else if (key.extraKey == Input.HOME_KEY)
			{
				_cursor.setCaretPos(0, _cursor.getCaretY());
			}
			else if (key.extraKey == Input.END_KEY)
			{
				_cursor.setCaretPos(text.length(), _cursor.getCaretY());
			}
		}
		_text.setText(text);

		_cursor.setVisible(Time.time() % 0.666f < 0.333f);
	}
}
