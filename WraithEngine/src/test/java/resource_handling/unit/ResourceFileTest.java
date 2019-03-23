package resource_handling.unit;

import java.io.File;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import net.whg.we.client_logic.resources.ResourceFile;
import net.whg.we.main.Plugin;

public class ResourceFileTest
{
	@Test
	public void equals_SamePath_SameName()
	{
		Plugin plugin = Mockito.mock(Plugin.class);
		File file = new File(".");

		ResourceFile a = new ResourceFile(plugin, "path/to/file.txt:name", file);
		ResourceFile b = new ResourceFile(plugin, "path/to/file.txt:name", file);

		Assert.assertEquals(a, b);
	}

	@Test
	public void notEqual_SamePath_DifferentName()
	{
		Plugin plugin = Mockito.mock(Plugin.class);
		File file = new File(".");

		ResourceFile a = new ResourceFile(plugin, "path/to/file.txt:name1", file);
		ResourceFile b = new ResourceFile(plugin, "path/to/file.txt:name2", file);

		Assert.assertNotEquals(a, b);

	}

	@Test
	public void equals_SamePath_NoName()
	{
		Plugin plugin = Mockito.mock(Plugin.class);
		File file = new File(".");

		ResourceFile a = new ResourceFile(plugin, "path/to/file.txt", file);
		ResourceFile b = new ResourceFile(plugin, "path/to/file.txt", file);

		Assert.assertEquals(a, b);
	}

	@Test
	public void notEqual_DifferentPath_SameName()
	{
		Plugin plugin = Mockito.mock(Plugin.class);
		File file = new File(".");

		ResourceFile a = new ResourceFile(plugin, "path/to/file1.txt:name", file);
		ResourceFile b = new ResourceFile(plugin, "path/to/file2.txt:name", file);

		Assert.assertNotEquals(a, b);
	}

	@Test
	public void notEqual_DifferentPath_DifferentName()
	{
		Plugin plugin = Mockito.mock(Plugin.class);
		File file = new File(".");

		ResourceFile a = new ResourceFile(plugin, "path/to/file1.txt:name1", file);
		ResourceFile b = new ResourceFile(plugin, "path/to/file2.txt:name2", file);

		Assert.assertNotEquals(a, b);
	}

	@Test
	public void notEqual_DifferentPlugin()
	{
		Plugin plugin1 = Mockito.mock(Plugin.class);
		Plugin plugin2 = Mockito.mock(Plugin.class);
		File file = new File(".");

		ResourceFile a = new ResourceFile(plugin1, "path/to/file.txt:name", file);
		ResourceFile b = new ResourceFile(plugin2, "path/to/file.txt:name", file);

		Assert.assertNotEquals(a, b);
	}

	@Test
	public void notEqual_DifferentObject()
	{
		Plugin plugin = Mockito.mock(Plugin.class);
		File file = new File(".");

		ResourceFile a = new ResourceFile(plugin, "file.txt", file);

		Assert.assertNotEquals(a, new Object());
	}

	@Test
	public void getPlugin()
	{
		Plugin plugin = Mockito.mock(Plugin.class);
		File file = new File(".");
		ResourceFile res = new ResourceFile(plugin, "file.txt", file);

		Assert.assertEquals(res.getPlugin(), plugin);
	}

	@Test
	public void getSimpleathname()
	{
		Plugin plugin = Mockito.mock(Plugin.class);
		File file = new File(".");
		ResourceFile res = new ResourceFile(plugin, "file.txt:name", file);

		Assert.assertEquals(res.getSimplePathname(), "file.txt");
	}

	@Test
	public void getFullPathname()
	{
		Plugin plugin = Mockito.mock(Plugin.class);
		File file = new File(".");
		ResourceFile res = new ResourceFile(plugin, "file.txt:name", file);

		Assert.assertEquals(res.getFullPathname(), "file.txt:name");
	}

	@Test
	public void getName()
	{
		Plugin plugin = Mockito.mock(Plugin.class);
		File file = new File(".");
		ResourceFile res = new ResourceFile(plugin, "file.txt:name", file);

		Assert.assertEquals("name", res.getName());
	}

