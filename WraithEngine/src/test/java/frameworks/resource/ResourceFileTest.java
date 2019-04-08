package frameworks.resource;

import org.junit.Assert;
import org.junit.Test;
import net.whg.frameworks.resource.ResourceFile;

public class ResourceFileTest
{
	@Test
	public void equals_SamePath_SameName()
	{
		ResourceFile a = new ResourceFile("path/to/file.txt:name");
		ResourceFile b = new ResourceFile("path/to/file.txt:name");

		Assert.assertEquals(a, b);
	}

	@Test
	public void notEqual_SamePath_DifferentName()
	{
		ResourceFile a = new ResourceFile("path/to/file.txt:name1");
		ResourceFile b = new ResourceFile("path/to/file.txt:name2");

		Assert.assertNotEquals(a, b);
	}

	@Test
	public void equals_SamePath_NoName()
	{
		ResourceFile a = new ResourceFile("path/to/file.txt");
		ResourceFile b = new ResourceFile("path/to/file.txt");

		Assert.assertEquals(a, b);
	}

	@Test
	public void notEqual_DifferentPath_SameName()
	{
		ResourceFile a = new ResourceFile("path/to/file1.txt:name");
		ResourceFile b = new ResourceFile("path/to/file2.txt:name");

		Assert.assertNotEquals(a, b);
	}

	@Test
	public void notEqual_DifferentPath_DifferentName()
	{
		ResourceFile a = new ResourceFile("path/to/file1.txt:name1");
		ResourceFile b = new ResourceFile("path/to/file2.txt:name2");

		Assert.assertNotEquals(a, b);
	}

	@Test
	public void notEqual_DifferentObject()
	{
		ResourceFile a = new ResourceFile("file.txt");
		Assert.assertNotEquals(a, new Object());
	}

	@Test
	public void getSimpleathname()
	{
		ResourceFile res = new ResourceFile("file.txt:name");

		Assert.assertEquals("file.txt", res.getPathname());
	}

	@Test
	public void getFullPathname()
	{
		ResourceFile res = new ResourceFile("file.txt:name");

		Assert.assertEquals("file.txt:name", res.getFullPathname());
	}

	@Test
	public void getName()
	{
		ResourceFile res = new ResourceFile("file.txt:name");

		Assert.assertEquals("name", res.getName());
	}

	@Test
	public void getDefaultName()
	{
		ResourceFile res = new ResourceFile("path/to/file.txt");

		Assert.assertEquals("default", res.getName());
	}

	@Test
	public void getFileExtention()
	{
		ResourceFile res = new ResourceFile("path/to/file.txt");

		Assert.assertEquals("txt", res.getFileExtension());
	}

	@Test
	public void getFileExtention_WithResource()
	{
		ResourceFile res = new ResourceFile("path/to/file.txt:res");

		Assert.assertEquals("txt", res.getFileExtension());
	}

	@Test
	public void asString()
	{
		ResourceFile res = new ResourceFile("path/to/file.txt");

		Assert.assertEquals("[Res: path/to/file.txt:default]", res.toString());
	}
}
