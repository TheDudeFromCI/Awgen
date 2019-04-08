package net.whg.we.client_logic.resources.graphics;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import net.whg.frameworks.logging.Log;
import net.whg.frameworks.resource.FileLoader;
import net.whg.frameworks.resource.ResourceFile;
import net.whg.frameworks.resource.ResourceManager;
import net.whg.we.client_logic.rendering.TextureProperties;

public class TextureLoader implements FileLoader
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
	public TextureResource loadFile(ResourceManager resourceManager, ResourceFile resourceFile)
	{
		try
		{
			BufferedImage image = ImageIO.read(resourceManager.getFile(resourceFile));

			TextureProperties properties = new TextureProperties();

			int[] rgb = image.getRGB(0, 0, image.getWidth(), image.getHeight(),
					new int[image.getWidth() * image.getHeight()], 0, image.getWidth());

			properties.setPixels(rgb, image.getWidth(), image.getHeight());
			TextureResource resource =
					new TextureResource(resourceFile, resourceFile.getName(), properties);
			resourceManager.getResourceDatabase().addResource(resource);

			Log.debugf("Successfully loaded texture resource, %s.", resource);
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
