package net.whg.we.client_logic.ui;

public interface UIContainer extends UIComponent
{
	void addComponent(UIComponent component);

	void removeComponent(UIComponent component);
}
