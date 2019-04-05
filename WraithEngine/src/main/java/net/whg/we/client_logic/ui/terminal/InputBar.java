package net.whg.we.client_logic.ui.terminal;

import net.whg.frameworks.logging.Log;
import net.whg.we.client_logic.scene.ClientGameState;
import net.whg.we.client_logic.ui.TextEditor;
import net.whg.we.client_logic.ui.TypedKeyInput;
import net.whg.we.client_logic.ui.UIComponent;
import net.whg.we.client_logic.ui.UIImage;
import net.whg.we.client_logic.ui.font.UIString;
import net.whg.we.client_logic.utils.Input;
import net.whg.we.network.multiplayer.MultiplayerClient;
import net.whg.we.network.packet.Packet;
import net.whg.we.packets.TerminalCommandPacket;
import net.whg.we.scene.Transform2D;
import net.whg.we.utils.Time;

public class InputBar implements UIComponent
{
	private Transform2D _transform = new Transform2D();
	private UIImage _entryBar;
	private UIString _text;
	private TextEditor _textEditor;
	private boolean _disposed;
	private Terminal _terminal;
	private ClientGameState _gameState;

	public InputBar(Terminal terminal, UIImage entryBar, UIString text, ClientGameState gameState)
	{
		_entryBar = entryBar;
		_text = text;
		_terminal = terminal;
		_gameState = gameState;

		_textEditor = new TextEditor(_text, _text.getCursor(), _text.getTextSelection());
		_textEditor.setSingleLine(false);

		_entryBar.getTransform().setParent(_transform);
		_text.getTransform().setParent(_transform);
	}

	@Override
	public Transform2D getTransform()
	{
		return _transform;
	}

	@Override
	public void init()
	{
	}

	@Override
	public void update()
	{
	}

	@Override
	public void updateFrame()
	{
		if (!_terminal.activeAndOpen())
			return;

		_text.getCursor().setVisible(Time.time() % 0.666f < 0.333f);

		for (TypedKeyInput input : Input.getTypedKeys())
		{
			if (input.extraKey == TypedKeyInput.ENTER_KEY && !input.shift)
			{
				String command = _text.getText();
				_textEditor.clear();

				Log.infof("Issued command '%s'.", command);

				MultiplayerClient client = _gameState.getNetworkHandler();
				Packet packet = client.newPacket("common.terminal.out");
				((TerminalCommandPacket) packet.getPacketType()).build(packet, command);
				client.sendPacket(packet);

				continue;
			}

			_textEditor.typeCharacter(input);
		}

		int lineCount = _textEditor.getLineCount();
		float height = lineCount * 12f + 4f;
		_entryBar.getTransform().setSize(800f, height);
		_entryBar.getTransform().setPosition(400f, height / -2f);
	}

	@Override
	public void render()
	{
		_entryBar.render();
		_text.render();
	}

	@Override
	public void dispose()
	{
		if (_disposed)
			return;

		_disposed = true;
		_text.dispose();
	}

	@Override
	public boolean isDisposed()
	{
		return _disposed;
	}
}
