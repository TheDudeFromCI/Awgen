package net.whg.we.scene;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import net.whg.we.event.EventCaller;
import net.whg.we.network.multiplayer.MultiplayerClient;
import net.whg.we.client_logic.rendering.Camera;
import net.whg.we.client_logic.rendering.GraphicsPipeline;
import net.whg.we.client_logic.rendering.ScreenClearType;
import net.whg.we.resources.ResourceFetcher;
import net.whg.we.resources.ResourceManager;
import net.whg.we.resources.scene.ModelResource;
import net.whg.we.scene.behaviours.MeshColliderBehaviour;
import net.whg.we.scene.behaviours.RenderBehaviour;
import net.whg.we.ui.terminal.Terminal;
import net.whg.we.utils.Color;
import net.whg.we.client_logic.utils.FPSLogger;
import net.whg.we.utils.FirstPersonCamera;
import net.whg.we.client_logic.utils.Input;
import net.whg.we.client_logic.utils.Screen;
import net.whg.we.utils.Time;
import net.whg.we.utils.logging.Log;

public class WindowedGameLoop implements GameLoop
{
    private boolean _isRunning;
    private UpdateEventCaller _updateListener = new UpdateEventCaller();
    private GraphicsPipeline _graphicsPipeline;
    private ResourceManager _resourceManager;
    private MultiplayerClient _client;
    private Terminal _terminal;
    private Scene _scene;
    private Camera _camera;
    private FirstPersonCamera _firstPerson;
    private CorePlugin _corePlugin;

    public WindowedGameLoop(ResourceManager resourceManager,
            MultiplayerClient client)
    {
        _resourceManager = resourceManager;
        _graphicsPipeline = new GraphicsPipeline();
        _client = client;
        _corePlugin = new CorePlugin();
    }

    @Override
    public void run()
    {
        if (_isRunning)
        {
            Log.warn("Cannot run game loop! Already running!");
            return;
        }

        _isRunning = true;

        try
        {
            ResourceFetcher fetch = new ResourceFetcher(_resourceManager,
                    _graphicsPipeline.getGraphics());
            _terminal = new Terminal(this, fetch);
            _scene = new Scene();
            _scene.getUIStack().addComponent(_terminal);

            _camera = new Camera();
            _scene.getRenderPass().setCamera(_camera);
            _firstPerson = new FirstPersonCamera(_camera);

            _graphicsPipeline.getGraphics()
                    .setClearScreenColor(new Color(0.2f, 0.4f, 0.8f));

            long startTime = System.currentTimeMillis();
            long usedPhysicsFrames = 0;

            _updateListener.init();

            // TODO Remove this. Loading a resources should occur through
            // commands and startup files, not pre-loaded.
            {
                ModelResource terrain = (ModelResource) _resourceManager
                        .loadResource(_corePlugin, "models/terrain.model");
                terrain.compile(_graphicsPipeline.getGraphics());
                Model model = terrain.getData();
                model.getLocation().setScale(new Vector3f(100f, 100f, 100f));
                model.getLocation().setRotation(new Quaternionf()
                        .rotateX((float) Math.toRadians(-90f)));
                GameObject go = _scene.getGameObjectManager().createNew();
                go.addBehaviour(new RenderBehaviour(model));
                go.addBehaviour(new MeshColliderBehaviour(
                        terrain.getMeshResource(0).getVertexData(),
                        model.getLocation()));
            }

            while (true)
            {
                try
                {
                    long currentTime = System.currentTimeMillis();
                    double passed = (currentTime - startTime) / 1000.0;
                    double physicsFrames = passed * Time.getPhysicsFramerate();

                    while (usedPhysicsFrames < physicsFrames)
                    {
                        usedPhysicsFrames++;

                        if (!_client.isRunning())
                            requestClose();

                        _client.updatePhysics();
                        _updateListener.onUpdate();
                        _scene.update();
                    }

                    // Calculate frame data
                    Time.updateTime();
                    FPSLogger.logFramerate();

                    updateFrame();

                    _graphicsPipeline.getGraphics().clearScreenPass(
                            ScreenClearType.CLEAR_COLOR_AND_DEPTH);
                    _updateListener.render();
                    _scene.render();

                    // End frame
                    Input.endFrame();
                    if (_graphicsPipeline.getWindow().endFrame())
                        break;
                }
                catch (Exception exception)
                {
                    Log.fatalf("Fatal error thrown in main thread!", exception);
                    _graphicsPipeline.requestClose();
                    break;
                }
            }

            _updateListener.dispose();
        }
        catch (Exception exception)
        {
            Log.fatalf("Uncaught exception in game loop!", exception);
        }
        finally
        {
            _client.stopClient();
            _resourceManager.disposeAllResources();
            _graphicsPipeline.dispose();
            _isRunning = false;
        }
    }

    private void updateFrame()
    {
        _updateListener.onUpdateFrame();
        _scene.updateFrame();

        // TODO Move first person camera to scene.
        _firstPerson.setMoveSpeed(Input.isKeyHeld("control") ? 70f : 7f);
        _firstPerson.update();

        if (Input.isKeyDown("q"))
            Screen.setMouseLocked(!Screen.isMouseLocked());
        if (Input.isKeyDown("escape"))
            requestClose();

        // TODO Ground raycasting should not be done here.
        Collision col = _scene.getPhysicsWorld().raycast(
                _firstPerson.getLocation().getPosition(),
                new Vector3f(0f, -1f, 0f), 10f);
        if (col != null)
            _firstPerson.getLocation()
                    .setPosition(col.getPosition().add(0f, 1.8f, 0f));
    }

    @Override
    public EventCaller<UpdateListener> getUpdateEvent()
    {
        return _updateListener;
    }

    public GraphicsPipeline getGraphicsPipeline()
    {
        return _graphicsPipeline;
    }

    @Override
    public void requestClose()
    {
        _graphicsPipeline.getWindow().requestClose();
    }

    public ResourceManager getResourceManager()
    {
        return _resourceManager;
    }

    public MultiplayerClient getClient()
    {
        return _client;
    }

    public Terminal getTerminal()
    {
        return _terminal;
    }

    public Scene getScene()
    {
        return _scene;
    }

    public CorePlugin getCorePlugin()
    {
        return _corePlugin;
    }
}
