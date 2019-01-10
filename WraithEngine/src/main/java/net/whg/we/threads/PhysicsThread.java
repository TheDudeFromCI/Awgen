package net.whg.we.threads;

import net.whg.we.event.EventManager;
import net.whg.we.utils.Log;
import net.whg.we.utils.Time;
import whg.core.CorePlugin;

public class PhysicsThread extends ThreadInstance
{
	private CorePlugin _core;
	private PhysicsEventCaller _eventCaller;
	private boolean _running;

	PhysicsThread(CorePlugin core)
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

				while (_running)
				{
					try
					{
						handleMessages();

						long currentTime = System.currentTimeMillis();
						double passed = (currentTime - startTime) / 1000.0;
						double physicsFrames = passed * Time.getPhysicsFramerate();
						double inputFrames = passed * Time.getInputFramerate();

						if (usedInputFrames <= inputFrames)
						{
							_eventCaller.onInputUpdate();
							usedInputFrames++;
						}

						if (usedPhysicsFrames <= physicsFrames)
						{
							_eventCaller.onUpdate();
							_eventCaller.onLateUpdate();
							usedPhysicsFrames++;
						}

						if (usedInputFrames > inputFrames && usedPhysicsFrames > physicsFrames)
						{
							try
							{
								Thread.sleep(1);
							}
							catch (Exception exception)
							{
							}
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
		initalize(thread);
		thread.start();
	}

	void requestClose()
	{
		_running = false;
	}
}
