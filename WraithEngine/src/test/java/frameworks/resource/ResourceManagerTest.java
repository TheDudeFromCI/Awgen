package frameworks.resource;

import java.io.File;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import net.whg.frameworks.resource.FileDatabase;
import net.whg.frameworks.resource.Resource;
import net.whg.frameworks.resource.ResourceDatabase;
import net.whg.frameworks.resource.ResourceFile;
import net.whg.frameworks.resource.ResourceLoader;
import net.whg.frameworks.resource.ResourceManager;

public class ResourceManagerTest
{
	@Test
	public void init()
	{
		ResourceDatabase database = new ResourceDatabase();
		ResourceLoader loader = new ResourceLoader();
		FileDatabase fileDatabase = Mockito.mock(FileDatabase.class);
		ResourceManager manager = new ResourceManager(database, loader, fileDatabase);

		Assert.assertEquals(database, manager.getResourceDatabase());
		Assert.assertEquals(fileDatabase, manager.getFileDatabase());
		Assert.assertEquals(loader, manager.getResourceLoader());
	}

	@Test
	public void disposeAllResources()
	{
		ResourceDatabase database = Mockito.mock(ResourceDatabase.class);
		ResourceLoader loader = new ResourceLoader();
		FileDatabase fileDatabase = Mockito.mock(FileDatabase.class);
		ResourceManager manager = new ResourceManager(database, loader, fileDatabase);

		manager.disposeAllResources();

		Mockito.verify(database).dispose();
	}

	@Test
	public void getFile()
	{
		ResourceDatabase database = new ResourceDatabase();
		ResourceLoader loader = new ResourceLoader();
		FileDatabase fileDatabase = Mockito.mock(FileDatabase.class);
		ResourceManager manager = new ResourceManager(database, loader, fileDatabase);

		ResourceFile resourceFile = new ResourceFile("res.bat");
		File file = Mockito.mock(File.class);
		Mockito.when(fileDatabase.getFile(resourceFile)).thenReturn(file);

		Assert.assertEquals(file, manager.getFile(resourceFile));
	}

	@Test
	public void loadResource()
	{
		ResourceDatabase database = new ResourceDatabase();
		ResourceLoader loader = Mockito.mock(ResourceLoader.class);
		FileDatabase fileDatabase = Mockito.mock(FileDatabase.class);
		ResourceManager manager = new ResourceManager(database, loader, fileDatabase);

		Resource resource = Mockito.mock(Resource.class);
		ResourceFile resourceFile = new ResourceFile("res.bat");
		Mockito.when(loader.loadResource(resourceFile, manager)).thenReturn(resource);

		Assert.assertEquals(resource, manager.loadResource(resourceFile));
	}
}
