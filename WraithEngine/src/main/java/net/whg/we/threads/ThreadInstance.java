package net.whg.we.threads;

import java.util.LinkedList;
import net.whg.we.utils.Log;

public class ThreadInstance
{
	private long _threadId = -1;
	private String _threadName;
	private LinkedList<ThreadMessage> _messages = new LinkedList<>();

	void initalize(Thread thread)
	{
		_threadId = thread.getId();
		_threadName = thread.getName();
	}

	public String getThreadName()
	{
		return _threadName;
	}

	public long getThreadId()
	{
		return _threadId;
	}

	public void sendMessage(ThreadMessage message)
	{
		Log.tracef("Sent a %s message to the %s thread.", message.getClass().getName(),
				getThreadName());

		synchronized (_messages)
		{
			_messages.addLast(message);
		}
	}

	public boolean isCurrentThread()
	{
		return Thread.currentThread().getId() == _threadId;
	}

	void handleMessages()
	{
		if (!isCurrentThread())
		{
			Log.warnf("Tried to handle thread messages from the wrong thread! Current Thread: %s",
					Thread.currentThread().getName());
			return;
		}

		synchronized (_messages)
		{
			for (ThreadMessage mes : _messages)
				mes.run();
			_messages.clear();
		}
	}
}
