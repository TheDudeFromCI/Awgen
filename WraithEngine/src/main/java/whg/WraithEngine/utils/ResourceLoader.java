package whg.WraithEngine.utils;

import java.io.IOException;
import java.util.ArrayList;
import net.whg.we.utils.FileUtils;
import whg.WraithEngine.core.DisposableResource;
import whg.WraithEngine.rendering.Shader;

public class ResourceLoader
{
	private static ArrayList<DisposableResource> _resources = new ArrayList<>();
	
	public static void addResource(DisposableResource resource)
	{
		_resources.add(resource);
	}
	
	public static void removeResource(DisposableResource resource)
	{
		_resources.remove(resource);
	}
	
	public static void disposeAllResources()
	{
		for (int i = _resources.size() - 1; i >= 0; i--)
		{
			DisposableResource res = _resources.get(i);
			if (!res.isDisposed())
				res.dispose();
		}
		_resources.clear();
	}

	public static Shader loadShader(String name)
	{
		String vert, frag;
		try
		{
			vert = FileUtils.loadFileAsString(FileUtils.getResource(null, name + ".vert"));
			frag = FileUtils.loadFileAsString(FileUtils.getResource(null, name + ".frag"));
		}
		catch(IOException exception)
		{
			System.err.println("Failed to load shader!");
			// TODO Return default shader
			return null;
		}
		
		Shader shader = new Shader(name, vert, frag);
		return shader;
	}
}
