package net.whg.we.scene;

import net.whg.we.event.EventCaller;
import net.whg.we.network.multiplayer.MultiplayerClient;
import net.whg.we.rendering.Camera;
import net.whg.we.rendering.GraphicsPipeline;
import net.whg.we.rendering.ScreenClearType;
import net.whg.we.resources.ResourceFetcher;
import net.whg.we.resources.ResourceManager;
import net.whg.we.ui.terminal.Terminal;
import net.whg.we.utils.Color;
import net.whg.we.utils.FPSLogger;
import net.whg.we.utils.FirstPersonCamera;
import net.whg.we.utils.Input;
import net.whg.we.utils.Screen;
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

    public WindowedGameLoop(ResourceManager resourceManager,
            MultiplayerClient client)
    {
        _resourceManager = resourceManager;
        _graphicsPipeline = new GraphicsPipeline();
        _client = client;
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

        _firstPerson.setMoveSpeed(Input.isKeyHeld("control") ? 70f : 7f);
        _firstPerson.update();

        if (Input.isKeyDown("q"))
            Screen.setMouseLocked(!Screen.isMouseLocked());
        if (Input.isKeyDown("escape"))
            requestClose();
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
}
