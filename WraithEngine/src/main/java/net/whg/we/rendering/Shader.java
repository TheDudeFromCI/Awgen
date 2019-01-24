package net.whg.we.rendering;

import java.nio.FloatBuffer;
import java.util.HashMap;

public class Shader
{
	private VShader _shader;
	private HashMap<String, Integer> _uniforms = new HashMap<>();
	private boolean _disposed;
	private String _name;

	public Shader(String name, VShader shader)
	{
		_name = name;
		_shader = shader;
	}

	private int getUniformLocation(String name)
	{
		if (_disposed)
			throw new IllegalStateException("Shader already disposed!");

		if (_uniforms.containsKey(name))
			return _uniforms.get(name);

		int location = _shader.getUniformLocation(name);
		_uniforms.put(name, location);
		return location;
	}

	public void bind()
	{
		if (_disposed)
			throw new IllegalStateException("Shader already disposed!");

		_shader.bind();
	}

	public String getName()
	{
		return _name;
	}

	public int getShaderId()
	{
		return _shader.getShaderId();
	}

	public void dispose()
	{
		if (_disposed)
			return;

		_disposed = true;
		_shader.dispose();
		_shader = null;
	}

	public void setUniformMat4(String name, FloatBuffer mat)
	{
		if (_disposed)
			throw new IllegalStateException("Shader already disposed!");

		int location = getUniformLocation(name);
		_shader.setUniformMat4(location, mat);
	}

	public void setUniformMat4Array(String name, FloatBuffer mat)
	{
		if (_disposed)
			throw new IllegalStateException("Shader already disposed!");

		int location = getUniformLocation(name);
		_shader.setUniformMat4Array(location, mat);
	}

	public void setUniformInt(String name, int value)
	{
		if (_disposed)
			throw new IllegalStateException("Shader already disposed!");

		int location = getUniformLocation(name);
		_shader.setUniformInt(location, value);
	}

	public boolean isDisposed()
	{
		return _disposed;
	}
}
