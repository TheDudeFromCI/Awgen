package resource_handling.unit;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import net.whg.we.main.Plugin;
import net.whg.we.resources.FileLoader;
import net.whg.we.resources.Resource;
import net.whg.we.resources.ResourceDatabase;
import net.whg.we.resources.ResourceFile;
import net.whg.we.resources.ResourceLoader;

public class ResourceLoaderTests
{
	@SuppressWarnings("unchecked")
	@Test
	public void addFileLoader()
	{
		ResourceLoader resourceLoader = new ResourceLoader();
		FileLoader<Object> fileLoader = Mockito.mock(FileLoader.class);

		Assert.assertEquals(resourceLoader.getFileLoaderCount(), 0);

		resourceLoader.addFileLoader(fileLoader);
		resourceLoader.addFileLoader(fileLoader);

		Assert.assertEquals(resourceLoader.getFileLoaderCount(), 1);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void removeFileLoader()
	{
		ResourceLoader resourceLoader = new ResourceLoader();
		FileLoader<Object> fileLoader = Mockito.mock(FileLoader.class);
		resourceLoader.addFileLoader(fileLoader);

		resourceLoader.removeFileLoader(fileLoader);

		Assert.assertEquals(resourceLoader.getFileLoaderCount(), 0);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getFileLoader()
	{
		ResourceLoader resourceLoader = new ResourceLoader();
		FileLoader<Object> fileLoader0 = Mockito.mock(FileLoader.class);
		FileLoader<Object> fileLoader1 = Mockito.mock(FileLoader.class);
		FileLoader<Object> fileLoader2 = Mockito.mock(FileLoader.class);

		resourceLoader.addFileLoader(fileLoader0);
		resourceLoader.addFileLoader(fileLoader1);
		resourceLoader.addFileLoader(fileLoader2);

		Assert.assertEquals(resourceLoader.getFileLoader(0), fileLoader0);
		Assert.assertEquals(resourceLoader.getFileLoader(1), fileLoader1);
		Assert.assertEquals(resourceLoader.getFileLoader(2), fileLoader2);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void loadFile_NotInDatabase()
	{
		ResourceLoader resourceLoader = new ResourceLoader();
		ResourceDatabase database = new ResourceDatabase();
		FileLoader<Object> fileLoader = Mockito.mock(FileLoader.class);
		Plugin plugin = Mockito.mock(Plugin.class);
		ResourceFile resourceFile = new ResourceFile(plugin, "file.txt", null, null);
		Resource<Object> resource = Mockito.mock(Resource.class);

		resourceLoader.addFileLoader(fileLoader);
		Mockito.doReturn(resource).when(fileLoader).loadFile(resourceLoader, database,
				resourceFile);

		Assert.assertEquals(resource, resourceLoader.loadResource(resourceFile, database));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void loadFile_InDatabase()
	{
		ResourceLoader resourceLoader = new ResourceLoader();
		ResourceDatabase database = new ResourceDatabase();
		FileLoader<Object> fileLoader = Mockito.mock(FileLoader.class);
		Plugin plugin = Mockito.mock(Plugin.class);
		ResourceFile resourceFile = new ResourceFile(plugin, "file.txt", null, null);
		Resource<Object> resource = Mockito.mock(Resource.class);
		database.addResource(resource);

		resourceLoader.addFileLoader(fileLoader);

		Assert.assertEquals(resource, resourceLoader.loadResource(resourceFile, database));
		Mockito.verify(fileLoader, Mockito.calls(0));
	}
}
