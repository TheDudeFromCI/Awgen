package net.whg.we.client_logic.rendering;

import java.nio.FloatBuffer;
import java.util.HashMap;
import org.joml.Vector4f;
import net.whg.frameworks.logging.Log;
import net.whg.we.legacy.Color;

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

        Log.tracef("Disposed shader '%s'.", _name);
    }

    public boolean hasUniform(String name)
    {
        if (_disposed)
            throw new IllegalStateException("Shader already disposed!");

        return getUniformLocation(name) != -1;
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

    public void setUniformVec4(String name, Vector4f value)
    {
        if (_disposed)
            throw new IllegalStateException("Shader already disposed!");

        int location = getUniformLocation(name);
        _shader.setUniformVec4(location, value.x, value.y, value.z, value.w);
    }

    public void setUniformVec4(String name, Color value)
    {
        if (_disposed)
            throw new IllegalStateException("Shader already disposed!");

        int location = getUniformLocation(name);
        _shader.setUniformVec4(location, value.r, value.g, value.b, value.a);
    }

    public boolean isDisposed()
    {
        return _disposed;
    }
}
