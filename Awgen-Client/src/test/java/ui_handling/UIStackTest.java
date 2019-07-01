package ui_handling;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import net.whg.we.client_logic.ui.UIComponent;
import net.whg.we.client_logic.ui.UIStack;
import net.whg.we.client_logic.ui.UIStack.ComponentAction;

public class UIStackTest
{
	@Test
	public void componentIsDisposed()
	{
		UIStack stack = new UIStack();
		UIComponent comp = Mockito.mock(UIComponent.class);

		stack.addComponent(comp);
		stack.removeComponent(comp);

		Mockito.verify(comp).dispose();
	}

	@Test
	public void componentUpdate()
	{
		UIStack stack = new UIStack();
		UIComponent comp = Mockito.mock(UIComponent.class);

		stack.addComponent(comp);
		stack.update();

		Mockito.verify(comp).update();
	}

	@Test
	public void componentUpdateFrame()
	{
		UIStack stack = new UIStack();
		UIComponent comp = Mockito.mock(UIComponent.class);

		stack.addComponent(comp);
		stack.updateFrame();

		Mockito.verify(comp).updateFrame();
	}

	@Test
	public void componentRender()
	{
		UIStack stack = new UIStack();
		UIComponent comp = Mockito.mock(UIComponent.class);

		stack.addComponent(comp);
		stack.render();

		Mockito.verify(comp).render();
	}

	@Test
	public void componentAutoRemove_Update()
	{
		UIStack stack = new UIStack();
		UIComponent comp = Mockito.mock(UIComponent.class);
		Mockito.when(comp.isDisposed()).thenReturn(true);

		stack.addComponent(comp);
		comp.dispose();

		stack.update();

		Mockito.verify(comp).dispose();
		Mockito.verify(comp, Mockito.never()).update();
	}

	@Test
	public void componentAutoRemove_UpdateFrame()
	{
		UIStack stack = new UIStack();
		UIComponent comp = Mockito.mock(UIComponent.class);
		Mockito.when(comp.isDisposed()).thenReturn(true);

		stack.addComponent(comp);
		comp.dispose();

		stack.updateFrame();

		Mockito.verify(comp).dispose();
		Mockito.verify(comp, Mockito.never()).updateFrame();
	}

	@Test
	public void componentAutoRemove_Render()
	{
		UIStack stack = new UIStack();
		UIComponent comp = Mockito.mock(UIComponent.class);
		Mockito.when(comp.isDisposed()).thenReturn(true);

		stack.addComponent(comp);
		comp.dispose();

		stack.render();

		Mockito.verify(comp).dispose();
		Mockito.verify(comp, Mockito.never()).render();
	}

	@Test
	public void disposeAll()
	{
		UIStack stack = new UIStack();
		UIComponent comp1 = Mockito.mock(UIComponent.class);
		UIComponent comp2 = Mockito.mock(UIComponent.class);

		stack.addComponent(comp1);
		stack.addComponent(comp2);

		stack.dispose();

		Mockito.verify(comp1).dispose();
		Mockito.verify(comp2).dispose();

		stack.render();

		Mockito.verify(comp1, Mockito.never()).render();
		Mockito.verify(comp2, Mockito.never()).render();
	}

	@Test
	public void disposeOnlyOnce()
	{
		UIStack stack = new UIStack();
		UIComponent comp = Mockito.mock(UIComponent.class);
		Mockito.when(comp.isDisposed()).thenReturn(true);

		stack.addComponent(comp);
		comp.dispose();

		stack.dispose();

		Mockito.verify(comp, Mockito.times(1)).dispose();
	}

	@Test
	public void moveForward()
	{
		UIStack stack = new UIStack();
		UIComponent comp1 = Mockito.mock(UIComponent.class);
		UIComponent comp2 = Mockito.mock(UIComponent.class);
		UIComponent comp3 = Mockito.mock(UIComponent.class);

		stack.addComponent(comp1);
		stack.addComponent(comp2);
		stack.addComponent(comp3);

		InOrder order = Mockito.inOrder(comp1, comp2, comp3);

		stack.moveForward(comp1);
		stack.update();

		order.verify(comp2).update();
		order.verify(comp1).update();
		order.verify(comp3).update();
	}

