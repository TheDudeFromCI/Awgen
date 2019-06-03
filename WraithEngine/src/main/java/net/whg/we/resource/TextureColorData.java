package net.whg.we.resource;

import java.io.Externalizable;
import java.nio.ByteBuffer;
import net.whg.we.legacy.Color;

public interface TextureColorData extends Externalizable
{
	int width();

	int height();

	int bytesPerPixel();

	Color getColorAt(int x, int y);

	void put(ByteBuffer buffer);
}
