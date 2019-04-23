package net.whg.we.resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import net.whg.frameworks.logging.Log;
import net.whg.frameworks.resource.Resource;
import net.whg.frameworks.resource.ResourceData;
import net.whg.frameworks.resource.ResourceFile;
import net.whg.frameworks.resource.ResourceFuture;
import net.whg.frameworks.resource.ResourceManager;
import net.whg.we.client_logic.rendering.Graphics;

public class ShaderConverterFuture implements ResourceFuture
{
	private Object LOCK = new Object();

	private Graphics _graphics;
	private File _file;
	private int _loadState;
	private UncompiledShader _shader;
	private ResourceManager _resourceManager;
	private String _destinationFolder;

	public ShaderConverterFuture(Graphics graphics,
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
		try (BufferedReader in = new BufferedReader(new FileReader(_file)))
		{
			_shader = new UncompiledShader();
			_shader.name = _file.getName();
			_shader.name = _shader.name.substring(0, _shader.name.length() - 5);

			StringBuilder vertShader = new StringBuilder();
			StringBuilder geoShader = new StringBuilder();
			StringBuilder fragShader = new StringBuilder();

			// Mode is the state of the loader.
			// 0 = Loading Vertex Shader
			// 1 = Loading Geometetry Shader
			// 2 = Loading Fragment Shader
			int mode = 0;

			String line;
			while ((line = in.readLine()) != null)
			{
				if (line.equals("---"))
				{
					mode++;

					if (mode == 3)
						throw new RuntimeException(
								"Unable to parse shader file format! Too many states defined.");
				}
				else if (mode == 0)
					vertShader.append(line).append("\n");
				else if (mode == 1)
					geoShader.append(line).append("\n");
				else
					fragShader.append(line).append("\n");
			}

			if (mode != 2)
				throw new RuntimeException(
						"Unable to parse shader file format! Incorrect number of states defined.");

			if (vertShader.length() > 0)
				_shader.vertShader = vertShader.toString();

			if (geoShader.length() > 0)
				_shader.geoShader = geoShader.toString();

			if (fragShader.length() > 0)
				_shader.fragShader = fragShader.toString();

			_shader.path = new ResourceFile(
					_destinationFolder + "/" + _shader.name + ".asset_mesh");

			ShaderSaver.save(_shader, _resourceManager.getFile(_shader.path));

			synchronized (LOCK)
			{
				_loadState = ResourceFuture.FULLY_LOADED;
			}
		}
		catch (Exception e)
		{
			Log.errorf("Failed to convert shader file at %s!", e, _file);

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

			ShaderData shaderData = new ShaderData(_graphics,
					_shader.vertShader, _shader.geoShader, _shader.fragShader);
			Resource resource = new Resource(_shader.path, shaderData);

			_resourceManager.getResourceDatabase().addResource(resource);

			ConverterData converterData = (ConverterData) data;
			converterData.setLoadedResourceFiles(new ResourceFile[]
			{
				_shader.path
			});

			return ResourceFuture.FULLY_LOADED;
		}
	}
}
