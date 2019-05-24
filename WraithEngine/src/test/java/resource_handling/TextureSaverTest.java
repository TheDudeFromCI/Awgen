package resource_handling;

import java.io.File;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import net.whg.frameworks.resource.Resource;
import net.whg.frameworks.resource.ResourceDatabase;
import net.whg.frameworks.resource.ResourceFile;
import net.whg.frameworks.resource.ResourceLoader;
import net.whg.frameworks.resource.ResourceManager;
import net.whg.frameworks.resource.ResourceState;
import net.whg.we.client_logic.rendering.Graphics;
import net.whg.we.client_logic.rendering.TextureSampleMode;
import net.whg.we.legacy.Color;
import net.whg.we.resource.ConverterData;
import net.whg.we.resource.IntRGBAColorData;
import net.whg.we.resource.SimpleFileDatabase;
import net.whg.we.resource.TextureConverterLoader;
import net.whg.we.resource.TextureData;
import net.whg.we.resource.TextureSaver;
import net.whg.we.resource.UncompiledTexture;

public class TextureSaverTest
{
	@Test
	public void saveAndLoad() throws IOException
	{
		UncompiledTexture texture = new UncompiledTexture();
		texture.name = "grass_diffuse";

		IntRGBAColorData colorData = new IntRGBAColorData(16, 16);
		for (int x = 0; x < 16; x++)
			for (int y = 0; y < 16; y++)
				colorData.setPixel(x, y, new Color(x / 16f, y / 16f, 0));

		texture.mipmapping = false;
		texture.sampleMode = TextureSampleMode.NEAREST;

		File file = new SimpleFileDatabase(new File("."))
				.getFile(new ResourceFile("unit_tests/texture_save_test.asset_texture"));

		TextureSaver.save(texture, file);

		UncompiledTexture texture2 = TextureSaver.load(file);

		Assert.assertEquals(texture, texture2);

		file.delete();
	}

	@Test(timeout = 20000)
	public void loadPng() throws IOException, InterruptedException
	{
		Graphics graphics = Mockito.mock(Graphics.class);
		ResourceDatabase database = new ResourceDatabase();
		ResourceLoader loader = new ResourceLoader();
		SimpleFileDatabase fileDatabase = new SimpleFileDatabase(new File("."));
		ResourceManager manager = new ResourceManager(database, loader, fileDatabase);
		loader.addFileLoader(new TextureConverterLoader(graphics));

		Resource resource = manager.loadResource(new ResourceFile("unit_tests/dirt.png"));

		while (!resource.reload())
			Thread.sleep(10);

		Assert.assertEquals(ResourceState.FULLY_LOADED, resource.getResourceState());

		ConverterData converterData = (ConverterData) resource.getData();
		Resource dirtTexture = manager.loadResource(converterData.getResourceFiles()[0]);

		Assert.assertEquals(ResourceState.FULLY_LOADED, dirtTexture.getResourceState());

		TextureData textureData = (TextureData) dirtTexture.getData();

		Assert.assertEquals(8, textureData.getColorData().width());
		Assert.assertEquals(8, textureData.getColorData().height());
	}
}
