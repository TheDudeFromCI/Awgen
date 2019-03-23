package net.whg.we.client_logic.rendering;

import net.whg.we.utils.Color;

public class TextureProperties
{
    private String _name;
    private boolean _mipmapping = true;
    private boolean _colorData = true;
    private TextureSampleMode _sampleMode = TextureSampleMode.BILINEAR;
    private GraphicsStreamingMode _streamMode = GraphicsStreamingMode.STATIC;
    private NormalMapType _normalSampling = NormalMapType.NON_NORMALMAP;
    private int _textureWidth;
    private int _textureHeight;
    private Color[] _pixels;
    private int[] _pixelsInt;

    public String getName()
    {
        return _name;
    }

    public void setName(String name)
    {
        _name = name;
    }

    public boolean hasMipmapping()
    {
        return _mipmapping;
    }

    public void setMipmapping(boolean mipmapping)
    {
        _mipmapping = mipmapping;
    }

    public boolean isColorData()
    {
        return _colorData;
    }

    public void setColorData(boolean colorData)
    {
        _colorData = colorData;
    }

    public TextureSampleMode getSampleMode()
    {
        return _sampleMode;
    }

    public void setSampleMode(TextureSampleMode sampleMode)
    {
        _sampleMode = sampleMode;
    }

    public GraphicsStreamingMode getStreamingMode()
    {
        return _streamMode;
    }

    public void setStreamingMode(GraphicsStreamingMode streamMode)
    {
        _streamMode = streamMode;
    }

    public NormalMapType getNormalMappingType()
    {
        return _normalSampling;
    }

    public void setNormalMappingType(NormalMapType normalSampling)
    {
        _normalSampling = normalSampling;
    }

    public int getWidth()
    {
        return _textureWidth;
    }

    public int getHeight()
    {
        return _textureHeight;
    }

    public Color[] getPixels()
    {
        return _pixels;
    }

    public int[] getPixelsInt()
    {
        return _pixelsInt;
    }

    public void setPixels(Color[] pixels, int width, int height)
    {
        _pixels = pixels;
        _pixelsInt = null;

        _textureWidth = width;
        _textureHeight = height;
    }

    public void setPixels(int[] pixels, int width, int height)
    {
        _pixelsInt = pixels;
        _pixels = null;

        _textureWidth = width;
        _textureHeight = height;
    }
}
