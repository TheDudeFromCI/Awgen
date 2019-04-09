package frameworks.resource;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import net.whg.frameworks.resource.FileLoader;
import net.whg.frameworks.resource.ResourceLoader;

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
}
