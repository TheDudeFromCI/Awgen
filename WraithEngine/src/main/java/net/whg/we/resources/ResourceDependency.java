package net.whg.we.resources;

public class ResourceDependency
{
	private ResourceFile _resourceFile;
	private String _identifier;

	public ResourceDependency(ResourceFile resourceFile, String identifier)
	{
		_resourceFile = resourceFile;
		_identifier = identifier;
	}

	public ResourceFile getResourceFile()
	{
		return _resourceFile;
	}

	public String getIdentifier()
	{
		return _identifier;
	}
}
