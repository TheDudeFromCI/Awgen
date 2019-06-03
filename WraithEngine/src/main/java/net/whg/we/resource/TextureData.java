package net.whg.we.resource;

import java.util.UUID;
import net.whg.frameworks.resource.ResourceData;
import net.whg.we.client_logic.rendering.Graphics;
import net.whg.we.client_logic.rendering.VTexture;

public class TextureData implements ResourceData
{
	private UncompiledTexture _data;
	private VTexture _vTexture;
	private UUID _uuid;

	public TextureData(Graphics graphics, UUID uuid)
	{
		emptyTexture();
		_vTexture = graphics.prepareTexture(_data);
		_uuid = uuid;
	}

	public TextureData(Graphics graphics, UncompiledTexture data, UUID uuid)
	{
		_data = data;
		_vTexture = graphics.prepareTexture(_data);
		_uuid = uuid;
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

	@Override
	public UUID getUUID()
	{
		return _uuid;
	}

	public TextureColorData getColorData()
	{
		return _data.colorData;
	}
}
