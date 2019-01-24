package net.whg.we.resources;

public class SubResourceFile
{
	private ResourceFile _resourceFile;
	private String _name;
	private Class<?> _resourceType;

	public SubResourceFile(ResourceFile resourceFile, String name, Class<?> resourceType)
	{
		_resourceFile = resourceFile;
		_name = name;
		_resourceType = resourceType;
	}

	public ResourceFile getResourceFile()
	{
		return _resourceFile;
	}

	public String getName()
	{
		return _name;
	}

	public Class<?> getResourceType()
	{
		return _resourceType;
	}
}
