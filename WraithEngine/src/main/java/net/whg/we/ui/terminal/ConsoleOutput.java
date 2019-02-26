package net.whg.we.ui.terminal;

import org.joml.Matrix4f;
import net.whg.we.rendering.Graphics;
import net.whg.we.rendering.Material;
import net.whg.we.rendering.Mesh;
import net.whg.we.ui.Transform2D;
import net.whg.we.ui.UIComponent;
import net.whg.we.ui.UIUtils;
import net.whg.we.ui.font.Font;

public class ConsoleOutput implements UIComponent
{
	public static final int CONSOLE_LINE_BUFFER_COUNT = 500;
	public static final int SHOWN_ROWS = 25;

	private Mesh[] _lines = new Mesh[CONSOLE_LINE_BUFFER_COUNT];
	private Transform2D _transform = new Transform2D();
	private Matrix4f _lineBuffer = new Matrix4f();
	private Font _font;
	private Graphics _graphics;
	private Material _material;
	private int _scrollPos = CONSOLE_LINE_BUFFER_COUNT - 1;
	private boolean _disposed;

	public ConsoleOutput(Font font, Graphics graphics, Material material)
	{
		_font = font;
		_graphics = graphics;
		_material = material;
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
	}

	public void append(String text)
	{
		if (text == null)
			text = "";

		int lineStart = 0;
		for (int i = 0; i < text.length(); i++)
		{
			if (text.charAt(i) != '\n')
				continue;

			appendLine(text.substring(lineStart, i));
			lineStart = i + 1;
		}

		appendLine(text.substring(lineStart, text.length()));
	}

	private void appendLine(String text)
	{
		_scrollPos = (_scrollPos + 1) % CONSOLE_LINE_BUFFER_COUNT;
		int line = (_scrollPos - 1 + CONSOLE_LINE_BUFFER_COUNT) % CONSOLE_LINE_BUFFER_COUNT;

		if (_lines[line] == null)
			_lines[line] = new Mesh("", UIUtils.textVertexData(_font, text), _graphics);
		else
			_lines[line].rebuild(UIUtils.textVertexData(_font, text));
	}

	@Override
	public void render()
	{
		_material.bind();

		for (int i = 0; i < SHOWN_ROWS; i++)
		{
			int line = (_scrollPos - i + CONSOLE_LINE_BUFFER_COUNT) % CONSOLE_LINE_BUFFER_COUNT;

			if (_lines[line] == null)
				continue;

			_lineBuffer.set(_transform.getFullMatrix());
			_lineBuffer.translate(0f, -12f * (SHOWN_ROWS - i - 1), 0f);
			_lineBuffer.scale(12f);
			_material.setOrthoMVP(_lineBuffer);

			_lines[line].render();
		}
	}

	@Override
	public void dispose()
	{
		_disposed = true;

		for (int i = 0; i < CONSOLE_LINE_BUFFER_COUNT; i++)
		{
			if (_lines[i] == null)
				continue;
			_lines[i].dispose();
			_lines[i] = null;
		}
	}

	@Override
	public boolean isDisposed()
	{
		return _disposed;
	}
}
