package net.whg.we.resource;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import net.whg.frameworks.logging.Log;
import net.whg.frameworks.resource.Resource;
import net.whg.frameworks.resource.ResourceData;
import net.whg.frameworks.resource.ResourceFile;
import net.whg.frameworks.resource.ResourceFuture;
import net.whg.frameworks.resource.ResourceManager;
import net.whg.we.client_logic.rendering.Graphics;

public class TextureConverterFuture implements ResourceFuture
{
	private Object LOCK = new Object();

	private Graphics _graphics;
	private File _file;
	private int _loadState;
	private UncompiledTexture _texture;
	private ResourceManager _resourceManager;
	private String _destinationFolder;

	public TextureConverterFuture(Graphics graphics,
			ResourceManager resourceManager, File file,
			String destinationFolder)
	{
		_graphics = graphics;
		_file = file;
		_loadState = ResourceFuture.NO_CHANGE;
		_resourceManager = resourceManager;
		_destinationFolder = destinationFolder;
	}

	@Override
	public void run()
	{
		try
		{
			BufferedImage image = ImageIO.read(_file);

			_texture = new UncompiledTexture();

			IntRGBAColorData colorData =
					new IntRGBAColorData(image.getWidth(), image.getHeight());
			image.getRGB(0, 0, image.getWidth(), image.getHeight(),
					colorData.getAsIntArray(), 0, image.getWidth());
			_texture.colorData = colorData;

			_texture.path = new ResourceFile(_destinationFolder + "/"
					+ _texture.name + ".asset_texture");

			TextureSaver.save(_texture,
					_resourceManager.getFile(_texture.path));

			synchronized (LOCK)
			{
				_loadState = ResourceFuture.FULLY_LOADED;
			}
		}
		catch (Exception e)
		{
			Log.errorf("Failed to convert texture file at %s!", e, _file);

			synchronized (LOCK)
			{
				_loadState = ResourceFuture.UNABLE_TO_LOAD;
			}
		}
	}

	@Override
	public int sync(ResourceData data)
	{
		synchronized (LOCK)
		{
			if (_loadState != ResourceFuture.FULLY_LOADED)
				return _loadState;

			TextureData textureData = new TextureData(_graphics, _texture);
			Resource resource = new Resource(_texture.path, textureData);
			_resourceManager.getResourceDatabase().addResource(resource);

			ResourceFile[] resourceFiles = new ResourceFile[]
			{
				_texture.path
			};

			ConverterData converterData = (ConverterData) data;
			converterData.setLoadedResourceFiles(resourceFiles);

			return ResourceFuture.FULLY_LOADED;
		}
	}
}
