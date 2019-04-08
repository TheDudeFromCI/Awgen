package frameworks.resource;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import net.whg.frameworks.resource.Resource;
import net.whg.frameworks.resource.ResourceDatabase;

public class ResourceDatabaseTest
{
	@Test
	public void addResource()
	{
		ResourceDatabase database = new ResourceDatabase();
		Resource resource = Mockito.mock(Resource.class);

		Assert.assertEquals(database.getResourceCount(), 0);

		database.addResource(resource);
		database.addResource(resource);

		Assert.assertEquals(database.getResourceCount(), 1);
	}

	@Test
	public void removeResource()
	{
		ResourceDatabase database = new ResourceDatabase();
		Resource resource = Mockito.mock(Resource.class);
		database.addResource(resource);

		Assert.assertEquals(database.getResourceCount(), 1);

		database.removeResource(resource);

		Assert.assertEquals(database.getResourceCount(), 0);
	}

	@Test
	public void resourceIsDisposed()
	{
		ResourceDatabase database = new ResourceDatabase();
		Resource resource = Mockito.mock(Resource.class);

		database.addResource(resource);
		database.removeResource(resource);

		Mockito.verify(resource).dispose();
	}

	@Test
	public void allResourcesDisposed()
	{
		ResourceDatabase database = new ResourceDatabase();
		Resource resource1 = Mockito.mock(Resource.class);
		Resource resource2 = Mockito.mock(Resource.class);

		database.addResource(resource1);
		database.addResource(resource2);

		Mockito.verify(resource1, Mockito.never()).dispose();
		Mockito.verify(resource2, Mockito.never()).dispose();

		database.dispose();

		Mockito.verify(resource1).dispose();
		Mockito.verify(resource2).dispose();
	}

	@Test
	public void getResourceByIndex()
	{
		ResourceDatabase database = new ResourceDatabase();
		Resource resource0 = Mockito.mock(Resource.class);
		Resource resource1 = Mockito.mock(Resource.class);
		Resource resource2 = Mockito.mock(Resource.class);

		database.addResource(resource0);
		database.addResource(resource1);
		database.addResource(resource2);

		Assert.assertEquals(database.getResourceAt(0), resource0);
		Assert.assertEquals(database.getResourceAt(1), resource1);
		Assert.assertEquals(database.getResourceAt(2), resource2);
	}
}
