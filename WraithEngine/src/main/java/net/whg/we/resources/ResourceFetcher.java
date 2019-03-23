package net.whg.we.resources;

import net.whg.we.main.Plugin;
import net.whg.we.client_logic.rendering.Graphics;
import net.whg.we.client_logic.rendering.Material;
import net.whg.we.client_logic.rendering.Mesh;
import net.whg.we.client_logic.rendering.Shader;
import net.whg.we.client_logic.rendering.Texture;
import net.whg.we.resources.graphics.MeshResource;
import net.whg.we.resources.graphics.ShaderResource;
import net.whg.we.resources.graphics.TextureResource;
import net.whg.we.resources.scene.FontResource;
import net.whg.we.resources.scene.MaterialResource;
import net.whg.we.resources.scene.ModelResource;
import net.whg.we.scene.Model;
import net.whg.we.ui.font.Font;

public class ResourceFetcher
{
    private ResourceManager _resourceManager;
    private Graphics _graphics;

    public ResourceFetcher(ResourceManager resourceManager, Graphics graphics)
    {
        _resourceManager = resourceManager;
        _graphics = graphics;
    }

    public ResourceManager getResourceManager()
    {
        return _resourceManager;
    }

    public Graphics getGraphics()
    {
        return _graphics;
    }

    public Mesh getMesh(ResourceFile resourceFile)
    {
        MeshResource meshRes =
                (MeshResource) _resourceManager.loadResource(resourceFile);
        meshRes.compile(_graphics);
        return meshRes.getData();
    }

    public Mesh getMesh(Plugin plugin, String pathname)
    {
        MeshResource meshRes =
                (MeshResource) _resourceManager.loadResource(plugin, pathname);
        meshRes.compile(_graphics);
        return meshRes.getData();
    }

    public Shader getShader(ResourceFile resourceFile)
    {
        ShaderResource shaderRes =
                (ShaderResource) _resourceManager.loadResource(resourceFile);
        shaderRes.compile(_graphics);
        return shaderRes.getData();
    }

    public Shader getShader(Plugin plugin, String pathname)
    {
        ShaderResource shaderRes = (ShaderResource) _resourceManager
                .loadResource(plugin, pathname);
        shaderRes.compile(_graphics);
        return shaderRes.getData();
    }

    public Texture getTexture(ResourceFile resourceFile)
    {
        TextureResource texRes =
                (TextureResource) _resourceManager.loadResource(resourceFile);
        texRes.compile(_graphics);
        return texRes.getData();
    }

    public Texture getTexture(Plugin plugin, String pathname)
    {
        TextureResource texRes = (TextureResource) _resourceManager
                .loadResource(plugin, pathname);
        texRes.compile(_graphics);
        return texRes.getData();
    }

    public Font getFont(ResourceFile resourceFile)
    {
        FontResource fontRes =
                (FontResource) _resourceManager.loadResource(resourceFile);
        return fontRes.getData();
    }

    public Font getFont(Plugin plugin, String pathname)
    {
        FontResource fontRes =
                (FontResource) _resourceManager.loadResource(plugin, pathname);
        return fontRes.getData();
    }

    public Material getMaterial(ResourceFile resourceFile)
    {
        MaterialResource matRes =
                (MaterialResource) _resourceManager.loadResource(resourceFile);
        matRes.compile(_graphics);
        return matRes.getData();
    }

    public Material getMaterial(Plugin plugin, String pathname)
    {
        MaterialResource matRes = (MaterialResource) _resourceManager
                .loadResource(plugin, pathname);
        matRes.compile(_graphics);
        return matRes.getData();
    }

    public Model getModel(ResourceFile resourceFile)
    {
        ModelResource modelRes =
                (ModelResource) _resourceManager.loadResource(resourceFile);
        modelRes.compile(_graphics);
        return modelRes.getData();
    }

    public Model getModel(Plugin plugin, String pathname)
    {
        ModelResource modelRes =
                (ModelResource) _resourceManager.loadResource(plugin, pathname);
        modelRes.compile(_graphics);
        return modelRes.getData();
    }
}
