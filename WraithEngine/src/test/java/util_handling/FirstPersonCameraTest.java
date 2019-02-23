package util_handling;

import static org.junit.Assert.*;

import org.joml.Vector3f;
import org.junit.Test;

import net.whg.we.rendering.Camera;
import net.whg.we.utils.FirstPersonCamera;
import net.whg.we.utils.MathUtils;

public class FirstPersonCameraTest {
	
	@Test
	public void testInstantiation() {
		FirstPersonCamera fpc = new FirstPersonCamera(new Camera());
		assertTrue(fpc.getMouseSensitivity() == 3f);
		assertTrue(fpc.getMoveSpeed() == 7f);
		fpc.setMouseSensitivity(5f);
		fpc.setMoveSpeed(8f);
		assertTrue(fpc.getMouseSensitivity() == 5f);
		assertTrue(fpc.getMoveSpeed() == 8f);
		Vector3f v = new Vector3f();
		assertTrue(fpc.getBaseRotation().equals(v, 0));
		assertTrue(fpc.getExtraRotation().equals(v, 0));
	}
	
	@Test
	public void testMathUtils() {
		Vector3f v = new Vector3f();
		assertTrue(MathUtils.isValid(v));
		assertTrue(MathUtils.clamp(5f, 4f, 6f) == 5f);
		assertTrue(MathUtils.clamp(4f, 5f, 6f) == 5f);
		assertTrue(MathUtils.clamp(6f, 4f, 5f) == 5f);
	}
	
	@Test
	public void testUpdateCameraRotation() {
		FirstPersonCamera fpc = new FirstPersonCamera(new Camera());
		fpc.update();
		assertTrue(true);
	}
	
	/*
	@Test
	public void testUpdateCameraPosition() {
		
	}
	*/
	

}
