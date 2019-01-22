package event_handling;

import net.whg.we.event.EventCallerBase;
import net.whg.we.event.Listener;
import org.junit.Assert;
import org.junit.Test;

public class EventCallerBaseTest
{
	public class TestEventCaller extends EventCallerBase<TestListener>
	{
		private static final int ADD_LISTENER_EVENT = 0;
		private static final int REMOVE_LISTENER_EVENT = 1;
		private static final int DISPOSE_EVENT = 2;

		public void addListenerEvent()
		{
			callEvent(ADD_LISTENER_EVENT);
		}

		public void removeListenerEvent()
		{
			callEvent(REMOVE_LISTENER_EVENT);
		}

		public void disposeEvent()
		{
			callEvent(DISPOSE_EVENT);
		}

		@Override
		protected void runEvent(TestListener listener, int index, Object[] args)
		{
			switch(index)
			{
				case ADD_LISTENER_EVENT:
					listener.addAnotherListener(this);
					break;

				case REMOVE_LISTENER_EVENT:
					listener.removeThisListener(this);
					break;

				case DISPOSE_EVENT:
					listener.disposeEvent(this);
					break;
			}
		}
	}

	public class TestListener implements Listener
	{
		private boolean _wasCalled;
		private int _priority;

		public TestListener()
		{
			_priority = 0;
		}

		public TestListener(int priority)
		{
			_priority = priority;
		}

		@Override
		public int getPriority()
		{
			return _priority;
		}

		public void addAnotherListener(TestEventCaller caller)
		{
			_wasCalled = true;
			caller.addListener(new TestListener());
		}

		public void removeThisListener(TestEventCaller caller)
		{
			_wasCalled = true;
			caller.removeListener(this);
		}

		public void disposeEvent(TestEventCaller caller)
		{
			_wasCalled = true;
			caller.dispose();
		}

		public boolean wasCalled()
		{
			return _wasCalled;
		}
	}

	@Test
	public void addingAndRemovingListeners()
	{
		TestEventCaller caller = new TestEventCaller();
		TestListener listener = new TestListener();

		Assert.assertEquals(caller.getListenerCount(), 0);

		caller.addListener(listener);

		Assert.assertEquals(caller.getListenerCount(), 1);

		caller.removeListener(listener);

		Assert.assertEquals(caller.getListenerCount(), 0);
	}

	@Test
	public void addEventWhileRunning()
	{
		TestEventCaller caller = new TestEventCaller();
		TestListener listener = new TestListener();

		caller.addListener(listener);
		caller.addListenerEvent();

		Assert.assertEquals(caller.getListenerCount(), 2);
	}

	@Test
	public void removeEventWhileRunning()
	{
		TestEventCaller caller = new TestEventCaller();
		TestListener listener = new TestListener();

		caller.addListener(listener);
		caller.removeListenerEvent();

		Assert.assertEquals(caller.getListenerCount(), 0);
	}

	@Test
	public void disposeEventCaller()
	{
		TestEventCaller caller = new TestEventCaller();
		TestListener listener = new TestListener();

		caller.addListener(listener);
		caller.dispose();

		Assert.assertEquals(caller.getListenerCount(), 0);
	}

	@Test
	public void disposeWhileInEvent()
	{
		TestEventCaller caller = new TestEventCaller();
		TestListener listener1 = new TestListener();
		TestListener listener2 = new TestListener();

		caller.addListener(listener1);
		caller.addListener(listener2);
		caller.disposeEvent();

		Assert.assertEquals(caller.getListenerCount(), 0);
		Assert.assertTrue(listener1.wasCalled());
		Assert.assertTrue(listener2.wasCalled());
	}

	@Test
	public void isSortedByPriority()
	{
		TestEventCaller caller = new TestEventCaller();
		TestListener listener0 = new TestListener(0);
		TestListener listener1 = new TestListener(1);
		TestListener listener2 = new TestListener(2);

		caller.addListener(listener2);
		caller.addListener(listener0);
		caller.addListener(listener1);

		Assert.assertNotEquals(listener0, listener1);

		Assert.assertEquals(caller.getListener(0), listener0);
		Assert.assertEquals(caller.getListener(1), listener1);
		Assert.assertEquals(caller.getListener(2), listener2);
	}
}
