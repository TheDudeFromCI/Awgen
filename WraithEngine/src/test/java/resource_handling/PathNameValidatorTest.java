package resource_handling;

import org.junit.Assert;
import org.junit.Test;
import net.whg.we.resources.SimplePathNameValidator;

public class PathNameValidatorTest
{
	@Test
	public void pathName()
	{
		SimplePathNameValidator val = new SimplePathNameValidator();

		Assert.assertTrue(val.isValidPathName("abd.bat"));
		Assert.assertTrue(val.isValidPathName("1230.bat"));
		Assert.assertTrue(val.isValidPathName("abd_123.bat"));
		Assert.assertTrue(val.isValidPathName("abd.bat123"));
		Assert.assertFalse(val.isValidPathName("abdbat."));
		Assert.assertFalse(val.isValidPathName(" abd.bat"));
		Assert.assertFalse(val.isValidPathName("abd.bat_"));
		Assert.assertTrue(val.isValidPathName("123/abd.bat123"));
		Assert.assertFalse(val.isValidPathName("123//abd.bat123"));
		Assert.assertFalse(val.isValidPathName("/123/a/abd.bat123"));
		Assert.assertFalse(val.isValidPathName("123/a/abd.bat123/"));
		Assert.assertFalse(val.isValidPathName(".123/a/abd.bat123"));
		Assert.assertTrue(val.isValidPathName("123 asd/a/abd.bat123"));
		Assert.assertTrue(val.isValidPathName("123 asd/a_1/abasdd.baast123"));
		Assert.assertTrue(val.isValidPathName("123 asd/a_1/abasd"));
		Assert.assertFalse(val.isValidPathName("a%basd"));
	}
}
