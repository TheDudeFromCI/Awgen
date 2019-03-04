package net.whg.we.network;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface TCPChannel extends Closeable
{
	String getIPString();

	OutputStream getOutputStream() throws IOException;

	InputStream getInputStream() throws IOException;
}
