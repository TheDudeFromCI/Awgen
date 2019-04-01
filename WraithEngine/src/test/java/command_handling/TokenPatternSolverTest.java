package command_handling;

import java.util.LinkedList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import net.whg.frameworks.command.Token;
import net.whg.frameworks.command.TokenPatternSolver;
import net.whg.frameworks.command.TokenTemplate;

public class TokenPatternSolverTest
{
    private void validateSolver(TokenPatternSolver solver, char type,
            char quantifier)
    {
        Assert.assertEquals(type, solver.getType());
        Assert.assertEquals(quantifier, solver.getQuantifier());
    }

    @Test
    public void compile1()
    {
        TokenPatternSolver[] s = TokenPatternSolver.compile("v+=ap*;?");

        validateSolver(s[0], 'v', '+');
        validateSolver(s[1], '=', '1');
        validateSolver(s[2], 'a', '1');
        validateSolver(s[3], 'p', '*');
        validateSolver(s[4], ';', '?');
    }

    @Test
    public void compile2()
    {
        TokenPatternSolver[] s = TokenPatternSolver.compile("v=s");

        validateSolver(s[0], 'v', '1');
        validateSolver(s[1], '=', '1');
        validateSolver(s[2], 's', '1');
    }

    @Test
    public void isGreedy()
    {
        Assert.assertTrue(new TokenPatternSolver('a', '*').isGreedy());
        Assert.assertTrue(new TokenPatternSolver('a', '+').isGreedy());
        Assert.assertFalse(new TokenPatternSolver('a', '?').isGreedy());
        Assert.assertFalse(new TokenPatternSolver('a', '1').isGreedy());
    }

    @Test
    public void isOptional()
    {
        Assert.assertTrue(new TokenPatternSolver('a', '*').isOptional());
        Assert.assertTrue(new TokenPatternSolver('a', '?').isOptional());
        Assert.assertFalse(new TokenPatternSolver('a', '+').isOptional());
        Assert.assertFalse(new TokenPatternSolver('a', '1').isOptional());
    }

    @Test
    public void matches()
    {
        Assert.assertTrue(new TokenPatternSolver('a', '1')
                .matches(new Token(TokenTemplate.STANDARD, "clear")));

        Assert.assertTrue(new TokenPatternSolver('v', '1')
                .matches(new Token(TokenTemplate.VARIABLE, "$red")));

        Assert.assertTrue(new TokenPatternSolver('n', '1').matches(
                new Token(TokenTemplate.NESTED_COMMAND, "time -f ss")));

        Assert.assertTrue(new TokenPatternSolver('s', '1')
                .matches(new Token(TokenTemplate.STRING, "Hello World!")));

        Assert.assertTrue(new TokenPatternSolver('p', '1')
                .matches(new Token(TokenTemplate.STANDARD, "clear")));

        Assert.assertTrue(new TokenPatternSolver('p', '1')
                .matches(new Token(TokenTemplate.VARIABLE, "clear")));

        Assert.assertTrue(new TokenPatternSolver('p', '1')
                .matches(new Token(TokenTemplate.NESTED_COMMAND, "clear")));

        Assert.assertTrue(new TokenPatternSolver('p', '1')
                .matches(new Token(TokenTemplate.STRING, "Hello World!")));

        Assert.assertTrue(new TokenPatternSolver('=', '1')
                .matches(new Token(TokenTemplate.SYMBOL, "=")));

        Assert.assertTrue(new TokenPatternSolver(';', '1')
                .matches(new Token(TokenTemplate.SYMBOL, ";")));

        Assert.assertFalse(new TokenPatternSolver('a', '1')
                .matches(new Token(TokenTemplate.STRING, "clear")));

        Assert.assertFalse(new TokenPatternSolver('n', '1')
                .matches(new Token(TokenTemplate.STRING, "clear")));

        Assert.assertFalse(new TokenPatternSolver('a', '1')
                .matches(new Token(TokenTemplate.VARIABLE, "clear")));

        Assert.assertFalse(new TokenPatternSolver('n', '1')
                .matches(new Token(TokenTemplate.STANDARD, "clear")));

        Assert.assertFalse(new TokenPatternSolver('v', '1')
                .matches(new Token(TokenTemplate.STANDARD, "clear")));

        Assert.assertFalse(new TokenPatternSolver('s', '1')
                .matches(new Token(TokenTemplate.STANDARD, "clear")));

        Assert.assertFalse(new TokenPatternSolver('p', '1')
                .matches(new Token(TokenTemplate.SYMBOL, "=")));

        Assert.assertFalse(new TokenPatternSolver('p', '1')
                .matches(new Token(TokenTemplate.SYMBOL, ";")));
        Assert.assertFalse(new TokenPatternSolver(';', '1')
                .matches(new Token(TokenTemplate.SYMBOL, "=")));

        Assert.assertFalse(new TokenPatternSolver('=', '1')
                .matches(new Token(TokenTemplate.SYMBOL, ";")));

        Assert.assertFalse(new TokenPatternSolver('=', '1')
                .matches(new Token(TokenTemplate.STRING, "=")));

        Assert.assertFalse(new TokenPatternSolver(';', '1')
                .matches(new Token(TokenTemplate.STRING, ";")));
    }

