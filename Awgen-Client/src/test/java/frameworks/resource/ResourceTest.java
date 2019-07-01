package frameworks.resource;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import net.whg.frameworks.resource.Resource;
import net.whg.frameworks.resource.ResourceData;
import net.whg.frameworks.resource.ResourceFile;
import net.whg.frameworks.resource.ResourceFuture;
import net.whg.frameworks.resource.ResourceState;
import net.whg.frameworks.resource.UnexpectedResourceState;

public class ResourceTest
{
	@Test
	public void init()
	{
		ResourceFile resourceFile = new ResourceFile("test.txt");
		ResourceData resourceData = Mockito.mock(ResourceData.class);
		Resource resource = new Resource(resourceFile, resourceData);

		Assert.assertEquals(resourceFile, resource.getResourceFile());
		Assert.assertNull(resource.getData());
		Assert.assertEquals(ResourceState.UNLOADED, resource.getResourceState());
	}

	@Test
	public void setResourceFuture()
	{
		ResourceFile resourceFile = new ResourceFile("test.txt");
		ResourceData resourceData = Mockito.mock(ResourceData.class);
		Resource resource = new Resource(resourceFile, resourceData);

		ResourceFuture future = Mockito.mock(ResourceFuture.class);
		resource.setResourceFuture(future);

		Assert.assertNull(resource.getData());
		Assert.assertEquals(ResourceState.UNLOADED, resource.getResourceState());
	}

	@Test
	public void reload_NoChange()
	{
		ResourceFile resourceFile = new ResourceFile("test.txt");
		ResourceData resourceData = Mockito.mock(ResourceData.class);
		Resource resource = new Resource(resourceFile, resourceData);

		ResourceFuture future = Mockito.mock(ResourceFuture.class);
		Mockito.when(future.sync(resourceData)).thenReturn(ResourceFuture.NO_CHANGE);
		resource.setResourceFuture(future);
		resource.reload();

		Assert.assertNull(resource.getData());
		Assert.assertEquals(ResourceState.UNLOADED, resource.getResourceState());
	}

	@Test
	public void reload_PartiallyLoaded()
	{
		ResourceFile resourceFile = new ResourceFile("test.txt");
		ResourceData resourceData = Mockito.mock(ResourceData.class);
		Resource resource = new Resource(resourceFile, resourceData);

		ResourceFuture future = Mockito.mock(ResourceFuture.class);
		Mockito.when(future.sync(resourceData)).thenReturn(ResourceFuture.PARTIALLY_LOADED);
		resource.setResourceFuture(future);
		resource.reload();

		Assert.assertEquals(resourceData, resource.getData());
		Assert.assertEquals(ResourceState.PARTIALLY_LOADED, resource.getResourceState());
	}

	@Test
	public void reload_FullyLoaded()
	{
		ResourceFile resourceFile = new ResourceFile("test.txt");
		ResourceData resourceData = Mockito.mock(ResourceData.class);
		Resource resource = new Resource(resourceFile, resourceData);

		ResourceFuture future = Mockito.mock(ResourceFuture.class);
		Mockito.when(future.sync(resourceData)).thenReturn(ResourceFuture.FULLY_LOADED);
		resource.setResourceFuture(future);
		resource.reload();

		Assert.assertEquals(resourceData, resource.getData());
		Assert.assertEquals(ResourceState.FULLY_LOADED, resource.getResourceState());
	}

	@Test
	public void reload_UnableToLoad()
	{
		ResourceFile resourceFile = new ResourceFile("test.txt");
		ResourceData resourceData = Mockito.mock(ResourceData.class);
		Resource resource = new Resource(resourceFile, resourceData);

		ResourceFuture future = Mockito.mock(ResourceFuture.class);
		Mockito.when(future.sync(resourceData)).thenReturn(ResourceFuture.UNABLE_TO_LOAD);
		resource.setResourceFuture(future);
		resource.reload();

		Assert.assertNull(resource.getData());
		Assert.assertEquals(ResourceState.UNABLE_TO_LOAD, resource.getResourceState());
	}

	@Test(expected = UnexpectedResourceState.class)
	public void reload_UnexpectedResponse()
	{
		ResourceFile resourceFile = new ResourceFile("test.txt");
		ResourceData resourceData = Mockito.mock(ResourceData.class);
		Resource resource = new Resource(resourceFile, resourceData);

		ResourceFuture future = Mockito.mock(ResourceFuture.class);
		Mockito.when(future.sync(resourceData)).thenReturn(-145961);
		resource.setResourceFuture(future);
		resource.reload();
	}

	@Test
	public void dispose()
	{
		ResourceFile resourceFile = new ResourceFile("test.txt");
		ResourceData resourceData = Mockito.mock(ResourceData.class);
		Resource resource = new Resource(resourceFile, resourceData);

		ResourceFuture future = Mockito.mock(ResourceFuture.class);
		Mockito.when(future.sync(resourceData)).thenReturn(ResourceFuture.FULLY_LOADED);
		resource.setResourceFuture(future);
		resource.reload();

		Assert.assertEquals(resourceData, resource.getData());
		Assert.assertEquals(ResourceState.FULLY_LOADED, resource.getResourceState());

		resource.dispose();

		Assert.assertNull(resource.getData());
		Assert.assertEquals(ResourceState.DISPOSED, resource.getResourceState());
	}

