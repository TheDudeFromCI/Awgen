package net.whg.we.network.netty;

/**
 * The current login state of the user connection.
 *
 * @author TheDudeFromCI
 */
public class UserState
{
	private boolean _isClient;
	private boolean _isAuthenticated;
	private String _username;
	private String _token;

	/**
	 * Creates a blank user state.
	 *
	 * @param isClient
	 *            - True if this is the client side of the connection. False if this
	 *            is called from the server side.
	 */
	public UserState(boolean isClient)
	{
		_isClient = isClient;
	}

	/**
	 * Checks if this user connection represents a client connecting to a server.
	 *
	 * @return True if this is currently the client side of a connection, false if
	 *         this is the server side of the connection.
	 */
	public boolean isClient()
	{
		return _isClient;
	}

	/**
	 * Checks if the user for this connection has been authenticated.
	 *
	 * @return True if this connection has been successfully been authenticated,
	 *         false otherwise. If called from a client user state, this will always
	 *         return true.
	 */
	public boolean isAuthenticated()
	{
		return _isAuthenticated || _isClient;
	}

	/**
	 * Authenticates this user connection with the given username and token.
	 *
	 * @param username
	 *            - The username for the client connecting to this server.
	 * @param token
	 *            - The token for the client connecting to this server.
	 * @throws IllegalStateException
	 *             if this method is called for a user state that has already been
	 *             authenticated, or called on the client side of a connection.
	 */
	public void authenticate(String username, String token)
	{
		if (isAuthenticated())
			throw new IllegalStateException("UserConnection has already been authenticated!");

		_isAuthenticated = true;
		_username = username;
		_token = token;
	}

	/**
	 * Gets the username for this user.
	 *
	 * @return The username for this user, or null if this user has not yet been
	 *         authenticated. This will also return null if this UserState
	 *         represents the client side of a connection.
	 */
	public String getUsername()
	{
		return _username;
	}

	/**
	 * Gets the token for this user.
	 *
	 * @return The token for this user, or null if this user has not yet been
	 *         authenticated. This will also return null if this UserState
	 *         represents the client side of a connection.
	 */
	public String getToken()
	{
		return _token;
	}
}
