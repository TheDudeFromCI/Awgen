package net.whg.we.rendering;

import java.nio.FloatBuffer;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import net.whg.we.utils.Location;

public class Material
{
	// FIELDS
	private Shader _shader;
	private Texture[] _textures;
	private String _name;

	// BUFFERS
	private FloatBuffer _matrixFloatBuffer;
	private Matrix4f _projectionMatrix = new Matrix4f();
	private Matrix4f _viewMatrix = new Matrix4f();
	private Matrix4f _modelMatrix = new Matrix4f();
	private Matrix4f _mvpMatrix = new Matrix4f();
	private float _sorterId;

	public Material(Shader shader, String name)
	{
		_shader = shader;
		_name = name;
		_matrixFloatBuffer = BufferUtils.createFloatBuffer(16);
		_sorterId = (float) Math.random();
	}

	public void setTextures(Texture[] textures)
	{
		_textures = textures;
	}

	public float getSorterId()
	{
		return _sorterId + _shader.getShaderId();
	}

	public Texture[] getTextures()
	{
		return _textures;
	}

	public Shader getShader()
	{
		return _shader;
	}

	public String getName()
	{
		return _name;
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
		_shader.bind();

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
