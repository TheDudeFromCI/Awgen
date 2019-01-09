package net.whg.we.rendering;

import java.util.ArrayList;
import net.whg.we.scene.Model;

// TODO Refactor
public class MeshScene
{
	public ArrayList<Mesh> _meshes = new ArrayList<>();
	public ArrayList<Material> _materials = new ArrayList<>();
	public ArrayList<Model> _models = new ArrayList<>();

	public void dispose()
	{
		for (Mesh m : _meshes)
			m.dispose();
		_meshes.clear();
	}
}
