package net.whg.we.render;

import net.whg.we.client_logic.rendering.Graphics;
import net.whg.we.client_logic.rendering.ScreenClearType;
import net.whg.we.client_logic.rendering.VMesh;
import net.whg.we.client_logic.rendering.VShader;
import net.whg.we.client_logic.rendering.VTexture;
import net.whg.we.client_logic.rendering.VertexData;
import net.whg.we.legacy.Color;
import net.whg.we.resource.UncompiledShader;
import net.whg.we.resource.UncompiledTexture;

/***
 * This class represents a headless graphics environment. It handles all of the
 * correct functions expected for graphics, with the exception that no graphics
 * driver bindings or calls actually occur.
 *
 * @author TheDudeFromCI
 */
public class HeadlessGraphics implements Graphics
{
	@Override
	public void init()
	{
	}

	@Override
	public VMesh prepareMesh(VertexData vertexData)
	{
		return new HeadlessMesh();
	}

	@Override
	public VTexture prepareTexture(UncompiledTexture data)
	{
		return new HeadlessTexture();
	}

	@Override
	public VShader prepareShader(UncompiledShader data)
	{
		return new HeadlessShader();
	}

	@Override
	public void clearScreenPass(ScreenClearType screenClear)
	{
	}

	@Override
	public void setClearScreenColor(Color color)
	{
	}

	@Override
	public void dispose()
	{
	}
}
