package net.whg.we.client_logic.resources.scene;

import net.whg.frameworks.resource.CompilableResource;
import net.whg.frameworks.resource.ResourceFile;
import net.whg.we.client_logic.rendering.Graphics;
import net.whg.we.client_logic.rendering.Material;
import net.whg.we.client_logic.rendering.Texture;
import net.whg.we.client_logic.resources.graphics.ShaderResource;
import net.whg.we.client_logic.resources.graphics.TextureResource;

public class MaterialResource implements CompilableResource
{
    private Material _material;
    private String _name;
    private ShaderResource _shader;
    private String[] _textureParamNames;
    private TextureResource[] _textures;
    private ResourceFile _resourceFile;

    public MaterialResource(ResourceFile resourceFile, String name,
            ShaderResource shader, String[] textureParamNames,
            TextureResource[] textures)
    {
        _resourceFile = resourceFile;
        _name = name;
        _shader = shader;
        _textureParamNames = textureParamNames;
        _textures = textures;
    }

    @Override
    public Material getData()
    {
        return _material;
    }

    @Override
    public ResourceFile getResourceFile()
    {
        return _resourceFile;
    }

    @Override
    public void dispose()
    {
        _material = null;
        _shader = null;
        _textures = null;
    }

    @Override
    public void compile(Graphics graphics)
    {
        if (_material != null)
            return;

        if (!_shader.isCompiled())
            _shader.compile(graphics);

        for (TextureResource tex : _textures)
            if (!tex.isCompiled())
                tex.compile(graphics);

        Texture[] textures = new Texture[_textures.length];

        for (int i = 0; i < textures.length; i++)
            textures[i] = _textures[i].getData();

        _material = new Material(_shader.getData(), _name);
        _material.setTextures(_textureParamNames, textures);

        _shader = null;
        _textures = null;
    }

    @Override
    public String getName()
    {
        return _name;
    }

    @Override
    public boolean isCompiled()
    {
        return _material != null;
    }

    @Override
    public String toString()
    {
        return String.format("[MaterialResource: %s at %s]", _name,
                _resourceFile);
    }
}
