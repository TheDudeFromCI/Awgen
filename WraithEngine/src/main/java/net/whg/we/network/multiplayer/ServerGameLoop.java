package net.whg.we.network.multiplayer;

import javax.annotation.Resource;
import net.whg.we.event.EventCaller;
import net.whg.we.network.packet.PacketServer;
import net.whg.we.resources.ResourceManager;
import net.whg.we.scene.GameLoop;
import net.whg.we.scene.UpdateEventCaller;
import net.whg.we.scene.UpdateListener;
import net.whg.we.utils.Time;
import net.whg.we.utils.logging.Log;

public class ServerGameLoop implements GameLoop
{
    private boolean _running = true;
    private PacketServer _server;
    private UpdateEventCaller _updateListener = new UpdateEventCaller();
    private ResourceManager _resourceManager;
    private boolean _disconnectWhenNoClients;

    public ServerGameLoop(PacketServer server, ResourceManager resourceManager,
            boolean disconnectWhenNoClients)
    {
        _server = server;
        _resourceManager = resourceManager;
        _disconnectWhenNoClients = disconnectWhenNoClients;
    }

    @Override
    public void run()
    {
        long startTime = System.currentTimeMillis();
        long usedPhysicsFrames = 0;

        _updateListener.init();

        boolean clientConnected = false;
        while (_running)
        {
            try
            {
                long currentTime = System.currentTimeMillis();
                double passed = (currentTime - startTime) / 1000.0;
                double physicsFrames = passed * Time.getPhysicsFramerate();

                while (usedPhysicsFrames++ < physicsFrames)
                {
                    _server.handlePackets();
                    _updateListener.onUpdate();

                    // Sets flag to true when at least one client has connected.
                    if (_server.getClientList().getClientCount() > 0)
                        clientConnected = true;

                    // Closes server when last player has logged off.
                    if (clientConnected
                            && _server.getClientList().getClientCount() == 0)
                        requestClose();
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
        _running = false;
    }

    @Override
    public EventCaller<UpdateListener> getUpdateEvent()
    {
        return null;
    }

    public PacketServer getServer()
    {
        return _server;
    }

    public ResourceManager getResourceManager()
    {
        return _resourceManager;
    }
}
