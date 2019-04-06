package net.whg.we.client_logic.resources.graphics;

import net.whg.frameworks.resource.CompilableResource;
import net.whg.frameworks.resource.ResourceFile;
import net.whg.we.client_logic.rendering.Graphics;
import net.whg.we.client_logic.rendering.Texture;
import net.whg.we.client_logic.rendering.TextureProperties;

public class TextureResource implements CompilableResource
{
    private String _name;
    private Texture _texture;
    private TextureProperties _properties;
    private ResourceFile _resourceFile;

    TextureResource(ResourceFile resourceFile, String name,
            TextureProperties properties)
    {
        _resourceFile = resourceFile;
        _name = name;
        _properties = properties;
    }

    @Override
    public Texture getData()
    {
        return _texture;
    }

    @Override
    public boolean isCompiled()
    {
        return _texture != null;
    }

    @Override
    public void compile(Graphics graphics)
    {
        if (_texture != null)
            return;

        _texture =
                new Texture(graphics.prepareTexture(_properties), _properties);

        _properties = null;
    }

    @Override
    public ResourceFile getResourceFile()
    {
        return _resourceFile;
    }

    @Override
    public void dispose()
    {
        _properties = null;

        if (_texture != null)
        {
            _texture.dispose();
            _texture = null;
        }
    }

    @Override
    public String getName()
    {
        return _name;
    }

    @Override
    public String toString()
    {
        return String.format("[TextureResource: %s at %s]", _name,
                _resourceFile);
    }
}
