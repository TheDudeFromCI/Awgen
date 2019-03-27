package net.whg.we.network.multiplayer;

import net.whg.we.network.packet.Packet;
import net.whg.we.utils.logging.Log;

public class MultiplayerClientListener implements ClientListener
{
	@Override
	public int getPriority()
	{
		return 0;
	}

	@Override
	public void onConnectToServer(MultiplayerClient client)
	{
		String username = client.getPlayer().getUsername();
		String token = client.getPlayer().getToken();

		Log.infof(
				"Successfully connected to server. Sending handshake packet now. Username: %s, Token: %s",
				username, token);

		Packet packet = client.newPacket("auth.handshake");
		((HandshakePacket) packet.getPacketType()).build(packet, username, token);
		client.sendPacket(packet);
	}

	@Override
	public void onDisconnectedFromServer(MultiplayerClient client)
	{
		Log.infof("Disconnected from server.");
	}
}
