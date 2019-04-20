package net.whg.we.resource;

import net.whg.frameworks.resource.ResourceData;
import net.whg.frameworks.resource.ResourceFile;

/**
 * Represents the output resources from an asset conversion process. This class,
 * once the conversion file is finished loading, points to the newly created
 * resource files.
 *
 * @author TheDudeFromCI
 */
public class ConverterData implements ResourceData
{
	private ResourceFile[] _newResources;

	/**
	 * Creates a new ConverterData object.
	 */
	public ConverterData()
	{
		_newResources = new ResourceFile[0];
	}

	@Override
	public void dispose()
	{
		// Nothing to dispose
	}

	/**
	 * Gets all the resource files that were generated as the output of the
	 * input file which was converted. The resources are also loaded during this
	 * process. The ResourceFiles returned can be used to retreive the new
	 * resources from the Resource Database.
	 *
	 * @return An array of ResourceFiles, pointing to all the newly converted
	 *     resource.
	 */
	public ResourceFile[] getResourceFiles()
	{
		return _newResources;
	}

	/**
	 * Assigns a new array of ResourceFile pointers to this data object. This is
	 * called by the converter file after it finishes loading to determine which
	 * resources were loaded. If the input is null, the internal state of this
	 * converter data object is assigned an empty array instead.
	 *
	 * @param newResources
	 *     - The new ResourceFile array that this converter data represents.
	 */
	public void setLoadedResourceFiles(ResourceFile[] newResources)
	{
		if (newResources == null)
			newResources = new ResourceFile[0];

		_newResources = newResources;
	}
}
