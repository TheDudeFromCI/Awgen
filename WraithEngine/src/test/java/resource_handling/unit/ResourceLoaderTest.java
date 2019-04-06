package resource_handling.unit;

import java.io.File;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import net.whg.frameworks.resource.FileLoader;
import net.whg.frameworks.resource.Resource;
import net.whg.frameworks.resource.ResourceDatabase;
import net.whg.frameworks.resource.ResourceFile;
import net.whg.frameworks.resource.ResourceLoader;
import net.whg.we.main.Plugin;

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
	public void loadFile_NotInDatabase()
	{
		ResourceLoader resourceLoader = new ResourceLoader();
		ResourceDatabase database = new ResourceDatabase();
		FileLoader fileLoader = Mockito.mock(FileLoader.class);
		Plugin plugin = Mockito.mock(Plugin.class);
		File file = new File(".");
		ResourceFile resourceFile = new ResourceFile(plugin, "file.txt", file);
		Resource resource = Mockito.mock(Resource.class);

		resourceLoader.addFileLoader(fileLoader);

		Mockito.doReturn(new String[]
		{
				"txt"
		}).when(fileLoader).getTargetFileTypes();
		Mockito.doReturn(resource).when(fileLoader).loadFile(resourceLoader, database,
				resourceFile);

		Assert.assertEquals(resource, resourceLoader.loadResource(resourceFile, database));
	}

	@Test
	public void loadFile_InDatabase()
	{
		ResourceLoader resourceLoader = new ResourceLoader();
		ResourceDatabase database = new ResourceDatabase();
		FileLoader fileLoader = Mockito.mock(FileLoader.class);
		Plugin plugin = Mockito.mock(Plugin.class);
		File file = new File(".");
		ResourceFile resourceFile = new ResourceFile(plugin, "file.txt", file);
		Resource resource = Mockito.mock(Resource.class);
		Mockito.doReturn(resourceFile).when(resource).getResourceFile();
		database.addResource(resource);

		resourceLoader.addFileLoader(fileLoader);

		Assert.assertEquals(resource, resourceLoader.loadResource(resourceFile, database));
		Mockito.verify(fileLoader, Mockito.never()).loadFile(resourceLoader, database,
				resourceFile);
	}
}
