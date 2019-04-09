package frameworks.resource;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import net.whg.frameworks.resource.FileDatabase;
import net.whg.frameworks.resource.FileLoader;
import net.whg.frameworks.resource.Resource;
import net.whg.frameworks.resource.ResourceData;
import net.whg.frameworks.resource.ResourceDatabase;
import net.whg.frameworks.resource.ResourceFile;
import net.whg.frameworks.resource.ResourceFuture;
import net.whg.frameworks.resource.ResourceLoader;
import net.whg.frameworks.resource.ResourceManager;
import net.whg.frameworks.resource.UnsupportedFileFormat;

public class ResourceLoaderTest
{
	@Test
	public void addFileLoader()
	{
		ResourceLoader resourceLoader = new ResourceLoader();
		FileLoader fileLoader = Mockito.mock(FileLoader.class);

		Assert.assertEquals(resourceLoader.getFileLoaderCount(), 0);

		resourceLoader.addFileLoader(fileLoader);
		resourceLoader.addFileLoader(fileLoader);

		Assert.assertEquals(resourceLoader.getFileLoaderCount(), 1);
	}

	@Test
	public void removeFileLoader()
	{
		ResourceLoader resourceLoader = new ResourceLoader();
		FileLoader fileLoader = Mockito.mock(FileLoader.class);
		resourceLoader.addFileLoader(fileLoader);

		resourceLoader.removeFileLoader(fileLoader);

		Assert.assertEquals(resourceLoader.getFileLoaderCount(), 0);
	}

	@Test
	public void getFileLoader()
	{
		ResourceLoader resourceLoader = new ResourceLoader();
		FileLoader fileLoader0 = Mockito.mock(FileLoader.class);
		FileLoader fileLoader1 = Mockito.mock(FileLoader.class);
		FileLoader fileLoader2 = Mockito.mock(FileLoader.class);

		resourceLoader.addFileLoader(fileLoader0);
		resourceLoader.addFileLoader(fileLoader1);
		resourceLoader.addFileLoader(fileLoader2);

		Assert.assertEquals(resourceLoader.getFileLoader(0), fileLoader0);
		Assert.assertEquals(resourceLoader.getFileLoader(1), fileLoader1);
		Assert.assertEquals(resourceLoader.getFileLoader(2), fileLoader2);
	}

	@Test
	public void loadResource_AlreadyInDatabase()
	{
		ResourceLoader loader = new ResourceLoader();
		ResourceDatabase database = Mockito.mock(ResourceDatabase.class);
		FileDatabase fileDatabase = Mockito.mock(FileDatabase.class);
		ResourceManager manager = new ResourceManager(database, loader, fileDatabase);

		ResourceFile resourceFile = new ResourceFile("abc.txt");
		ResourceData resourceData = Mockito.mock(ResourceData.class);
		Resource resource = new Resource(resourceFile, resourceData);

		Mockito.when(database.getResource(resourceFile)).thenReturn(resource);

		Assert.assertEquals(resource, loader.loadResource(resourceFile, manager));
	}

	@Test
	public void loadResource_Normal()
	{
		ResourceLoader loader = new ResourceLoader();
		ResourceDatabase database = Mockito.mock(ResourceDatabase.class);
		FileDatabase fileDatabase = Mockito.mock(FileDatabase.class);
		ResourceManager manager = new ResourceManager(database, loader, fileDatabase);
		Mockito.when(database.getResource(Mockito.any())).thenReturn(null);

		ResourceFile resourceFile = new ResourceFile("abc.txt");
		ResourceFuture future = Mockito.mock(ResourceFuture.class);

		FileLoader fileLoader = Mockito.mock(FileLoader.class);
		Mockito.when(fileLoader.getTargetFileTypes()).thenReturn(new String[]
		{
				"txt"
		});
		Mockito.when(fileLoader.loadFile(manager, resourceFile)).thenReturn(future);
		Mockito.when(fileLoader.createDataInstace()).thenReturn(Mockito.mock(ResourceData.class));
		loader.addFileLoader(fileLoader);

		Resource resource = loader.loadResource(resourceFile, manager);
		resource.reload();

		Assert.assertEquals(resourceFile, resource.getResourceFile());
		Mockito.verify(future).sync(Mockito.any(ResourceData.class));
	}

	@Test(expected = UnsupportedFileFormat.class)
	public void loadResource_NoFileLoaders()
	{
		ResourceLoader loader = new ResourceLoader();
		ResourceDatabase database = Mockito.mock(ResourceDatabase.class);
		FileDatabase fileDatabase = Mockito.mock(FileDatabase.class);
		ResourceManager manager = new ResourceManager(database, loader, fileDatabase);
		Mockito.when(database.getResource(Mockito.any())).thenReturn(null);

		ResourceFile resourceFile = new ResourceFile("abc.txt");
		loader.loadResource(resourceFile, manager);
	}

	@Test(expected = UnsupportedFileFormat.class)
	public void loadResource_NoSupportedFileLoaders()
	{
		ResourceLoader loader = new ResourceLoader();
		ResourceDatabase database = Mockito.mock(ResourceDatabase.class);
		FileDatabase fileDatabase = Mockito.mock(FileDatabase.class);
		ResourceManager manager = new ResourceManager(database, loader, fileDatabase);
		Mockito.when(database.getResource(Mockito.any())).thenReturn(null);

		FileLoader fileLoader1 = Mockito.mock(FileLoader.class);
		FileLoader fileLoader2 = Mockito.mock(FileLoader.class);
		FileLoader fileLoader3 = Mockito.mock(FileLoader.class);
		Mockito.when(fileLoader1.getTargetFileTypes()).thenReturn(new String[]
		{
				"a", "b", "c"
		});
		Mockito.when(fileLoader2.getTargetFileTypes()).thenReturn(new String[]
		{
				"1", "2"
		});
		Mockito.when(fileLoader3.getTargetFileTypes()).thenReturn(new String[]
		{
				"abc"
		});

		loader.addFileLoader(fileLoader1);
		loader.addFileLoader(fileLoader2);
		loader.addFileLoader(fileLoader3);

		ResourceFile resourceFile = new ResourceFile("abc.txt");
		loader.loadResource(resourceFile, manager);
	}
}
