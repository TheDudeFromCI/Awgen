package net.whg.we.resource;

import java.io.File;
import net.whg.frameworks.logging.Log;
import net.whg.frameworks.resource.ResourceData;
import net.whg.frameworks.resource.ResourceFuture;
import net.whg.we.client_logic.rendering.VertexData;

public class MeshFuture implements ResourceFuture
{
	private Object LOCK = new Object();

	private File _file;
	private int _loadState;
	private VertexData _vertexData;

	public MeshFuture(File file)
	{
		_file = file;
		_loadState = ResourceFuture.NO_CHANGE;
	}

	@Override
	public void run()
	{
		try
		{
			UncompiledMesh mesh = MeshSaver.load(_file);
			_vertexData = mesh.vertexData;

			synchronized (LOCK)
			{
				_loadState = ResourceFuture.FULLY_LOADED;
			}
		}
		catch (Exception e)
		{
			Log.errorf("Failed to load mesh file at %s!", e, _file);

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

			MeshData meshData = (MeshData) data;
			meshData.setVertexData(_vertexData);

			return ResourceFuture.FULLY_LOADED;
		}
	}
}
