package event_handling;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import net.whg.frameworks.event.EventCallerBase;
import net.whg.frameworks.event.EventManager;

public class EventManagerTest
{
	@Test
	@SuppressWarnings("rawtypes")
	public void registerEventCaller()
	{
		EventManager manager = new EventManager();
		EventCallerBase caller = Mockito.mock(EventCallerBase.class);
		Mockito.when(caller.getName()).thenAnswer(i -> "Test Event Caller");

		manager.registerEventCaller(caller);

		Assert.assertNotNull(manager.getEventCaller("Test Event Caller"));
	}

	@Test
	@SuppressWarnings("rawtypes")
	public void unregisterEventCaller()
	{
		EventManager manager = new EventManager();
		EventCallerBase caller = Mockito.mock(EventCallerBase.class);
		Mockito.when(caller.getName()).thenAnswer(i -> "Test Event Caller");

		manager.registerEventCaller(caller);
		manager.unregisterEventCaller(caller);

		Assert.assertNull(manager.getEventCaller("Test Event Caller"));
	}

	@Test
	public void registerEventCallerWithSameNameAndPlugin()
	{
		// TODO
	}

	@Test
	public void registerEventCallerTwice()
	{
		// TODO
	}

	@Test
	public void getEventCallerByPluginAndName()
	{
		// TODO
	}

	@Test
	public void getMultipleEventCallersByName()
	{
		// TODO
	}

	@Test
	public void disallowRegisterEventNoPlugin()
	{
		// TODO
	}
}
