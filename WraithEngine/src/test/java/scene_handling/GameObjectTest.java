package scene_handling;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import net.whg.we.scene.GameObjectManager;
import net.whg.we.scene.GameObject;
import net.whg.we.main.GameState;

public class GameObjectTest
{
	@Test
	public void initBehaviour()
	{
		GameState gameState = Mockito.mock(GameState.class);
		GameObjectManager manager = new GameObjectManager(gameState);
		GameObject go = manager.createNew();

		Assert.assertEquals(manager, go.getManager());
	}

	@Test
	public void disposeBehaviour()
	{
		GameState gameState = Mockito.mock(GameState.class);
		GameObjectManager manager = new GameObjectManager(gameState);
		GameObject go = manager.createNew();

		go.destroy();

		Assert.assertFalse(go.isDisposed());

		manager.endFrame();

		Assert.assertTrue(go.isDisposed());
	}

	@Test
	public void gameObjectName()
	{
		GameState gameState = Mockito.mock(GameState.class);
		GameObjectManager manager = new GameObjectManager(gameState);
		GameObject go = manager.createNew();

		Assert.assertEquals(go.getName(), "Untitled GameObject");

		go.setName("New Name");

		Assert.assertEquals(go.getName(), "New Name");
	}

	@Test
	public void gameObjectToString()
	{
		GameState gameState = Mockito.mock(GameState.class);
		GameObjectManager manager = new GameObjectManager(gameState);
		GameObject go = manager.createNew();

		Assert.assertEquals(go.toString(), "[GameObject: Untitled GameObject]");
	}

	@Test
	public void managerSavesGameState()
	{
		GameState gameState = Mockito.mock(GameState.class);
		GameObjectManager manager = new GameObjectManager(gameState);

		Assert.assertEquals(gameState, manager.getGameState());
	}
}
