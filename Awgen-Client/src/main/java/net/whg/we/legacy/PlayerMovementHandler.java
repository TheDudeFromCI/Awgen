package net.whg.we.legacy;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import net.whg.frameworks.network.multiplayer.MultiplayerClient;
import net.whg.frameworks.network.packet.Packet;
import net.whg.frameworks.scene.Transform3D;
import net.whg.we.main.GameState;
import net.whg.we.packets.PlayerMovePacket;

public class PlayerMovementHandler
{
	private GameState _gameState;
	private Transform3D _playerLocation;

	private Vector3f _lastMovePacketPos = new Vector3f();
	private Quaternionf _lastMovePacketRot = new Quaternionf();

	public PlayerMovementHandler(GameState gameState, Transform3D playerLocation)
	{
		_gameState = gameState;
		_playerLocation = playerLocation;
	}

	public void update()
	{
		if (_lastMovePacketPos.distanceSquared(_playerLocation.getPosition()) > 0.1f
				|| _lastMovePacketRot.dot(_playerLocation.getRotation()) <= 0.95f)
		{
			_lastMovePacketPos.set(_playerLocation.getPosition());
			_lastMovePacketRot.set(_playerLocation.getRotation());

			MultiplayerClient client = (MultiplayerClient) _gameState.getNetworkHandler();
			Packet movePacket = client.newPacket("common.player.move");
			((PlayerMovePacket) movePacket.getPacketType()).build(movePacket, client.getToken(),
					_playerLocation);
			client.sendPacket(movePacket);
		}
	}
}
