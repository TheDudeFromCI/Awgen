package net.whg.we.resources.graphics;

import net.whg.we.rendering.Graphics;
import net.whg.we.rendering.Shader;
import net.whg.we.resources.CompilableResource;
import net.whg.we.resources.ResourceFile;
import net.whg.we.utils.Log;

/**
 * A wrapper for a loaded shader resource.
 *
 * @author TheDudeFromCI
 */
public class ShaderResource implements CompilableResource<Shader>
{
	private ShaderProperties _properties;
	private String _vertShader;
	private String _geoShader;
	private String _fragShader;
	private Shader _shader;
	private ResourceFile _resource;

	public ShaderResource(ShaderProperties properties, String vertShader, String geoShader,
			String fragShader, ResourceFile resource)
	{
		_properties = properties;
		_vertShader = vertShader;
		_geoShader = geoShader;
		_fragShader = fragShader;
		_resource = resource;
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
		return _resource;
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
}
