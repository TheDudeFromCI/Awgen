package net.whg.we.client_logic.resources.graphics;

import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.Assimp;
import net.whg.frameworks.logging.Log;
import net.whg.we.client_logic.rendering.Skeleton;
import net.whg.we.client_logic.rendering.VertexData;
import net.whg.we.client_logic.resources.FileLoader;
import net.whg.we.client_logic.resources.ResourceDatabase;
import net.whg.we.client_logic.resources.ResourceFile;
import net.whg.we.client_logic.resources.ResourceLoader;

/**
 * Loads a mesh scene file. These can contain either a single mesh, or multiple
 * meshes,
 *
 * @author TheDudeFromCI
 */
public class MeshLoader implements FileLoader
{
    private static final String[] FILE_TYPES =
    {
            "fbx", "obj", "dae", "gltf", "glb", "blend", "3ds", "ase", "ifc",
            "xgl", "zgl", "ply", "lwo", "lws", "lxo", "stl", "x", "ac", "ms3d"
    };

    @Override
    public String[] getTargetFileTypes()
    {
        return FILE_TYPES;
    }

    @Override
    public MeshResource loadFile(ResourceLoader resourceLoader,
            ResourceDatabase database, ResourceFile resourceFile)
    {
        try
        {
            // Load the scene file
            AIScene scene =
                    Assimp.aiImportFile(resourceFile.getFile().toString(),
                            Assimp.aiProcess_Triangulate
                                    | Assimp.aiProcess_GenSmoothNormals
                                    | Assimp.aiProcess_FlipUVs
                                    | Assimp.aiProcess_CalcTangentSpace
                                    | Assimp.aiProcess_LimitBoneWeights
                                    | Assimp.aiProcess_SplitLargeMeshes
                                    | Assimp.aiProcess_OptimizeMeshes
                                    | Assimp.aiProcess_JoinIdenticalVertices);

            // If scene could not be loaded, return null
            if (scene == null)
                return null;

            // Count scene information
            int meshCount = scene.mNumMeshes();

            MeshResource mainMesh = null;

            // Load scene meshes
            for (int i = 0; i < meshCount; i++)
            {
                AIMesh mesh = AIMesh.create(scene.mMeshes().get(i));
                String meshName = mesh.mName().dataString();
                VertexData vertexData = AssimpMeshParser.loadMesh(mesh);
                Skeleton skeleton = AssimpSkeletonParser.loadSkeleton(scene,
                        mesh, vertexData);

                MeshResource resource = new MeshResource(resourceFile, meshName,
                        vertexData, skeleton);
                database.addResource(resource);

                Log.debugf("Successfully loaded mesh resource, %s.", resource);

                if (resource.getName().equals(resourceFile.getName())
                        || meshCount == 1)
                    mainMesh = resource;
            }

            scene.free();

            Log.debugf("Successfully returned main mesh resource, %s.",
                    mainMesh);
            return mainMesh;
        }
        catch (Exception exception)
        {
            Log.errorf("Failed to load mesh resource %s!", exception,
                    resourceFile);
            return null;
        }
    }

    @Override
    public int getPriority()
    {
        return 0;
    }
}
