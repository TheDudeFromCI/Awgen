package resource_handling;

import org.junit.Assert;
import org.junit.Test;
import net.whg.we.utils.FileUtils;

public class FileUtilsTest
{
	@Test
	public void correctFileExtentions()
	{
		Assert.assertEquals(FileUtils.getFileExtention("abc"), null);
		Assert.assertEquals(FileUtils.getFileExtention("abc.txt"), "txt");
		Assert.assertEquals(FileUtils.getFileExtention("abc/def.txt0"), "txt0");
		Assert.assertEquals(FileUtils.getFileExtention("abc/def"), null);
	}
}
