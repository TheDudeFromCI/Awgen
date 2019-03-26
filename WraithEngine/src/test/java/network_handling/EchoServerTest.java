package network_handling;

import org.junit.Assert;
import org.junit.Test;
import net.whg.we.network.netty.Client;
import net.whg.we.network.netty.Server;
import net.whg.we.utils.logging.Log;

public class EchoServerTest
{
	@Test
	public void echoServer() throws InterruptedException
	{
		Log.info("Testing echo server");

		final int port = 8123;

		Server server = new Server(port);
		Client client = new Client("localhost", port);

		server.start();
		Thread.sleep(100);

		client.start();
		Thread.sleep(2000);

		synchronized (client._list)
		{
			Assert.assertEquals(12, client._list.size());

			for (String s : client._list)
				Log.info(s);
		}

		server.stop();
		client.stop();
	}
}
