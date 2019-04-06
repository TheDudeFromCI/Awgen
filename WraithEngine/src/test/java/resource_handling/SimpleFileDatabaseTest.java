package resource_handling;

import java.io.File;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import net.whg.frameworks.resource.ResourceFile;
import net.whg.frameworks.resource.SimpleFileDatabase;
import net.whg.we.main.Plugin;

public class SimpleFileDatabaseTest
{
	@Test
	public void getResource()
	{
		File workingDir = new File(System.getProperty("user.dir"));
		SimpleFileDatabase db = new SimpleFileDatabase(workingDir);
		Plugin plugin = Mockito.mock(Plugin.class);
		Mockito.when(plugin.getPluginName()).thenReturn("TestPlugin");

		ResourceFile resource = db.getResourceFile(plugin, "Unit Tests/simply.yml");

		Assert.assertNotNull(resource);
		Assert.assertEquals(resource.getFileExtension(), "yml");
		Assert.assertNotNull(resource.getFile());
	}

	@Test
	public void getResourceNoPlugin()
	{
		File workingDir = new File(System.getProperty("user.dir"));
		SimpleFileDatabase db = new SimpleFileDatabase(workingDir);

		ResourceFile resource = db.getResourceFile(null, "Unit Tests/simply.yml");

		Assert.assertNull(resource);
	}

	@Test
	public void getBrokenPath()
	{
		File workingDir = new File(System.getProperty("user.dir"));
		SimpleFileDatabase db = new SimpleFileDatabase(workingDir);

		ResourceFile resource = db.getResourceFile(null, "234098$%^a/asdhkjas!@#$%^&*");

		Assert.assertNull(resource);
	}
}
