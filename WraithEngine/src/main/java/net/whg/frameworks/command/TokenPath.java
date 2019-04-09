package net.whg.frameworks.command;

public class TokenPath
{
    private Token[] _tokens;

    public TokenPath(Token[] tokens)
    {
        _tokens = tokens;
    }

    public Token[] getTokens()
    {
        return _tokens;
    }
}
