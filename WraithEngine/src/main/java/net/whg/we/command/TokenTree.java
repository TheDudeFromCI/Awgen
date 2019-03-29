package net.whg.we.command;

import java.util.LinkedList;
import java.util.List;
import net.whg.we.utils.logging.Log;

public class TokenTree
{
    private TokenTreePattern[] _patterns =
    {
    /* @formatter:off */

		new TokenTreePattern("ap*;?"),
		new TokenTreePattern("v=ap*;?"),
		new TokenTreePattern("v=s;?"),
		new TokenTreePattern("v=n;?"),
		new TokenTreePattern("v=v;?"),
		new TokenTreePattern(";"),

		/* @formatter:on */
    };
    private List<Token> _tokens = new LinkedList<>();

    public void addTokens(Tokenizer tokenizer)
    {
        while (tokenizer.hasNextToken())
            _tokens.add(tokenizer.nextToken());
    }

    public boolean hasNextPath()
    {
        return _tokens.size() > 0;
    }

    public TokenPath nextPath()
    {
        int patternId = -1;
        for (TokenTreePattern pattern : _patterns)
        {
            patternId++;

            TokenPath path = pattern.build(_tokens);
            if (path != null)
            {
                Log.tracef("Found path; Pattern Id: %d", patternId);
                return path;
            }
        }

        throw new CommandParseException("Incorrectly formatted command!");
    }
}
