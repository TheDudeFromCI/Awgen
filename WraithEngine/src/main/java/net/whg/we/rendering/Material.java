package net.whg.we.rendering;

import java.nio.FloatBuffer;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import net.whg.we.utils.Location;

public class Material
{
	private Shader _shader;
	private FloatBuffer _matrixFloatBuffer;
	private Matrix4f _projectionMatrix = new Matrix4f();
	private Matrix4f _viewMatrix = new Matrix4f();
	private Matrix4f _modelMatrix = new Matrix4f();
	private Matrix4f _mvpMatrix = new Matrix4f();
	private Texture[] _textures;

	public Material(Shader shader)
	{
		_shader = shader;
		_matrixFloatBuffer = BufferUtils.createFloatBuffer(16);
	}

	public void setTextures(Texture[] textures)
	{
		_textures = textures;
	}

	public Texture[] getTextures()
	{
		return _textures;
	}

	public Shader getShader()
	{
		return _shader;
	}

	public void bind()
	{
		_shader.bind();

		if (_textures != null)
		{
			for (int i = 0; i < _textures.length; i++)
				_textures[i].bind(i);
		}
	}

	public void setMVPUniform(Camera camera, Location entityLocation)
	{
		ShaderDatabase.bindShader(_shader);

		camera.getProjectionMatrix(_projectionMatrix);
		camera.getViewMatrix(_viewMatrix);
		entityLocation.getMatrix(_modelMatrix);

		_mvpMatrix.set(_projectionMatrix);
		_mvpMatrix.mul(_viewMatrix);
		_mvpMatrix.mul(_modelMatrix);
		_mvpMatrix.get(_matrixFloatBuffer);
		_shader.setUniformMat4("_mvpMat", _matrixFloatBuffer);
	}
}
