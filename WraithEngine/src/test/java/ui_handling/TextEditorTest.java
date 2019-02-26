package ui_handling;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.junit.Assert;
import org.junit.Test;
import net.whg.we.ui.BasicCursor;
import net.whg.we.ui.BasicTextHolder;
import net.whg.we.ui.BasicTextSelection;
import net.whg.we.ui.TextEditor;
import net.whg.we.ui.TextHolder;
import net.whg.we.ui.TypedKeyInput;
import net.whg.we.ui.font.Cursor;
import net.whg.we.ui.font.Font;
import net.whg.we.ui.font.Glyph;
import net.whg.we.ui.font.TextSelection;

public class TextEditorTest
{
	private Font defaultFont()
	{
		String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 ";
		Glyph[] glyphs = new Glyph[chars.length()];
		for (int i = 0; i < glyphs.length; i++)
			glyphs[i] =
					new Glyph(chars.charAt(i), new Vector4f(), new Vector2f(), new Vector2f(), 1f);
		return new Font(glyphs);
	}

	public TypedKeyInput keyInput(char key)
	{
		TypedKeyInput t = new TypedKeyInput();
		t.key = key;
		return t;
	}

	public TypedKeyInput keyInput(int extraKey)
	{
		TypedKeyInput t = new TypedKeyInput();
		t.extraKey = extraKey;
		return t;
	}

	public TypedKeyInput keyInput(char key, boolean shift, boolean control)
	{
		TypedKeyInput t = new TypedKeyInput();
		t.key = key;
		t.shift = shift;
		t.control = control;
		return t;
	}

	public TypedKeyInput keyInput(int extraKey, boolean shift, boolean control)
	{
		TypedKeyInput t = new TypedKeyInput();
		t.extraKey = extraKey;
		t.shift = shift;
		t.control = control;
		return t;
	}

	private TextEditor textEditor()
	{
		TextHolder holder = new BasicTextHolder(defaultFont());
		Cursor cursor = new BasicCursor();
		TextSelection selection = new BasicTextSelection();

		return new TextEditor(holder, cursor, selection);
	}

	@Test
	public void writeCharacter()
	{
		TextEditor editor = textEditor();

		editor.typeCharacter(keyInput('a'));

		Assert.assertEquals("a", editor.getTextHolder().getText());
		Assert.assertEquals(1, editor.getCursor().getCaretX());
		Assert.assertEquals(0, editor.getCursor().getCaretY());
	}

	@Test
	public void writeUnknownCharacter()
	{
		TextEditor editor = textEditor();

		editor.typeCharacter(keyInput('%'));

		Assert.assertEquals("", editor.getTextHolder().getText());
		Assert.assertEquals(0, editor.getCursor().getCaretX());
		Assert.assertEquals(0, editor.getCursor().getCaretY());
	}

	@Test
	public void writeCharacter_Insert()
	{
		TextEditor editor = textEditor();
		editor.typeCharacter(keyInput('a'));
		editor.typeCharacter(keyInput('b'));
		editor.typeCharacter(keyInput('c'));

		editor.getCursor().setInsertMode(true);
		editor.getCursor().setCaretPos(0, 0);
		editor.typeCharacter(keyInput('0'));

		Assert.assertEquals("0bc", editor.getTextHolder().getText());
	}

	@Test
	public void writeCharacter_Selection()
	{
		TextEditor editor = textEditor();
		editor.typeCharacter(keyInput('a'));
		editor.typeCharacter(keyInput('b'));
		editor.typeCharacter(keyInput('c'));

		editor.getSelection().setSelection(1, 1, 2);
		editor.getCursor().setCaretPos(0, 0);
		editor.typeCharacter(keyInput('0'));

		Assert.assertEquals("a0", editor.getTextHolder().getText());
	}

	@Test
	public void writeCharacter_Selection_Insert()
	{
		TextEditor editor = textEditor();
		editor.typeCharacter(keyInput('a'));
		editor.typeCharacter(keyInput('b'));
		editor.typeCharacter(keyInput('c'));

		editor.getSelection().setSelection(1, 1, 2);
		editor.getCursor().setInsertMode(true);
		editor.typeCharacter(keyInput('0'));

		Assert.assertEquals("a0", editor.getTextHolder().getText());
	}

