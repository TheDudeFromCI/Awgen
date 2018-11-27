package whg.WraithEngine.core;

import java.io.File;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import whg.WraithEngine.gamelogic.DefaultGameLoop;
import whg.WraithEngine.gamelogic.FirstPersonCamera;
import whg.WraithEngine.gamelogic.Location;
import whg.WraithEngine.gamelogic.Universe;
import whg.WraithEngine.gamelogic.World;
import whg.WraithEngine.rendering.Material;
import whg.WraithEngine.rendering.Mesh;
import whg.WraithEngine.rendering.Model;
import whg.WraithEngine.rendering.ModelScene;
import whg.WraithEngine.rendering.Shader;
import whg.WraithEngine.rendering.SkinnedMesh;
import whg.WraithEngine.rendering.SkinnedModel;
import whg.WraithEngine.utils.FileUtils;
import whg.WraithEngine.utils.InitalizerEntity;
import whg.WraithEngine.utils.Log;
import whg.WraithEngine.utils.ModelLoader;
import whg.WraithEngine.utils.ResourceLoader;
import whg.WraithEngine.window.QuitGameListener;
import whg.WraithEngine.window.Window;

public class Main
{
	public static final String VERSION = "v1";
	
	public static void main(String[] args)
	{
		Log.trace("Starting WraithEngine.");
		Log.debugf("WraithEngine Version: %s", VERSION);
		Log.debugf("Operating System: %s", System.getProperty("os.name"));
		Log.debugf("Operating System Arch: %s", System.getProperty("os.arch"));
		Log.debugf("Java Version: %s", System.getProperty("java.version"));
		Log.debugf("Java Vendor: %s", System.getProperty("java.vendor"));
		Log.debugf("System User: %s", System.getProperty("user.name"));
		Log.debugf("Working Directory: %s", System.getProperty("user.dir"));
		Log.debugf("LWJGL Version: %s", org.lwjgl.Version.getVersion());
		
		 DefaultGameLoop gameLoop = new DefaultGameLoop();
		 
		 Window window = new Window();
		 window.setWindowName("WraithEngine");
		 window.setResizable(true);
		 window.setRenderLoop(gameLoop);

			Universe universe = gameLoop.getUniverse();
			World world = new World(gameLoop.getCamera());
			universe.addWorld(world);
			
			FirstPersonCamera firstPersonCamera = new FirstPersonCamera(gameLoop.getCamera());
			firstPersonCamera.setMouseSensitivity(1f);
			world.addEntity(firstPersonCamera);
			
			world.addEntity(new QuitGameListener(window, "escape"));
			world.addEntity(new InitalizerEntity(world)
			{
				@Override
				public void run()
				{
					GL11.glClearColor(0.2f, 0.4f, 0.8f, 1f);
					GL11.glEnable(GL11.GL_DEPTH_TEST);
					
					world.getCamera().getLocation().setPosition(new Vector3f(0f, 3f, 5f));
					
					File monkeyHeadModel = FileUtils.getResource("monkey_head.fbx");
					ModelScene scene = ModelLoader.loadModel(monkeyHeadModel);
					
					SkinnedMesh monkey = (SkinnedMesh) scene._meshes.get(0);
					Mesh floor = scene._meshes.get(1);
					
					Shader baseShader = ResourceLoader.loadShader("shader");
					Shader boneShader = ResourceLoader.loadShader("shader_bone");
					
					baseShader.loadUniform("_mvpMat");
					boneShader.loadUniform("_mvpMat");
					boneShader.loadUniform("_bones");

					Material baseMaterial = new Material(baseShader);
					Material boneMaterial = new Material(boneShader);

					SkinnedModel monkeyModel = new SkinnedModel(monkey, new Location(), boneMaterial);
					Model floorModel = new Model(floor, new Location(), baseMaterial);
					
					world.addEntity(monkeyModel);
					world.addEntity(floorModel);
					
					Quaternionf rot = new Quaternionf().rotateLocalX((float)(-Math.PI/2f));
					monkeyModel.getLocation().setRotation(rot);
					floorModel.getLocation().setRotation(rot);

					monkey.getSkeleton().getBones()[1].getLocalTransform().rotateLocalZ(1f);
					monkey.rebuildTransformBuffer();
				}
			});
		 
		 window.build();
		 window.loop();
		 window.destroy();
	}
}	
