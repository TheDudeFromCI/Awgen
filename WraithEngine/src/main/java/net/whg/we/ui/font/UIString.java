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

	public UIString(Font font, String text, Graphics graphics, Material material)
	{
		super(new Mesh("TextMesh", UIUtils.textVertexData(font, text), graphics), material);

		_font = font;
		_text = text;

		getTransform().setSize(_size, _size);
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

		getMesh().rebuild(UIUtils.textVertexData(_font, text));
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
}
