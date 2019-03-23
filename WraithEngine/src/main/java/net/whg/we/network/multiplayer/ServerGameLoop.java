package net.whg.we.network.multiplayer;

import net.whg.we.client_logic.resources.ResourceManager;
import net.whg.we.event.EventCaller;
import net.whg.we.scene.GameLoop;
import net.whg.we.scene.UpdateEventCaller;
import net.whg.we.scene.UpdateListener;
import net.whg.we.utils.Time;
import net.whg.we.utils.logging.Log;

public class ServerGameLoop implements GameLoop
{
    private boolean _running = true;
    private MultiplayerServer _server;
    private UpdateEventCaller _updateListener = new UpdateEventCaller();
    private ResourceManager _resourceManager;
    private boolean _disconnectWhenNoClients;

    public ServerGameLoop(MultiplayerServer server,
            ResourceManager resourceManager, boolean disconnectWhenNoClients)
    {
        _server = server;
        _resourceManager = resourceManager;
        _disconnectWhenNoClients = disconnectWhenNoClients;
    }

    @Override
    public void run()
    {
        Log.info("Starting server game loop.");

        long startTime = System.currentTimeMillis();
        long usedPhysicsFrames = 0;

        _updateListener.init();

        // TODO Localhost players will fail to connect if they time out too
        // quickly. This will cause the client to close, and the server to stay
        // open in the background.
        boolean clientConnected = false;
        while (_running)
        {
            try
            {
                long currentTime = System.currentTimeMillis();
                double passed = (currentTime - startTime) / 1000.0;
                double physicsFrames = passed * Time.getPhysicsFramerate();

                while (usedPhysicsFrames < physicsFrames)
                {
                    usedPhysicsFrames++;
                    _server.updatePhysics();
                    _updateListener.onUpdate();

                    // Sets flag to true when at least one client has connected.
                    if (_server.getPlayerList().getPlayerCount() > 0)
                        clientConnected = true;

                    // Closes server when last player has logged off.
                    if (clientConnected && _disconnectWhenNoClients
                            && _server.getPlayerList().getPlayerCount() == 0)
                    {
                        Log.info(
                                "Last player has disconnected from server. Closing server.");
                        requestClose();
                    }
                }

                // Sleep for 1 millisecond to prevent maxing the CPU.
                sleep();
            }
            catch (Exception e)
            {
                Log.fatalf("Uncaught exception in main thread!", e);
                break;
            }
        }

        Log.info("Stopping server game loop.");
        _server.stopServer();
        _resourceManager.disposeAllResources();
    }

    private void sleep()
    {
        try
        {
            Thread.sleep(1);
        }
        catch (InterruptedException e)
        {
        }
    }

    @Override
    public void requestClose()
    {
        Log.debug("Requesting close for server game loop.");
        _running = false;
    }

    @Override
    public EventCaller<UpdateListener> getUpdateEvent()
    {
        return _updateListener;
    }

    public MultiplayerServer getServer()
    {
        return _server;
    }

    public ResourceManager getResourceManager()
    {
        return _resourceManager;
    }
}