	@Test
	public void setResourceFuture_Null_PartiallyLoaded()
	{
		ResourceFile resourceFile = new ResourceFile("test.txt");
		ResourceData resourceData = Mockito.mock(ResourceData.class);
		Resource resource = new Resource(resourceFile, resourceData);

		ResourceFuture future = Mockito.mock(ResourceFuture.class);
		Mockito.when(future.sync(resourceData)).thenReturn(ResourceFuture.PARTIALLY_LOADED);
		resource.setResourceFuture(future);
		resource.reload();

		resource.setResourceFuture(null);
		resource.reload();

		Assert.assertEquals(resourceData, resource.getData());
		Assert.assertEquals(ResourceState.FULLY_LOADED, resource.getResourceState());
	}

	@Test
	public void setResourceFuture_Null_Unloaded()
	{
		ResourceFile resourceFile = new ResourceFile("test.txt");
		ResourceData resourceData = Mockito.mock(ResourceData.class);
		Resource resource = new Resource(resourceFile, resourceData);

		ResourceFuture future = Mockito.mock(ResourceFuture.class);
		Mockito.when(future.sync(resourceData)).thenReturn(ResourceFuture.NO_CHANGE);
		resource.setResourceFuture(future);
		resource.reload();

		resource.setResourceFuture(null);
		resource.reload();

		Assert.assertNull(resource.getData());
		Assert.assertEquals(ResourceState.UNLOADED, resource.getResourceState());
	}

	@Test
	public void setResourceFuture_Null_FullyLoaded()
	{
		ResourceFile resourceFile = new ResourceFile("test.txt");
		ResourceData resourceData = Mockito.mock(ResourceData.class);
		Resource resource = new Resource(resourceFile, resourceData);

		ResourceFuture future = Mockito.mock(ResourceFuture.class);
		Mockito.when(future.sync(resourceData)).thenReturn(ResourceFuture.FULLY_LOADED);
		resource.setResourceFuture(future);
		resource.reload();

		resource.setResourceFuture(null);
		resource.reload();

		Assert.assertEquals(resourceData, resource.getData());
		Assert.assertEquals(ResourceState.FULLY_LOADED, resource.getResourceState());
	}

	@Test
	public void setResourceFuture_StillLoading()
	{
		ResourceFile resourceFile = new ResourceFile("test.txt");
		ResourceData resourceData = Mockito.mock(ResourceData.class);
		Resource resource = new Resource(resourceFile, resourceData);

		ResourceFuture future = Mockito.mock(ResourceFuture.class);
		Mockito.when(future.sync(resourceData)).thenReturn(ResourceFuture.PARTIALLY_LOADED);
		resource.setResourceFuture(future);
		resource.reload();

		ResourceFuture future2 = Mockito.mock(ResourceFuture.class);
		resource.setResourceFuture(future2);

		Assert.assertEquals(resourceData, resource.getData());
		Assert.assertEquals(ResourceState.OUTDATED, resource.getResourceState());
	}

	@Test
	public void setResourceFuture_FullyLoaded()
	{
		ResourceFile resourceFile = new ResourceFile("test.txt");
		ResourceData resourceData = Mockito.mock(ResourceData.class);
		Resource resource = new Resource(resourceFile, resourceData);

		ResourceFuture future = Mockito.mock(ResourceFuture.class);
		Mockito.when(future.sync(resourceData)).thenReturn(ResourceFuture.FULLY_LOADED);
		resource.setResourceFuture(future);
		resource.reload();

		ResourceFuture future2 = Mockito.mock(ResourceFuture.class);
		resource.setResourceFuture(future2);

		Assert.assertEquals(resourceData, resource.getData());
		Assert.assertEquals(ResourceState.OUTDATED, resource.getResourceState());
	}

	@Test
	public void setResourceFuture_Unloaded()
	{
		ResourceFile resourceFile = new ResourceFile("test.txt");
		ResourceData resourceData = Mockito.mock(ResourceData.class);
		Resource resource = new Resource(resourceFile, resourceData);

		ResourceFuture future = Mockito.mock(ResourceFuture.class);
		Mockito.when(future.sync(resourceData)).thenReturn(ResourceFuture.NO_CHANGE);
		resource.setResourceFuture(future);
		resource.reload();

		ResourceFuture future2 = Mockito.mock(ResourceFuture.class);
		resource.setResourceFuture(future2);

		Assert.assertNull(resource.getData());
		Assert.assertEquals(ResourceState.UNLOADED, resource.getResourceState());
	}

	@Test
	public void setResourceFuture_Disposed()
	{
		ResourceFile resourceFile = new ResourceFile("test.txt");
		ResourceData resourceData = Mockito.mock(ResourceData.class);
		Resource resource = new Resource(resourceFile, resourceData);

		resource.dispose();

		Assert.assertNull(resource.getData());
		Assert.assertEquals(ResourceState.DISPOSED, resource.getResourceState());

		ResourceFuture future2 = Mockito.mock(ResourceFuture.class);
		resource.setResourceFuture(future2);

		Assert.assertNull(resource.getData());
		Assert.assertEquals(ResourceState.UNLOADED, resource.getResourceState());
	}
}
