package whg.WraithEngine;

import java.nio.FloatBuffer;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class Shader implements DisposableResource
{
	private int _shaderId;
	private HashMap<String,Integer> _uniforms;
	private boolean _disposed;

	public Shader(String vert, String frag)
	{
		_uniforms = new HashMap<>();
		
		int vId = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		int fId = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		
		GL20.glShaderSource(vId, vert);
		GL20.glCompileShader(vId);
		
		if (GL20.glGetShaderi(vId, GL20.GL_COMPILE_STATUS) != GL11.GL_TRUE)
		{
			String logMessage = GL20.glGetShaderInfoLog(vId);
			throw new RuntimeException("Failed to compiled vertex shader! '"+logMessage+"'");
		}
				
		GL20.glShaderSource(fId, frag);
		GL20.glCompileShader(fId);

		if (GL20.glGetShaderi(fId, GL20.GL_COMPILE_STATUS) != GL11.GL_TRUE)
		{
			String logMessage = GL20.glGetShaderInfoLog(fId);
			throw new RuntimeException("Failed to compiled fragment shader! '"+logMessage+"'");
		}
		
		_shaderId = GL20.glCreateProgram();
		GL20.glAttachShader(_shaderId, vId);
		GL20.glAttachShader(_shaderId, fId);
		GL20.glLinkProgram(_shaderId);
		
		if (GL20.glGetProgrami(_shaderId, GL20.GL_LINK_STATUS) != GL11.GL_TRUE)
		{
			String logMessage = GL20.glGetProgramInfoLog(_shaderId);
			throw new RuntimeException("Failed to link shader program! '"+logMessage+"'");
		}
		
		GL20.glDeleteShader(vId);
		GL20.glDeleteShader(fId);
		
		ResourceLoader.addResource(this);
	}
	
	public void loadUniform(String name)
	{
		if (_disposed)
			throw new IllegalStateException("Shader already disposed!");

		int location = GL20.glGetUniformLocation(_shaderId, name);
		_uniforms.put(name, location);
	}
	
	public void bind()
	{
		if (_disposed)
			throw new IllegalStateException("Shader already disposed!");

		GL20.glUseProgram(_shaderId);
	}
	
	public void unbind()
	{
		if (_disposed)
			throw new IllegalStateException("Shader already disposed!");

		GL20.glUseProgram(0);
	}
	
	public void dispose()
	{
		if (_disposed)
			return;

		_disposed = true;
		ResourceLoader.removeResource(this);
		GL20.glDeleteProgram(_shaderId);
	}
	
	private int getUniformLocation(String name)
	{
		if (_disposed)
			throw new IllegalStateException("Shader already disposed!");

		if (_uniforms.containsKey(name))
			return _uniforms.get(name);
		loadUniform(name);
		return _uniforms.get(name);
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

	@Override
	public boolean isDisposed()
	{
		return _disposed;
	}
}
