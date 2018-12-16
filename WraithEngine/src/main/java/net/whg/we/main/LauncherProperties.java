package net.whg.we.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;
import net.whg.we.utils.FileUtils;
import net.whg.we.utils.Log;

class LauncherProperties
{
	static LauncherProperties loadLauncherProperties()
	{
		File file = new File(FileUtils.getCoreFolder(), "properties.yml");

		if (!file.exists())
		{
			Log.info("Launcher Properties file not found. Generating default properties file now.");
			try
			{
				file.createNewFile();
				return generateDefaultLauncherProperties(file);
			}
			catch (IOException e)
			{
				Log.errorf("Failed to create new Launcher Properties file! Default launcher"
						+ "properties will be used.", e);
				return new LauncherProperties();
			}
		}

		try
		{
			// Load yaml file
			Log.debug("Loading Launcher Properties.");
			Yaml yaml = new Yaml();
			Map<String, Object> map = yaml.load(new FileReader(file));
			LauncherProperties properties = new LauncherProperties();
			boolean updateYamlFile = false;

			// Send properties to object
			{
				Log.trace("Loading 'ConsoleApplication' value.");
				if (map.containsKey("ConsoleApplication"))
				{
					properties._isConsoleApplication = (boolean) map.get("ConsoleApplication");
					Log.debugf("Loaded 'ConsoleApplication' value as %s.",
							properties._isConsoleApplication);
				}
				else
				{
					Log.debugf("'ConsoleApplication' property not defined. Defaulting value to %s.",
							properties._isConsoleApplication);
					map.put("ConsoleApplication", properties._isConsoleApplication);
					updateYamlFile = true;
				}
			}

			// Update yaml if needed
			if (updateYamlFile)
			{
				Log.debug("Updating local Launcher Properties file.");
				try
				{
					yaml.dump(map, new FileWriter(file));
					Log.trace("Successfully updated local Launcher Properties file.");
				}
				catch (IOException e)
				{
					Log.errorf("Failed to update current Launcher Properties failed!", e);
				}
			}

			// Return object
			Log.trace("Successfully loaded Launcher Properties file.");
			return properties;
		}
		catch (FileNotFoundException e)
		{
			Log.errorf("Failed to load Launcher Properties file! Generating default properties file"
					+ " now.", e);
			return generateDefaultLauncherProperties(file);
		}
	}

	static LauncherProperties generateDefaultLauncherProperties(File file)
	{
		Log.trace("Starting generation of default Launcher Properties file.");

		LauncherProperties properties = new LauncherProperties();
		HashMap<String, Object> map = new HashMap<>();

		Log.debugf("Setting default 'ConsoleApplication' property to %s.",
				properties._isConsoleApplication);
		map.put("ConsoleApplication", properties._isConsoleApplication);

		try
		{
			Yaml yaml = new Yaml();
			yaml.dump(map, new FileWriter(file));
			Log.trace("Successfully generated local Launcher Properties file.");
		}
		catch (IOException e)
		{
			Log.errorf("Failed to update current Launcher Properties failed!", e);
		}

		return properties;
	}

	private boolean _isConsoleApplication;

	private LauncherProperties()
	{
	}
}
