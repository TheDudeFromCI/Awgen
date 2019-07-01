package net.whg.frameworks.render;

import net.whg.frameworks.external.BGFXWindow;

public class BgfxTest
{
	public static void main(String[] args)
	{
		WindowProperties properties = new WindowProperties();
		properties.title = "BGFX Test";
		properties.width = 640;
		properties.height = 480;

		Window window = new BGFXWindow();
		window.init(properties);
		window.show();
	}
}
