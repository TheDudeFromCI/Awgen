package frameworks.resource;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import net.whg.frameworks.resource.Resource;
import net.whg.frameworks.resource.ResourceDatabase;
import net.whg.frameworks.resource.ResourceFile;

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
		database.addResource(null);

		Assert.assertEquals(1, database.getResourceCount());
	}

	@Test
	public void removeResource()
	{
		ResourceDatabase database = new ResourceDatabase();
		Resource resource = Mockito.mock(Resource.class);

		database.addResource(resource);
		database.removeResource(null);

		Assert.assertEquals(1, database.getResourceCount());

		database.removeResource(resource);

		Assert.assertEquals(0, database.getResourceCount());
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

		Assert.assertEquals(resource0, database.getResourceAt(0));
		Assert.assertEquals(resource1, database.getResourceAt(1));
		Assert.assertEquals(resource2, database.getResourceAt(2));
	}

	@Test
	public void getResourceByResourceFile()
	{
		ResourceDatabase database = new ResourceDatabase();
		Resource resource0 = Mockito.mock(Resource.class);
		Resource resource1 = Mockito.mock(Resource.class);
		Resource resource2 = Mockito.mock(Resource.class);

		ResourceFile resourceFile0 = Mockito.mock(ResourceFile.class);
		ResourceFile resourceFile1 = Mockito.mock(ResourceFile.class);
		ResourceFile resourceFile2 = Mockito.mock(ResourceFile.class);
		ResourceFile resourceFile3 = Mockito.mock(ResourceFile.class);

		Mockito.when(resource0.getResourceFile()).thenReturn(resourceFile0);
		Mockito.when(resource1.getResourceFile()).thenReturn(resourceFile1);
		Mockito.when(resource2.getResourceFile()).thenReturn(resourceFile2);

		database.addResource(resource0);
		database.addResource(resource1);
		database.addResource(resource2);

		Assert.assertEquals(resource0, database.getResource(resourceFile0));
		Assert.assertEquals(resource1, database.getResource(resourceFile1));
		Assert.assertEquals(resource2, database.getResource(resourceFile2));
		Assert.assertNull(database.getResource(resourceFile3));
		Assert.assertNull(database.getResource(null));
	}

	@Test
	public void getResourceByResourceFile_ResourceReturnsNullResourceFile()
	{
		ResourceDatabase database = new ResourceDatabase();
		Resource resource = Mockito.mock(Resource.class);

		Mockito.when(resource.getResourceFile()).thenReturn(null);

		database.addResource(resource);

		Assert.assertNull(database.getResource(new ResourceFile("res.txt")));
	}

	@Test
	public void forEach()
	{
		ResourceDatabase database = new ResourceDatabase();
		Resource resource0 = Mockito.mock(Resource.class);
		Resource resource1 = Mockito.mock(Resource.class);
		Resource resource2 = Mockito.mock(Resource.class);

		database.addResource(resource0);
		database.addResource(resource1);
		database.addResource(resource2);

		database.forEach(res ->
		{
			res.reload();
		});

		Mockito.verify(resource0).reload();
		Mockito.verify(resource1).reload();
		Mockito.verify(resource2).reload();
	}
}
