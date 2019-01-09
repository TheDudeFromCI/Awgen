package net.whg.we.resources;

import java.io.File;
import net.whg.we.main.Plugin;

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

		YamlFile yaml = new YamlFile();
		if (yaml.load(propertiesFile))
		{
			AssetProperties properties = new AssetProperties(plugin, yaml);
			return properties;
		}
		else
			return null;
	}
}
