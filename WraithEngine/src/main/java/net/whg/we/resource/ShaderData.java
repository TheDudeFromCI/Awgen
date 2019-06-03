package net.whg.we.resource;

import java.util.UUID;
import net.whg.frameworks.resource.ResourceData;
import net.whg.we.client_logic.rendering.Graphics;
import net.whg.we.client_logic.rendering.VShader;

public class ShaderData implements ResourceData
{
	private VShader _vShader;
	private UncompiledShader _shader;
	private UUID _uuid;

	public ShaderData(Graphics graphics, UUID uuid)
	{
		emptyShader();
		_vShader = graphics.prepareShader(_shader);
		_uuid = uuid;
	}

	public ShaderData(Graphics graphics, UncompiledShader shader, UUID uuid)
	{
		_shader = shader;
		_vShader = graphics.prepareShader(_shader);
		_uuid = uuid;
	}

	private void emptyShader()
	{
		_shader = new UncompiledShader();
		_shader.name = "default_shader";
		_shader.vertShader = "void main(){gl_Position = vec4(0.0, 0.0, 0.0, 1.0);}";
		_shader.fragShader = "out vec4 color;void main(){color = vec4(1.0, 1.0, 1.0, 1.0);}";
	}

	@Override
	public void dispose()
	{
		if (_vShader != null)
			_vShader.dispose();

		_vShader = null;
		_shader = null;
	}

	public void setShaderData(UncompiledShader shader)
	{
		_shader = shader;
		_vShader.recompile(_shader);
	}

	public String getName()
	{
		return _shader.name;
	}

	public String getVertexShaderCode()
	{
		return _shader.vertShader;
	}

	public String getGeometryShaderCode()
	{
		return _shader.geoShader;
	}

	public String getFragmentShaderCode()
	{
		return _shader.fragShader;
	}

	public VShader getVShader()
	{
		return _vShader;
	}

	@Override
	public UUID getUUID()
	{
		return _uuid;
	}
}
