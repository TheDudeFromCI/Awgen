package net.whg.we.client_logic.utils;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import net.whg.we.scene.Location;

public class PlayerCameraModelSync
{
	private Location _playerLocation;
	private Location _modelLocation;

	private Vector3f _angleBuf = new Vector3f();
	private Quaternionf _rotBuf = new Quaternionf();
	private Vector3f _up = new Vector3f(0f, 1f, 0f);

	public PlayerCameraModelSync(Location player, Location model)
	{
		_playerLocation = player;
		_modelLocation = model;
	}

	public void sync()
	{
		Vector3f pos = _playerLocation.getPosition();
		_modelLocation.setPosition(pos.x, pos.y - 1.85f, pos.z);

		_playerLocation.getRotation().normalizedPositiveZ(_angleBuf);
		_angleBuf.y = 0f;
		_angleBuf.normalize();

		_rotBuf.identity();
		_rotBuf.lookAlong(_angleBuf, _up);
		_modelLocation.setRotation(_rotBuf);
	}
}
