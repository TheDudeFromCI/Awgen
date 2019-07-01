package net.whg.we.resource;

import java.io.File;
import net.whg.frameworks.logging.Log;
import net.whg.frameworks.resource.ResourceData;
import net.whg.frameworks.resource.ResourceFuture;

public class TextureFuture implements ResourceFuture
{
	private Object LOCK = new Object();

	private File _file;
	private int _loadState;
	private UncompiledTexture _texture;

	public TextureFuture(File file)
	{
		_file = file;
		_loadState = ResourceFuture.NO_CHANGE;
	}

	@Override
	public void run()
	{
		try
		{
			_texture = TextureSaver.load(_file);

			synchronized (LOCK)
			{
				_loadState = ResourceFuture.FULLY_LOADED;
			}
		}
		catch (Exception e)
		{
			Log.errorf("Failed to load texture file at %s!", e, _file);

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

			TextureData textureData = (TextureData) data;
			textureData.set(_texture);

			return ResourceFuture.FULLY_LOADED;
		}
	}
}
