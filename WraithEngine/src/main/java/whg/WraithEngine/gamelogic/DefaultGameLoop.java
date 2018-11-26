package whg.WraithEngine.gamelogic;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import whg.WraithEngine.DefaultInputHandler;
import whg.WraithEngine.DefaultWindowHandler;
import whg.WraithEngine.KeyboardEventsHandler;
import whg.WraithEngine.MouseEventsHandler;
import whg.WraithEngine.Window;
import whg.WraithEngine.WindowEventsHandler;
import whg.WraithEngine.core.Input;
import whg.WraithEngine.core.Time;
import whg.WraithEngine.rendering.Camera;
import whg.WraithEngine.rendering.Universe;
import whg.WraithEngine.utils.FPSLogger;
import whg.WraithEngine.utils.ResourceLoader;

public class DefaultGameLoop implements RenderLoop
{
	private DefaultInputHandler _inputHandler;
	private DefaultWindowHandler _windowHandler;
	private Camera _camera;
	private FPSLogger _fpsLogger;
	private Universe _universe;
	
	public DefaultGameLoop()
	{
		_camera = new Camera();
		_fpsLogger = new FPSLogger();
		_universe = new Universe();

		_inputHandler = new DefaultInputHandler();
		_windowHandler = new DefaultWindowHandler(_camera);
	}
	
	public Camera getCamera()
	{
		return _camera;
	}
	
	public FPSLogger getFPSLogger()
	{
		return _fpsLogger;
	}
	
	public Universe getUniverse()
	{
		return _universe;
	}

	@Override
	public void loop(Window window)
	{
		// LOOP
		while(!window.isRequestingClose())
		{
			Time.updateTime();
			_fpsLogger.logFramerate();
			window.pollEvents();
			
			_universe.update();
			_universe.render();
			
			// ERROR CHECK
			int error;
			while ((error = GL11.glGetError()) != GL11.GL_NO_ERROR)
			{
				String codeName;
				switch (error)
				{
					case GL11.GL_INVALID_OPERATION:
						codeName = "Invalid Operation";
						break;
					case GL11.GL_INVALID_ENUM:
						codeName = "Invalid Enum";
						break;
					case GL11.GL_INVALID_VALUE:
						codeName = "Invalid Value";
						break;
					case GL11.GL_OUT_OF_MEMORY:
						codeName = "Out of Memory";
						break;
					case GL30.GL_INVALID_FRAMEBUFFER_OPERATION:
						codeName = "Invalid Framebuffer Operation";
						break;
					default:
						codeName = "Unknown Error";
						break;
				}

				System.err.println("OpenGL Error. " + codeName);
				window.requestClose();
			}
			
			// FINISH
			Input.endFrame();
			window.endFrame();
		}
		
		// DISPOSE
		ResourceLoader.disposeAllResources();
	}

	@Override
	public WindowEventsHandler getWindowEventsHandler()
	{
		return _windowHandler;
	}

	@Override
	public KeyboardEventsHandler getKeyboardEventsHandler()
	{
		return _inputHandler;
	}

	@Override
	public MouseEventsHandler getMouseEventsHandler()
	{
		return _inputHandler;
	}

}
