package frameworks.resource;

import org.junit.Assert;
import org.junit.Test;
import net.whg.frameworks.resource.ResourceFile;

public class ResourceFileTest
{
	@Test
	public void equals()
	{
		ResourceFile a = new ResourceFile("path/to/file.txt");
		ResourceFile b = new ResourceFile("path/to/file.txt");

		Assert.assertEquals(a, b);
	}

	@Test
	public void notEqual()
	{
		ResourceFile a = new ResourceFile("path/to/file1.txt");
		ResourceFile b = new ResourceFile("path/to/file2.txt");

		Assert.assertNotEquals(a, b);
	}

	@Test
	public void getPathname()
	{
		ResourceFile res = new ResourceFile("file.txt");

		Assert.assertEquals("file.txt", res.getPathname());
	}

	@Test
	public void getFileExtention()
	{
		ResourceFile res = new ResourceFile("path/to/file.txt");

		Assert.assertEquals("txt", res.getFileExtension());
	}

	@Test
	public void asString()
	{
		ResourceFile res = new ResourceFile("path/to/file.txt");

		Assert.assertEquals("[Res: path/to/file.txt]", res.toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void wrongPathname()
	{
		new ResourceFile("/broken pathname %");
	}

	@Test
	public void hashCode_equals()
	{
		ResourceFile a = new ResourceFile("path/to/file.txt");
		ResourceFile b = new ResourceFile("path/to/file.txt");

		Assert.assertEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void hashCode_notEquals()
	{
		ResourceFile a = new ResourceFile("path/to/file1.txt");
		ResourceFile b = new ResourceFile("path/to/file2.txt");

		Assert.assertNotEquals(a.hashCode(), b.hashCode());
	}
}
