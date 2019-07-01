package frameworks.scene;

import org.junit.Assert;
import org.junit.Test;
import net.whg.frameworks.scene.Scene;
import net.whg.frameworks.scene.SceneList;
import net.whg.frameworks.scene.SceneNode;

public class SceneListTest
{
	@Test
	public void add_remove_scene()
	{
		SceneList sceneList = new SceneList();
		Scene scene = new Scene();

		sceneList.addScene(scene);

		Assert.assertEquals(1, sceneList.getSceneCount());
		Assert.assertEquals(scene, sceneList.getSceneAt(0));

		sceneList.removeScene(scene);

		Assert.assertEquals(0, sceneList.getSceneCount());
	}

	@Test
	public void add_null_scene()
	{
		SceneList sceneList = new SceneList();
		sceneList.addScene(null);

		Assert.assertEquals(0, sceneList.getSceneCount());
	}

	@Test
	public void remove_null_scene()
	{
		SceneList sceneList = new SceneList();
		sceneList.addScene(new Scene());

		sceneList.removeScene(null);

		Assert.assertEquals(1, sceneList.getSceneCount());
	}

	@Test
	public void add_scene_twice()
	{
		SceneList sceneList = new SceneList();
		Scene scene = new Scene();

		sceneList.addScene(scene);
		sceneList.addScene(scene);

		Assert.assertEquals(1, sceneList.getSceneCount());
	}

	@Test
	public void getSceneById()
	{
		SceneList sceneList = new SceneList();
		Scene scene1 = new Scene();
		Scene scene2 = new Scene();
		Scene scene3 = new Scene();
		Scene scene4 = new Scene();

		sceneList.addScene(scene1);
		sceneList.addScene(scene2);
		sceneList.addScene(scene3);
		sceneList.addScene(scene4);

		Assert.assertEquals(scene3, sceneList.getSceneById(scene3.getSceneId()));
	}

	@Test
	public void getSceneById_SceneNotFound()
	{
		SceneList sceneList = new SceneList();
		Scene scene1 = new Scene(1L);
		Scene scene2 = new Scene(2L);

		sceneList.addScene(scene1);
		sceneList.addScene(scene2);

		Assert.assertNull(sceneList.getSceneById(0L));
	}

	@Test
	public void getSceneById_MultipleScenes_SameId()
	{
		SceneList sceneList = new SceneList();
		Scene scene1 = new Scene(1L);
		Scene scene2 = new Scene(1L);

		sceneList.addScene(scene1);
		sceneList.addScene(scene2);

		Assert.assertEquals(scene1, sceneList.getSceneById(1L));
	}

	@Test
	public void getSceneById_NoScenes()
	{
		SceneList sceneList = new SceneList();
		Assert.assertNull(sceneList.getSceneById(0L));
	}

	@Test
	public void buildNewScene()
	{
		SceneList sceneList = new SceneList();
		Scene scene1 = sceneList.buildNewScene();
		Scene scene2 = sceneList.buildNewScene();

		Assert.assertEquals(scene1, sceneList.getSceneAt(0));
		Assert.assertEquals(scene2, sceneList.getSceneAt(1));
	}

	@Test
	public void forEach()
	{
		SceneList sceneList = new SceneList();
		Scene scene1 = sceneList.buildNewScene();
		Scene scene2 = sceneList.buildNewScene();
		Scene scene3 = sceneList.buildNewScene();

		scene2.getRoot().addChild(new SceneNode());

		sceneList.forEach(scene ->
		{
			scene.getRoot().addChild(new SceneNode());
		});

		Assert.assertEquals(1, scene1.getRoot().getChildCount());
		Assert.assertEquals(2, scene2.getRoot().getChildCount());
		Assert.assertEquals(1, scene3.getRoot().getChildCount());
	}
}
