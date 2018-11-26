package whg.WraithEngine.gamelogic;

import java.util.ArrayList;

public class Universe
{
	private ArrayList<World> _worlds = new ArrayList<>();
	private ArrayList<World> _toAdd = new ArrayList<>();
	private ArrayList<World> _toRemove = new ArrayList<>();

	public void update()
	{
		for (World w : _toRemove)
			_worlds.remove(w);
		_toRemove.clear();

		for (World w : _toAdd)
			_worlds.add(w);
		_toAdd.clear();
		
		for (World w : _worlds)
			w.update();
	}
	
	public void render()
	{
		for (World w : _worlds)
			w.render();
	}
	
	public void addWorld(World world)
	{
		_toAdd.add(world);
	}
	
	public void removeWorld(World world)
	{
		_toRemove.remove(world);
	}
}