	@Test
	public void backspace()
	{
		TextEditor editor = textEditor();
		editor.typeCharacter(keyInput('a'));
		editor.typeCharacter(keyInput('b'));
		editor.typeCharacter(keyInput('c'));

		editor.typeCharacter(keyInput(TypedKeyInput.BACKSPACE_KEY));

		Assert.assertEquals("ab", editor.getTextHolder().getText());
	}

	@Test
	public void backspace_CursorPos1()
	{
		TextEditor editor = textEditor();
		editor.typeCharacter(keyInput('a'));
		editor.typeCharacter(keyInput('b'));
		editor.typeCharacter(keyInput('c'));

		editor.getCursor().setCaretPos(1, 0);
		editor.typeCharacter(keyInput(TypedKeyInput.BACKSPACE_KEY));

		Assert.assertEquals("bc", editor.getTextHolder().getText());
	}

	@Test
	public void backspace_Selection()
	{
		TextEditor editor = textEditor();
		editor.typeCharacter(keyInput('a'));
		editor.typeCharacter(keyInput('b'));
		editor.typeCharacter(keyInput('c'));
		editor.typeCharacter(keyInput('d'));
		editor.typeCharacter(keyInput('e'));
		editor.typeCharacter(keyInput('f'));

		editor.getSelection().setSelection(2, 2, 4);
		editor.typeCharacter(keyInput(TypedKeyInput.BACKSPACE_KEY));
		editor.typeCharacter(keyInput('0'));

		Assert.assertEquals("ab0f", editor.getTextHolder().getText());
	}

	@Test
	public void backspace_NewLine()
	{
		TextEditor editor = textEditor();
		editor.typeCharacter(keyInput('a'));
		editor.typeCharacter(keyInput('b'));
		editor.typeCharacter(keyInput('c'));
		editor.typeCharacter(keyInput(TypedKeyInput.ENTER_KEY));

		editor.typeCharacter(keyInput(TypedKeyInput.BACKSPACE_KEY));

		Assert.assertEquals("abc", editor.getTextHolder().getText());
	}

	@Test
	public void delete()
	{
		TextEditor editor = textEditor();
		editor.typeCharacter(keyInput('a'));
		editor.typeCharacter(keyInput('b'));
		editor.typeCharacter(keyInput('c'));

		editor.getCursor().setCaretPos(1, 0);
		editor.typeCharacter(keyInput(TypedKeyInput.DELETE_KEY));

		Assert.assertEquals("ac", editor.getTextHolder().getText());
	}

	@Test
	public void delete_NewLine()
	{
		TextEditor editor = textEditor();
		editor.typeCharacter(keyInput('a'));
		editor.typeCharacter(keyInput('b'));
		editor.typeCharacter(keyInput('c'));
		editor.typeCharacter(keyInput(TypedKeyInput.ENTER_KEY));
		editor.typeCharacter(keyInput('d'));
		editor.typeCharacter(keyInput('e'));
		editor.typeCharacter(keyInput('f'));

		editor.getCursor().setCaretPos(3, 0);
		editor.typeCharacter(keyInput(TypedKeyInput.DELETE_KEY));

		Assert.assertEquals("abcdef", editor.getTextHolder().getText());
	}

	@Test
	public void delete_EmptyString()
	{
		TextEditor editor = textEditor();

		editor.typeCharacter(keyInput(TypedKeyInput.DELETE_KEY));

		Assert.assertEquals("", editor.getTextHolder().getText());
	}

	@Test
	public void delete_LastLine()
	{
		TextEditor editor = textEditor();
		editor.typeCharacter(keyInput('a'));
		editor.typeCharacter(keyInput('b'));
		editor.typeCharacter(keyInput('c'));
		editor.typeCharacter(keyInput(TypedKeyInput.ENTER_KEY));

		editor.typeCharacter(keyInput(TypedKeyInput.DELETE_KEY));

		Assert.assertEquals("abc\n", editor.getTextHolder().getText());
	}

