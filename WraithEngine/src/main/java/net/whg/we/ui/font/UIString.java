package net.whg.we.ui.font;

import net.whg.we.rendering.Graphics;
import net.whg.we.rendering.Material;
import net.whg.we.rendering.Mesh;
import net.whg.we.ui.UIImage;
import net.whg.we.ui.UIUtils;

public class UIString extends UIImage
{
	private Font _font;
	private String _text;
	private float _size = 12f;
	private UICursor _cursor;
	private UISelection _selection;

	public UIString(Font font, String text, Graphics graphics, Material material, Mesh quadMesh,
			Material cursorMat, Material selMat)
	{
		super(new Mesh("TextMesh", UIUtils.textVertexData(font, text), graphics), material);

		_font = font;
		_text = text;

		getTransform().setSize(_size, _size);

		_cursor = new UICursor(quadMesh, cursorMat, this);
		_cursor.getTransform().setParent(getTransform());

		_selection = new UISelection(quadMesh, selMat, this);
	}

	public String getText()
	{
		return _text;
	}

	public void setText(String text)
	{
		if (_text.equals(text))
			return;

		_text = text;

		getMesh().rebuild(UIUtils.textVertexData(_font, _text));
		_cursor.updateCaretPos();
		_selection.updateSelectionPos();
	}

	public float getFontSize()
	{
		return _size;
	}

	public void setFontSize(float size)
	{
		_size = size;
		getTransform().setSize(_size, _size);
	}

	@Override
	public void dispose()
	{
		getMesh().dispose();
		super.dispose();
	}

	@Override
	public void render()
	{
		super.render();
		_cursor.render();
		_selection.render();
	}

	public Font getFont()
	{
		return _font;
	}

	public float getMonoWidth(boolean normalized)
	{
		if (normalized)
			return _font.getGlyphs()[0].getWidth();
		return _font.getGlyphs()[0].getWidth() * _size;
	}

	public Cursor getCursor()
	{
		return _cursor;
	}

	public TextSelection getTextSelection()
	{
		return _selection;
	}
}
