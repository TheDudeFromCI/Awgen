package net.whg.we.network.multiplayer;

import java.io.IOException;
import net.whg.we.network.multiplayer.MultiplayerUtils;
import net.whg.we.network.packet.DefaultPacketFactory;
import net.whg.we.network.packet.PacketClient;
import net.whg.we.network.packet.PacketHandler;
import net.whg.we.network.packet.PacketServer;
import net.whg.we.utils.logging.Log;

public class NetworkManager
{
    public static NetworkManager parseArgs(String[] args)
    {
        NetworkManager networkManager = new NetworkManager();

        for (int i = 0; i < args.length; i++)
        {
            if (args[i].equals("-s") || args[i].equals("-server"))
            {
                if (args.length <= i + 1)
                    throw new IllegalArgumentException(
                            "Port not specified for server!");

                i++;
                int port;

                try
                {
                    port = Integer.valueOf(args[i]);
                }
                catch (NumberFormatException e)
                {
                    throw new IllegalArgumentException(
                            "Not a number! '" + args[i] + "'");
                }

                try
                {
                    networkManager.attachServer(port);
                }
                catch (IOException e)
                {
                    Log.errorf("Failed to start server!", e);
                }

            }

            if (args[i].equals("-c") || args[i].equals("-client"))
            {
                if (args.length <= i + 1)
                    throw new IllegalArgumentException(
                            "IP not specified for client!");

                i++;

                String[] ip_parts = args[i].split(":");

                String ip = ip_parts[0];
                int port;

                try
                {
                    port = Integer.valueOf(ip_parts[1]);
                }
                catch (NumberFormatException e)
                {
                    throw new IllegalArgumentException(
                            "Not a number! '" + args[i] + "'");
                }
                catch (ArrayIndexOutOfBoundsException e)
                {
                    throw new IllegalArgumentException(
                            "Port not specified! '" + args[i] + "'");
                }

                try
                {
                    networkManager.attachClient(ip, port);
                }
                catch (IOException e)
                {
                    Log.errorf("Failed to start server!", e);
                }

            }
        }

        return networkManager;
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

    public boolean isLocalHost()
    {
        return hasServer() && hasClient();
    }
}
