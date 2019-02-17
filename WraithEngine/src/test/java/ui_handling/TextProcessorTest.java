package ui_handling;

import org.junit.Assert;
import org.junit.Test;
import net.whg.we.ui.TextProcessor;

public class TextProcessorTest
{
	@Test
	public void parseText()
	{
		TextProcessor text = new TextProcessor("This is one line.");

		Assert.assertEquals(1, text.getLineCount());
		Assert.assertEquals("This is one line.", text.getLine(0));
	}

	@Test
	public void parseText_MultipleLines()
	{
		TextProcessor text = new TextProcessor("Line 1\nLine 2\nLine 3");

		Assert.assertEquals(3, text.getLineCount());
		Assert.assertEquals("Line 1", text.getLine(0));
		Assert.assertEquals("Line 2", text.getLine(1));
		Assert.assertEquals("Line 3", text.getLine(2));
	}

	@Test
	public void parseText_NullInput()
	{
		TextProcessor text = new TextProcessor("Text");
		text.set(null);

		Assert.assertEquals(1, text.getLineCount());
		Assert.assertEquals("", text.getLine(0));
	}

	@Test
	public void parseText_MultipleBlankLines()
	{
		TextProcessor text = new TextProcessor();
		text.set("\n\n\n\n\n");

		Assert.assertEquals(6, text.getLineCount());
		Assert.assertEquals("", text.getLine(0));
		Assert.assertEquals("", text.getLine(1));
		Assert.assertEquals("", text.getLine(2));
		Assert.assertEquals("", text.getLine(3));
		Assert.assertEquals("", text.getLine(4));
		Assert.assertEquals("", text.getLine(5));
	}

	@Test
	public void getLineLength()
	{
		TextProcessor text = new TextProcessor("123456\n1234");

		Assert.assertEquals(6, text.getLineLength(0));
		Assert.assertEquals(4, text.getLineLength(1));
	}

	@Test
	public void insert()
	{
		TextProcessor text = new TextProcessor();
		text.insert(0, 0, "Hello");

		Assert.assertEquals("Hello", text.getLine(0));
	}

