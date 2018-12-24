package net.whg.we.gamelogic;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.joml.Quaternionf;
import net.whg.we.main.CorePlugin;
import net.whg.we.rendering.Camera;
import net.whg.we.rendering.Material;
import net.whg.we.rendering.RenderGroup;
import net.whg.we.rendering.ScreenClearType;
import net.whg.we.rendering.Shader;
import net.whg.we.scene.Model;
import net.whg.we.utils.Color;
import net.whg.we.utils.FPSLogger;
import net.whg.we.utils.FileUtils;
import net.whg.we.utils.FirstPersonCamera;
import net.whg.we.utils.Input;
import net.whg.we.utils.Log;
import net.whg.we.utils.ModelLoader;
import net.whg.we.utils.Screen;
import net.whg.we.utils.Time;

public class DefaultGameLoop implements GameLoop
{
	private CorePlugin _core;

	public DefaultGameLoop(CorePlugin core)
	{
		_core = core;
	}

	@Override
	public void loop()
	{
		// Initialize
		Time.resetTime();
		FPSLogger.resetLogTimer();
		_core.getGraphics().setClearScreenColor(new Color(0.2f, 0.4f, 0.8f));

		String vert = "";
		String frag = "";
		try
		{
			vert = FileUtils.loadFileAsString(FileUtils.getResource(_core, "shader.vert"));
			frag = FileUtils.loadFileAsString(FileUtils.getResource(_core, "shader.frag"));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		Shader defaultShader = new Shader("default", vert, frag);
		Material defaultMaterial = new Material(defaultShader);

		RenderGroup renderGroup = new RenderGroup();
		Model monkey, quad;

		{
			monkey = new Model(
					ModelLoader.loadModel(FileUtils.getResource(_core, "monkey_head.fbx"),
							_core.getGraphics())._meshes.get(0),
					defaultMaterial);
			monkey.getLocation().setRotation(new Quaternionf().rotateX(-3.1415f / 2f));
			renderGroup.addRenderable(monkey);

			quad = new Model(ModelLoader.loadModel(FileUtils.getResource(_core, "floor.obj"),
					_core.getGraphics())._meshes.get(0), defaultMaterial);
			renderGroup.addRenderable(quad);
		}

		Camera camera = new Camera();
		FirstPersonCamera firstPerson = new FirstPersonCamera(camera);

		Log.trace("Starting default update loop.");
		while (true)
		{
			// Update
			Time.updateTime();
			FPSLogger.logFramerate();
			firstPerson.update();

			if (Input.isKeyDown("q"))
				Screen.setMouseLocked(!Screen.isMouseLocked());
			if (Input.isKeyDown("escape"))
				_core.getWindow().requestClose();

			// Render
			_core.getGraphics().clearScreenPass(ScreenClearType.CLEAR_COLOR_AND_DEPTH);
			renderGroup.render(camera);

			// End frame
			Input.endFrame();
			if (_core.getWindow().endFrame())
				break;
		}

		Log.debug("Disposing game.");
		monkey.getMesh().dispose();
		quad.getMesh().dispose();
		defaultShader.dispose();

		_core.getGraphics().dispose();
	}
}
