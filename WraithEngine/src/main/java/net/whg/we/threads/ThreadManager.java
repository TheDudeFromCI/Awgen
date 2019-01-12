package net.whg.we.threads;

import whg.core.CorePlugin;

public class ThreadManager
{
	public static ThreadManager INSTANCE;

	private GraphicsThread _graphicsThread;
	private PhysicsThread _physicsThread;

	public void buildThreads(CorePlugin _core)
	{
		if (INSTANCE != null)
			throw new IllegalStateException("ThreadManager Instance already exists!");

		_graphicsThread = new GraphicsThread(_core);
		_physicsThread = new PhysicsThread(_core);
		INSTANCE = this;

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
