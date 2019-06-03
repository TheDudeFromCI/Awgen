package resource_handling;

import java.io.File;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import net.whg.frameworks.resource.FileUtils;
import net.whg.frameworks.resource.Resource;
import net.whg.frameworks.resource.ResourceDatabase;
import net.whg.frameworks.resource.ResourceFile;
import net.whg.frameworks.resource.ResourceLoader;
import net.whg.frameworks.resource.ResourceManager;
import net.whg.frameworks.resource.ResourceState;
import net.whg.we.client_logic.rendering.Graphics;
import net.whg.we.client_logic.rendering.VShader;
import net.whg.we.resource.ConverterData;
import net.whg.we.resource.ShaderConverterLoader;
import net.whg.we.resource.ShaderData;
import net.whg.we.resource.ShaderLoader;
import net.whg.we.resource.ShaderSaver;
import net.whg.we.resource.SimpleFileDatabase;
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
	public void load() throws IOException, InterruptedException
	{
		loadShader();
		loadShaderAsset();
	}

	private void loadShader() throws IOException, InterruptedException
	{
		Graphics graphics = Mockito.mock(Graphics.class);
		ResourceDatabase database = new ResourceDatabase();
		ResourceLoader loader = new ResourceLoader();
		SimpleFileDatabase fileDatabase = new SimpleFileDatabase(new File("."));
		ResourceManager manager = new ResourceManager(database, loader, fileDatabase);
		loader.addFileLoader(new ShaderConverterLoader(graphics));

		Resource resource = manager.loadResource(new ResourceFile("unit_tests/normal_shader.glsl"));

		while (!resource.reload())
			Thread.sleep(10);

		Assert.assertEquals(ResourceState.FULLY_LOADED, resource.getResourceState());

		ConverterData converterData = (ConverterData) resource.getData();
		Resource normalShader = manager.loadResource(converterData.getResourceFiles()[0]);

		Assert.assertEquals(ResourceState.FULLY_LOADED, normalShader.getResourceState());

		ShaderData shaderData = (ShaderData) normalShader.getData();

		Assert.assertEquals("normal_shader", shaderData.getName());
		Assert.assertNotNull(shaderData.getVertexShaderCode());
		Assert.assertNull(shaderData.getGeometryShaderCode());
		Assert.assertNotNull(shaderData.getFragmentShaderCode());
	}

	private void loadShaderAsset() throws IOException, InterruptedException
	{
		Graphics graphics = Mockito.mock(Graphics.class);
		Mockito.when(graphics.prepareShader(ArgumentMatchers.any())).thenReturn(Mockito.mock(VShader.class));
		ResourceDatabase database = new ResourceDatabase();
		ResourceLoader loader = new ResourceLoader();
		SimpleFileDatabase fileDatabase = new SimpleFileDatabase(new File("."));
		ResourceManager manager = new ResourceManager(database, loader, fileDatabase);
		loader.addFileLoader(new ShaderLoader(graphics));

		Resource resource =
				manager.loadResource(new ResourceFile("unit_tests/normal_shader_glsl/normal_shader.asset_shader"));

		while (!resource.reload())
			Thread.sleep(10);

		Assert.assertEquals(ResourceState.FULLY_LOADED, resource.getResourceState());

		ShaderData shaderData = (ShaderData) resource.getData();

		Assert.assertEquals("normal_shader", shaderData.getName());
		Assert.assertNotNull(shaderData.getVertexShaderCode());
		Assert.assertNull(shaderData.getGeometryShaderCode());
		Assert.assertNotNull(shaderData.getFragmentShaderCode());

		FileUtils.deleteDirectory(fileDatabase.getFile("unit_tests/normal_shader_glsl"));
	}
}
