package net.whg.frameworks.command;

public class CommandParseException extends RuntimeException
{
    private static final long serialVersionUID = 6932263312565399856L;

    public CommandParseException(String string)
    {
        super(string);
    }
}
