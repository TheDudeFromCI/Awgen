package net.whg.we.client_logic.ui.terminal;

import net.whg.we.client_logic.rendering.Mesh;
import net.whg.we.client_logic.ui.SimpleContainer;
import net.whg.we.client_logic.ui.UIUtils;
import net.whg.we.legacy.AnimatedProperty;
import net.whg.we.legacy.ClientGameState;
import net.whg.we.legacy.Input;
import net.whg.we.legacy.Screen;
import net.whg.we.legacy.Time;

public class Terminal extends SimpleContainer
{
	private Mesh _imageMesh;
	private AnimatedProperty _verticalPos;
	private ConsoleOutput _consoleOut;
	private boolean _active;
	private ClientGameState _gameState;

	public Terminal(ClientGameState gameState)
	{
		_gameState = gameState;
		_imageMesh = new Mesh("UI Quad", UIUtils.defaultImageVertexData(),
				_gameState.getGraphicsPipeline().getGraphics());

		_verticalPos = new AnimatedProperty(1f);
		_verticalPos.setSpeed(0.4f);
	}

	@Override
	public void init()
	{
		// Shader shader = _fetcher.getShader(plugin, "shaders/ui_color.glsl");
		//
		// Material bgMat = new Material(shader, "");
		// bgMat.setColor(new Color(0f, 0f, 0f, 0.5f));
		//
		// Material inputMat = new Material(shader, "");
		// inputMat.setColor(new Color(0f, 0f, 0f, 0.75f));
		//
		// Material cursorMat = new Material(shader, "");
		// cursorMat.setColor(new Color(1f));
		//
		// Material selMat = new Material(shader, "");
		// selMat.setColor(new Color(1f, 1f, 1f, 0.5f));
		//
		// UIImage background = new UIImage(_imageMesh, bgMat);
		// background.getTransform().setSize(800f, 300f);
		// background.getTransform().setPosition(400f, 450f);
		// addComponent(background);
		//
		// {
		// UIImage bar = new UIImage(_imageMesh, inputMat);
		// bar.getTransform().setSize(800f, 16f);
		// bar.getTransform().setPosition(400f, 0f);
		//
		// Font font = _fetcher.getFont(plugin, "ui/fonts/ubuntu.fnt");
		// Material textMat = _fetcher.getMaterial(plugin, "ui/fonts/ubuntu.material");
		// UIString string = new UIString(font, "", _fetcher.getGraphics(), textMat,
		// _imageMesh,
		// cursorMat, selMat);
		// string.getTransform().setPosition(2f, -14f);
		//
		// InputBar inputBar = new InputBar(this, bar, string, _gameState);
		// inputBar.getTransform().setPosition(0f, 300f);
		// addComponent(inputBar);
		//
		// _consoleOut = new ConsoleOutput(font, _fetcher.getGraphics(), textMat);
		// _consoleOut.getTransform().setParent(getTransform());
		// _consoleOut.getTransform().setPosition(0f, 600f - 24f);
		// addComponent(_consoleOut);
		// }

		super.init();
	}

	@Override
	public void updateFrame()
	{
		if (Input.isKeyDown("`") && Input.isKeyHeld("shift"))
		{
			_active = !_active;

			if (_active)
				Screen.setMouseLocked(false);
		}

		_verticalPos.setGoalValue(_active ? 0f : 1f);
		float vertPos = _verticalPos.update(Time.deltaTime());
		vertPos *= 316f;
		getTransform().setPosition(0f, vertPos);

		super.updateFrame();
	}

	@Override
	public void dispose()
	{
		_imageMesh.dispose();
		super.dispose();
	}

	public boolean isActive()
	{
		return _active;
	}

	public boolean activeAndOpen()
	{
		return _active && _verticalPos.getValue() == 0f;
	}

	public ConsoleOutput getConsoleOutput()
	{
		return _consoleOut;
	}
}