	@Test
	public void insert_middle()
	{
		TextProcessor text = new TextProcessor();
		text.insert(0, 0, "Hello");
		text.insert(0, 2, "Derp");

		Assert.assertEquals("HeDerpllo", text.getLine(0));
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void insert_PastEnd()
	{
		TextProcessor text = new TextProcessor();
		text.insert(0, 2, "Too long");
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void insert_Negative()
	{
		TextProcessor text = new TextProcessor();
		text.insert(0, -1, "Negative");
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void insert_UnknownLine()
	{
		TextProcessor text = new TextProcessor();
		text.insert(1, 0, "Only one line.");
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void insert_Negative_Line()
	{
		TextProcessor text = new TextProcessor();
		text.insert(-1, 0, "Negative");
	}

	@Test
	public void delete()
	{
		TextProcessor text = new TextProcessor("SomeText");
		text.delete(0, 0, 4);

		Assert.assertEquals("Text", text.getLine(0));
	}

	@Test
	public void delete_tooFar()
	{
		TextProcessor text = new TextProcessor("SomeText");
		text.delete(0, 4, 50);

		Assert.assertEquals("Some", text.getLine(0));
	}

	@Test
	public void append()
	{
		TextProcessor text = new TextProcessor("SomeText");
		text.append(0, "Added");

		Assert.assertEquals("SomeTextAdded", text.getLine(0));
	}

	@Test
	public void appendNewLine()
	{
		TextProcessor text = new TextProcessor("SomeText");
		text.append(0, "Added\n12");

		Assert.assertEquals(2, text.getLineCount());
		Assert.assertEquals("SomeTextAdded", text.getLine(0));
		Assert.assertEquals("12", text.getLine(1));
	}

	@Test
	public void appendNull()
	{
		TextProcessor text = new TextProcessor("SomeText");
		text.append(0, null);

		Assert.assertEquals(1, text.getLineCount());
		Assert.assertEquals("SomeText", text.getLine(0));
	}

	@Test
	public void appendMultipleNewLines()
	{
		TextProcessor text = new TextProcessor("SomeText");
		text.append(0, "Added\n12\nabc");

		Assert.assertEquals(3, text.getLineCount());
		Assert.assertEquals("SomeTextAdded", text.getLine(0));
		Assert.assertEquals("12", text.getLine(1));
		Assert.assertEquals("abc", text.getLine(2));
	}

	@Test
	public void insertNewLine()
	{
		TextProcessor text = new TextProcessor("SomeText");
		text.insert(0, 4, "\n");

		Assert.assertEquals(2, text.getLineCount());
		Assert.assertEquals("Some", text.getLine(0));
		Assert.assertEquals("Text", text.getLine(1));
	}

	@Test
	public void insertMultipleNewLine()
	{
		TextProcessor text = new TextProcessor("Some\nText");
		text.insert(0, 4, "\nA\nB\nC");

		Assert.assertEquals(5, text.getLineCount());
		Assert.assertEquals("Some", text.getLine(0));
		Assert.assertEquals("A", text.getLine(1));
		Assert.assertEquals("B", text.getLine(2));
		Assert.assertEquals("C", text.getLine(3));
		Assert.assertEquals("Text", text.getLine(4));
	}

	@Test
	public void insertNewLine_direct()
	{
		TextProcessor text = new TextProcessor("SomeText");
		text.insertNewLineAt(0);

		Assert.assertEquals(2, text.getLineCount());
		Assert.assertEquals("", text.getLine(0));
		Assert.assertEquals("SomeText", text.getLine(1));
	}

	@Test
	public void insertNewLine_direct2()
	{
		TextProcessor text = new TextProcessor("Some\nText");
		text.insertNewLineAt(1);

		Assert.assertEquals(3, text.getLineCount());
		Assert.assertEquals("Some", text.getLine(0));
		Assert.assertEquals("", text.getLine(1));
		Assert.assertEquals("Text", text.getLine(2));
	}

	@Test
	public void insertNewLine_direct3()
	{
		TextProcessor text = new TextProcessor("Some\nText");
		text.insertNewLineAt(2);

		Assert.assertEquals(3, text.getLineCount());
		Assert.assertEquals("Some", text.getLine(0));
		Assert.assertEquals("Text", text.getLine(1));
		Assert.assertEquals("", text.getLine(2));
	}

	@Test
	public void deleteFirstLine()
	{
		TextProcessor text = new TextProcessor("Some\nText");
		text.deleteLine(0);

		Assert.assertEquals(1, text.getLineCount());
		Assert.assertEquals("Text", text.getLine(0));
	}

	@Test
	public void deleteLastLine()
	{
		TextProcessor text = new TextProcessor("Some\nText");
		text.deleteLine(1);

		Assert.assertEquals(1, text.getLineCount());
		Assert.assertEquals("Some", text.getLine(0));
	}

	@Test
	public void deleteOnlyLine()
	{
		TextProcessor text = new TextProcessor("Text");
		text.deleteLine(0);

		Assert.assertEquals("", text.toString());
	}

	@Test
	public void deleteEmptyLine()
	{
		TextProcessor text = new TextProcessor("Some\n\nText");
		text.deleteLine(1);

		Assert.assertEquals(2, text.getLineCount());
		Assert.assertEquals("Some", text.getLine(0));
		Assert.assertEquals("Text", text.getLine(1));
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void deleteNegativeLine()
	{
		TextProcessor text = new TextProcessor("Some\nText");
		text.deleteLine(-1);
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void deleteOutOfBoundsLine()
	{
		TextProcessor text = new TextProcessor("Some\nText");
		text.deleteLine(2);
	}

	@Test
	public void setLine()
	{
		TextProcessor text = new TextProcessor("\n\n");
		text.setLine(0, "Hello");
		text.setLine(2, "There");

		Assert.assertEquals("Hello\n\nThere", text.toString());
	}

	@Test
	public void appendNewLine_shortcut()
	{
		TextProcessor text = new TextProcessor();
		text.setLine(0, "Hello");
		text.appendNewLine(1, "World!");

		Assert.assertEquals("Hello\nWorld!", text.toString());
	}

	@Test
	public void appendNewLine_Middle_shortcut()
	{
		TextProcessor text = new TextProcessor();
		text.setLine(0, "A");
		text.appendNewLine(1, "C");
		text.appendNewLine(1, "B");

		Assert.assertEquals("A\nB\nC", text.toString());
	}
}
