package net.whg.we.resource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.Assimp;
import net.whg.frameworks.logging.Log;
import net.whg.frameworks.resource.Resource;
import net.whg.frameworks.resource.ResourceData;
import net.whg.frameworks.resource.ResourceFile;
import net.whg.frameworks.resource.ResourceFuture;
import net.whg.frameworks.resource.ResourceManager;
import net.whg.frameworks.resource.ResourceState;
import net.whg.we.client_logic.rendering.Graphics;

public class MeshConverterFuture implements ResourceFuture
{
	private Object LOCK = new Object();

	private Graphics _graphics;
	private File _file;
	private int _loadState;
	private List<UncompiledMesh> _meshes;
	private ResourceManager _resourceManager;
	private String _destinationFolder;

	public MeshConverterFuture(Graphics graphics, ResourceManager resourceManager, File file, String destinationFolder)
	{
		_graphics = graphics;
		_file = file;
		_loadState = ResourceFuture.NO_CHANGE;
		_resourceManager = resourceManager;
		_destinationFolder = destinationFolder;
	}

	@Override
	public void run()
	{
		try
		{
			// Load the scene file
			AIScene scene = Assimp.aiImportFile(_file.toString(),
					Assimp.aiProcess_Triangulate | Assimp.aiProcess_GenSmoothNormals | Assimp.aiProcess_FlipUVs
							| Assimp.aiProcess_CalcTangentSpace | Assimp.aiProcess_LimitBoneWeights
							| Assimp.aiProcess_SplitLargeMeshes | Assimp.aiProcess_OptimizeMeshes
							| Assimp.aiProcess_JoinIdenticalVertices);

			// If scene could not be loaded, return null
			if (scene == null)
				throw new IllegalStateException("Failed to load scene!");

			// Count scene information
			int meshCount = scene.mNumMeshes();

			_meshes = new ArrayList<>();

			// Load scene meshes
			for (int i = 0; i < meshCount; i++)
			{
				// Load each mesh from the file
				UncompiledMesh m = new UncompiledMesh();

				AIMesh mesh = AIMesh.create(scene.mMeshes().get(i));
				m.name = mesh.mName().dataString();
				m.vertexData = AssimpMeshParser.loadMesh(mesh);
				m.skeleton = AssimpSkeletonParser.loadSkeleton(scene, mesh, m.vertexData);
				m.path = new ResourceFile(_destinationFolder + "/" + m.name + ".asset_mesh");

				_meshes.add(m);

				// And save each mesh while we're here
				MeshSaver.save(m, _resourceManager.getFile(m.path));
			}

			scene.free();

			synchronized (LOCK)
			{
				_loadState = ResourceFuture.FULLY_LOADED;
			}
		}
		catch (Exception e)
		{
			Log.errorf("Failed to convert mesh file at %s!", e, _file);

			synchronized (LOCK)
			{
				_loadState = ResourceFuture.UNABLE_TO_LOAD;
			}
		}
	}

	@Override
	public int sync(ResourceData data)
	{
		synchronized (LOCK)
		{
			if (_loadState != ResourceFuture.FULLY_LOADED)
				return _loadState;

			int resourceCount = _meshes.size();
			ResourceFile[] resourceFiles = new ResourceFile[resourceCount];

			for (int i = 0; i < resourceCount; i++)
			{
				// Compile each mesh as we push over the data
				UncompiledMesh mesh = _meshes.get(i);

				MeshData meshData = new MeshData(_graphics, mesh.vertexData);
				Resource resource = new Resource(mesh.path, meshData, ResourceState.FULLY_LOADED);

				_resourceManager.getResourceDatabase().addResource(resource);
				resourceFiles[i] = mesh.path;
			}

			ConverterData converterData = (ConverterData) data;
			converterData.setLoadedResourceFiles(resourceFiles);

			return ResourceFuture.FULLY_LOADED;
		}
	}
}