	@Test
	public void getDefaultName()
	{
		Plugin plugin = Mockito.mock(Plugin.class);
		File file = new File(".");
		ResourceFile res = new ResourceFile(plugin, "path/to/file.txt", file);

		Assert.assertEquals("file.txt", res.getName());
	}

	@Test
	public void getResourceName()
	{
		Plugin plugin = Mockito.mock(Plugin.class);
		File file = new File(".");
		ResourceFile res = new ResourceFile(plugin, "path/to/file.txt:res", file);

		Assert.assertEquals(res.getName(), "res");
	}

	@Test
	public void getFileExtention()
	{
		Plugin plugin = Mockito.mock(Plugin.class);
		File file = new File(".");
		ResourceFile res = new ResourceFile(plugin, "path/to/file.txt", file);

		Assert.assertEquals(res.getFileExtension(), "txt");
	}

	@Test
	public void getFileExtention_WithResource()
	{
		Plugin plugin = Mockito.mock(Plugin.class);
		File file = new File(".");
		ResourceFile res = new ResourceFile(plugin, "path/to/file.txt:res", file);

		Assert.assertEquals(res.getFileExtension(), "txt");
	}

	@Test
	public void getFile()
	{
		Plugin plugin = Mockito.mock(Plugin.class);
		File file = new File(".");
		ResourceFile res = new ResourceFile(plugin, "path/to/file.txt", file);

		Assert.assertEquals(res.getFile(), file);
	}

	@Test
	public void getPropertiesFile()
	{
		Plugin plugin = Mockito.mock(Plugin.class);
		File file = new File("awesome.txt");
		ResourceFile res = new ResourceFile(plugin, "path/to/file.txt", file);

		File properties = new File(file.getAbsolutePath() + ".asset");
		Assert.assertEquals(res.getPropertiesFile(), properties);
	}

	@Test
	public void asString()
	{
		Plugin plugin = Mockito.mock(Plugin.class);
		Mockito.doReturn("TestPlugin").when(plugin).getPluginName();

		File file = new File("awesome.txt");
		ResourceFile res = new ResourceFile(plugin, "path/to/file.txt", file);

		Assert.assertEquals("[Resource: TestPlugin/path/to/file.txt:file.txt]", res.toString());
	}

	@Test
	public void hashing()
	{
		Plugin plugin = Mockito.mock(Plugin.class);
		File file = new File(".");
		ResourceFile res = new ResourceFile(plugin, "path/to/file.txt", file);

		Assert.assertEquals(res.hashCode(), "path/to/file.txt".hashCode());
	}

	@Test
	public void fileExists_FileNotReal()
	{
		Plugin plugin = Mockito.mock(Plugin.class);
		File file = new File("res/TestPlugin/Unit Tests/not_real.txt");
		ResourceFile res = new ResourceFile(plugin, "file.txt", file);

		Assert.assertFalse(res.exists());
	}

	@Test
	public void fileExists_DoesExist()
	{
		Plugin plugin = Mockito.mock(Plugin.class);

		File file = new File("res/TestPlugin/Unit Tests/simple.yml");
		ResourceFile res = new ResourceFile(plugin, "file.txt", file);

		Assert.assertTrue(res.exists());
	}

	@Test
	public void propertiesFileExists()
	{
		Plugin plugin = Mockito.mock(Plugin.class);

		File file = new File("res/TestPlugin/Unit Tests/simple.yml");
		ResourceFile res = new ResourceFile(plugin, "file.txt", file);

		Assert.assertTrue(res.hasPropertiesFile());
	}

	@Test
	public void propertiesFile_NotExists()
	{
		Plugin plugin = Mockito.mock(Plugin.class);

		File file = new File("res/TestPlugin/Unit Tests/not_real.txt");
		ResourceFile res = new ResourceFile(plugin, "file.txt", file);

		Assert.assertFalse(res.hasPropertiesFile());
	}
}
