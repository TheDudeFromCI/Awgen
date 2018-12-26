package net.whg.we.resources;

import net.whg.we.rendering.Shader;
import net.whg.we.utils.Log;

/**
 * A wrapper for a loaded shader resource.
 *
 * @author TheDudeFromCI
 */
public class ShaderResource implements Resource
{
	private ShaderProperties _properties;
	private String _vertShader;
	@SuppressWarnings("unused") // TODO Make shaders support geometry shaders.
	private String _geoShader;
	private String _fragShader;
	private Shader _shader;

	public ShaderResource(ShaderProperties properties, String vertShader, String geoShader,
			String fragShader)
	{
		_properties = properties;
		_vertShader = vertShader;
		_geoShader = geoShader;
		_fragShader = fragShader;
	}

	@Override
	public Object getData()
	{
		if (_shader == null)
			Log.warnf("Attempting to retrieve shader %s from resource, but shader not compiled!",
					_properties.getName());
		return _shader;
	}

	public boolean needsCompiling()
	{
		return _shader == null;
	}

	public void compileShader()
	{
		if (_shader != null)
		{
			Log.warnf("Cannot compile shader! Shader %s already compiled.", _shader.getName());
			return;
		}

		_shader = new Shader(_properties.getName(), _vertShader, _fragShader);
	}
}
