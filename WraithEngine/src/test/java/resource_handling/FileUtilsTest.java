package resource_handling;

import org.junit.Assert;
import org.junit.Test;
import net.whg.frameworks.util.FileUtils;

public class FileUtilsTest
{
	@Test
	public void correctFileExtentions()
	{
		Assert.assertEquals(FileUtils.getFileExtention(null), null);
		Assert.assertEquals(FileUtils.getFileExtention("abc"), null);
		Assert.assertEquals(FileUtils.getFileExtention("abc.txt"), "txt");
		Assert.assertEquals(FileUtils.getFileExtention("abc/def.txt0"), "txt0");
		Assert.assertEquals(FileUtils.getFileExtention("abc/def"), null);
		Assert.assertEquals(FileUtils.getFileExtention(""), null);
	}

	@Test
	public void correctSimpleFileName()
	{
		Assert.assertEquals(FileUtils.getSimpleFileName(null), null);
		Assert.assertEquals(FileUtils.getSimpleFileName("path/to/file.txt"), "file.txt");
		Assert.assertEquals(FileUtils.getSimpleFileName("path/to/folder"), "folder");
		Assert.assertEquals(FileUtils.getSimpleFileName("image.png"), "image.png");
		Assert.assertEquals(FileUtils.getSimpleFileName(""), "");
	}
}
