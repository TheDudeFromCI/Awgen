package net.whg.we.ui.terminal;

import net.whg.we.command.CommandList;
import net.whg.we.command.common.HelpCommand;
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
	private CommandList _commandList;

	public Terminal(ResourceFetcher fetcher)
	{
		_fetcher = fetcher;
		_imageMesh = new Mesh("UI Quad", UIUtils.defaultImageVertexData(), fetcher.getGraphics());

		_commandList = new CommandList();
		_commandList.addCommand(new HelpCommand(_commandList));
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

		Material inputMat = new Material(shader, "");
		inputMat.setColor(new Color(0f, 0f, 0f, 0.75f));

		Material cursorMat = new Material(shader, "");
		cursorMat.setColor(new Color(1f));

		Material selMat = new Material(shader, "");
		selMat.setColor(new Color(1f, 1f, 1f, 0.5f));

		UIImage background = new UIImage(_imageMesh, bgMat);
		background.getTransform().setSize(800f, 300f);
		background.getTransform().setPosition(400f, 450f);
		addComponent(background);

		{
			UIImage bar = new UIImage(_imageMesh, inputMat);
			bar.getTransform().setSize(800f, 16f);
			bar.getTransform().setPosition(400f, 0f);

			Font font = _fetcher.getFont(plugin, "ui/fonts/ubuntu.fnt");
			Material textMat = _fetcher.getMaterial(plugin, "ui/fonts/ubuntu.material");
			UIString string = new UIString(font, "", _fetcher.getGraphics(), textMat, _imageMesh,
					cursorMat, selMat);
			string.getTransform().setPosition(2f, -14f);

			InputBar inputBar = new InputBar(_commandList, bar, string);
			inputBar.getTransform().setPosition(0f, 300f);
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

	public CommandList getCommandList()
	{
		return _commandList;
	}
}
