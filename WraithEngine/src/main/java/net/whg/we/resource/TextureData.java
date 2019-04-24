package net.whg.we.resource;

import net.whg.frameworks.resource.ResourceData;
import net.whg.we.client_logic.rendering.Graphics;
import net.whg.we.client_logic.rendering.VTexture;

public class TextureData implements ResourceData
{
	private TextureProperties _textureProperties;
	private VTexture _vTexture;

	public TextureData(Graphics graphics)
	{
		emptyTexture();
		_vTexture = graphics.prepareTexture(_textureProperties);
	}

	public TextureData(Graphics graphics, TextureProperties textureProperties)
	{
		_textureProperties = textureProperties;
		_vTexture = graphics.prepareTexture(_textureProperties);
	}

	private void emptyTexture()
	{
		_textureProperties = new TextureProperties();
		_textureProperties.mipmapping = false;
		_textureProperties.colorData = new IntRGBAColorData(4, 4);
	}

	@Override
	public void dispose()
	{
		_vTexture.dispose();
	}

	public TextureProperties getTextureProperties()
	{
		return _textureProperties;
	}

	public void bind(int textureSlot)
	{
		_vTexture.bind(textureSlot);
	}

	public void setTextureProperties(TextureProperties textureProperties)
	{
		_textureProperties = textureProperties;

		if (_textureProperties == null)
			emptyTexture();

		updateVTexture();
	}

	public void updateVTexture()
	{
		_vTexture.recompile(_textureProperties);
	}
}
