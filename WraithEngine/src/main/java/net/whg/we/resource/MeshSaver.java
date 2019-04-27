package net.whg.we.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import net.whg.frameworks.util.ByteReader;
import net.whg.frameworks.util.ByteWriter;

/**
 * A utility class used to save and load mesh file resources.
 *
 * @author TheDudeFromCI
 */
public class MeshSaver
{
	/**
	 * The current file version header which will be printed on all newly saved
	 * mesh asset files. This is incremented by one each time the mesh asset
	 * format is changed in future versions.
	 */
	public static final int FILE_VERSION = 1;

	/**
	 * Saves a mesh resource as an asset file to within the given file using the
	 * lastest file version to format the mesh asset file. If the file does not
	 * exist, it is created.
	 *
	 * @param mesh
	 *     - The mesh data to write to the file.
	 * @param file
	 *     - The file to write the data into.
	 * @throws IOException
	 *     If there is an error while writing to the file.
	 */
	public static void save(UncompiledMesh mesh, File file) throws IOException
	{
		if (!file.exists())
			file.createNewFile();

		try (FileOutputStream fileOutputStream = new FileOutputStream(file))
		{
			ByteWriter out = new ByteWriter(fileOutputStream);
			out.writeInt(FILE_VERSION);
			out.writeObject(mesh);
		}
	}

	/**
	 * Loads a mesh asset from a file. The loader will automatically handle
	 * different file versions for backwards compatability.
	 *
	 * @param file
	 *     - The file to load.
	 * @return An uncompiled mesh object, containing all the data within the
	 *     mesh that could be loaded.
	 * @throws FileNotFoundException
	 *     If the file does not exist or found not be found.
	 * @throws IOException
	 *     If an IO error occurs while loading this file.
	 */
	public static UncompiledMesh load(File file)
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

	private static UncompiledMesh load_v001(ByteReader in)
	{
		return (UncompiledMesh) in.readObject();
	}
}
