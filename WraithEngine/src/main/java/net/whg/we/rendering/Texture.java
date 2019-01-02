package net.whg.we.rendering;

import net.whg.we.resources.DisposableResource;

public class Texture implements DisposableResource
{
	private TextureProperties _properties;
	private VTexture _textureRaw;
	private boolean _disposed;

	public Texture(VTexture textureRaw, TextureProperties properties)
	{
		_textureRaw = textureRaw;
		_properties = properties;
	}

	public void bind(int textureSlot)
	{
		if (_disposed)
			throw new IllegalStateException("Texture already disposed!");

		_textureRaw.bind(textureSlot);
	}

	@Override
	public void dispose()
	{
		if (_disposed)
			return;

		_disposed = true;
		_textureRaw.dispose();
	}

	@Override
	public boolean isDisposed()
	{
		return _disposed;
	}

	public TextureProperties getProperties()
	{
		return _properties;
	}
}
