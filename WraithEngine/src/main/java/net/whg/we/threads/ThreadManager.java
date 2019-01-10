package net.whg.we.threads;

import whg.core.CorePlugin;

public class ThreadManager
{
	private GraphicsThread _graphicsThread;
	private PhysicsThread _physicsThread;

	public void buildThreads(CorePlugin _core)
	{
		_graphicsThread = new GraphicsThread(_core);
		_physicsThread = new PhysicsThread(_core);

		_graphicsThread.build();
		_physicsThread.build();
	}

	public void startThreads()
	{
		_graphicsThread.start();
		_physicsThread.start();
	}

	public GraphicsThread getGraphicsThread()
	{
		return _graphicsThread;
	}

	public PhysicsThread getPhysicsThread()
	{
		return _physicsThread;
	}
}
