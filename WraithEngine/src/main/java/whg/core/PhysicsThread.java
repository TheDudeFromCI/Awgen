package whg.core;

import net.whg.we.event.EventManager;
import net.whg.we.utils.Log;
import net.whg.we.utils.Time;

public class PhysicsThread
{
	private CorePlugin _core;
	private PhysicsEventCaller _eventCaller;

	public PhysicsThread(CorePlugin core)
	{
		_core = core;
	}

	void build()
	{
		_eventCaller = new PhysicsEventCaller(_core);
		EventManager.registerEventCaller(_eventCaller);
	}

	void start()
	{
		Thread thread = new Thread(() ->
		{
			try
			{
				_eventCaller.onPhysicsInit();

				long startTime = System.currentTimeMillis();

				int usedPhysicsFrames = 0;
				int usedInputFrames = 0;

				while (true)
				{
					try
					{
						long currentTime = System.currentTimeMillis();
						double passed = (currentTime - startTime) / 1000.0;
						double physicsFrames = passed * Time.getPhysicsFramerate();
						double inputFrames = passed * Time.getInputFramerate();

						if (usedInputFrames < inputFrames)
						{
							_eventCaller.onInputUpdate();
							usedInputFrames++;
						}

						if (usedPhysicsFrames < physicsFrames)
						{
							_eventCaller.onUpdate();
							_eventCaller.onLateUpdate();
							usedPhysicsFrames++;
						}
					}
					catch (Exception exception)
					{
						Log.fatalf("Fatal error thrown in physics thread!", exception);
						break;
					}
				}

				_eventCaller.onPhysicsDispose();
			}
			catch (Exception exception)
			{
				Log.fatalf("Uncaught exception through in physics thread!", exception);
				System.exit(1);
			}
		});

		thread.setName("physics");
		thread.setDaemon(false);
		thread.start();
	}
}
