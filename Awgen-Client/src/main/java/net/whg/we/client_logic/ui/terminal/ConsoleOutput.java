package net.whg.we.client_logic.ui.terminal;

import java.util.UUID;
import org.joml.Matrix4f;
import net.whg.frameworks.command.Console;
import net.whg.we.client_logic.rendering.Graphics;
import net.whg.we.client_logic.rendering.Material;
import net.whg.we.client_logic.ui.UIComponent;
import net.whg.we.client_logic.ui.UIUtils;
import net.whg.we.client_logic.ui.font.Font;
import net.whg.we.legacy.Transform2D;
import net.whg.we.resource.MeshData;

public class ConsoleOutput implements UIComponent
{
	public static final int SHOWN_ROWS = 25;

	private MeshData[] _lines = new MeshData[Console.LINE_COUNT];
	private Transform2D _transform = new Transform2D();

	private Matrix4f _lineBuffer = new Matrix4f();
	private Font _font;
	private Graphics _graphics;
	private Material _material;
	private boolean _disposed;
	private int _scrollPos;

	public ConsoleOutput(Font font, Graphics graphics, Material material)
	{
		_font = font;
		_graphics = graphics;
		_material = material;
	}

	public void setLine(int lineIndex, String text)
	{
		if (_lines[lineIndex] == null)
			_lines[lineIndex] = new MeshData(_graphics, UUID.randomUUID());

		_lines[lineIndex].setVertexData(UIUtils.textVertexData(_font, text));
	}

	public void setScroll(int scrollPos)
	{
		_scrollPos = scrollPos;
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

	@Override
	public void render()
	{
		_material.bind();

		for (int i = 0; i < SHOWN_ROWS; i++)
		{
			int line = (_scrollPos - i + Console.LINE_COUNT) % Console.LINE_COUNT;

			if (_lines[line] == null)
				continue;

			_lineBuffer.set(_transform.getFullMatrixFast());
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

		for (int i = 0; i < Console.LINE_COUNT; i++)
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