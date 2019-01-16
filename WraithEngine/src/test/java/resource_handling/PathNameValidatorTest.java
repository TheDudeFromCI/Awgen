package resource_handling;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import net.whg.we.resources.SimplePathNameValidator;

public class PathNameValidatorTest
{
	@Test
	public void pathName()
	{
		SimplePathNameValidator val = new SimplePathNameValidator();

		assertTrue(val.isValidPathName("abd.bat"));
		assertTrue(val.isValidPathName("1230.bat"));
		assertTrue(val.isValidPathName("abd_123.bat"));
		assertTrue(val.isValidPathName("abd.bat123"));
		assertFalse(val.isValidPathName("abdbat."));
		assertFalse(val.isValidPathName(" abd.bat"));
		assertFalse(val.isValidPathName("abd.bat_"));
		assertTrue(val.isValidPathName("123/abd.bat123"));
		assertFalse(val.isValidPathName("123//abd.bat123"));
		assertFalse(val.isValidPathName("/123/a/abd.bat123"));
		assertFalse(val.isValidPathName("123/a/abd.bat123/"));
		assertFalse(val.isValidPathName(".123/a/abd.bat123"));
		assertTrue(val.isValidPathName("123 asd/a/abd.bat123"));
		assertTrue(val.isValidPathName("123 asd/a_1/abasdd.baast123"));
		assertTrue(val.isValidPathName("123 asd/a_1/abasd"));
		assertFalse(val.isValidPathName("a%basd"));
	}
}
