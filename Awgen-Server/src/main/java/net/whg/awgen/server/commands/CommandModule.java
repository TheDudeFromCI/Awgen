package net.whg.awgen.server.commands;

import net.whg.awgen.server.core.GameState;
import net.whg.awgenshell.Module;

public class CommandModule
{
	public static Module build(GameState gameState)
	{
		Module mod = new Module();

		mod.loadCommand(new SceneCommand(gameState));

		return mod;
	}
}
