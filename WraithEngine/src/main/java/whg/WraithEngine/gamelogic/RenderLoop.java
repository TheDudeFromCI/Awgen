package whg.WraithEngine.gamelogic;

import whg.WraithEngine.window.KeyboardEventsHandler;
import whg.WraithEngine.window.MouseEventsHandler;
import whg.WraithEngine.window.Window;
import whg.WraithEngine.window.WindowEventsHandler;

public interface RenderLoop
{
	public void loop(Window window);
	
	public WindowEventsHandler getWindowEventsHandler();
	
	public KeyboardEventsHandler getKeyboardEventsHandler();
	
	public MouseEventsHandler getMouseEventsHandler();
}
