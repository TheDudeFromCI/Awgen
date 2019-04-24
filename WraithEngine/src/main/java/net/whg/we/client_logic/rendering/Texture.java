package net.whg.we.client_logic.rendering;

import net.whg.we.resource.TextureProperties;

public class Texture
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

    public TextureProperties getProperties()
    {
        return _properties;
    }
}
