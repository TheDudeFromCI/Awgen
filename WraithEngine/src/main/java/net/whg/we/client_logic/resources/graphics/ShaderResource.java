package net.whg.we.client_logic.resources.graphics;

import net.whg.frameworks.logging.Log;
import net.whg.frameworks.resource.CompilableResource;
import net.whg.frameworks.resource.ResourceFile;
import net.whg.frameworks.resource.ResourceState;
import net.whg.we.client_logic.rendering.Graphics;
import net.whg.we.client_logic.rendering.Shader;

/**
 * A wrapper for a loaded shader resource.
 *
 * @author TheDudeFromCI
 */
public class ShaderResource implements CompilableResource
{
	private ShaderProperties _properties;
	private String _vertShader;
	private String _geoShader;
	private String _fragShader;
	private Shader _shader;
	private ResourceFile _resourceFile;

	public ShaderResource(ResourceFile resourceFile, ShaderProperties properties, String vertShader,
			String geoShader, String fragShader)
	{
		_resourceFile = resourceFile;
		_properties = properties;
		_vertShader = vertShader;
		_geoShader = geoShader;
		_fragShader = fragShader;
	}

	@Override
	public Shader getData()
	{
		if (_shader == null)
			Log.warnf("Attempting to retrieve shader %s from resource, but shader not compiled!",
					_properties.getName());
		return _shader;
	}

	@Override
	public boolean isCompiled()
	{
		return _shader != null;
	}

	@Override
	public void compile(Graphics graphics)
	{
		if (_shader != null)
			return;

		_shader = new Shader(_properties.getName(),
				graphics.prepareShader(_vertShader, _geoShader, _fragShader));

		_vertShader = null;
		_geoShader = null;
		_fragShader = null;
	}

	@Override
	public ResourceFile getResourceFile()
	{
		return _resourceFile;
	}

	@Override
	public void dispose()
	{
		_vertShader = null;
		_geoShader = null;
		_fragShader = null;
		_properties = null;

		if (_shader != null)
		{
			_shader.dispose();
			_shader = null;
		}
	}

	@Override
	public String getName()
	{
		return _properties.getName();
	}

	@Override
	public String toString()
	{
		return String.format("[ShaderResource: %s at %s]", _properties.getName(), _resourceFile);
	}

	@Override
	public boolean reload()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ResourceState getResourceState()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