	@Test
	public void bringForward_AlreadyFront()
	{
		UIStack stack = new UIStack();
		UIComponent comp1 = Mockito.mock(UIComponent.class);
		UIComponent comp2 = Mockito.mock(UIComponent.class);
		UIComponent comp3 = Mockito.mock(UIComponent.class);

		stack.addComponent(comp1);
		stack.addComponent(comp2);
		stack.addComponent(comp3);

		InOrder order = Mockito.inOrder(comp1, comp2, comp3);

		stack.moveForward(comp3);
		stack.update();

		order.verify(comp1).update();
		order.verify(comp2).update();
		order.verify(comp3).update();
	}

	@Test
	public void moveBackward_AlreadyBack()
	{
		UIStack stack = new UIStack();
		UIComponent comp1 = Mockito.mock(UIComponent.class);
		UIComponent comp2 = Mockito.mock(UIComponent.class);
		UIComponent comp3 = Mockito.mock(UIComponent.class);

		stack.addComponent(comp1);
		stack.addComponent(comp2);
		stack.addComponent(comp3);

		InOrder order = Mockito.inOrder(comp1, comp2, comp3);

		stack.moveBackward(comp1);
		stack.update();

		order.verify(comp1).update();
		order.verify(comp2).update();
		order.verify(comp3).update();
	}

	@Test
	public void sendBackward()
	{
		UIStack stack = new UIStack();
		UIComponent comp1 = Mockito.mock(UIComponent.class);
		UIComponent comp2 = Mockito.mock(UIComponent.class);
		UIComponent comp3 = Mockito.mock(UIComponent.class);

		stack.addComponent(comp1);
		stack.addComponent(comp2);
		stack.addComponent(comp3);

		InOrder order = Mockito.inOrder(comp1, comp2, comp3);

		stack.moveBackward(comp3);
		stack.update();

		order.verify(comp1).update();
		order.verify(comp3).update();
		order.verify(comp2).update();
	}

	@Test
	public void sendToFront()
	{
		UIStack stack = new UIStack();
		UIComponent comp1 = Mockito.mock(UIComponent.class);
		UIComponent comp2 = Mockito.mock(UIComponent.class);
		UIComponent comp3 = Mockito.mock(UIComponent.class);

		stack.addComponent(comp1);
		stack.addComponent(comp2);
		stack.addComponent(comp3);

		InOrder order = Mockito.inOrder(comp1, comp2, comp3);

		stack.sendToFront(comp1);
		stack.update();

		order.verify(comp2).update();
		order.verify(comp3).update();
		order.verify(comp1).update();
	}

	@Test
	public void sendToBack()
	{
		UIStack stack = new UIStack();
		UIComponent comp1 = Mockito.mock(UIComponent.class);
		UIComponent comp2 = Mockito.mock(UIComponent.class);
		UIComponent comp3 = Mockito.mock(UIComponent.class);

		stack.addComponent(comp1);
		stack.addComponent(comp2);
		stack.addComponent(comp3);

		InOrder order = Mockito.inOrder(comp1, comp2, comp3);

		stack.sendToBack(comp3);
		stack.update();

		order.verify(comp3).update();
		order.verify(comp1).update();
		order.verify(comp2).update();
	}

	@Test
	public void addNullComponent()
	{
		UIStack stack = new UIStack();
		stack.addComponent(null);

		Assert.assertEquals(0, stack.size());
	}

	@Test
	public void removeNullComponent()
	{
		UIStack stack = new UIStack();
		stack.removeComponent(null);

		Assert.assertEquals(0, stack.size());
	}

	@Test
	public void addComponetWhileRunning()
	{
		UIStack stack = new UIStack();
		UIComponent comp1 = Mockito.mock(UIComponent.class);
		UIComponent comp2 = Mockito.mock(UIComponent.class);
		Mockito.doAnswer(arg0 ->
		{
			stack.addComponent(comp2);
			return null;
		}).when(comp1).update();

		stack.addComponent(comp1);
		stack.update();

		Assert.assertEquals(2, stack.size());
	}

	@Test
	public void removeComponetWhileRunning()
	{
		UIStack stack = new UIStack();
		UIComponent comp = Mockito.mock(UIComponent.class);
		Mockito.doAnswer(arg0 ->
		{
			stack.removeComponent(comp);
			return null;
		}).when(comp).update();

		stack.addComponent(comp);
		stack.update();

		Assert.assertEquals(0, stack.size());
	}

