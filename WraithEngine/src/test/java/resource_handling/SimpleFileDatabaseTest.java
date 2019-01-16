package resource_handling;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import java.io.File;
import org.junit.Test;
import net.whg.we.main.Plugin;
import net.whg.we.resources.ResourceFile;
import net.whg.we.resources.SimpleFileDatabase;

public class SimpleFileDatabaseTest
{
	private SimpleFileDatabase getDatabase()
	{
		File workingDir = new File(System.getProperty("user.dir"));
		return new SimpleFileDatabase(workingDir);
	}

	private Plugin getTestPlugin()
	{
		return new Plugin()
		{

			@Override
			public String getPluginName()
			{
				return "TestPlugin";
			}

			@Override
			public void initPlugin()
			{
			}

			@Override
			public void enablePlugin()
			{
			}

			@Override
			public int getPriority()
			{
				return 0;
			}
		};
	}

	@Test
	public void getResource()
	{
		// Get a resource file and make sure it links to a file.

		SimpleFileDatabase db = getDatabase();
		Plugin plugin = getTestPlugin();

		ResourceFile resource = db.getResourceFile(plugin, "monkey_head.fbx");

		assertNotNull(resource);
		assertEquals(resource.getFileExtension(), "fbx");
		assertNotNull(resource.getFile());
	}

	public void getResourceNoPlugin()
	{
		// Make sure that nothing is returned is a plugin is not defined.

		SimpleFileDatabase db = getDatabase();

		ResourceFile resource = db.getResourceFile(null, "resource.obj");

		assertNull(resource);
	}

	public void getBrokenPath()
	{
		// Make sure that nothing is returned if the path is broken.

		SimpleFileDatabase db = getDatabase();

		ResourceFile resource = db.getResourceFile(null, "234098$%^a/asdhkjas!@#$%^&*");

		assertNull(resource);
	}
}
