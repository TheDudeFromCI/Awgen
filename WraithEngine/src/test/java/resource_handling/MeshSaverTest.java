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
import net.whg.we.client_logic.rendering.ShaderAttributes;
import net.whg.we.client_logic.rendering.VertexData;
import net.whg.we.resource.ConverterData;
import net.whg.we.resource.MeshConverterLoader;
import net.whg.we.resource.MeshData;
import net.whg.we.resource.MeshSaver;
import net.whg.we.resource.SimpleFileDatabase;
import net.whg.we.resource.UncompiledMesh;

public class MeshSaverTest
{
	@Test
	public void saveAndLoad() throws IOException
	{
		// Build mesh data
		UncompiledMesh mesh = new UncompiledMesh();
		mesh.name = "steve_model";

		ShaderAttributes att = new ShaderAttributes();
		att.addAttribute("pos", 3);

		float[] verts = new float[]
		{
			0f, 1f, 3f, 5f, 2f, -10f, -1230984f, 1000000f, -23049832f,
		};
		short[] tris = new short[]
		{
			0, 2, 1,
		};

		VertexData vertexData = new VertexData(verts, tris, att);
		mesh.vertexData = vertexData;

		// Where are we saving?
		File file =
				new SimpleFileDatabase(new File(".")).getFile(new ResourceFile("unit_tests/mesh_save_test.asset_mesh"));

		// Save mesh data
		MeshSaver.save(mesh, file);

		// Load mesh data
		UncompiledMesh mesh2 = MeshSaver.load(file);

		// Compare
		Assert.assertEquals(mesh, mesh2);

		file.delete();
	}

	@Test(timeout = 20000)
	public void loadFbx() throws IOException, InterruptedException
	{
		Graphics graphics = Mockito.mock(Graphics.class);
		ResourceDatabase database = new ResourceDatabase();
		ResourceLoader loader = new ResourceLoader();
		SimpleFileDatabase fileDatabase = new SimpleFileDatabase(new File("."));
		ResourceManager manager = new ResourceManager(database, loader, fileDatabase);
		loader.addFileLoader(new MeshConverterLoader(graphics));

		Resource resource = manager.loadResource(new ResourceFile("unit_tests/cube.fbx"));

		while (!resource.reload())
			Thread.sleep(10);

		Assert.assertEquals(ResourceState.FULLY_LOADED, resource.getResourceState());

		ConverterData converterData = (ConverterData) resource.getData();
		Resource cubeMesh = manager.loadResource(converterData.getResourceFiles()[0]);

		Assert.assertEquals(ResourceState.FULLY_LOADED, cubeMesh.getResourceState());

		MeshData meshData = (MeshData) cubeMesh.getData();

		Assert.assertEquals(24, meshData.getVertexData().getVertexCount());
		Assert.assertEquals(12, meshData.getVertexData().getTriangleCount());
	}
}
