package net.whg.we.resources;

import java.util.ArrayList;
import net.whg.we.rendering.Graphics;
import net.whg.we.rendering.MeshScene;
import net.whg.we.utils.Log;

public class MeshSceneResource implements Resource<MeshScene>
{
	private MeshScene _scene;
	private ArrayList<UncompiledMesh> _meshes;

	public MeshSceneResource(ArrayList<UncompiledMesh> meshes)
	{
		_meshes = meshes;
	}

	@Override
	public MeshScene getData()
	{
		return _scene;
	}

	public boolean isCompiled()
	{
		return _scene != null;
	}

	public void compile(Graphics graphics)
	{
		if (_scene != null)
		{
			Log.warn("Cannot compile mesh scene! Mesh Scene already compiled.");
			return;
		}

		_scene = new MeshScene();

		for (UncompiledMesh mesh : _meshes)
			_scene._meshes.add(mesh.compile(graphics));
	}
}
