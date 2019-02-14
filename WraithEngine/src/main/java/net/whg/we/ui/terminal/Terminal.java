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

		Material cursorMat = new Material(shader, "");
		cursorMat.setColor(new Color(0.85f));

		UIImage background = new UIImage(_imageMesh, bgMat);
		background.getTransform().setSize(800f, 300f);
		background.getTransform().setPosition(400f, 450f);
		addComponent(background);

		{
			UIImage bar = new UIImage(_imageMesh, bgMat);
			bar.getTransform().setSize(800f, 16f);
			bar.getTransform().setPosition(400f, 0f);

			UIImage cursor = new UIImage(_imageMesh, cursorMat);
			cursor.getTransform().setSize(1f, 12f);
			cursor.getTransform().setPosition(2f, 0f);

			Font font = _fetcher.getFont(plugin, "ui/fonts/ubuntu.fnt");
			Material textMat = _fetcher.getMaterial(plugin, "ui/fonts/ubuntu.material");
			UIString string = new UIString(font, "", _fetcher.getGraphics(), textMat);
			string.getTransform().setPosition(2f, -6f);

			InputBar inputBar = new InputBar(bar, string, cursor);
			inputBar.getTransform().setPosition(0f, 308f);
			addComponent(inputBar);
		}

		super.init();
	}

	@Override
	public void dispose()
	{
		_imageMesh.dispose();
		super.dispose();
	}
}
