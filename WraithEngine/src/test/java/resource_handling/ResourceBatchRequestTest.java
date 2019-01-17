package resource_handling;

import org.junit.Assert;
import org.junit.Test;
import net.whg.we.resources.ResourceBatchRequest;
import net.whg.we.resources.ResourceFile;
import net.whg.we.resources.Resource;
import net.whg.we.resources.SimpleFileDatabase;
import net.whg.we.main.Plugin;
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

		Resource<Object> res = new Resource<Object>()
		{
			@Override
			public Object getData()
			{
				return null;
			}

			@Override
			public ResourceFile getResourceFile()
			{
				SimpleFileDatabase db = CommonMock.getSimpleFileDatabase();
				return db.getResourceFile(CommonMock.getTestPlugin(), "abc");
			}

			@Override
			public void setResourceFile(ResourceFile file)
			{
			}

			@Override
			public void dispose()
			{
			}
		};

		batch.addResource(res);

		Assert.assertEquals(batch.getResourceCount(), 1);
	}

	@Test
	public void nextUnloadedResource()
	{
		ResourceBatchRequest batch = new ResourceBatchRequest();
		final Plugin plugin = CommonMock.getTestPlugin();
		final SimpleFileDatabase db = CommonMock.getSimpleFileDatabase();

		batch.addResourceFile(db.getResourceFile(plugin, "abc"));

		// Here we run twice to make sure repeating the command does not change the outcome.
		Assert.assertNotNull(batch.nextUnloadedResource());
		Assert.assertNotNull(batch.nextUnloadedResource());

		Resource<Object> res = new Resource<Object>()
		{
			@Override
			public Object getData()
			{
				return null;
			}

			@Override
			public ResourceFile getResourceFile()
			{
				return db.getResourceFile(plugin, "abc");
			}

			@Override
			public void setResourceFile(ResourceFile file)
			{
			}

			@Override
			public void dispose()
			{
			}
		};

		batch.addResource(res);

		Assert.assertNull(batch.nextUnloadedResource());
	}
}
