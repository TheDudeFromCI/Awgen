package net.whg.we.client_logic.ui.font;

public interface TextSelection
{
	int selStart();

	int selOrigin();

	int selEnd();

	boolean hasSelection();

	void clearSelection();

	void setSelection(int start, int origin, int end);
}
