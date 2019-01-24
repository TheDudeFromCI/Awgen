package resource_handling;

import org.junit.Assert;
import org.junit.Test;
import net.whg.we.resources.SimpleFileDatabase;
import net.whg.we.resources.YamlFile;
import util.CommonMock;

public class YamlFileTest
{
	@Test
	public void loadFile()
	{
		// Makes sure some information could be loaded from a file.

		YamlFile yaml = new YamlFile();
		SimpleFileDatabase db = CommonMock.getSimpleFileDatabase();

		yaml.load(
				db.getResourceFile(CommonMock.getTestPlugin(), "Unit Tests/simple.yml").getFile());

		Assert.assertFalse(yaml.getRoots().isEmpty());
	}

	@Test
	public void parseRoots()
	{
		// Checks to make sure the correct information was loaded from the file.

		YamlFile yaml = new YamlFile();
		SimpleFileDatabase db = CommonMock.getSimpleFileDatabase();

		yaml.load(
				db.getResourceFile(CommonMock.getTestPlugin(), "Unit Tests/simple.yml").getFile());

		Assert.assertTrue(yaml.getRoots().size() == 2);
		Assert.assertTrue(yaml.getRoots().containsKey("root1"));
		Assert.assertTrue(yaml.getRoots().containsKey("root2"));
	}

	@Test
	public void parseNestedAsNumber()
	{
		// Checks to make sure the correct information was loaded from the file.

		YamlFile yaml = new YamlFile();
		SimpleFileDatabase db = CommonMock.getSimpleFileDatabase();

		yaml.load(
				db.getResourceFile(CommonMock.getTestPlugin(), "Unit Tests/simple.yml").getFile());

		Assert.assertEquals(yaml.getInt("root1", "some_data", "really_nested_data"), 2);
	}

	@Test
	public void parseDotPath()
	{
		// Checks to make sure the correct information was loaded from the file.

		YamlFile yaml = new YamlFile();
		SimpleFileDatabase db = CommonMock.getSimpleFileDatabase();

		yaml.load(
				db.getResourceFile(CommonMock.getTestPlugin(), "Unit Tests/simple.yml").getFile());

		Assert.assertEquals(yaml.getInt("root1.some_data.really_nested_data"), 2);
	}

	@Test
	public void parseIntPathName()
	{
		// Checks to make sure the correct information was loaded from the file.

		YamlFile yaml = new YamlFile();
		SimpleFileDatabase db = CommonMock.getSimpleFileDatabase();

		yaml.load(
				db.getResourceFile(CommonMock.getTestPlugin(), "Unit Tests/simple.yml").getFile());

		Assert.assertEquals(yaml.getInt("root2.1"), 12);
	}

	@Test
	public void parseFakePath()
	{
		// Try to parse a fake path.

		YamlFile yaml = new YamlFile();
		SimpleFileDatabase db = CommonMock.getSimpleFileDatabase();

		yaml.load(
				db.getResourceFile(CommonMock.getTestPlugin(), "Unit Tests/simple.yml").getFile());

		Assert.assertEquals(yaml.getString("root4.4234.123"), null);
	}
}
