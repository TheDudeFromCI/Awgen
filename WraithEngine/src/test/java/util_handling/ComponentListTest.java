package util_handling;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import net.whg.we.utils.ComponentList;

public class ComponentListTest
{
	@Test
	public void addAndRemove()
	{
		ComponentList<Object> list = new ComponentList<>();
		Object obj = new Object();

		list.add(obj);
		Assert.assertEquals(0, list.size());
		list.endFrame();
		Assert.assertEquals(1, list.size());

		list.remove(obj);
		Assert.assertEquals(1, list.size());
		list.endFrame();
		Assert.assertEquals(0, list.size());
	}

	@Test
	public void cantAddTwice()
	{
		ComponentList<Object> list = new ComponentList<>();
		Object obj = new Object();

		list.add(obj);
		list.add(obj);

		list.endFrame();
		Assert.assertEquals(1, list.size());
	}

	@Test
	public void cantAddTwice_NotPending()
	{
		ComponentList<Object> list = new ComponentList<>();
		Object obj = new Object();

		list.add(obj);
		list.endFrame();

		list.add(obj);
		list.endFrame();

		Assert.assertEquals(1, list.size());
	}

	@Test
	public void removeFromPending()
	{
		ComponentList<Object> list = new ComponentList<>();
		Object obj = new Object();

		list.add(obj);
		list.remove(obj);
		list.endFrame();
		Assert.assertEquals(0, list.size());
	}

	@Test
	public void contains()
	{
		ComponentList<Object> list = new ComponentList<>();
		Object obj = new Object();

		Assert.assertFalse(list.contains(obj));
		Assert.assertFalse(list.containPending(obj));

		list.add(obj);

		Assert.assertFalse(list.contains(obj));
		Assert.assertTrue(list.containPending(obj));

		list.endFrame();

		Assert.assertTrue(list.contains(obj));
		Assert.assertTrue(list.containPending(obj));
	}

	@Test
	public void contains_RemoveFromPending()
	{
		ComponentList<Object> list = new ComponentList<>();
		Object obj = new Object();

		list.add(obj);
		list.remove(obj);

		Assert.assertFalse(list.contains(obj));
		Assert.assertFalse(list.containPending(obj));

		list.endFrame();

		Assert.assertFalse(list.contains(obj));
		Assert.assertFalse(list.containPending(obj));
	}

	@Test
	public void contains_Real_PendingRemoval()
	{
		ComponentList<Object> list = new ComponentList<>();
		Object obj = new Object();

		list.add(obj);
		list.endFrame();

		list.remove(obj);

		Assert.assertTrue(list.contains(obj));
		Assert.assertFalse(list.containPending(obj));
	}

	@Test
	public void clear_full()
	{
		ComponentList<Object> list = new ComponentList<>();
		Object a = new Object();
		Object b = new Object();
		Object c = new Object();

		list.add(a);
		list.add(c);
		list.endFrame();

		list.add(b);

		list.remove(c);

		Assert.assertTrue(list.containPending(a));
		Assert.assertTrue(list.containPending(b));
		Assert.assertFalse(list.containPending(c));

		list.clearPending();

		Assert.assertFalse(list.containPending(a));
		Assert.assertFalse(list.containPending(b));
		Assert.assertFalse(list.containPending(c));
	}

	@Test
	public void clear_current()
	{
		ComponentList<Object> list = new ComponentList<>();
		Object a = new Object();
		Object b = new Object();

		list.add(a);
		list.endFrame();
		list.add(b);

		list.clear();

		Assert.assertFalse(list.containPending(a));
		Assert.assertTrue(list.containPending(b));
	}

	@Test
	public void addRemoveInstant()
	{
		ComponentList<Object> list = new ComponentList<>();
		Object obj = new Object();

		list.addInstant(obj);

		Assert.assertEquals(1, list.size());

		list.removeInstant(obj);

		Assert.assertEquals(0, list.size());
	}

	@Test
	public void addInstantRemovesPending()
	{
		ComponentList<Object> list = new ComponentList<>();
		Object obj = new Object();

		list.add(obj);
		list.addInstant(obj);

		list.endFrame();

		Assert.assertEquals(1, list.size());
	}

	@Test
	public void addInstantPreventsPending()
	{
		ComponentList<Object> list = new ComponentList<>();
		Object obj = new Object();

		list.addInstant(obj);
		list.add(obj);

		list.endFrame();

		Assert.assertEquals(1, list.size());
	}

	@Test
	public void removeInstantRemovesPending()
	{
		ComponentList<Object> list = new ComponentList<>();
		Object obj = new Object();
		list.addInstant(obj);

		list.remove(obj);
		list.removeInstant(obj);

		list.endFrame();

		Assert.assertEquals(0, list.size());
	}

	@Test
	public void preformComponentAction()
	{
		ComponentList<Object> list = new ComponentList<>();
		Runnable action = Mockito.mock(Runnable.class);

		list.preformComponentAction(action);
		list.endFrame();

		Mockito.verify(action).run();
	}

	@Test
	public void actionNotPreformedUntilEndFrame()
	{
		ComponentList<Object> list = new ComponentList<>();
		Runnable action = Mockito.mock(Runnable.class);

		list.preformComponentAction(action);

		Mockito.verify(action, Mockito.never()).run();
	}

	@Test
	public void actionsPreformededInOrder()
	{
		ComponentList<Object> list = new ComponentList<>();
		Runnable action1 = Mockito.mock(Runnable.class);
		Runnable action2 = Mockito.mock(Runnable.class);
		Runnable action3 = Mockito.mock(Runnable.class);

		InOrder order = Mockito.inOrder(action1, action2, action3);

		list.preformComponentAction(action1);
		list.preformComponentAction(action2);
		list.preformComponentAction(action3);
		list.endFrame();

		order.verify(action1).run();
		order.verify(action2).run();
		order.verify(action3).run();
	}
}
