package net.whg.we.ui.terminal;

import net.whg.we.ui.TextEditor;
import net.whg.we.ui.Transform2D;
import net.whg.we.ui.TypedKeyInput;
import net.whg.we.ui.UIComponent;
import net.whg.we.ui.UIImage;
import net.whg.we.ui.font.UIString;
import net.whg.we.utils.Input;
import net.whg.we.utils.Time;

public class InputBar implements UIComponent
{
	private Transform2D _transform = new Transform2D();
	private UIImage _entryBar;
	private UIString _text;
	private TextEditor _textEditor;
	private boolean _disposed;

	public InputBar(UIImage entryBar, UIString text)
	{
		_entryBar = entryBar;
		_text = text;

		_textEditor = new TextEditor(_text, _text.getCursor(), _text.getTextSelection());
		_textEditor.setSingleLine(false);

		_entryBar.getTransform().setParent(_transform);
		_text.getTransform().setParent(_transform);
	}

	@Override
	public Transform2D getTransform()
	{
		return _transform;
	}

	@Override
	public void init()
	{
	}

	@Override
	public void update()
	{
	}

	@Override
	public void updateFrame()
	{
		_text.getCursor().setVisible(Time.time() % 0.666f < 0.333f);

		for (TypedKeyInput input : Input.getTypedKeys())
			_textEditor.typeCharacter(input);

		int lineCount = _textEditor.getLineCount();
		float height = lineCount * 12f + 4f;
		_entryBar.getTransform().setSize(800f, height);
		_entryBar.getTransform().setPosition(400f, height / -2f);
	}

	@Override
	public void render()
	{
		_entryBar.render();
		_text.render();
	}

	@Override
	public void dispose()
	{
		if (_disposed)
			return;

		_disposed = true;
		_text.dispose();
	}

	@Override
	public boolean isDisposed()
	{
		return _disposed;
	}
}
