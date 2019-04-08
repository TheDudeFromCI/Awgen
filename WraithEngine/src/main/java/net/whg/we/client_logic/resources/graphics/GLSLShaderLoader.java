package net.whg.we.client_logic.resources.graphics;

import java.io.BufferedReader;
import java.io.FileReader;
import net.whg.frameworks.logging.Log;
import net.whg.frameworks.resource.FileLoader;
import net.whg.frameworks.resource.ResourceFile;
import net.whg.frameworks.resource.ResourceManager;

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
	public ShaderResource loadFile(ResourceManager resourceManager, ResourceFile resourceFile)
	{
		try (BufferedReader in =
				new BufferedReader(new FileReader(resourceManager.getFile(resourceFile))))
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
			resourceManager.getResourceDatabase().addResource(shader);

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
