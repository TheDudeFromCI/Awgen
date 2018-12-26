package whg.core;

public class PhysicsThread
{
	void start()
	{
		Thread thread = new Thread(() ->
		{
			// TODO
		});

		thread.setName("physics");
		thread.setDaemon(false);
		thread.start();
	}
}
