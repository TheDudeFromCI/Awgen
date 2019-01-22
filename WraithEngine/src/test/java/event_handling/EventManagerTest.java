package event_handling;

import net.whg.we.event.EventCallerBase;
import net.whg.we.event.EventManager;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

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

	}

	@Test
	public void registerEventCallerTwice()
	{

	}

	@Test
	public void getEventCallerByPluginAndName()
	{

	}

	@Test
	public void getMultipleEventCallersByName()
	{

	}

	@Test
	public void disallowRegisterEventNoPlugin()
	{

	}
}
