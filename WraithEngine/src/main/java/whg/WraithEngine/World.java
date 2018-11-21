package whg.WraithEngine;

import java.util.ArrayList;

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
	}
	
	public Camera getCamera()
	{
		return _camera;
	}
	
	public void render()
	{
		for (Entity e : _entities)
			e.render(_camera);
	}
}
