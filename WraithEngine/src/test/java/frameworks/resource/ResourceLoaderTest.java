package frameworks.resource;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import net.whg.frameworks.resource.FileDatabase;
import net.whg.frameworks.resource.FileLoader;
import net.whg.frameworks.resource.Resource;
import net.whg.frameworks.resource.ResourceDatabase;
import net.whg.frameworks.resource.ResourceFile;
import net.whg.frameworks.resource.ResourceLoader;
import net.whg.frameworks.resource.ResourceManager;

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
		ResourceFile resourceFile = new ResourceFile("file.txt");
		Resource resource = Mockito.mock(Resource.class);
		FileDatabase fileDatabase = Mockito.mock(FileDatabase.class);

		ResourceManager manager = new ResourceManager(database, resourceLoader, fileDatabase);

		resourceLoader.addFileLoader(fileLoader);

		Mockito.doReturn(new String[]
		{
				"txt"
		}).when(fileLoader).getTargetFileTypes();
		Mockito.doReturn(resource).when(fileLoader).loadFile(manager, resourceFile);

		Assert.assertEquals(resource, resourceLoader.loadResource(resourceFile, manager));
	}

	@Test
	public void loadFile_InDatabase()
	{
		ResourceLoader resourceLoader = new ResourceLoader();
		ResourceDatabase database = new ResourceDatabase();
		FileLoader fileLoader = Mockito.mock(FileLoader.class);
		ResourceFile resourceFile = new ResourceFile("file.txt");
		Resource resource = Mockito.mock(Resource.class);
		Mockito.doReturn(resourceFile).when(resource).getResourceFile();
		database.addResource(resource);
		FileDatabase fileDatabase = Mockito.mock(FileDatabase.class);

		ResourceManager manager = new ResourceManager(database, resourceLoader, fileDatabase);

		resourceLoader.addFileLoader(fileLoader);

		Assert.assertEquals(resource, resourceLoader.loadResource(resourceFile, manager));
		Mockito.verify(fileLoader, Mockito.never()).loadFile(manager, resourceFile);
	}
}
