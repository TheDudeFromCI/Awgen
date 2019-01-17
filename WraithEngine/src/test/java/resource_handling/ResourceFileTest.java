package resource_handling;

import org.junit.Assert;
import org.junit.Test;
import net.whg.we.main.Plugin;
import util.CommonMock;
import net.whg.we.resources.ResourceFile;
import net.whg.we.resources.SimpleFileDatabase;

public class ResourceFileTest
{
	@Test
	public void resourceFileEquals()
	{
		Plugin plugin = CommonMock.getTestPlugin();
		SimpleFileDatabase db = CommonMock.getSimpleFileDatabase();

		ResourceFile a = db.getResourceFile(plugin, "abc");
		ResourceFile b = db.getResourceFile(plugin, "abc");

		Assert.assertEquals(a, b);
	}

	@Test
	public void resourceFileNotEqualPath()
	{
		Plugin plugin = CommonMock.getTestPlugin();
		SimpleFileDatabase db = CommonMock.getSimpleFileDatabase();

		ResourceFile a = db.getResourceFile(plugin, "abc");
		ResourceFile b = db.getResourceFile(plugin, "def");

		Assert.assertNotEquals(a, b);
	}

	@Test
	public void resourceFileNotEqualPlugin()
	{
		Plugin plugin1 = CommonMock.getTestPlugin();
		Plugin plugin2 = CommonMock.getTestPlugin();
		SimpleFileDatabase db = CommonMock.getSimpleFileDatabase();

		ResourceFile a = db.getResourceFile(plugin1, "abc");
		ResourceFile b = db.getResourceFile(plugin2, "abc");

		Assert.assertNotEquals(a, b);
	}
}
