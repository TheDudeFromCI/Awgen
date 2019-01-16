package resource_handling;

import org.junit.Assert;
import org.junit.Test;
import net.whg.we.resources.ResourceFile;
import net.whg.we.resources.YamlFile;
import util.CommonMock;

public class YamlFileTest
{
	@Test
	public void loadFile()
	{
		// Makes sure some information could be loaded from a file.

		YamlFile yaml = new YamlFile();
		yaml.load(new ResourceFile(CommonMock.getTestPlugin(), "Unit Tests/simple.yml"));

		Assert.assertFalse(yaml.getRoots().isEmpty());
	}

	@Test
	public void parseRoots()
	{
		// Checks to make sure the correct information was loaded from the file.

		YamlFile yaml = new YamlFile();
		yaml.load(new ResourceFile(CommonMock.getTestPlugin(), "Unit Tests/simple.yml"));

		Assert.assertTrue(yaml.getRoots().size() == 2);
		Assert.assertTrue(yaml.getRoots().containsKey("root1"));
		Assert.assertTrue(yaml.getRoots().containsKey("root2"));
	}

	@Test
	public void parseNestedAsNumber()
	{
		// Checks to make sure the correct information was loaded from the file.

		YamlFile yaml = new YamlFile();
		yaml.load(new ResourceFile(CommonMock.getTestPlugin(), "Unit Tests/simple.yml"));

		Assert.assertEquals(yaml.getInt("root1", "some_data", "really_nested_data"), 2);
	}

	@Test
	public void parseDotPath()
	{
		// Checks to make sure the correct information was loaded from the file.

		YamlFile yaml = new YamlFile();
		yaml.load(new ResourceFile(CommonMock.getTestPlugin(), "Unit Tests/simple.yml"));

		Assert.assertEquals(yaml.getInt("root1.some_data.really_nested_data"), 2);
	}

	@Test
	public void parseIntPathName()
	{
		// Checks to make sure the correct information was loaded from the file.

		YamlFile yaml = new YamlFile();
		yaml.load(new ResourceFile(CommonMock.getTestPlugin(), "Unit Tests/simple.yml"));

		Assert.assertEquals(yaml.getInt("root2.1"), 12);
	}
}
