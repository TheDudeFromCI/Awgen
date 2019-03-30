package net.whg.we.scene.behaviours;

import net.whg.we.scene.GameObject;
import net.whg.we.scene.Model;
import net.whg.we.scene.ObjectBehaviour;

public class RenderBehaviour implements ObjectBehaviour
{
	private GameObject _owner;
	private Model _model;

	public RenderBehaviour(Model model)
	{
		_model = model;
	}

	@Override
	public void init(GameObject owner)
	{
		_owner = owner;
		// _owner.getManager().getScene().getRenderPass().addModel(_model);
	}

	@Override
	public void dispose()
	{
		// _owner.getManager().getScene().getRenderPass().removeModel(_model);

		_owner = null;
		_model = null;
	}

	@Override
	public GameObject getOwner()
	{
		return _owner;
	}

	public Model getModel()
	{
		return _model;
	}
}
