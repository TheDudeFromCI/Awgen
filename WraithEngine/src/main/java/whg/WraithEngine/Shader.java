package whg.WraithEngine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class Shader
{
	private int _shaderId;

	public Shader(String vert, String frag)
	{
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
	}
	
	public void bind()
	{
		GL20.glUseProgram(_shaderId);
	}
	
	public void unbind()
	{
		GL20.glUseProgram(0);
	}
	
	public void dispose()
	{
		GL20.glDeleteProgram(_shaderId);
	}
}
