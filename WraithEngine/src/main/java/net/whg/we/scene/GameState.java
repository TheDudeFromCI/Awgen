package net.whg.we.scene;

import net.whg.frameworks.network.connect.PlayerList;
import net.whg.frameworks.network.multiplayer.NetworkHandler;
import net.whg.we.client_logic.resources.ResourceManager;
import net.whg.we.legacy.SceneManager;

/**
 * The game state is the class in charge of handling the current active state of
 * the game. This includes all active scenes, resources, players, and so on.
 *
 * @author TheDudeFromCI
 */
public interface GameState
{
	GameLoop getGameLoop();

	SceneManager getSceneManager();

	PlayerList getPlayerList();

	ResourceManager getResourceManager();

	NetworkHandler getNetworkHandler();
}
