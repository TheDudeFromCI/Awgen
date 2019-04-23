package net.whg.we.resource;

import net.whg.frameworks.resource.ResourceData;
import net.whg.we.client_logic.rendering.Graphics;
import net.whg.we.client_logic.rendering.VShader;

public class ShaderData implements ResourceData
{
	private String _vertShader;
	private String _geoShader;
	private String _fragShader;
	private VShader _vShader;

	public ShaderData(Graphics graphics)
	{
		emptyShader();
		_vShader = graphics.prepareShader(_vertShader, _geoShader, _fragShader);
	}

	public ShaderData(Graphics graphics, String vert, String geo, String frag)
	{
		_vertShader = vert;
		_geoShader = geo;
		_fragShader = frag;
		_vShader = graphics.prepareShader(_vertShader, _geoShader, _fragShader);
	}

	private void emptyShader()
	{
		_vertShader = "void main(){gl_Position = vec4(0.0, 0.0, 0.0, 1.0);}";
		_geoShader = null;
		_fragShader =
				"out vec4 color;void main(){color = vec4(1.0, 1.0, 1.0, 1.0);}";
	}

	@Override
	public void dispose()
	{
		if (_vShader != null)
			_vShader.dispose();

		_vShader = null;
	}

	public void compile(String vert, String geo, String frag)
	{
		_vertShader = vert;
		_geoShader = geo;
		_fragShader = frag;

		_vShader.recompile(vert, geo, frag);
	}

	public String getVertexShaderCode()
	{
		return _vertShader;
	}

	public String getGeometryShaderCode()
	{
		return _geoShader;
	}

	public String getFragmentShaderCode()
	{
		return _fragShader;
	}

	public VShader getVShader()
	{
		return _vShader;
	}
}
