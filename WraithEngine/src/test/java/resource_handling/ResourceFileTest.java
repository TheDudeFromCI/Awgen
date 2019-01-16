package resource_handling;

import org.junit.Assert;
import org.junit.Test;
import net.whg.we.main.Plugin;
import util.CommonMock;
import net.whg.we.resources.ResourceFile;

public class ResourceFileTest
{
	@Test
	public void resourceFileEquals()
	{
		Plugin plugin = CommonMock.getTestPlugin();

		ResourceFile a = new ResourceFile(plugin, "abc");
		ResourceFile b = new ResourceFile(plugin, "abc");

		Assert.assertEquals(a, b);
	}

	@Test
	public void resourceFileNotEqualPath()
	{
		Plugin plugin = CommonMock.getTestPlugin();

		ResourceFile a = new ResourceFile(plugin, "abc");
		ResourceFile b = new ResourceFile(plugin, "def");

		Assert.assertNotEquals(a, b);
	}

	@Test
	public void resourceFileNotEqualPlugin()
	{
		Plugin plugin1 = CommonMock.getTestPlugin();
		Plugin plugin2 = CommonMock.getTestPlugin();

		ResourceFile a = new ResourceFile(plugin1, "abc");
		ResourceFile b = new ResourceFile(plugin2, "abc");

		Assert.assertNotEquals(a, b);
	}
}
