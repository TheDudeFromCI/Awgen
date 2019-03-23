package net.whg.we.client_logic.ui.font;

public interface Cursor
{
	void setVisible(boolean visible);

	int getCaretX();

	int getCaretY();

	void setCaretPos(int x, int y);

	void setInsertMode(boolean insert);

	boolean getInsertMode();
}
