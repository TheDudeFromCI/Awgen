package net.whg.we.resource;

import java.nio.ByteBuffer;
import net.whg.we.legacy.Color;

public interface TextureColorData
{
	int width();

	int height();

	int bytesPerPixel();

	Color getColorAt(int x, int y);

	void put(ByteBuffer buffer);
}
