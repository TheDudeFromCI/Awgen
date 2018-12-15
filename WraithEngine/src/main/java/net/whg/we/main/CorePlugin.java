package net.whg.we.main;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;
import net.whg.we.utils.FileUtils;
import net.whg.we.utils.Log;

/**
 * This is the core plugin loader for WraithEngine. It is in charge of loading and sending events
 * between plugins.
 * 
 * @author TheDudeFromCI
 */
public class CorePlugin extends BasePlugin
{
	@Override
	public String getPluginName()
	{
		return "Core";
	}

	@Override
	public int getPriority()
	{
		return 1000;
	}
	
	void loadPluginsFromFile()
	{
		File folder = FileUtils.getPluginFolder();
		
		for (File file : folder.listFiles())
		{
			String fileName = file.getName();

			if (file.isDirectory())
			{
				Log.warnf("File '%s' is in plugin folder, but is a directory!", fileName);
				continue;
			}

			if (!file.canRead())
			{
				Log.warnf("File '%s' is in plugin folder, but is not cannot be read!", fileName);
				continue;
			}
			
			if (!fileName.endsWith(".jar"))
			{
				Log.warnf("File '%s' is in plugin folder, but is not a jar file", fileName);
				continue;
			}
			
			Log.infof("Attempting to load plugin %s...", fileName);
			attemptLoadPlugin(file);
		}
	}
	
	private Plugin attemptLoadPlugin(File file)
	{
		try
		{
			URL[] url = new URL[] {file.toURI().toURL()};
			URLClassLoader classLoader = new URLClassLoader(url, this.getClass().getClassLoader());
			InputStream pluginProperties = classLoader.getResourceAsStream("plugin.yml");
			
			Yaml yaml = new Yaml();
			Map<String, Object> map = yaml.load(pluginProperties);
			
			String mainClassPath = (String) map.get("MainClass");

			Class<?> mainClass = Class.forName(mainClassPath, true, classLoader);
			Plugin plugin = (Plugin) mainClass.newInstance();
			
			return plugin;
		}
		catch(MalformedURLException exception)
		{
			Log.errorf("Failed to read file for plugin %s!", exception, file.getName());
			return null;
		}
		catch(ClassNotFoundException | InstantiationException | IllegalAccessException
				| ClassCastException exception)
		{
			Log.errorf("Failed to load main class for plugin %s!", exception, file.getName());
			return null;
		}
	}
}
