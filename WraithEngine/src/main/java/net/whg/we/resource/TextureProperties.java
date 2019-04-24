package net.whg.we.resource;

import net.whg.we.client_logic.rendering.GraphicsStreamingMode;
import net.whg.we.client_logic.rendering.NormalMapType;
import net.whg.we.client_logic.rendering.TextureSampleMode;

public class TextureProperties
{
	public boolean mipmapping = true;
	public TextureSampleMode sampleMode = TextureSampleMode.BILINEAR;
	public GraphicsStreamingMode streamMode = GraphicsStreamingMode.STATIC;
	public NormalMapType normalSampling = NormalMapType.NON_NORMALMAP;
	public TextureColorData colorData;
}
