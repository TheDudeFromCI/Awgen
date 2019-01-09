package net.whg.we.resources;

import java.util.HashMap;
import net.whg.we.utils.Log;

public class ResourceDatabase
{
	private static HashMap<ResourceFile, Resource<?>> _resourceReferences = new HashMap<>();

	public static boolean hasResource(ResourceFile resourceName)
	{
		synchronized (_resourceReferences)
		{
			return _resourceReferences.containsKey(resourceName);
		}
	}

	public static Resource<?> getResource(ResourceFile resourceName)
	{
		synchronized (_resourceReferences)
		{
			return _resourceReferences.get(resourceName);
		}
	}

	public static void addResource(ResourceFile resourceName, Resource<?> resource)
	{
		synchronized (_resourceReferences)
		{
			_resourceReferences.put(resourceName, resource);
		}
	}

	public static void removeResource(ResourceFile resourceName)
	{
		Resource<?> res;
		synchronized (_resourceReferences)
		{
			res = _resourceReferences.remove(resourceName);
		}

		if (res != null)
			res.dispose();
	}

	public static void disposeAll()
	{
		Log.info("Disposing all resources.");
		synchronized (_resourceReferences)
		{
			for (ResourceFile resName : _resourceReferences.keySet())
			{
				Log.debugf("Disposing resources %s.", resName);
				_resourceReferences.get(resName).dispose();
			}

			_resourceReferences.clear();
		}
	}
}
