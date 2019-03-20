package net.whg.we.network.multiplayer;

import java.io.IOException;
import net.whg.we.network.multiplayer.MultiplayerUtils;
import net.whg.we.network.packet.DefaultPacketFactory;
import net.whg.we.network.packet.PacketClient;
import net.whg.we.network.packet.PacketHandler;
import net.whg.we.network.packet.PacketServer;

public class NetworkManager
{
    public static NetworkManager parseArgs(String[] args)
    {
        // TODO Sanity check inputs, and give better error feedback.

        if (args[0].equals("-s") || args[0].equals("-server"))
        {
            // Server but no client

            int port = Integer.valueOf(args[1]);

            NetworkManager networkManager = new NetworkManager();

            try
            {
                networkManager.attachServer(port);
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return networkManager;
        }

        if (args[0].equals("-c") || args[0].equals("-client"))
        {
            // Client but no server

            String[] ip_parts = args[1].split(":");
            String ip = ip_parts[0];
            int port = Integer.valueOf(ip_parts[1]);

            NetworkManager networkManager = new NetworkManager();

            try
            {
                networkManager.attachClient(ip, port);
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return networkManager;
        }

        if (args[0].equals("-l") || args[0].equals("-localhost"))
        {
            // Server and Client
            int port = Integer.valueOf(args[1]);

            NetworkManager networkManager = new NetworkManager();

            try
            {
                networkManager.attachServer(port);
                networkManager.attachClient("localhost", port);
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return networkManager;
        }

        throw new RuntimeException("Unspecified run time!");
    }

    private DefaultPacketFactory _factory;
    private PacketHandler _handler;
    private PacketServer _server;
    private PacketClient _client;

    public NetworkManager()
    {
        _factory = new DefaultPacketFactory();
        MultiplayerUtils.addDefaultPackets(_factory);
    }

    public void attachServer(int port) throws IOException
    {
        if (hasServer())
            throw new IllegalStateException("Server already attached!");

        _server = new PacketServer(_factory, _handler, port);
    }

    public void attachClient(String ip, int port) throws IOException
    {
        if (hasClient())
            throw new IllegalStateException("Client already attached!");

        _client = new PacketClient(_factory, _handler, ip, port);
    }

    public boolean hasServer()
    {
        return _server != null;
    }

    public boolean hasClient()
    {
        return _client != null;
    }

    public PacketServer getServer()
    {
        return _server;
    }

    public PacketClient getClient()
    {
        return _client;
    }
}
