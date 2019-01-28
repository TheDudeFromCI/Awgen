package net.whg.we.resources.graphics;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import net.whg.we.rendering.Texture;
import net.whg.we.rendering.TextureProperties;
import net.whg.we.resources.FileLoader;
import net.whg.we.resources.Resource;
import net.whg.we.resources.ResourceDatabase;
import net.whg.we.resources.ResourceFile;
import net.whg.we.resources.ResourceLoader;
import net.whg.we.utils.Color;
import net.whg.we.utils.Log;

public class TextureLoader implements FileLoader<Texture>
{
	private static final String[] FILE_TYPES =
	{
			"png", "jpg", "gif", "bmp", "jpeg"
	};

	@Override
	public String[] getTargetFileTypes()
	{
		return FILE_TYPES;
	}

	@Override
	public Resource<Texture> loadFile(ResourceLoader resourceLoader, ResourceDatabase database,
			ResourceFile resourceFile)
	{
		try
		{
			BufferedImage image = ImageIO.read(resourceFile.getFile());

			TextureProperties properties = new TextureProperties();
			Color[] pixels = new Color[image.getWidth() * image.getHeight()];
			int[] rgb = image.getRGB(0, 0, image.getWidth(), image.getHeight(),
					new int[pixels.length], 0, image.getWidth());

			int index;
			float r, g, b, a;
			for (int y = 0; y < image.getHeight(); y++)
			{
				for (int x = 0; x < image.getWidth(); x++)
				{
					index = y * image.getWidth() + x;

					r = (rgb[index] >> 16) / 255f;
					g = (rgb[index] >> 8) / 255f;
					b = rgb[index] / 255f;
					a = (rgb[index] >> 24) / 255f;
					pixels[index] = new Color(r, g, b, a);
				}
			}

			properties.setPixels(pixels, image.getWidth(), image.getHeight());
			TextureResource resource =
					new TextureResource(properties, resourceFile, resourceFile.getName());
			database.addResource(resource);
			return resource;
		}
		catch (Exception exception)
		{
			Log.errorf("Failed to read image file, %s!", exception, resourceFile);
			return null;
		}
	}

	@Override
	public int getPriority()
	{
		return 0;
	}
}
