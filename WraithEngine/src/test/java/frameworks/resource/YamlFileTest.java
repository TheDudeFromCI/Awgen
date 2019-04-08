package frameworks.resource;

import java.io.File;
import org.junit.Assert;
import org.junit.Test;
import net.whg.frameworks.resource.ResourceFile;
import net.whg.frameworks.resource.SimpleFileDatabase;
import net.whg.frameworks.resource.YamlFile;

public class YamlFileTest
{
	@Test
	public void loadFile()
	{
		File workingDir = new File(System.getProperty("user.dir"));
		SimpleFileDatabase db = new SimpleFileDatabase(workingDir);
		YamlFile yaml = new YamlFile();

		yaml.load(db.getFile(new ResourceFile("Unit Tests/simple.yml")));

		Assert.assertFalse(yaml.getRoots().isEmpty());
	}

	@Test
	public void parseRoots()
	{
		File workingDir = new File(System.getProperty("user.dir"));
		SimpleFileDatabase db = new SimpleFileDatabase(workingDir);
		YamlFile yaml = new YamlFile();

		yaml.load(db.getFile(new ResourceFile("Unit Tests/simple.yml")));

		Assert.assertTrue(yaml.getRoots().size() == 2);
		Assert.assertTrue(yaml.getRoots().containsKey("root1"));
		Assert.assertTrue(yaml.getRoots().containsKey("root2"));
	}

	@Test
	public void parseNestedAsNumber()
	{
		File workingDir = new File(System.getProperty("user.dir"));
		SimpleFileDatabase db = new SimpleFileDatabase(workingDir);
		YamlFile yaml = new YamlFile();

		yaml.load(db.getFile(new ResourceFile("Unit Tests/simple.yml")));

		Assert.assertEquals(yaml.getInt("root1", "some_data", "really_nested_data"), 2);
	}

	@Test
	public void parseDotPath()
	{
		File workingDir = new File(System.getProperty("user.dir"));
		SimpleFileDatabase db = new SimpleFileDatabase(workingDir);
		YamlFile yaml = new YamlFile();

		yaml.load(db.getFile(new ResourceFile("Unit Tests/simple.yml")));

		Assert.assertEquals(yaml.getInt("root1.some_data.really_nested_data"), 2);
	}

	@Test
	public void parseIntPathName()
	{
		File workingDir = new File(System.getProperty("user.dir"));
		SimpleFileDatabase db = new SimpleFileDatabase(workingDir);
		YamlFile yaml = new YamlFile();

		yaml.load(db.getFile(new ResourceFile("Unit Tests/simple.yml")));

		Assert.assertEquals(yaml.getInt("root2.1"), 12);
	}

	@Test
	public void parseFakePath()
	{
		File workingDir = new File(System.getProperty("user.dir"));
		SimpleFileDatabase db = new SimpleFileDatabase(workingDir);
		YamlFile yaml = new YamlFile();

		yaml.load(db.getFile(new ResourceFile("Unit Tests/simple.yml")));

		Assert.assertEquals(yaml.getString("root4.4234.123"), null);
	}
}