	@Test
	public void delete_Selection()
	{
		TextEditor editor = textEditor();
		editor.typeCharacter(keyInput('a'));
		editor.typeCharacter(keyInput('b'));
		editor.typeCharacter(keyInput('c'));

		editor.getSelection().setSelection(1, 1, 2);
		editor.typeCharacter(keyInput(TypedKeyInput.DELETE_KEY));

		Assert.assertEquals("a", editor.getTextHolder().getText());
	}

	@Test
	public void leftKey()
	{
		TextEditor editor = textEditor();
		editor.typeCharacter(keyInput('a'));
		editor.typeCharacter(keyInput('b'));
		editor.typeCharacter(keyInput('c'));

		editor.typeCharacter(keyInput(TypedKeyInput.LEFT_KEY));

		Assert.assertEquals(2, editor.getCursor().getCaretX());
	}

	@Test
	public void leftKey_Shift()
	{
		TextEditor editor = textEditor();
		editor.typeCharacter(keyInput('a'));
		editor.typeCharacter(keyInput('b'));
		editor.typeCharacter(keyInput('c'));

		editor.typeCharacter(keyInput(TypedKeyInput.LEFT_KEY, true, false));

		Assert.assertEquals(2, editor.getSelection().selStart());
		Assert.assertEquals(3, editor.getSelection().selEnd());
	}

	@Test
	public void leftKey_NoShift()
	{
		TextEditor editor = textEditor();
		editor.typeCharacter(keyInput('a'));
		editor.typeCharacter(keyInput('b'));
		editor.typeCharacter(keyInput('c'));

		editor.getSelection().setSelection(1, 1, 2);
		editor.typeCharacter(keyInput(TypedKeyInput.LEFT_KEY));

		Assert.assertFalse(editor.getSelection().hasSelection());
	}

	@Test
	public void leftKey_LineWrap()
	{
		TextEditor editor = textEditor();
		editor.typeCharacter(keyInput('a'));
		editor.typeCharacter(keyInput('b'));
		editor.typeCharacter(keyInput('c'));
		editor.typeCharacter(keyInput(TypedKeyInput.ENTER_KEY));

		Assert.assertEquals(0, editor.getCursor().getCaretX());
		Assert.assertEquals(1, editor.getCursor().getCaretY());

		editor.typeCharacter(keyInput(TypedKeyInput.LEFT_KEY));

		Assert.assertEquals(3, editor.getCursor().getCaretX());
		Assert.assertEquals(0, editor.getCursor().getCaretY());
	}

	@Test
	public void rightKey()
	{
		TextEditor editor = textEditor();
		editor.typeCharacter(keyInput('a'));
		editor.typeCharacter(keyInput('b'));
		editor.typeCharacter(keyInput('c'));

		editor.getCursor().setCaretPos(0, 0);
		editor.typeCharacter(keyInput(TypedKeyInput.RIGHT_KEY));

		Assert.assertEquals(1, editor.getCursor().getCaretX());
	}

	@Test
	public void rightKey_Shift()
	{
		TextEditor editor = textEditor();
		editor.typeCharacter(keyInput('a'));
		editor.typeCharacter(keyInput('b'));
		editor.typeCharacter(keyInput('c'));

		editor.getCursor().setCaretPos(0, 0);
		editor.typeCharacter(keyInput(TypedKeyInput.RIGHT_KEY, true, false));

		Assert.assertEquals(0, editor.getSelection().selStart());
		Assert.assertEquals(1, editor.getSelection().selEnd());
	}

	@Test
	public void rightKey_NoShift()
	{
		TextEditor editor = textEditor();
		editor.typeCharacter(keyInput('a'));
		editor.typeCharacter(keyInput('b'));
		editor.typeCharacter(keyInput('c'));

		editor.getSelection().setSelection(1, 1, 2);
		editor.typeCharacter(keyInput(TypedKeyInput.RIGHT_KEY));

		Assert.assertFalse(editor.getSelection().hasSelection());
	}

