package net.whg.we.resource;

import net.whg.frameworks.resource.ResourceData;
import net.whg.we.client_logic.rendering.Graphics;
import net.whg.we.client_logic.rendering.VTexture;

public class TextureData implements ResourceData
{
	private UncompiledTexture _data;
	private VTexture _vTexture;

	public TextureData(Graphics graphics)
	{
		emptyTexture();
		_vTexture = graphics.prepareTexture(_data);
	}

	public TextureData(Graphics graphics, UncompiledTexture data)
	{
		_data = data;
		_vTexture = graphics.prepareTexture(_data);
	}

	private void emptyTexture()
	{
		_data = new UncompiledTexture();
	}

	@Override
	public void dispose()
	{
		_vTexture.dispose();
	}

	public UncompiledTexture get()
	{
		return _data;
	}

	public void bind(int textureSlot)
	{
		_vTexture.bind(textureSlot);
	}

	public void set(UncompiledTexture texture)
	{
		_data = texture;

		if (_data == null)
			emptyTexture();

		updateVTexture();
	}

	public void updateVTexture()
	{
		_vTexture.recompile(_data);
	}
}
