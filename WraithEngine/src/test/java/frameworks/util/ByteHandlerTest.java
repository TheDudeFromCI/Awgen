package frameworks.util;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;
import org.junit.Assert;
import org.junit.Test;
import net.whg.frameworks.scene.Transform3D;
import net.whg.frameworks.util.ByteReader;
import net.whg.frameworks.util.ByteWriter;

public class ByteHandlerTest
{
	@Test
	public void read_write_bytes()
	{
		byte[] byteBuffer = new byte[100];

		// Write data
		ByteWriter out = new ByteWriter(byteBuffer);
		out.writeByte((byte) 10);
		out.writeByte((byte) -14);
		out.writeByte((byte) 130);
		out.writeShort((short) 7000);
		out.writeInt(32000000);
		out.writeLong(1234567890123L);
		out.writeFloat(0.595f);
		out.writeDouble(0.45548432);
		out.writeBool(true);
		out.writeString("Hello!", StandardCharsets.UTF_8);
		out.writeString("¤¥(<U", StandardCharsets.UTF_16);
		Assert.assertEquals(56, out.getPos());

		// Read data
		ByteReader in = new ByteReader(byteBuffer);
		Assert.assertEquals((byte) 10, in.getByte());
		Assert.assertEquals((byte) -14, in.getByte());
		Assert.assertEquals((byte) 130, in.getByte());
		Assert.assertEquals((short) 7000, in.getShort());
		Assert.assertEquals(32000000, in.getInt());
		Assert.assertEquals(1234567890123L, in.getLong());
		Assert.assertEquals(0.595f, in.getFloat(), 0);
		Assert.assertEquals(0.45548432, in.getDouble(), 0);
		Assert.assertEquals(true, in.getBool());
		Assert.assertEquals("Hello!", in.getString(StandardCharsets.UTF_8));
		Assert.assertEquals("¤¥(<U", in.getString(StandardCharsets.UTF_16));
		Assert.assertEquals(56, in.getPos());
	}

	@Test
	public void read_write_bytestream() throws IOException
	{
		PipedInputStream inputStream = new PipedInputStream();
		PipedOutputStream outputStream = new PipedOutputStream(inputStream);

		// Write data
		ByteWriter out = new ByteWriter(outputStream);
		out.writeByte((byte) 10);
		out.writeByte((byte) -14);
		out.writeByte((byte) 130);
		out.writeShort((short) 7000);
		out.writeInt(32000000);
		out.writeLong(1234567890123L);
		out.writeFloat(0.595f);
		out.writeDouble(0.45548432);
		out.writeBool(true);
		out.writeString("Hello!", StandardCharsets.UTF_8);
		out.writeString("¤¥(<U", StandardCharsets.UTF_16);
		Assert.assertEquals(56, out.getPos());

		// Read data
		ByteReader in = new ByteReader(inputStream);
		Assert.assertEquals((byte) 10, in.getByte());
		Assert.assertEquals((byte) -14, in.getByte());
		Assert.assertEquals((byte) 130, in.getByte());
		Assert.assertEquals((short) 7000, in.getShort());
		Assert.assertEquals(32000000, in.getInt());
		Assert.assertEquals(1234567890123L, in.getLong());
		Assert.assertEquals(0.595f, in.getFloat(), 0);
		Assert.assertEquals(0.45548432, in.getDouble(), 0);
		Assert.assertEquals(true, in.getBool());
		Assert.assertEquals("Hello!", in.getString(StandardCharsets.UTF_8));
		Assert.assertEquals("¤¥(<U", in.getString(StandardCharsets.UTF_16));
		Assert.assertEquals(56, in.getPos());
	}

	@Test
	public void read_write_capacity()
	{
		byte[] bytes = new byte[8];

		ByteWriter out = new ByteWriter(bytes);
		out.writeByte((byte) 10);
		out.writeByte((byte) 20);
		out.writeByte((byte) 30);

		Assert.assertEquals(8, out.getCapacity());
		Assert.assertEquals(3, out.getPos());
		Assert.assertEquals(5, out.getRemainingBytes());

		ByteReader in = new ByteReader(bytes);
		in.getByte();
		in.getByte();
		in.getByte();

		Assert.assertEquals(8, in.getCapacity());
		Assert.assertEquals(3, in.getPos());
		Assert.assertEquals(5, in.getRemainingBytes());
	}

	@Test
	public void read_write_capacity_stream() throws IOException
	{
		PipedInputStream inputStream = new PipedInputStream();
		PipedOutputStream outputStream = new PipedOutputStream(inputStream);

		ByteWriter out = new ByteWriter(outputStream);
		out.writeByte((byte) 10);
		out.writeByte((byte) 20);
		out.writeByte((byte) 30);

		Assert.assertEquals(-1, out.getCapacity());
		Assert.assertEquals(3, out.getPos());
		Assert.assertEquals(-1, out.getRemainingBytes());

		ByteReader in = new ByteReader(inputStream);
		in.getByte();
		in.getByte();
		in.getByte();

		Assert.assertEquals(-1, in.getCapacity());
		Assert.assertEquals(3, in.getPos());
		Assert.assertEquals(-1, in.getRemainingBytes());
	}

	@Test
	public void read_write_object_stream() throws IOException
	{
		PipedInputStream inputStream = new PipedInputStream();
		PipedOutputStream outputStream = new PipedOutputStream(inputStream);

		Transform3D original = new Transform3D();
		original.setPosition(15f, 12f, 23f);
		original.getRotation().x = 19f;
		original.setSize(123f, -12f, 30f);

		ByteWriter out = new ByteWriter(outputStream);
		out.writeObject(original);

		ByteReader in = new ByteReader(inputStream);
		Transform3D clone = (Transform3D) in.readObject();

		Assert.assertEquals(original, clone);
	}

	@Test
	public void read_write_object_bytes() throws IOException
	{
		byte[] bytes = new byte[1024];

		Transform3D original = new Transform3D();
		original.setPosition(15f, 12f, 23f);
		original.getRotation().x = 19f;
		original.setSize(123f, -12f, 30f);

		ByteWriter out = new ByteWriter(bytes);
		out.writeObject(original);

		ByteReader in = new ByteReader(bytes);
		Transform3D clone = (Transform3D) in.readObject();

		Assert.assertEquals(original, clone);
	}
}
