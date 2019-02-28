package command_handling;

import org.junit.Assert;
import org.junit.Test;
import net.whg.we.command.CommandParseException;
import net.whg.we.command.Token;
import net.whg.we.command.TokenTemplate;
import net.whg.we.command.Tokenizer;

public class TokenizerTest
{
	private void validateToken(Token token, String value, int type)
	{
		Assert.assertEquals(value, token.getValue());
		Assert.assertEquals(type, token.getType());
	}

	@Test
	public void singleWord()
	{
		String code = "clear";
		Tokenizer token = new Tokenizer(code);

		Assert.assertTrue(token.hasNextToken());
		validateToken(token.nextToken(), "clear", TokenTemplate.STANDARD);
		Assert.assertFalse(token.hasNextToken());
	}

	@Test
	public void wordWithParameters()
	{
		String code = "clear -1f";
		Tokenizer token = new Tokenizer(code);

		validateToken(token.nextToken(), "clear", TokenTemplate.STANDARD);
		validateToken(token.nextToken(), "-1f", TokenTemplate.STANDARD);
		Assert.assertFalse(token.hasNextToken());
	}

	@Test
	public void wordWithParameters_Variable()
	{
		String code = "clear -1f $red";
		Tokenizer token = new Tokenizer(code);

		validateToken(token.nextToken(), "clear", TokenTemplate.STANDARD);
		validateToken(token.nextToken(), "-1f", TokenTemplate.STANDARD);
		validateToken(token.nextToken(), "$red", TokenTemplate.VARIABLE);
		Assert.assertFalse(token.hasNextToken());
	}

	@Test
	public void variable_assignment()
	{
		String code = "$red = 0234";
		Tokenizer token = new Tokenizer(code);

		validateToken(token.nextToken(), "$red", TokenTemplate.VARIABLE);
		validateToken(token.nextToken(), "=", TokenTemplate.SYMBOL);
		validateToken(token.nextToken(), "0234", TokenTemplate.STANDARD);
		Assert.assertFalse(token.hasNextToken());
	}

	@Test
	public void lotsOfSpaces()
	{
		String code = "$red     $green = \n\n 0234     clear\t$blue";
		Tokenizer token = new Tokenizer(code);

		validateToken(token.nextToken(), "$red", TokenTemplate.VARIABLE);
		validateToken(token.nextToken(), "$green", TokenTemplate.VARIABLE);
		validateToken(token.nextToken(), "=", TokenTemplate.SYMBOL);
		validateToken(token.nextToken(), "0234", TokenTemplate.STANDARD);
		validateToken(token.nextToken(), "clear", TokenTemplate.STANDARD);
		validateToken(token.nextToken(), "$blue", TokenTemplate.VARIABLE);
		Assert.assertFalse(token.hasNextToken());
	}

	@Test
	public void nestedCommand()
	{
		String code = "abc (123 \"456\" $789) def";
		Tokenizer token = new Tokenizer(code);

		validateToken(token.nextToken(), "abc", TokenTemplate.STANDARD);
		validateToken(token.nextToken(), "123 \"456\" $789", TokenTemplate.NESTED_COMMAND);
		validateToken(token.nextToken(), "def", TokenTemplate.STANDARD);
		Assert.assertFalse(token.hasNextToken());
	}

	@Test
	public void string()
	{
		String code = "abc \"123%$$*\"  def";
		Tokenizer token = new Tokenizer(code);

		validateToken(token.nextToken(), "abc", TokenTemplate.STANDARD);
		validateToken(token.nextToken(), "123%$$*", TokenTemplate.STRING);
		validateToken(token.nextToken(), "def", TokenTemplate.STANDARD);
		Assert.assertFalse(token.hasNextToken());
	}

	@Test
	public void emptyCommand()
	{
		String code = "";
		Tokenizer token = new Tokenizer(code);

		validateToken(token.nextToken(), "", TokenTemplate.EMPTY);
		Assert.assertFalse(token.hasNextToken());
	}

	@Test
	public void endOfLine()
	{
		String code = "abc; def;";
		Tokenizer token = new Tokenizer(code);

		validateToken(token.nextToken(), "abc", TokenTemplate.STANDARD);
		validateToken(token.nextToken(), ";", TokenTemplate.SYMBOL);
		validateToken(token.nextToken(), "def", TokenTemplate.STANDARD);
		validateToken(token.nextToken(), ";", TokenTemplate.SYMBOL);
		Assert.assertFalse(token.hasNextToken());
	}

	@Test
	public void dynamicVariable()
	{
		String code = "$[clear]";
		Tokenizer token = new Tokenizer(code);

		validateToken(token.nextToken(), "clear", TokenTemplate.DYNAMIC_VARIABLE);
		Assert.assertFalse(token.hasNextToken());
	}

	@Test
	public void nestedDynamicVariable()
	{
		String code = "$[clear (time -f ss)]";
		Tokenizer token = new Tokenizer(code);

		validateToken(token.nextToken(), "clear (time -f ss)", TokenTemplate.DYNAMIC_VARIABLE);
		Assert.assertFalse(token.hasNextToken());
	}

	@Test(expected = CommandParseException.class)
	public void unquotedSymbols()
	{
		new Tokenizer("!@#%$%&^$%").nextToken();
	}

	@Test(expected = CommandParseException.class)
	public void doubleSVariable()
	{
		new Tokenizer("$$red").nextToken();
	}

	@Test(expected = CommandParseException.class)
	public void noSpace_Variable()
	{
		new Tokenizer("$red$green").nextToken();
	}

	@Test(expected = CommandParseException.class)
	public void unfinishedQuote()
	{
		new Tokenizer("\"Hello World!").nextToken();
	}

	@Test(expected = CommandParseException.class)
	public void midSentenceQuote()
	{
		new Tokenizer("arg\"arg2").nextToken();
	}

	@Test(expected = CommandParseException.class)
	public void unfinishedNest()
	{
		new Tokenizer("(Nest").nextToken();
	}

	@Test(expected = CommandParseException.class)
	public void endOfNest()
	{
		new Tokenizer(")").nextToken();
	}

	@Test(expected = CommandParseException.class)
	public void numberOnlyVariable()
	{
		new Tokenizer("$0123").nextToken();
	}

	@Test(expected = CommandParseException.class)
	public void numberPrefixedVariable()
	{
		new Tokenizer("$0abc").nextToken();
	}
}
