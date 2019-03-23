package net.whg.we.client_logic.rendering.opengl;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import net.whg.we.client_logic.rendering.ShaderAttributes;
import net.whg.we.client_logic.rendering.VMesh;
import net.whg.we.client_logic.rendering.VertexData;
import net.whg.we.utils.logging.Log;

public class GLVMesh implements VMesh
{
    private OpenGLGraphics _opengl;
    private int _vboId;
    private int _vaoId;
    private int _indexId;
    private int _indexCount;
    private boolean _disposed;

    public GLVMesh(OpenGLGraphics opengl, VertexData vertexData)
    {
        _opengl = opengl;

        FloatBuffer vertexBuffer =
                BufferUtils.createFloatBuffer(vertexData.getDataArray().length);
        vertexBuffer.put(vertexData.getDataArray());
        vertexBuffer.flip();

        ShortBuffer indexBuffer =
                BufferUtils.createShortBuffer(vertexData.getTriangles().length);
        indexBuffer.put(vertexData.getTriangles());
        indexBuffer.flip();

        _indexCount = vertexData.getTriangles().length;

        _vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(_vaoId);

        _vboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, _vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer,
                GL15.GL_STATIC_DRAW);

        int stride = vertexData.getVertexByteSize();
        int offset = 0;

        ShaderAttributes attributes = vertexData.getAttributeSizes();
        for (int i = 0; i < attributes.getCount(); i++)
        {
            GL20.glVertexAttribPointer(i, attributes.getAttributeSize(i),
                    GL11.GL_FLOAT, false, stride, offset);
            GL20.glEnableVertexAttribArray(i);
            offset += attributes.getAttributeSize(i) * 4;
        }

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        _indexId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, _indexId);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuffer,
                GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

        GL30.glBindVertexArray(0);

        _opengl.checkForErrors("Loaded Mesh");
    }

    @Override
    public void render()
    {
        if (_disposed)
            throw new IllegalStateException("Mesh already disposed!");

        GL30.glBindVertexArray(_vaoId);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, _indexId);
        GL11.glDrawElements(GL11.GL_TRIANGLES, _indexCount,
                GL11.GL_UNSIGNED_SHORT, 0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

        GL30.glBindVertexArray(0);

        _opengl.checkForErrors(Log.TRACE, "Rendered Mesh");
    }

    @Override
    public void dispose()
    {
        if (_disposed)
            return;

        _disposed = true;
        GL15.glDeleteBuffers(_vboId);
        GL15.glDeleteBuffers(_indexId);
        GL30.glDeleteVertexArrays(_vaoId);

        _opengl.checkForErrors("Disposed Mesh");

    }

    @Override
    public boolean isDisposed()
    {
        return _disposed;
    }

    @Override
    public void rebuild(VertexData vertexData)
    {
        FloatBuffer vertexBuffer =
                BufferUtils.createFloatBuffer(vertexData.getDataArray().length);
        vertexBuffer.put(vertexData.getDataArray());
        vertexBuffer.flip();

        ShortBuffer indexBuffer =
                BufferUtils.createShortBuffer(vertexData.getTriangles().length);
        indexBuffer.put(vertexData.getTriangles());
        indexBuffer.flip();

        _indexCount = vertexData.getTriangles().length;

        GL30.glBindVertexArray(_vaoId);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, _vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer,
                GL15.GL_STATIC_DRAW);

        int stride = vertexData.getVertexByteSize();
        int offset = 0;

        ShaderAttributes attributes = vertexData.getAttributeSizes();
        for (int i = 0; i < attributes.getCount(); i++)
        {
            GL20.glVertexAttribPointer(i, attributes.getAttributeSize(i),
                    GL11.GL_FLOAT, false, stride, offset);
            GL20.glEnableVertexAttribArray(i);
            offset += attributes.getAttributeSize(i) * 4;
        }

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        _indexId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, _indexId);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuffer,
                GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

        GL30.glBindVertexArray(0);
    }
}