    @Test(expected = IllegalStateException.class)
    public void unknownPattern()
    {
        new TokenPatternSolver('6', '1')
                .matches(new Token(TokenTemplate.EMPTY, ""));
    }

    @Test
    public void count_Single()
    {
        List<Token> tokens = new LinkedList<>();
        tokens.add(new Token(TokenTemplate.STANDARD, "a"));
        tokens.add(new Token(TokenTemplate.STANDARD, "b"));
        tokens.add(new Token(TokenTemplate.STANDARD, "c"));
        tokens.add(new Token(TokenTemplate.STANDARD, "d"));

        TokenPatternSolver solver = new TokenPatternSolver('a', '1');
        Assert.assertEquals(1, solver.count(tokens, 0));
    }

    @Test
    public void count_Greedy()
    {
        List<Token> tokens = new LinkedList<>();
        tokens.add(new Token(TokenTemplate.STANDARD, "a"));
        tokens.add(new Token(TokenTemplate.STANDARD, "b"));
        tokens.add(new Token(TokenTemplate.STANDARD, "c"));
        tokens.add(new Token(TokenTemplate.STANDARD, "d"));
        tokens.add(new Token(TokenTemplate.STRING, "e"));

        TokenPatternSolver solver = new TokenPatternSolver('a', '+');
        Assert.assertEquals(4, solver.count(tokens, 0));
    }

    @Test
    public void count_QuestionMark()
    {
        List<Token> tokens = new LinkedList<>();
        tokens.add(new Token(TokenTemplate.STANDARD, "a"));
        tokens.add(new Token(TokenTemplate.STANDARD, "b"));
        tokens.add(new Token(TokenTemplate.STANDARD, "c"));

        TokenPatternSolver solver = new TokenPatternSolver('a', '?');
        Assert.assertEquals(1, solver.count(tokens, 0));
    }

    @Test
    public void count_Empty()
    {
        List<Token> tokens = new LinkedList<>();

        TokenPatternSolver solver = new TokenPatternSolver('a', '1');
        Assert.assertEquals(0, solver.count(tokens, 0));
    }

    @Test
    public void count_MidList()
    {
        List<Token> tokens = new LinkedList<>();
        tokens.add(new Token(TokenTemplate.STANDARD, "a"));
        tokens.add(new Token(TokenTemplate.STANDARD, "b"));
        tokens.add(new Token(TokenTemplate.STANDARD, "c"));

        TokenPatternSolver solver = new TokenPatternSolver('a', '*');
        Assert.assertEquals(2, solver.count(tokens, 1));
    }
}
