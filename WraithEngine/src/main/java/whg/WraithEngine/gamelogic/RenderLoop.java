package whg.WraithEngine.gamelogic;

import whg.WraithEngine.KeyboardEventsHandler;
import whg.WraithEngine.MouseEventsHandler;
import whg.WraithEngine.Window;
import whg.WraithEngine.WindowEventsHandler;

public interface RenderLoop
{
	public void loop(Window window);
	
	public WindowEventsHandler getWindowEventsHandler();
	
	public KeyboardEventsHandler getKeyboardEventsHandler();
	
	public MouseEventsHandler getMouseEventsHandler();
}
