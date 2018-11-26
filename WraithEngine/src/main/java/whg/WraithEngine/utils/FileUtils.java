package whg.WraithEngine.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileUtils
{
	public static File getResourcesFolder()
	{
		String dir = System.getProperty("user.dir");
		return new File(dir, "res");
	}
	
	public static File getResource(String name)
	{
		name = name.replace('/', File.separatorChar);
		return new File(getResourcesFolder(), name);
	}
	
	public static String loadFileAsString(File file) throws FileNotFoundException, IOException
	{
		StringBuilder sb = new StringBuilder();
		
		try (BufferedReader in = new BufferedReader(new FileReader(file)))
		{
			String line;
			while ((line = in.readLine()) != null)
			{
				sb.append(line);
				sb.append('\n');
			}
		}
		
		return sb.toString();
	}
}
