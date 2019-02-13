package net.whg.we.ui;

public interface UIContainer extends UIComponent
{
	void addComponent(UIComponent component);

	void removeComponent(UIComponent component);
}
