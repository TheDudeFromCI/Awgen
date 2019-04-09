package frameworks.resource;

import org.junit.Assert;
import org.junit.Test;
import net.whg.frameworks.resource.FileUtils;

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
		Assert.assertEquals("fbx:human", FileUtils.getFileExtention("abc/file.fbx:human"));
	}

	@Test
	public void correctSimpleFileName()
	{
		Assert.assertNull(FileUtils.getSimpleFileName(null));

		Assert.assertEquals("file.txt", FileUtils.getSimpleFileName("path/to/file.txt"));
		Assert.assertEquals("folder", FileUtils.getSimpleFileName("path/to/folder"));
		Assert.assertEquals("image.png", FileUtils.getSimpleFileName("image.png"));
		Assert.assertEquals("", FileUtils.getSimpleFileName(""));
		Assert.assertEquals("model.fbx:human", FileUtils.getSimpleFileName("abc/model.fbx:human"));
	}

	@Test
	public void isValidPathname()
	{
		Assert.assertFalse(FileUtils.isValidPathname("12.30.bat"));
		Assert.assertFalse(FileUtils.isValidPathname("abdbat."));
		Assert.assertFalse(FileUtils.isValidPathname(" abd.bat"));
		Assert.assertFalse(FileUtils.isValidPathname("123//abd.bat123"));
		Assert.assertFalse(FileUtils.isValidPathname("/123/a/abd.bat123"));
		Assert.assertFalse(FileUtils.isValidPathname("123/a/abd.bat123/"));
		Assert.assertFalse(FileUtils.isValidPathname(".123/a/abd.bat123"));
		Assert.assertFalse(FileUtils.isValidPathname("a%basd"));
		Assert.assertFalse(FileUtils.isValidPathname("file.txt 2"));

		Assert.assertTrue(FileUtils.isValidPathname("abd.bat"));
		Assert.assertTrue(FileUtils.isValidPathname("1230.bat"));
		Assert.assertTrue(FileUtils.isValidPathname("abd_123.bat"));
		Assert.assertTrue(FileUtils.isValidPathname("abd.bat123"));
		Assert.assertTrue(FileUtils.isValidPathname("123/abd.bat123"));
		Assert.assertTrue(FileUtils.isValidPathname("123 asd/a/abd.bat123"));
		Assert.assertTrue(FileUtils.isValidPathname("123 asd/a_1/abasdd.baast123"));
		Assert.assertTrue(FileUtils.isValidPathname("123 asd/a_1/abasd.123"));
		Assert.assertTrue(FileUtils.isValidPathname("abd/a/b/cd._d_"));
		Assert.assertTrue(FileUtils.isValidPathname("abd.bat_"));
	}
}
