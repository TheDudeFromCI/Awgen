package net.whg.we.rendering;

import java.nio.FloatBuffer;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import net.whg.we.utils.Location;
import net.whg.we.utils.Screen;

public class Material
{
	// FIELDS
	private Shader _shader;
	private String[] _textureParamNames;
	private Texture[] _textures;
	private String _name;

	// BUFFERS
	private FloatBuffer _matrixFloatBuffer;
	private Matrix4f _projectionMatrix = new Matrix4f();
	private Matrix4f _viewMatrix = new Matrix4f();
	private Matrix4f _modelMatrix = new Matrix4f();
	private Matrix4f _mvpMatrix = new Matrix4f();
	private float _sorterId;
	private boolean _needsUpdateTexturePointers;

	public Material(Shader shader, String name)
	{
		_shader = shader;
		_name = name;
		_matrixFloatBuffer = BufferUtils.createFloatBuffer(16);
		_sorterId = (float) Math.random();
	}

	public void setTextures(String[] textureParamNames, Texture[] textures)
	{
		_textureParamNames = textureParamNames;
		_textures = textures;
		_needsUpdateTexturePointers = true;
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

			if (_needsUpdateTexturePointers)
			{
				for (int i = 0; i < _textures.length; i++)
					_shader.setUniformInt(_textureParamNames[i], i);
			}
		}
	}

	public void setMVPUniform(Camera camera, Location entityLocation)
	{
		camera.getProjectionMatrix(_projectionMatrix);
		camera.getViewMatrix(_viewMatrix);
		entityLocation.getMatrix(_modelMatrix);

		if (_shader.hasUniform("_mMat"))
		{
			_modelMatrix.get(_matrixFloatBuffer);
			_shader.setUniformMat4("_mMat", _matrixFloatBuffer);
		}

		_mvpMatrix.set(_projectionMatrix);
		_mvpMatrix.mul(_viewMatrix);
		_mvpMatrix.mul(_modelMatrix);
		_mvpMatrix.get(_matrixFloatBuffer);
		_shader.setUniformMat4("_mvpMat", _matrixFloatBuffer);
	}

	public void setMVPUniform(Vector2f pos, Vector2f size)
	{
		_projectionMatrix.identity();
		_projectionMatrix.ortho(0f, Screen.width(), 0f, Screen.height(), -1f, 1f);

		_modelMatrix.identity();
		_modelMatrix.translate(pos.x, pos.y, 0f);
		_modelMatrix.scale(size.x, size.y, 1f);

		_mvpMatrix.set(_projectionMatrix);
		_mvpMatrix.mul(_modelMatrix);
		_mvpMatrix.get(_matrixFloatBuffer);
		_shader.setUniformMat4("_mvpMat", _matrixFloatBuffer);
	}
}
