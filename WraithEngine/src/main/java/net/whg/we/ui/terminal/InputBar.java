package net.whg.we.ui.terminal;

import net.whg.we.ui.Transform2D;
import net.whg.we.ui.UIComponent;
import net.whg.we.ui.UIImage;
import net.whg.we.ui.font.UIString;

public class InputBar implements UIComponent
{
	private Transform2D _transform = new Transform2D();
	private UIImage _entryBar;
	private UIImage _cursor;
	private UIString _text;
	private TextEditor _textEditor;
	private boolean _disposed;

	public InputBar(UIImage entryBar, UIString text, UIImage cursor)
	{
		_entryBar = entryBar;
		_text = text;
		_cursor = cursor;

		_textEditor = new TextEditor(_text, cursor);

		_entryBar.getTransform().setParent(_transform);
		_cursor.getTransform().setParent(_transform);
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
		_textEditor.updateFrame();
	}

	@Override
	public void render()
	{
		_entryBar.render();
		_text.render();
		_cursor.render();
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
