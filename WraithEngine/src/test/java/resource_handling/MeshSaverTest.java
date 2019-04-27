package resource_handling;

import java.io.File;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;
import net.whg.frameworks.resource.ResourceFile;
import net.whg.we.client_logic.rendering.ShaderAttributes;
import net.whg.we.client_logic.rendering.VertexData;
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
		File file = new SimpleFileDatabase(new File(".")).getFile(
				new ResourceFile("unit_tests/mesh_save_test.asset_mesh"));

		// Save mesh data
		MeshSaver.save(mesh, file);

		// Load mesh data
		UncompiledMesh mesh2 = MeshSaver.load(file);

		// Compare
		Assert.assertEquals(mesh, mesh2);
	}
}
