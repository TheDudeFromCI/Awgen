package net.whg.we.scene;

import java.io.IOException;
import net.whg.we.event.EventCaller;
import net.whg.we.network.packet.PacketClient;
import net.whg.we.rendering.GraphicsPipeline;
import net.whg.we.resources.ResourceManager;
import net.whg.we.test.TestScene;
import net.whg.we.utils.FPSLogger;
import net.whg.we.utils.Input;
import net.whg.we.utils.Time;
import net.whg.we.utils.logging.Log;

public class WindowedGameLoop implements GameLoop
{
    private boolean _isRunning;
    private UpdateEventCaller _updateListener = new UpdateEventCaller();
    private GraphicsPipeline _graphicsPipeline;
    private ResourceManager _resourceManager;
    private PacketClient _client;

    public WindowedGameLoop(ResourceManager resourceManager,
            PacketClient client)
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
            new TestScene(this);

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

                    while (usedPhysicsFrames++ < physicsFrames)
                    {
                        if (_client.isClosed())
                            requestClose();

                        _client.handlePackets();
                        _updateListener.onUpdate();
                    }

                    // Calculate frame data
                    Time.updateTime();
                    FPSLogger.logFramerate();

                    _updateListener.onUpdateFrame();
                    _updateListener.render();

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
            try
            {
                _client.close();
            }
            catch (IOException e)
            {
                Log.errorf("Failed to close client!", e);
            }

            _resourceManager.disposeAllResources();
            _graphicsPipeline.dispose();
            _isRunning = false;
        }
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

    public PacketClient getClient()
    {
        return _client;
    }
}
