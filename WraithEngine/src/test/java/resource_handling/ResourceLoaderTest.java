package resource_handling;

import org.junit.Assert;
import org.junit.Test;
import net.whg.we.main.Plugin;
import net.whg.we.resources.Resource;
import net.whg.we.resources.ResourceBatchRequest;
import net.whg.we.resources.ResourceFile;
import net.whg.we.resources.ResourceLoader;
import net.whg.we.resources.SimpleFileDatabase;
import util.CommonMock;

public class ResourceLoaderTest
{
	@Test
	public void addResource()
	{
		SimpleFileDatabase fileDatabase = CommonMock.getSimpleFileDatabase();
		ResourceLoader resourceLoader = new ResourceLoader(fileDatabase);
		Plugin plugin = CommonMock.getTestPlugin();
		ResourceFile dummyResource = fileDatabase.getResourceFile(plugin, "abc");

		Assert.assertFalse(resourceLoader.hasResource(dummyResource));

		Resource<Object> res = CommonMock.getResource(dummyResource);
		resourceLoader.addResource(res);

		Assert.assertTrue(resourceLoader.hasResource(dummyResource));
	}

	@Test
	public void loadResource()
	{
		SimpleFileDatabase fileDatabase = CommonMock.getSimpleFileDatabase();
		ResourceLoader resourceLoader = new ResourceLoader(fileDatabase);
		Plugin plugin = CommonMock.getTestPlugin();
		ResourceFile dummyResource = fileDatabase.getResourceFile(plugin, "abc");

		ResourceBatchRequest request = new ResourceBatchRequest();
		request.addResourceFile(dummyResource);
		resourceLoader.loadResources(request);

		// TODO Load a simple file
	}
}
