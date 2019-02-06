package net.whg.we.rendering.opengl;

import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import net.whg.we.rendering.TextureProperties;
import net.whg.we.rendering.TextureSampleMode;
import net.whg.we.rendering.VTexture;
import net.whg.we.utils.Color;
import net.whg.we.utils.Log;

public class GLVTexture implements VTexture
{
	private OpenGLGraphics _opengl;
	private int _textureId;

	GLVTexture(OpenGLGraphics opengl, TextureProperties properties)
	{
		_opengl = opengl;

		ByteBuffer pixels =
				BufferUtils.createByteBuffer(properties.getWidth() * properties.getHeight() * 4);

		int x, y;
		Color color;
		for (y = 0; y < properties.getHeight(); y++)
		{
			for (x = 0; x < properties.getWidth(); x++)
			{
				color = properties.getPixels()[y * properties.getWidth() + x];

				pixels.put((byte) Math.round(color.r * 255));
				pixels.put((byte) Math.round(color.g * 255));
				pixels.put((byte) Math.round(color.b * 255));
				pixels.put((byte) Math.round(color.a * 255));
			}
		}

		pixels.flip();

		_textureId = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, _textureId);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, properties.getWidth(),
				properties.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixels);

		if (properties.hasMipmapping())
		{
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

			if (properties.getSampleMode() == TextureSampleMode.NEAREST)
			{
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
						GL11.GL_NEAREST_MIPMAP_NEAREST);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
						GL11.GL_NEAREST);
			}
			else if (properties.getSampleMode() == TextureSampleMode.BILINEAR)
			{
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
						GL11.GL_LINEAR_MIPMAP_NEAREST);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
						GL11.GL_LINEAR);
			}
			else
			{
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
						GL11.GL_LINEAR_MIPMAP_LINEAR);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
						GL11.GL_LINEAR);
			}
		}
		else
		{
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_GENERATE_MIPMAP, GL11.GL_FALSE);

			if (properties.getSampleMode() == TextureSampleMode.NEAREST)
			{
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
						GL11.GL_NEAREST);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
						GL11.GL_NEAREST);
			}
			else
			{
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
						GL11.GL_LINEAR);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
						GL11.GL_LINEAR);
			}
		}

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

		_opengl.checkForErrors("Loaded Texture");
	}

	@Override
	public void bind(int textureSlot)
	{
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + textureSlot);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, _textureId);

		_opengl.checkForErrors(Log.TRACE, "Bound Texture");
	}

	@Override
	public void dispose()
	{
		GL11.glDeleteTextures(_textureId);

		_opengl.checkForErrors("Disposed Texture");
	}
}
