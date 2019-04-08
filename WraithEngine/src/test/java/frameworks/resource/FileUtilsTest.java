package frameworks.resource;

import org.junit.Assert;
import org.junit.Test;
import net.whg.frameworks.util.FileUtils;

public class FileUtilsTest
{
	@Test
	public void correctFileExtentions()
	{
		Assert.assertNull(FileUtils.getFileExtention(null));
		Assert.assertNull(FileUtils.getFileExtention("abc"));
		Assert.assertNull(FileUtils.getFileExtention("abc/def"));
		Assert.assertNull(FileUtils.getFileExtention(""));
		Assert.assertNull(FileUtils.getFileExtention("abc/123:red"));

		Assert.assertEquals("txt", FileUtils.getFileExtention("abc.txt"));
		Assert.assertEquals("txt0", FileUtils.getFileExtention("abc/def.txt0"));
		Assert.assertEquals("fbx", FileUtils.getFileExtention("abc/file.fbx:human"));
	}

	@Test
	public void correctSimpleFileName()
	{
		Assert.assertNull(FileUtils.getSimpleFileName(null));

		Assert.assertEquals("file.txt", FileUtils.getSimpleFileName("path/to/file.txt"));
		Assert.assertEquals("folder", FileUtils.getSimpleFileName("path/to/folder"));
		Assert.assertEquals("image.png", FileUtils.getSimpleFileName("image.png"));
		Assert.assertEquals("", FileUtils.getSimpleFileName(""));
		Assert.assertEquals("model.fbx", FileUtils.getSimpleFileName("abc/model.fbx:human"));
	}

	@Test
	public void correctPathname()
	{
		Assert.assertNull(FileUtils.getPathnameWithoutResource(null));

		Assert.assertEquals("path/to/file.txt",
				FileUtils.getPathnameWithoutResource("path/to/file.txt"));
		Assert.assertEquals("folder", FileUtils.getPathnameWithoutResource("folder"));
		Assert.assertEquals("red", FileUtils.getPathnameWithoutResource("red:blue"));
		Assert.assertEquals("", FileUtils.getPathnameWithoutResource(""));
	}

	@Test
	public void correctAssetName()
	{
		Assert.assertNull(FileUtils.getPathnameOnlyResource(null));

		Assert.assertEquals("default", FileUtils.getPathnameOnlyResource("path/to/file.txt"));
		Assert.assertEquals("default", FileUtils.getPathnameOnlyResource("folder"));
		Assert.assertEquals("blue", FileUtils.getPathnameOnlyResource("red:blue"));
		Assert.assertEquals("default", FileUtils.getPathnameOnlyResource(""));
	}
}
