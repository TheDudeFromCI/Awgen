package net.whg.we.client_logic.rendering;

import net.whg.we.resource.UncompiledTexture;

public class Texture
{
	private UncompiledTexture _texture;
	private VTexture _textureRaw;
	private boolean _disposed;

	public Texture(VTexture textureRaw, UncompiledTexture texture)
	{
		_textureRaw = textureRaw;
		_texture = texture;
	}

	public void bind(int textureSlot)
	{
		if (_disposed)
			throw new IllegalStateException("Texture already disposed!");

		_textureRaw.bind(textureSlot);
	}

	public void dispose()
	{
		if (_disposed)
			return;

		_disposed = true;
		_textureRaw.dispose();
	}

	public boolean isDisposed()
	{
		return _disposed;
	}

	public UncompiledTexture getProperties()
	{
		return _texture;
	}
}
