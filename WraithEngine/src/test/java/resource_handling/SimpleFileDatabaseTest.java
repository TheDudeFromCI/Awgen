package resource_handling;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import net.whg.we.main.Plugin;
import net.whg.we.resources.ResourceFile;
import net.whg.we.resources.SimpleFileDatabase;
import util.CommonMock;

public class SimpleFileDatabaseTest
{
	@Test
	public void getResource()
	{
		// Get a resource file and make sure it links to a file.

		SimpleFileDatabase db = CommonMock.getSimpleFileDatabase();
		Plugin plugin = CommonMock.getTestPlugin();

		ResourceFile resource = db.getResourceFile(plugin, "monkey_head.fbx");

		assertNotNull(resource);
		assertEquals(resource.getFileExtension(), "fbx");
		assertNotNull(resource.getFile());
	}

	public void getResourceNoPlugin()
	{
		// Make sure that nothing is returned is a plugin is not defined.

		SimpleFileDatabase db = CommonMock.getSimpleFileDatabase();

		ResourceFile resource = db.getResourceFile(null, "resource.obj");

		assertNull(resource);
	}

	public void getBrokenPath()
	{
		// Make sure that nothing is returned if the path is broken.

		SimpleFileDatabase db = CommonMock.getSimpleFileDatabase();

		ResourceFile resource = db.getResourceFile(null, "234098$%^a/asdhkjas!@#$%^&*");

		assertNull(resource);
	}
}