	@Test
	public void sendToFront_WhileRunning()
	{
		UIStack stack = new UIStack();

		UIComponent comp1 = Mockito.mock(UIComponent.class);
		UIComponent comp2 = Mockito.mock(UIComponent.class);
		UIComponent comp3 = Mockito.mock(UIComponent.class);

		Mockito.doAnswer(arg0 ->
		{
			stack.sendToFront(comp1);
			return null;
		}).when(comp1).update();

		stack.addComponent(comp1);
		stack.addComponent(comp2);
		stack.addComponent(comp3);
		stack.update();

		InOrder other = Mockito.inOrder(comp1, comp2, comp3);

		stack.update();

		other.verify(comp2).update();
		other.verify(comp3).update();
		other.verify(comp1).update();
	}

	@Test
	public void sendToBack_WhileRunning()
	{
		UIStack stack = new UIStack();

		UIComponent comp1 = Mockito.mock(UIComponent.class);
		UIComponent comp2 = Mockito.mock(UIComponent.class);
		UIComponent comp3 = Mockito.mock(UIComponent.class);

		Mockito.doAnswer(arg0 ->
		{
			stack.sendToBack(comp3);
			return null;
		}).when(comp1).update();

		stack.addComponent(comp1);
		stack.addComponent(comp2);
		stack.addComponent(comp3);
		stack.update();

		InOrder other = Mockito.inOrder(comp1, comp2, comp3);

		stack.update();

		other.verify(comp3).update();
		other.verify(comp1).update();
		other.verify(comp2).update();
	}

	@Test
	public void moveForward_WhileRunning()
	{
		UIStack stack = new UIStack();

		UIComponent comp1 = Mockito.mock(UIComponent.class);
		UIComponent comp2 = Mockito.mock(UIComponent.class);
		UIComponent comp3 = Mockito.mock(UIComponent.class);

		Mockito.doAnswer(arg0 ->
		{
			stack.moveForward(comp1);
			return null;
		}).when(comp1).update();

		stack.addComponent(comp1);
		stack.addComponent(comp2);
		stack.addComponent(comp3);
		stack.update();

		InOrder other = Mockito.inOrder(comp1, comp2, comp3);

		stack.update();

		other.verify(comp2).update();
		other.verify(comp1).update();
		other.verify(comp3).update();
	}

	@Test
	public void moveBackward_WhileRunning()
	{
		UIStack stack = new UIStack();

		UIComponent comp1 = Mockito.mock(UIComponent.class);
		UIComponent comp2 = Mockito.mock(UIComponent.class);
		UIComponent comp3 = Mockito.mock(UIComponent.class);

		Mockito.doAnswer(arg0 ->
		{
			stack.moveBackward(comp3);
			return null;
		}).when(comp1).update();

		stack.addComponent(comp1);
		stack.addComponent(comp2);
		stack.addComponent(comp3);
		stack.update();

		InOrder other = Mockito.inOrder(comp1, comp2, comp3);

		stack.update();

		other.verify(comp1).update();
		other.verify(comp3).update();
		other.verify(comp2).update();
	}

	@Test
	public void componentAction_nullComponent()
	{
		ComponentAction action = new UIStack().new ComponentAction();

		// -1 action should throw an error. If comp is null, it should return normally
		// instead.
		action.set(null, -1);
		action.run();
	}

	@Test
	public void componentAction_disposedComponent()
	{
		ComponentAction action = new UIStack().new ComponentAction();
		UIComponent comp = Mockito.mock(UIComponent.class);
		Mockito.when(comp.isDisposed()).thenReturn(true);

		// -1 action should throw an error. If comp is disposed, it should return
		// normally
		// instead.
		action.set(comp, -1);
		action.run();
	}

	@Test
	public void componentAction_componentNotInStack()
	{
		ComponentAction action = new UIStack().new ComponentAction();
		UIComponent comp = Mockito.mock(UIComponent.class);

		// -1 action should throw an error. If comp is not in stack, it should return
		// normally
		// instead.
		action.set(comp, -1);
		action.run();
	}

	@Test(expected = IllegalStateException.class)
	public void componentAction_unknownAction()
	{
		UIStack stack = new UIStack();
		ComponentAction action = stack.new ComponentAction();
		UIComponent comp = Mockito.mock(UIComponent.class);
		stack.addComponent(comp);

		action.set(comp, -1);
		action.run();
	}
}
