package scene_handling;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import net.whg.we.scene.GameObject;
import net.whg.we.scene.GameObjectManager;
import net.whg.we.scene.Scene;

public class GameObjectTest
{
	@Test
	public void initBehaviour()
	{
		Scene scene = Mockito.mock(Scene.class);
		GameObjectManager manager = new GameObjectManager(scene);
		GameObject go = manager.createNew();

		Assert.assertEquals(manager, go.getManager());
	}

	@Test
	public void disposeBehaviour()
	{
		Scene scene = Mockito.mock(Scene.class);
		GameObjectManager manager = new GameObjectManager(scene);
		GameObject go = manager.createNew();

		go.destroy();

		Assert.assertFalse(go.isDisposed());

		manager.endFrame();

		Assert.assertTrue(go.isDisposed());
	}

	@Test
	public void gameObjectName()
	{
		Scene scene = Mockito.mock(Scene.class);
		GameObjectManager manager = new GameObjectManager(scene);
		GameObject go = manager.createNew();

		Assert.assertEquals(go.getName(), "Untitled GameObject");

		go.setName("New Name");

		Assert.assertEquals(go.getName(), "New Name");
	}

	@Test
	public void gameObjectToString()
	{
		Scene scene = Mockito.mock(Scene.class);
		GameObjectManager manager = new GameObjectManager(scene);
		GameObject go = manager.createNew();

		Assert.assertEquals(go.toString(), "[GameObject: Untitled GameObject]");
	}

	@Test
	public void managerSavesScene()
	{
		Scene scene = Mockito.mock(Scene.class);
		GameObjectManager manager = new GameObjectManager(scene);

		Assert.assertEquals(scene, manager.getScene());
	}
}
