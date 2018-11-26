package whg.WraithEngine.rendering;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import whg.WraithEngine.core.DisposableResource;
import whg.WraithEngine.utils.ResourceLoader;

public class Mesh implements DisposableResource
{
	private int _vboId;
	private int _vaoId;
	private int _indexId;
	private int _indexCount;
	private boolean _disposed;
	private String _meshName;

	public Mesh(String meshName, VertexData vertexData)
	{
		_meshName = meshName;

		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexData.getDataArray().length);
		vertexBuffer.put(vertexData.getDataArray());
		vertexBuffer.flip();
		
		ShortBuffer indexBuffer = BufferUtils.createShortBuffer(vertexData.getTriangles().length);
		indexBuffer.put(vertexData.getTriangles());
		indexBuffer.flip();
		
		_indexCount = vertexData.getTriangles().length;
		
		_vaoId = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(_vaoId);
		
		_vboId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, _vboId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);
		
		int stride = vertexData.getVertexByteSize();
		int offset = 0;
		
		int[] attributes = vertexData.getAttributeSizes();
		for (int i = 0; i < attributes.length; i++)
		{
			GL20.glVertexAttribPointer(i, attributes[i], GL11.GL_FLOAT, false, stride, offset);
			GL20.glEnableVertexAttribArray(i);
			offset += attributes[i] * 4;
		}
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		_indexId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, _indexId);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

		GL30.glBindVertexArray(0);

		ResourceLoader.addResource(this);
	}
	
	public void render()
	{
		if (_disposed)
			throw new IllegalStateException("Mesh already disposed!");

		GL30.glBindVertexArray(_vaoId);
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, _indexId);
		GL11.glDrawElements(GL11.GL_TRIANGLES, _indexCount, GL11.GL_UNSIGNED_SHORT, 0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		
		GL30.glBindVertexArray(0);
	}
	
	public void dispose()
	{
		if (_disposed)
			return;
		
		_disposed = true;
		ResourceLoader.removeResource(this);
		GL15.glDeleteBuffers(_vboId);
		GL15.glDeleteBuffers(_indexId);
		GL30.glDeleteVertexArrays(_vaoId);
	}

	@Override
	public boolean isDisposed()
	{
		return _disposed;
	}
	
	public String getMeshName()
	{
		return _meshName;
	}
}
