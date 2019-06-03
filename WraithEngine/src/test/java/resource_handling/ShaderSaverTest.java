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
import net.whg.we.resource.ConverterData;
import net.whg.we.resource.ShaderSaver;
import net.whg.we.resource.SimpleFileDatabase;
import net.whg.we.resource.TextureConverterLoader;
import net.whg.we.resource.TextureData;
import net.whg.we.resource.UncompiledShader;

public class ShaderSaverTest
{
	@Test
	public void saveAndLoad() throws IOException
	{
		UncompiledShader shader = new UncompiledShader();
		shader.name = "diffuse_unlit";
		shader.vertShader = "vertex shader code";
		shader.fragShader = "fragment shader code";

		File file = new SimpleFileDatabase(new File("."))
				.getFile(new ResourceFile("unit_tests/shader_save_test.asset_shader"));

		ShaderSaver.save(shader, file);

		UncompiledShader shader2 = ShaderSaver.load(file);

		Assert.assertEquals(shader, shader2);

		file.delete();
	}

	@Test(timeout = 20000)
	public void loadShader() throws IOException, InterruptedException
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
