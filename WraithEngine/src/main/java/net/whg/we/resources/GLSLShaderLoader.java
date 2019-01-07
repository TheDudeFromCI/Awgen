package net.whg.we.resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import net.whg.we.rendering.Shader;
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
	public Resource<Shader> loadFile(File file, AssetProperties assetProperties)
	{
		try (BufferedReader in = new BufferedReader(new FileReader(file)))
		{
			ShaderProperties properties = new ShaderProperties();
			StringBuilder vertShader = new StringBuilder();
			StringBuilder geoShader = new StringBuilder();
			StringBuilder fragShader = new StringBuilder();

			// Set default shader name to the name of the file
			// This can be reassigned by the file loader if the property is defined.
			properties.setName(file.getName());

			// Mode is the state of the loader.
			// 0 = Shader Properties
			// 1 = Loading Vertex Shader
			// 2 = Loading Geometetry Shader
			// 3 = Loading Fragment Shader

			int mode = 0;

			String line;
			while ((line = in.readLine()) != null)
			{
				if (line.equals("---"))
				{
					// Move to next state
					mode++;

					if (mode == 4)
						throw new RuntimeException(
								"Unable to parse shader file format! Too many states defined.");
				}
				else if (mode == 0)
				{
					// Load shader property
					if (line.startsWith("name="))
						properties.setName(line.substring(5));
					else
						throw new RuntimeException(
								"Unable to parse shader file format! Unknown shader property: "
										+ line);
				}
				else if (mode == 1)
				{
					// Load vertex shader
					vertShader.append(line).append("\n");
				}
				else if (mode == 2)
				{
					// Load geometry shader
					geoShader.append(line).append("\n");
				}
				else
				{
					// Load fragment shader
					fragShader.append(line).append("\n");
				}
			}

			return new ShaderResource(properties, vertShader.toString(), geoShader.toString(),
					fragShader.toString());
		}
		catch (Exception e)
		{
			Log.errorf("Failed to load GLSL shader file %s!", e, file.toString());
			return null;
		}
	}

	@Override
	public int getPriority()
	{
		return 0;
	}
}
