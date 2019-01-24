package net.whg.we.resources.graphics;

import java.io.BufferedReader;
import java.io.FileReader;
import net.whg.we.rendering.Shader;
import net.whg.we.resources.FileLoadState;
import net.whg.we.resources.FileLoader;
import net.whg.we.resources.ResourceBatchRequest;
import net.whg.we.resources.ResourceFile;
import net.whg.we.utils.Log;

public class GLSLShaderLoader implements FileLoader<Shader>
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
	public FileLoadState loadFile(ResourceBatchRequest request, ResourceFile resourceFile)
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

			request.addResource(new ShaderResource(properties, vertShader.toString(),
					geoShader.toString(), fragShader.toString(), resourceFile));
			return FileLoadState.LOADED_SUCCESSFULLY;
		}
		catch (Exception e)
		{
			Log.errorf("Failed to load GLSL shader file %s!", e, resourceFile);
			return FileLoadState.FAILED_TO_LOAD;
		}
	}

	@Override
	public int getPriority()
	{
		return 0;
	}
}
