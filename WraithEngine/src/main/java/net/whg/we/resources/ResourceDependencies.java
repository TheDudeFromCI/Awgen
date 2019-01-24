package net.whg.we.resources;

import java.util.ArrayList;
import net.whg.we.utils.Poolable;

public class ResourceDependencies implements Poolable
{
	private ArrayList<SubResourceFile> _dependencies = new ArrayList<>();
	private ResourceFile _resourceFile;

	public void setResourceFile(ResourceFile resourceFile)
	{
		_resourceFile = resourceFile;
	}

	public ResourceFile getResourceFile()
	{
		return _resourceFile;
	}

	public ArrayList<SubResourceFile> getDependencies()
	{
		return _dependencies;
	}

	public int getDependencyCount()
	{
		return _dependencies.size();
	}

	public void addDependency(SubResourceFile subResourceFile)
	{
		_dependencies.add(subResourceFile);
	}

	public void clearDependencies()
	{
		_dependencies.clear();
	}

	@Override
	public void init()
	{
	}

	@Override
	public void dispose()
	{
		_resourceFile = null;
		_dependencies.clear();
	}
}
