package net.whg.we.rendering;

import java.util.ArrayList;

// TODO Refactor
public class MeshScene
{
	public ArrayList<Mesh> _meshes = new ArrayList<>();

	public void dispose()
	{
		for (Mesh m : _meshes)
			m.dispose();
		_meshes.clear();
	}
}
