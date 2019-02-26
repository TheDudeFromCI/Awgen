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
		Assert.assertFalse(val.isValidPathName("12.30.bat"));
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
		Assert.assertTrue(val.isValidPathName("abd/a/b/cd")); //you should be able to have a path that doesn't end in a file?
	}

	@Test
	public void pathNameWithName()
	{
		SimplePathNameValidator val = new SimplePathNameValidator();

		Assert.assertTrue(val.isValidPathName("abd.bat:234"));
		Assert.assertTrue(val.isValidPathName("abd.bat:23 4"));
		Assert.assertFalse(val.isValidPathName("abd:23 4"));
		Assert.assertTrue(val.isValidPathName("abd/nas.123:23 4"));
		Assert.assertTrue(val.isValidPathName("abd/nas.123: d a y"));
		Assert.assertTrue(val.isValidPathName("abd/nas.123: d  % #@$%$^&#%@"));
		Assert.assertTrue(val.isValidPathName("a 0bd/nas.123:name"));
		Assert.assertFalse(val.isValidPathName("a 0bd/nas.123:name:"));
		Assert.assertTrue(val.isValidPathName("a 0bd/nas.123:name/234"));
		Assert.assertFalse(val.isValidPathName("a 0bd/nas.123:name:name2"));
		Assert.assertFalse(val.isValidPathName("a 0bd/nas.123/:name:name2"));
	}
}
