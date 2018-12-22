package net.whg.we.rendering;

import java.util.ArrayList;

public class ShaderDatabase
{
	// ===== FIELDS =====
	private static ArrayList<Shader> _loadedShaders = new ArrayList<>();
	private static Shader _boundShader;

	// ===== ENGINE ONLY =====

	static void loadShader(Shader shader)
	{
		_loadedShaders.add(shader);
	}

	static void unloadShader(Shader shader)
	{
		_loadedShaders.remove(shader);
	}

	// ===== PUBLIC API =====

	public static void bindShader(Shader shader)
	{
		if (shader != null && shader.isDisposed())
			shader = null;

		if (!isShaderBound(shader))
		{
			if (shader == null)
			{
				if (_boundShader != null && !_boundShader.isDisposed())
					_boundShader.unbind();
			}
			else
				shader.bind();

			_boundShader = shader;
		}
	}

	public static boolean isShaderBound(Shader shader)
	{
		if (_boundShader != null && _boundShader.isDisposed())
			return false;
		return _boundShader == shader;
	}

	public static Shader findShader(String name)
	{
		for (Shader shader : _loadedShaders)
			if (shader.getName().equals(name))
				return shader;
		return null;
	}
}
