package net.whg.we.resources;

import net.whg.we.rendering.Graphics;
import net.whg.we.rendering.Shader;
import net.whg.we.utils.Log;

/**
 * A wrapper for a loaded shader resource.
 *
 * @author TheDudeFromCI
 */
public class ShaderResource implements Resource<Shader>
{
	private ShaderProperties _properties;
	private String _vertShader;
	private String _geoShader;
	private String _fragShader;
	private Shader _shader;
	private ResourceFile _resource;

	public ShaderResource(ShaderProperties properties, String vertShader, String geoShader,
			String fragShader)
	{
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

	public boolean isCompiled()
	{
		return _shader != null;
	}

	public void compileShader(Graphics graphics)
	{
		if (_shader != null)
		{
			Log.warnf("Cannot compile shader! Shader %s already compiled.", _shader.getName());
			return;
		}

		_shader = new Shader(_properties.getName(),
				graphics.prepareShader(_vertShader, _geoShader, _fragShader));

		// Free memory
		_vertShader = null;
		_geoShader = null;
		_fragShader = null;
	}

	@Override
	public ResourceFile getResourceFile()
	{
		return _resource;
	}

	@Override
	public void setResourceFile(ResourceFile resource)
	{
		_resource = resource;
	}

	@Override
	public void dispose()
	{
		if (_shader == null)
			return;

		_shader.dispose();
		_shader = null;
	}
}
