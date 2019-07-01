package net.whg.we.scene;

import org.joml.Matrix4f;
import net.whg.frameworks.scene.SceneNode;
import net.whg.we.client_logic.rendering.Material;
import net.whg.we.resource.MeshData;

public class ModelNode extends SceneNode implements RenderableNode
{
	private MeshData _mesh;
	private Material _material;
	private CameraNode _camera;

	private Matrix4f _matrixBuffer = new Matrix4f();

	public ModelNode(MeshData mesh, Material material, CameraNode camera)
	{
		_mesh = mesh;
		_material = material;
		_camera = camera;
	}

	public MeshData getMesh()
	{
		return _mesh;
	}

	public Material getMaterial()
	{
		return _material;
	}

	public CameraNode getCamera()
	{
		return _camera;
	}

	@Override
	public void renderNode()
	{
		_material.bind();

		getFullMatrix(_matrixBuffer);
		_material.setMVPUniform(_camera, _matrixBuffer);

		_mesh.render();
	}

	@Override
	public String getNodeType()
	{
		return "render.model";
	}
}
