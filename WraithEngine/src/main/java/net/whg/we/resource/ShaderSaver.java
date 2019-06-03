package net.whg.we.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import net.whg.frameworks.util.ByteReader;
import net.whg.frameworks.util.ByteWriter;

/**
 * A utility class for saving and loading shader file resources.
 *
 * @author TheDudeFromCI
 */
public class ShaderSaver
{
	public static final int FILE_VERSION = 1;

	public static void save(UncompiledShader shader, File file) throws IOException
	{
		if (!file.exists())
		{
			file.getParentFile().mkdirs();
			file.createNewFile();
		}

		try (FileOutputStream fileOutputStream = new FileOutputStream(file))
		{
			ByteWriter out = new ByteWriter(fileOutputStream);
			out.writeInt(FILE_VERSION);
			out.writeObject(shader);
		}
	}

	public static UncompiledShader load(File file) throws FileNotFoundException, IOException
	{
		try (FileInputStream fileInputStream = new FileInputStream(file))
		{
			ByteReader in = new ByteReader(fileInputStream);
			int fileVersion = in.getInt();

			switch (fileVersion) {
				case 1:
					return load_v001(in);

				default:
					throw new RuntimeException("Unknown file version!" + fileVersion);
			}
		}
	}

	private static UncompiledShader load_v001(ByteReader in)
	{
		return (UncompiledShader) in.readObject();
	}
}
