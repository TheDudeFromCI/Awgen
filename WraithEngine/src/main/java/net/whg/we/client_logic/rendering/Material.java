package net.whg.we.client_logic.rendering;

import java.nio.FloatBuffer;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import net.whg.we.utils.Color;
import net.whg.we.client_logic.utils.Screen;
import net.whg.we.scene.Location;

public class Material
{
    // FIELDS
    private Shader _shader;
    private String[] _textureParamNames;
    private Texture[] _textures;
    private String _name;
    private Color _color;

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

    public Color getColor()
    {
        return _color;
    }

    public void setColor(Color color)
    {
        _color = color;
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

        if (_color != null)
            _shader.setUniformVec4("_color", _color);
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

    public void setOrthoMVP(Matrix4f model)
    {
        _projectionMatrix.identity();
        _projectionMatrix.ortho(0f, Screen.width(), 0f, Screen.height(), -1f,
                1f);

        _mvpMatrix.set(_projectionMatrix);
        _mvpMatrix.mul(model);
        _mvpMatrix.get(_matrixFloatBuffer);
        _shader.setUniformMat4("_mvpMat", _matrixFloatBuffer);
    }
}
