package net.whg.we.rendering;

import java.nio.FloatBuffer;
import java.util.HashMap;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import net.whg.we.resources.DisposableResource;

public class Shader implements DisposableResource
{
	// ===== FIELDS =====

	private int _shaderId;
	private HashMap<String, Integer> _uniforms;
	private boolean _disposed;
	private String _name;

	public Shader(String name, String vert, String frag)
	{
		_name = name;

		_uniforms = new HashMap<>();

		int vId = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		int fId = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);

		GL20.glShaderSource(vId, vert);
		GL20.glCompileShader(vId);

		if (GL20.glGetShaderi(vId, GL20.GL_COMPILE_STATUS) != GL11.GL_TRUE)
		{
			String logMessage = GL20.glGetShaderInfoLog(vId);
			throw new RuntimeException("Failed to compiled vertex shader! '" + logMessage + "'");
		}

		GL20.glShaderSource(fId, frag);
		GL20.glCompileShader(fId);

		if (GL20.glGetShaderi(fId, GL20.GL_COMPILE_STATUS) != GL11.GL_TRUE)
		{
			String logMessage = GL20.glGetShaderInfoLog(fId);
			throw new RuntimeException("Failed to compiled fragment shader! '" + logMessage + "'");
		}

		_shaderId = GL20.glCreateProgram();
		GL20.glAttachShader(_shaderId, vId);
		GL20.glAttachShader(_shaderId, fId);
		GL20.glLinkProgram(_shaderId);

		if (GL20.glGetProgrami(_shaderId, GL20.GL_LINK_STATUS) != GL11.GL_TRUE)
		{
			String logMessage = GL20.glGetProgramInfoLog(_shaderId);
			throw new RuntimeException("Failed to link shader program! '" + logMessage + "'");
		}

		GL20.glDeleteShader(vId);
		GL20.glDeleteShader(fId);

		ShaderDatabase.loadShader(this);
	}

	// ===== PRIVATE ONLY =====

	private int getUniformLocation(String name)
	{
		if (_disposed)
			throw new IllegalStateException("Shader already disposed!");

		if (_uniforms.containsKey(name))
			return _uniforms.get(name);
		loadUniform(name);
		return _uniforms.get(name);
	}

	// ===== ENGINE ONLY =====

	void bind()
	{
		if (_disposed)
			throw new IllegalStateException("Shader already disposed!");

		GL20.glUseProgram(_shaderId);
	}

	void unbind()
	{
		if (_disposed)
			throw new IllegalStateException("Shader already disposed!");

		GL20.glUseProgram(0);
	}

	// ===== PUBLIC API =====

	public String getName()
	{
		return _name;
	}

	public void loadUniform(String name)
	{
		if (_disposed)
			throw new IllegalStateException("Shader already disposed!");

		int location = GL20.glGetUniformLocation(_shaderId, name);
		_uniforms.put(name, location);
	}

	public int getShaderId()
	{
		return _shaderId;
	}

	@Override
	public void dispose()
	{
		if (_disposed)
			return;

		_disposed = true;
		ShaderDatabase.unloadShader(this);
		GL20.glDeleteProgram(_shaderId);

		if (ShaderDatabase.isShaderBound(this))
			ShaderDatabase.bindShader(null);
	}

	public void setUniformMat4(String name, FloatBuffer mat)
	{
		if (_disposed)
			throw new IllegalStateException("Shader already disposed!");

		int location = getUniformLocation(name);
		GL20.glUniformMatrix4fv(location, false, mat);
	}

	public void setUniformMat4Array(String name, FloatBuffer mat)
	{
		if (_disposed)
			throw new IllegalStateException("Shader already disposed!");

		int location = getUniformLocation(name);
		GL20.glUniformMatrix4fv(location, false, mat);
	}

	public void setUniformInt(String name, int value)
	{
		if (_disposed)
			throw new IllegalStateException("Shader already disposed!");

		int location = getUniformLocation(name);
		GL20.glUniform1i(location, value);
	}

	@Override
	public boolean isDisposed()
	{
		return _disposed;
	}
}
