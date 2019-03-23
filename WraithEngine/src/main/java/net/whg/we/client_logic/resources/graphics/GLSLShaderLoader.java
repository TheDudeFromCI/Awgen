package net.whg.we.client_logic.resources.graphics;

import java.io.BufferedReader;
import java.io.FileReader;
import net.whg.we.client_logic.resources.FileLoader;
import net.whg.we.client_logic.resources.ResourceDatabase;
import net.whg.we.client_logic.resources.ResourceFile;
import net.whg.we.client_logic.resources.ResourceLoader;
import net.whg.we.utils.logging.Log;

public class GLSLShaderLoader implements FileLoader
{
	private static final String[] FILE_TYPES =
	{
			"glsl"
	};

	@Override
	public String[] getTargetFileTypes()
	{
		return FILE_TYPES;
	}

	@Override
	public ShaderResource loadFile(ResourceLoader resourceLoader, ResourceDatabase database,
			ResourceFile resourceFile)
	{
		try (BufferedReader in = new BufferedReader(new FileReader(resourceFile.getFile())))
		{
			ShaderProperties properties = new ShaderProperties();
			StringBuilder vertShader = new StringBuilder();
			StringBuilder geoShader = new StringBuilder();
			StringBuilder fragShader = new StringBuilder();

			properties.setName(resourceFile.getName());

			// Mode is the state of the loader.
			// 0 = Loading Vertex Shader
			// 1 = Loading Geometetry Shader
			// 2 = Loading Fragment Shader

			int mode = 0;

			String line;
			while ((line = in.readLine()) != null)
			{
				if (line.equals("---"))
				{
					mode++;

					if (mode == 3)
						throw new RuntimeException(
								"Unable to parse shader file format! Too many states defined.");
				}
				else if (mode == 0)
					vertShader.append(line).append("\n");
				else if (mode == 1)
					geoShader.append(line).append("\n");
				else
					fragShader.append(line).append("\n");
			}

			ShaderResource shader = new ShaderResource(resourceFile, properties,
					vertShader.toString(), geoShader.toString(), fragShader.toString());
			database.addResource(shader);

			Log.debugf("Successfully loaded shader resource, %s.", shader);
			return shader;
		}
		catch (Exception e)
		{
			Log.errorf("Failed to load GLSL shader %s!", e, resourceFile);
			return null;
		}
	}

	@Override
	public int getPriority()
	{
		return 0;
	}
}
