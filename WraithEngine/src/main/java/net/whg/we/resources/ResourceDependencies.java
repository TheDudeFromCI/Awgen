package net.whg.we.resources;

import java.util.ArrayList;
import java.util.List;
import net.whg.we.utils.Poolable;

public class ResourceDependencies implements Poolable
{
	private ArrayList<ResourceDependency> _dependencies = new ArrayList<>();
	private ResourceFile _resourceFile;

	public void setResourceFile(ResourceFile resourceFile)
	{
		_resourceFile = resourceFile;
	}

	public ResourceFile getResourceFile()
	{
		return _resourceFile;
	}

	public ArrayList<ResourceDependency> getDependencies()
	{
		return _dependencies;
	}

	public boolean isFullyLoaded(ResourceBatchRequest request)
	{
		for (ResourceDependency dep : _dependencies)
		{
			if (request.getResource(dep.getResourceFile()) == null)
				return false;
		}

		return true;
	}

	public int getDependencyCount()
	{
		return _dependencies.size();
	}

	public void addDependency(ResourceDependency subResourceFile)
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

	public ResourceDependency getDependency(String identifier)
	{
		for (ResourceDependency dep : _dependencies)
			if (dep.getIdentifier().equals(identifier))
				return dep;
		return null;
	}

	public void searchDependencies(List<ResourceDependency> list, String regex)
	{
		for (ResourceDependency dep : _dependencies)
			if (dep.getIdentifier().matches(regex))
				list.add(dep);
	}
}
