package net.whg.we.rendering.opengl;

import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import net.whg.we.rendering.VShader;
import net.whg.we.utils.Log;

public class GLVShader implements VShader
{
	private int _shaderId;
	private OpenGLGraphics _opengl;

	GLVShader(OpenGLGraphics opengl, String vert, String geo, String frag)
	{
		_opengl = opengl;

		Log.tracef("Compiling Shader.  Vert Source:\n%s", vert);
		Log.tracef("Compiling Shader.  Geo Source:\n%s", geo);
		Log.tracef("Compiling Shader.  Frag Source:\n%s", frag);

		int vId = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		int gId = -1;
		// if (geo != null && !geo.isEmpty())
		// gId = GL20.glCreateShader(GL32.GL_GEOMETRY_SHADER);
		int fId = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);

		GL20.glShaderSource(vId, vert);
		GL20.glCompileShader(vId);

		if (GL20.glGetShaderi(vId, GL20.GL_COMPILE_STATUS) != GL11.GL_TRUE)
		{
			String logMessage = GL20.glGetShaderInfoLog(vId);
			throw new RuntimeException("Failed to compiled vertex shader! '" + logMessage + "'");
		}

		if (gId != -1)
		{
			GL20.glShaderSource(gId, geo);
			GL20.glCompileShader(gId);

			if (GL20.glGetShaderi(gId, GL20.GL_COMPILE_STATUS) != GL11.GL_TRUE)
			{
				String logMessage = GL20.glGetShaderInfoLog(gId);
				throw new RuntimeException(
						"Failed to compiled geometry shader! '" + logMessage + "'");
			}
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
		if (gId != -1)
			GL20.glAttachShader(_shaderId, gId);
		GL20.glAttachShader(_shaderId, fId);

		GL20.glLinkProgram(_shaderId);

		if (GL20.glGetProgrami(_shaderId, GL20.GL_LINK_STATUS) != GL11.GL_TRUE)
		{
			String logMessage = GL20.glGetProgramInfoLog(_shaderId);
			throw new RuntimeException("Failed to link shader program! '" + logMessage + "'");
		}

		GL20.glDeleteShader(vId);
		if (gId != -1)
			GL20.glDeleteShader(gId);
		GL20.glDeleteShader(fId);

		_opengl.checkForErrors("Loaded Shader");
	}

	@Override
	public void bind()
	{
		if (_opengl.getBoundShaderId() == _shaderId)
			return;

		GL20.glUseProgram(_shaderId);
		_opengl.setBoundShaderId(_shaderId);
		_opengl.checkForErrors(Log.TRACE, "Bound Shader");
	}

	@Override
	public boolean isBound()
	{
		return _opengl.getBoundShaderId() == _shaderId;
	}

	@Override
	public void dispose()
	{
		if (_opengl.getBoundShaderId() == _shaderId)
		{
			_opengl.setBoundShaderId(0);
			GL20.glUseProgram(0);
		}

		GL20.glDeleteProgram(_shaderId);
		_opengl.checkForErrors(Log.TRACE, "Disposed Shader");
	}

	@Override
	public int getShaderId()
	{
		return _shaderId;
	}

	@Override
	public int getUniformLocation(String name)
	{
		int location = GL20.glGetUniformLocation(_shaderId, name);
		_opengl.checkForErrors(Log.TRACE, "Got Shader Uniform Location");
		return location;
	}

	@Override
	public void setUniformMat4(int location, FloatBuffer value)
	{
		setUniformMat4Array(location, value);
	}

	@Override
	public void setUniformMat4Array(int location, FloatBuffer value)
	{
		if (isBound())
			GL20.glUniformMatrix4fv(location, false, value);
		else
		{
			GL20.glUseProgram(_shaderId);
			GL20.glUniformMatrix4fv(location, false, value);
			GL20.glUseProgram(_opengl.getBoundShaderId());
		}

		_opengl.checkForErrors(Log.TRACE, "Set Shader Uniform Matrix Array");
	}

	@Override
	public void setUniformInt(int location, int value)
	{
		if (isBound())
			GL20.glUniform1i(location, value);
		else
		{
			GL20.glUseProgram(_shaderId);
			GL20.glUniform1i(location, value);
			GL20.glUseProgram(_opengl.getBoundShaderId());
		}

		_opengl.checkForErrors(Log.TRACE, "Set Shader Uniform Int");
	}
}
