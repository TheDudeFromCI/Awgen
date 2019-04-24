package net.whg.we.client_logic.rendering.opengl;

import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import net.whg.frameworks.logging.Log;
import net.whg.we.client_logic.rendering.TextureSampleMode;
import net.whg.we.client_logic.rendering.VTexture;
import net.whg.we.resource.TextureColorData;
import net.whg.we.resource.TextureProperties;

public class GLVTexture implements VTexture
{
	private OpenGLGraphics _opengl;
	private int _textureId;

	GLVTexture(OpenGLGraphics opengl, TextureProperties properties)
	{
		_opengl = opengl;

		TextureColorData colorData = properties.colorData;
		ByteBuffer pixels = BufferUtils.createByteBuffer(colorData.width()
				* colorData.height() * colorData.bytesPerPixel());

		colorData.put(pixels);
		pixels.flip();

		_textureId = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, _textureId);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S,
				GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T,
				GL11.GL_REPEAT);

		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8,
				colorData.width(), colorData.height(), 0, GL11.GL_RGBA,
				GL11.GL_UNSIGNED_BYTE, pixels);

		// TODO Color data is currently expected to be in GL_RGBA8 form, only
		// Will fail on other color data states

		if (properties.mipmapping)
		{
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

			if (properties.sampleMode == TextureSampleMode.NEAREST)
			{
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
						GL11.GL_TEXTURE_MIN_FILTER,
						GL11.GL_NEAREST_MIPMAP_NEAREST);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
						GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			}
			else if (properties.sampleMode == TextureSampleMode.BILINEAR)
			{
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
						GL11.GL_TEXTURE_MIN_FILTER,
						GL11.GL_LINEAR_MIPMAP_NEAREST);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
						GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			}
			else
			{
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
						GL11.GL_TEXTURE_MIN_FILTER,
						GL11.GL_LINEAR_MIPMAP_LINEAR);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
						GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			}
		}
		else
		{
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_GENERATE_MIPMAP,
					GL11.GL_FALSE);

			if (properties.sampleMode == TextureSampleMode.NEAREST)
			{
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
						GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
						GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			}
			else
			{
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
						GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
						GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
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

	@Override
	public void recompile(TextureProperties properties)
	{
		dispose();

		TextureColorData colorData = properties.colorData;
		ByteBuffer pixels = BufferUtils.createByteBuffer(colorData.width()
				* colorData.height() * colorData.bytesPerPixel());

		colorData.put(pixels);
		pixels.flip();

		_textureId = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, _textureId);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S,
				GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T,
				GL11.GL_REPEAT);

		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8,
				colorData.width(), colorData.height(), 0, GL11.GL_RGBA,
				GL11.GL_UNSIGNED_BYTE, pixels);

		// TODO Color data is currently expected to be in GL_RGBA8 form, only
		// Will fail on other color data states

		if (properties.mipmapping)
		{
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

			if (properties.sampleMode == TextureSampleMode.NEAREST)
			{
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
						GL11.GL_TEXTURE_MIN_FILTER,
						GL11.GL_NEAREST_MIPMAP_NEAREST);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
						GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			}
			else if (properties.sampleMode == TextureSampleMode.BILINEAR)
			{
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
						GL11.GL_TEXTURE_MIN_FILTER,
						GL11.GL_LINEAR_MIPMAP_NEAREST);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
						GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			}
			else
			{
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
						GL11.GL_TEXTURE_MIN_FILTER,
						GL11.GL_LINEAR_MIPMAP_LINEAR);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
						GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			}
		}
		else
		{
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_GENERATE_MIPMAP,
					GL11.GL_FALSE);

			if (properties.sampleMode == TextureSampleMode.NEAREST)
			{
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
						GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
						GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			}
			else
			{
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
						GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
						GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			}
		}

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

		_opengl.checkForErrors("Loaded Texture");
	}
}
