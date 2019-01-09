package net.whg.we.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;
import net.whg.we.main.Plugin;
import net.whg.we.utils.Log;

public class AssetPropertiesParser
{
	public static AssetProperties loadProperties(Plugin plugin, File file)
	{
		File folder = file.getParentFile();
		String assetName = file.getName();

		String propertiesName = assetName + ".asset";
		File propertiesFile = new File(folder, propertiesName);

		if (!propertiesFile.exists())
			return null;

		Yaml yaml = new Yaml();
		try
		{
			Map<String, Object> map = yaml.load(new FileReader(propertiesFile));
			AssetProperties properties = new AssetProperties(plugin, map);
			return properties;
		}
		catch (FileNotFoundException e)
		{
			Log.errorf("Failed to load asset properties! %s", e, propertiesName);
			return null;
		}
	}
}
