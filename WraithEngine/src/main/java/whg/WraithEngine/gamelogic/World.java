package whg.WraithEngine.gamelogic;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import whg.WraithEngine.rendering.Camera;
import whg.WraithEngine.rendering.Renderable;

public class World
{
	private Camera _camera;
	private ArrayList<Entity> _entities = new ArrayList<>();
	private ArrayList<Entity> _toAdd = new ArrayList<>();
	private ArrayList<Entity> _toRemove = new ArrayList<>();
	
	public World(Camera camera)
	{
		_camera = camera;
	}
	
	public World()
	{
		this(new Camera());
	}
	
	public void addEntity(Entity entity)
	{
		if (!_entities.contains(entity) && !_toAdd.contains(entity))
			_toAdd.add(entity);
	}
	
	public void removeEntity(Entity entity)
	{
		if (_entities.contains(entity) && !_toRemove.contains(entity))
			_toRemove.add(entity);
	}
	
	public void update()
	{
		for (Entity e : _toRemove)
			_entities.remove(e);
		_toRemove.clear();
		
		for (Entity e : _toAdd)
			_entities.add(e);
		_toAdd.clear();
		
		for (Entity e : _entities)
			if (e instanceof Updateable)
				((Updateable) e).update();
	}
	
	public Camera getCamera()
	{
		return _camera;
	}
	
	public void render()
	{
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		for (Entity e : _entities)
			if (e instanceof Renderable)
				((Renderable)e).render(_camera);
	}
}
