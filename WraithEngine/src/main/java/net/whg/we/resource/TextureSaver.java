package net.whg.we.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import net.whg.frameworks.util.ByteReader;
import net.whg.frameworks.util.ByteWriter;

/**
 * A utility class for saving and loading texture file resources.
 *
 * @author TheDudeFromCI
 */
public class TextureSaver
{
	/**
	 * The current file version header which will be printed on all newly saved
	 * texture asset files. This is incremented by one each time the texture
	 * asset format is changed in future versions.
	 */
	public static final int FILE_VERSION = 1;

	/**
	 * Saves a texture resource as an asset file to within the given file using
	 * the lastest file version to format the texture asset file. If the file
	 * does not exist, it is created.
	 *
	 * @param texture
	 *     - The texture data to write to the file.
	 * @param file
	 *     - The file to write the data into.
	 * @throws IOException
	 *     If there is an error while writing to the file.
	 */
	public static void save(UncompiledTexture texture, File file)
			throws IOException
	{
		if (!file.exists())
			file.createNewFile();

		try (FileOutputStream fileOutputStream = new FileOutputStream(file))
		{
			ByteWriter out = new ByteWriter(fileOutputStream);
			out.writeInt(FILE_VERSION);
			out.writeObject(texture);
		}
	}

	/**
	 * Loads a texture asset from a file. The loader will automatically handle
	 * different file versions for backwards compatability.
	 *
	 * @param file
	 *     - The file to load.
	 * @return An uncompiled texture object, containing all the data within the
	 *     texture that could be loaded.
	 * @throws FileNotFoundException
	 *     If the file does not exist or found not be found.
	 * @throws IOException
	 *     If an IO error occurs while loading this file.
	 */
	public static UncompiledTexture load(File file)
			throws FileNotFoundException, IOException
	{
		try (FileInputStream fileInputStream = new FileInputStream(file))
		{
			ByteReader in = new ByteReader(fileInputStream);
			int fileVersion = in.getInt();

			switch (fileVersion)
			{
				case 1:
					return load_v001(in);

				default:
					throw new RuntimeException(
							"Unknown file version!" + fileVersion);
			}
		}
	}

	private static UncompiledTexture load_v001(ByteReader in)
	{
		// TODO Because we're using serialization, compatabily may break
		// in future versions. A more appropreiate handling method should be
		// used.
		return (UncompiledTexture) in.readObject();
	}
}
