package net.whg.we.client_logic.scene;

import java.util.ArrayList;
import java.util.Comparator;
import net.whg.we.client_logic.rendering.Camera;
import net.whg.we.client_logic.rendering.Material;
import net.whg.we.scene.Model;
import net.whg.we.scene.SubMesh;
import net.whg.we.utils.logging.Log;

public class RenderPass
{
	private ArrayList<Model> _models = new ArrayList<>();
	private ArrayList<SubMesh> _submeshes = new ArrayList<>();
	private Camera _camera;
	private Comparator<SubMesh> _meshSorter = (a, b) ->
	{
		return Float.compare(a.getMaterial().getSorterId(), b.getMaterial().getSorterId());
	};

	public void addModel(Model model)
	{
		if (_models.contains(model))
			return;

		_models.add(model);
		for (int i = 0; i < model.getSubMeshCount(); i++)
			_submeshes.add(model.getSubMesh(i));

		_submeshes.sort(_meshSorter);
	}

	public void removeModel(Model model)
	{
		if (!_models.contains(model))
			return;

		_models.remove(model);
		for (int i = 0; i < model.getSubMeshCount(); i++)
			_submeshes.remove(model.getSubMesh(i));
	}

	public void setCamera(Camera camera)
	{
		_camera = camera;
	}

	public Camera getCamera()
	{
		return _camera;
	}

	public void render()
	{
		if (_camera == null)
		{
			Log.debug("Camera not assigned to RenderPass, ignoring render.");
			return;
		}

		Material _lastMaterial = null;
		for (SubMesh mesh : _submeshes)
		{
			if (mesh.getMaterial() != _lastMaterial)
			{
				_lastMaterial = mesh.getMaterial();
				_lastMaterial.bind();
			}

			_lastMaterial.setMVPUniform(_camera, mesh.getModel().getLocation().getMatrix());
			mesh.getMesh().render();
		}
	}
}
