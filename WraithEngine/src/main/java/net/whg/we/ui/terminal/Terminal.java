package net.whg.we.ui.terminal;

import net.whg.we.main.Plugin;
import net.whg.we.rendering.Material;
import net.whg.we.rendering.Mesh;
import net.whg.we.rendering.Shader;
import net.whg.we.resources.ResourceFetcher;
import net.whg.we.ui.SimpleContainer;
import net.whg.we.ui.UIImage;
import net.whg.we.ui.UIUtils;
import net.whg.we.ui.font.Font;
import net.whg.we.ui.font.UIString;
import net.whg.we.utils.Color;

public class Terminal extends SimpleContainer
{
	private ResourceFetcher _fetcher;
	private Mesh _imageMesh;

	public Terminal(ResourceFetcher fetcher)
	{
		_fetcher = fetcher;
		_imageMesh = new Mesh("UI Quad", UIUtils.defaultImageVertexData(), fetcher.getGraphics());
	}

	@Override
	public void init()
	{
		Plugin plugin = new Plugin()
		{
			@Override
			public String getPluginName()
			{
				return "TestPlugin";
			}

			@Override
			public void initPlugin()
			{
			}

			@Override
			public void enablePlugin()
			{
			}

			@Override
			public int getPriority()
			{
				return 0;
			}
		};

		Shader shader = _fetcher.getShader(plugin, "shaders/ui_color.glsl");

		Material bgMat = new Material(shader, "");
		bgMat.setColor(new Color(0f, 0f, 0f, 0.5f));
		UIImage background = new UIImage(_imageMesh, bgMat);
		background.getTransform().setSize(800f, 300f);
		background.getTransform().setPosition(400f, 450f);
		addComponent(background);

		Font font = _fetcher.getFont(plugin, "ui/fonts/ubuntu.fnt");
		Material textMat = _fetcher.getMaterial(plugin, "ui/fonts/ubuntu.material");
		UIString text = new UIString(font, "Line 1\nLine 2", _fetcher.getGraphics(), textMat);
		text.getTransform().setPosition(0f, 600f - 12f);
		addComponent(text);

		super.init();
	}

	@Override
	public void updateFrame()
	{
		super.updateFrame();
	}

	@Override
	public void dispose()
	{
		_imageMesh.dispose();
		super.dispose();
	}
}
