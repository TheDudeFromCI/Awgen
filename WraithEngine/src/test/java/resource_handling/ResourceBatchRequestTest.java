package resource_handling;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import net.whg.we.main.Plugin;
import net.whg.we.resources.Resource;
import net.whg.we.resources.ResourceBatchRequest;
import net.whg.we.resources.ResourceFile;
import net.whg.we.resources.SimpleFileDatabase;
import util.CommonMock;

public class ResourceBatchRequestTest
{
	@Test
	public void addResourceFiles()
	{
		ResourceBatchRequest batch = new ResourceBatchRequest();
		Plugin plugin = CommonMock.getTestPlugin();
		SimpleFileDatabase db = CommonMock.getSimpleFileDatabase();

		Assert.assertEquals(batch.getResourceFileCount(), 0);

		batch.addResourceFile(db.getResourceFile(plugin, "abc"));
		batch.addResourceFile(db.getResourceFile(plugin, "def"));
		batch.addResourceFile(db.getResourceFile(plugin, "abc"));

		Assert.assertEquals(batch.getResourceFileCount(), 2);
	}

	@Test
	public void addResources()
	{
		ResourceBatchRequest batch = new ResourceBatchRequest();

		Assert.assertEquals(batch.getResourceCount(), 0);

		SimpleFileDatabase db = CommonMock.getSimpleFileDatabase();
		Resource<Object> res =
				CommonMock.getResource(db.getResourceFile(CommonMock.getTestPlugin(), "abc"));

		batch.addResource(res);

		Assert.assertEquals(batch.getResourceCount(), 1);
	}

	@Test
	public void nextUnloadedResource()
	{
		ResourceBatchRequest batch = new ResourceBatchRequest();
		Plugin plugin = CommonMock.getTestPlugin();
		SimpleFileDatabase db = CommonMock.getSimpleFileDatabase();
		ResourceFile resourceFile = db.getResourceFile(plugin, "abc");

		batch.addResourceFile(resourceFile);

		// Here we run twice to make sure repeating the command does not change the
		// outcome.
		Assert.assertNotNull(batch.nextUnloadedResource());
		Assert.assertNotNull(batch.nextUnloadedResource());

		Resource<Object> res = CommonMock.getResource(resourceFile);

		batch.addResource(res);

		Assert.assertNull(batch.nextUnloadedResource());
	}

	@Test
	public void pushResourceFileToBack()
	{
		ResourceBatchRequest batch = new ResourceBatchRequest();

		ResourceFile rf1 = Mockito.mock(ResourceFile.class);
		ResourceFile rf2 = Mockito.mock(ResourceFile.class);
		ResourceFile rf3 = Mockito.mock(ResourceFile.class);
		batch.addResourceFile(rf1);
		batch.addResourceFile(rf2);
		batch.addResourceFile(rf3);

		Assert.assertEquals(batch.getResourceFile(0), rf1);
		Assert.assertEquals(batch.getResourceFile(1), rf2);
		Assert.assertEquals(batch.getResourceFile(2), rf3);

		batch.removeResourceFile(rf2);
		batch.addResourceFile(rf2);

		Assert.assertEquals(batch.getResourceFile(0), rf1);
		Assert.assertEquals(batch.getResourceFile(1), rf3);
		Assert.assertEquals(batch.getResourceFile(2), rf2);
	}
}
