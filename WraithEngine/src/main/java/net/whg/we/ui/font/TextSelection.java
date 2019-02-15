package net.whg.we.ui.font;

public interface TextSelection
{
	int getSelectionStart();

	int getSelectionEnd();

	void setSelection(int start, int end);
}
