package net.whg.we.resources;

import java.util.HashMap;
import net.whg.we.utils.Log;

public class ResourceDatabase
{
	private static HashMap<String, Resource<?>> _resourceReferences = new HashMap<>();

	public static boolean hasResource(String resourceName)
	{
		synchronized (_resourceReferences)
		{
			return _resourceReferences.containsKey(resourceName);
		}
	}

	public static Resource<?> getResource(String resourceName)
	{
		synchronized (_resourceReferences)
		{
			return _resourceReferences.get(resourceName);
		}
	}

	public static void addResource(String resourceName, Resource<?> resource)
	{
		synchronized (_resourceReferences)
		{
			_resourceReferences.put(resourceName, resource);
		}
	}

	public static void removeResource(String resourceName)
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
			for (String resName : _resourceReferences.keySet())
			{
				Log.debugf("Disposing resources %s.", resName);
				_resourceReferences.get(resName).dispose();
			}

			_resourceReferences.clear();
		}
	}
}