	@Test
	public void rightKey_LineWrap()
	{
		TextEditor editor = textEditor();
		editor.typeCharacter(keyInput('a'));
		editor.typeCharacter(keyInput('b'));
		editor.typeCharacter(keyInput('c'));
		editor.typeCharacter(keyInput(TypedKeyInput.ENTER_KEY));

		editor.getCursor().setCaretPos(3, 0);
		editor.typeCharacter(keyInput(TypedKeyInput.RIGHT_KEY));

		Assert.assertEquals(0, editor.getCursor().getCaretX());
		Assert.assertEquals(1, editor.getCursor().getCaretY());
	}
	
	@Test
    public void upAndDownKey()
    {
        TextEditor editor = textEditor();
        editor.typeCharacter(keyInput('a'));
        editor.typeCharacter(keyInput('b'));
        editor.typeCharacter(keyInput('c'));
        editor.typeCharacter(keyInput(TypedKeyInput.ENTER_KEY));
        editor.typeCharacter(keyInput('d'));
        editor.typeCharacter(keyInput('e'));
        editor.typeCharacter(keyInput(TypedKeyInput.ENTER_KEY));
        editor.typeCharacter(keyInput('f'));
        editor.typeCharacter(keyInput(TypedKeyInput.ENTER_KEY));
        editor.typeCharacter(keyInput('g'));
        editor.typeCharacter(keyInput('h'));
        //writing
        //  "abc
        //   de
        //   f
        //   gh"
        //       in the text editor
        Assert.assertEquals(3, editor.getCursor().getCaretY());
        editor.typeCharacter(keyInput(TypedKeyInput.UP_KEY));
        Assert.assertEquals(2, editor.getCursor().getCaretY());
        Assert.assertEquals(1, editor.getCursor().getCaretX());
        editor.typeCharacter(keyInput(TypedKeyInput.UP_KEY));
        Assert.assertEquals(1, editor.getCursor().getCaretY());
        Assert.assertEquals(1, editor.getCursor().getCaretX());
        editor.typeCharacter(keyInput(TypedKeyInput.DOWN_KEY));
        Assert.assertEquals(2, editor.getCursor().getCaretY());
        Assert.assertEquals(1, editor.getCursor().getCaretX());
        
        //setting the cursor on the first row after the "abc"
        editor.typeCharacter(keyInput(TypedKeyInput.UP_KEY));
        editor.typeCharacter(keyInput(TypedKeyInput.UP_KEY)); 
        editor.typeCharacter(keyInput(TypedKeyInput.RIGHT_KEY));
        editor.typeCharacter(keyInput(TypedKeyInput.RIGHT_KEY));
        Assert.assertEquals(0, editor.getCursor().getCaretY());
        Assert.assertEquals(3, editor.getCursor().getCaretX());
        //to be sure that the cursor is in the right place
        
        editor.typeCharacter(keyInput(TypedKeyInput.DOWN_KEY));
        Assert.assertEquals(1, editor.getCursor().getCaretY());
        Assert.assertEquals(2, editor.getCursor().getCaretX());  
    }
	
	@Test
    public void homeAndEndKey()
    {
        TextEditor editor = textEditor();
        editor.typeCharacter(keyInput(TypedKeyInput.HOME_KEY));
        Assert.assertEquals(0, editor.getCursor().getCaretX());  
        editor.typeCharacter(keyInput(TypedKeyInput.END_KEY));
        Assert.assertEquals(0, editor.getCursor().getCaretX());  
        editor.typeCharacter(keyInput('a'));
        editor.typeCharacter(keyInput('b'));
        editor.typeCharacter(keyInput('c'));
        editor.typeCharacter(keyInput(TypedKeyInput.END_KEY));
        Assert.assertEquals(3, editor.getCursor().getCaretX());  
        editor.typeCharacter(keyInput(TypedKeyInput.HOME_KEY));
        Assert.assertEquals(0, editor.getCursor().getCaretX());  
        editor.typeCharacter(keyInput(TypedKeyInput.END_KEY));
        Assert.assertEquals(3, editor.getCursor().getCaretX());          
    }
	
}
