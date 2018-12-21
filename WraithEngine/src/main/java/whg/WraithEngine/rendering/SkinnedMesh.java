package whg.WraithEngine.rendering;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import net.whg.we.rendering.Graphics;
import net.whg.we.rendering.Mesh;

public class SkinnedMesh extends Mesh
{
	private Skeleton _skeleton;
	private FloatBuffer _boneTransformBuffer;

	public SkinnedMesh(String meshName, VertexData vertexData, Graphics graphics, Skeleton skeleton)
	{
		super(meshName, vertexData, graphics);

		_skeleton = skeleton;
		_boneTransformBuffer = BufferUtils.createFloatBuffer(Skeleton.MAX_BONES * 16);
	}

	public Skeleton getSkeleton()
	{
		return _skeleton;
	}

	public void setSkeleton(Skeleton skeleton)
	{
		_skeleton = skeleton;
	}

	public void rebuildTransformBuffer()
	{
		_skeleton.updateHeirarchy();
		for (int i = 0; i < _skeleton.getBones().length; i++)
			_skeleton.getBones()[i].getFinalTransform().get(i * 16, _boneTransformBuffer);
	}

	public void sendToShader(Shader shader)
	{
		// TODO Confirm that shader is currently bound.

		shader.setUniformMat4Array("_bones", _boneTransformBuffer);
	}
}
