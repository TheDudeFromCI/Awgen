package event_handling;

import net.whg.we.event.EventCallerBase;
import net.whg.we.event.Listener;
import net.whg.we.main.Plugin;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class EventCallerBaseTest
{
	public class TestEventCaller extends EventCallerBase<TestListener>
	{
		private static final int ADD_LISTENER_EVENT = 0;
		private static final int REMOVE_LISTENER_EVENT = 1;
		private static final int DISPOSE_EVENT = 2;
		private static final int INPUT_PARAM_EVENT = 3;
		private static final int THROW_ERROR_EVENT = 4;
		private static final int ADD_LISTENER_TWICE_EVENT = 5;

		private Plugin _plugin;

		public void setPlugin(Plugin plugin)
		{
			_plugin = plugin;
		}

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

		public void inputParamEvent(int param)
		{
			callEvent(INPUT_PARAM_EVENT, param);
		}

		public void throwErrorEvent()
		{
			callEvent(THROW_ERROR_EVENT);
		}

		public void addListenerTwiceEvent()
		{
			callEvent(ADD_LISTENER_TWICE_EVENT);
		}

		@Override
		protected void runEvent(TestListener listener, int index, Object arg)
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

				case INPUT_PARAM_EVENT:
					listener.inputParamEvent((int)arg);
					break;

				case THROW_ERROR_EVENT:
					listener.throwErrorEvent();
					break;

				case ADD_LISTENER_TWICE_EVENT:
					listener.addListenerTwiceEvent(this);
					break;
			}
		}

		@Override
		public Plugin getPlugin()
		{
			return _plugin;
		}
	}

	public class TestListener implements Listener
	{
		private boolean _wasCalled;
		private int _priority;
		private int _inputParam;

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

		public void inputParamEvent(int param)
		{
			_wasCalled = true;
			_inputParam = param;
		}

		public int getInputParam()
		{
			return _inputParam;
		}

		public void throwErrorEvent()
		{
			_wasCalled = true;
			throw new RuntimeException();
		}

		public void addListenerTwiceEvent(TestEventCaller caller)
		{
			_wasCalled = true;

			TestListener listener = new TestListener();
			caller.addListener(listener);
			caller.addListener(listener);
		}
	}

	@Test
	public void addingAndRemovingListenersWithoutPlugin()
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
	public void addingAndRemovingListenersWithPlugin()
	{
		TestEventCaller caller = new TestEventCaller();
		TestListener listener = new TestListener();
		Plugin plugin = Mockito.mock(Plugin.class);

		caller.setPlugin(plugin);

		Assert.assertEquals(caller.getListenerCount(), 0);

		caller.addListener(listener);
		Mockito.verify(plugin, Mockito.atLeastOnce()).getPluginName();
		Mockito.reset(plugin);

		Assert.assertEquals(caller.getListenerCount(), 1);

		caller.removeListener(listener);
		Mockito.verify(plugin, Mockito.atLeastOnce()).getPluginName();

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
	public void disposeEventCallerNoPlugin()
	{
		TestEventCaller caller = new TestEventCaller();
		TestListener listener = new TestListener();

		caller.addListener(listener);
		caller.dispose();

		Assert.assertEquals(caller.getListenerCount(), 0);
	}

	@Test
	public void disposeEventCallerWithPlugin()
	{
		TestEventCaller caller = new TestEventCaller();
		TestListener listener = new TestListener();
		Plugin plugin = Mockito.mock(Plugin.class);

		caller.setPlugin(plugin);
		caller.addListener(listener);
		caller.dispose();

		Mockito.verify(plugin, Mockito.atLeastOnce()).getPluginName();

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

	@Test
	public void getListenerIndex()
	{
		TestEventCaller caller = new TestEventCaller();
		TestListener listener = new TestListener();

		Assert.assertEquals(caller.getListenerIndex(listener), -1);

		caller.addListener(listener);

		Assert.assertEquals(caller.getListenerIndex(listener), 0);
	}

	@Test
	public void listenerParameter()
	{
		TestEventCaller caller = new TestEventCaller();
		TestListener listener = new TestListener();
		int param = 234;

		caller.addListener(listener);
		caller.inputParamEvent(param);

		Assert.assertEquals(listener.getInputParam(), param);
	}

	@Test
	public void listenerThrowsError()
	{
		TestEventCaller caller = new TestEventCaller();
		TestListener listener0 = new TestListener();
		TestListener listener1 = new TestListener();

		caller.addListener(listener0);
		caller.addListener(listener1);
		caller.throwErrorEvent();

		Assert.assertTrue(listener0.wasCalled());
		Assert.assertTrue(listener1.wasCalled());
	}

	@Test
	public void addingAListenerThatIsAlreadyAdded()
	{
		TestEventCaller caller = new TestEventCaller();
		TestListener listener = new TestListener();

		caller.addListener(listener);
		caller.addListener(listener);

		Assert.assertEquals(caller.getListenerCount(), 1);
	}

	@Test
	public void addingPendingListenerThatIsAlreadyAdded()
	{
		TestEventCaller caller = new TestEventCaller();
		TestListener listener = new TestListener();

		caller.addListener(listener);
		caller.addListenerTwiceEvent();

		Assert.assertEquals(caller.getListenerCount(), 2);
	}

	@Test
	public void getDefaultPluginReturnsNull()
	{
		EventCallerBase<Listener> eventCaller = new EventCallerBase<Listener>()
		{
			@Override
			protected void runEvent(Listener lister, int index, Object arg)
			{
			}
		};

		Assert.assertNull(eventCaller.getPlugin());
	}
}
