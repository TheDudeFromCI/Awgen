package net.whg.we.ui.terminal;

import org.joml.Vector2f;
import net.whg.we.ui.UIImage;
import net.whg.we.ui.font.UIString;
import net.whg.we.utils.Input;
import net.whg.we.utils.Input.TypedKey;
import net.whg.we.utils.Time;

public class TextEditor
{
	private UIString _text;
	private UIImage _cursor;
	private int _cursorPos;

	public TextEditor(UIString text, UIImage cursor)
	{
		_text = text;
		_cursor = cursor;
	}

	public void updateFrame()
	{
		_cursor.setVisible(Time.time() % 1f < 0.5f);

		Vector2f textPos = _text.getTransform().getPosition();
		Vector2f cursorPos = _cursor.getTransform().getPosition();
		float charWidth = _text.getMonoWidth();
		_cursor.getTransform().setPosition(textPos.x + _cursorPos * charWidth, cursorPos.y);

		String text = _text.getText();
		for (TypedKey key : Input.getTypedKeys())
		{
			if (key.extraKey == Input.NO_KEY)
			{
				text = text.substring(0, _cursorPos) + key.key
						+ text.substring(_cursorPos, text.length());
				_cursorPos++;
			}
			else if (key.extraKey == Input.BACKSPACE_KEY)
			{
				if (_cursorPos > 0)
				{
					text = text.substring(0, _cursorPos - 1)
							+ text.substring(_cursorPos, text.length());
					_cursorPos--;
				}
			}
		}
		_text.setText(text);
	}
}
