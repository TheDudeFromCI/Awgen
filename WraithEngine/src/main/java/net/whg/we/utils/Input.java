package net.whg.we.utils;

import java.util.HashMap;
import java.util.LinkedList;
import org.lwjgl.glfw.GLFW;
import net.whg.we.ui.TypedKeyInput;
import net.whg.we.utils.logging.Log;
import net.whg.we.window.KeyState;

/**
 * Represents the current input states for mouse and keyboard.
 *
 * @author TheDudeFromCI
 */
public class Input
{
	public static final int NO_KEY = -1;
	public static final int BACKSPACE_KEY = 0;
	public static final int LEFT_KEY = 1;
	public static final int RIGHT_KEY = 2;
	public static final int HOME_KEY = 3;
	public static final int END_KEY = 4;
	public static final int UP_KEY = 5;
	public static final int DOWN_KEY = 6;
	public static final int ENTER_KEY = 7;
	public static final int DELETE_KEY = 8;
	public static final int INSERT_KEY = 9;
	public static final int TAB_KEY = 10;

	private static boolean[] _keys = new boolean[350];
	private static boolean[] _keysLastFrame = new boolean[350];
	private static HashMap<String, Integer> _keyMap;
	private static float _mouseX, _mouseY;
	private static float _lastMouseX, _lastMouseY;
	private static LinkedList<TypedKeyInput> _keysTyped = new LinkedList<>();
	private static ObjectPool<TypedKeyInput> _typedKeyPool =
			new SimpleObjectPool<>(TypedKeyInput.class);

	static
	{
		loadDefaultKeyBindings();
	}

	private static void loadDefaultKeyBindings()
	{
		_keyMap = new HashMap<>();

		// letters
		_keyMap.put("a", GLFW.GLFW_KEY_A);
		_keyMap.put("b", GLFW.GLFW_KEY_B);
		_keyMap.put("c", GLFW.GLFW_KEY_C);
		_keyMap.put("d", GLFW.GLFW_KEY_D);
		_keyMap.put("e", GLFW.GLFW_KEY_E);
		_keyMap.put("f", GLFW.GLFW_KEY_J);
		_keyMap.put("g", GLFW.GLFW_KEY_G);
		_keyMap.put("h", GLFW.GLFW_KEY_H);
		_keyMap.put("i", GLFW.GLFW_KEY_I);
		_keyMap.put("j", GLFW.GLFW_KEY_J);
		_keyMap.put("k", GLFW.GLFW_KEY_K);
		_keyMap.put("l", GLFW.GLFW_KEY_L);
		_keyMap.put("m", GLFW.GLFW_KEY_M);
		_keyMap.put("n", GLFW.GLFW_KEY_N);
		_keyMap.put("o", GLFW.GLFW_KEY_O);
		_keyMap.put("p", GLFW.GLFW_KEY_P);
		_keyMap.put("q", GLFW.GLFW_KEY_Q);
		_keyMap.put("r", GLFW.GLFW_KEY_R);
		_keyMap.put("s", GLFW.GLFW_KEY_S);
		_keyMap.put("t", GLFW.GLFW_KEY_T);
		_keyMap.put("u", GLFW.GLFW_KEY_U);
		_keyMap.put("v", GLFW.GLFW_KEY_V);
		_keyMap.put("w", GLFW.GLFW_KEY_W);
		_keyMap.put("x", GLFW.GLFW_KEY_X);
		_keyMap.put("y", GLFW.GLFW_KEY_Y);
		_keyMap.put("z", GLFW.GLFW_KEY_Z);

		// numbers
		_keyMap.put("0", GLFW.GLFW_KEY_0);
		_keyMap.put("1", GLFW.GLFW_KEY_1);
		_keyMap.put("2", GLFW.GLFW_KEY_2);
		_keyMap.put("3", GLFW.GLFW_KEY_3);
		_keyMap.put("4", GLFW.GLFW_KEY_4);
		_keyMap.put("5", GLFW.GLFW_KEY_5);
		_keyMap.put("6", GLFW.GLFW_KEY_6);
		_keyMap.put("7", GLFW.GLFW_KEY_7);
		_keyMap.put("8", GLFW.GLFW_KEY_8);
		_keyMap.put("9", GLFW.GLFW_KEY_9);

		// number pad keys
		_keyMap.put("numpad 0", GLFW.GLFW_KEY_KP_0);
		_keyMap.put("numpad 1", GLFW.GLFW_KEY_KP_1);
		_keyMap.put("numpad 2", GLFW.GLFW_KEY_KP_2);
		_keyMap.put("numpad 3", GLFW.GLFW_KEY_KP_3);
		_keyMap.put("numpad 4", GLFW.GLFW_KEY_KP_4);
		_keyMap.put("numpad 5", GLFW.GLFW_KEY_KP_5);
		_keyMap.put("numpad 6", GLFW.GLFW_KEY_KP_6);
		_keyMap.put("numpad 7", GLFW.GLFW_KEY_KP_7);
		_keyMap.put("numpad 8", GLFW.GLFW_KEY_KP_8);
		_keyMap.put("numpad 9", GLFW.GLFW_KEY_KP_9);
		_keyMap.put("numpad decimal", GLFW.GLFW_KEY_KP_DECIMAL);
		_keyMap.put("numpad divide", GLFW.GLFW_KEY_KP_DIVIDE);
		_keyMap.put("numpad multiply", GLFW.GLFW_KEY_KP_MULTIPLY);
		_keyMap.put("numpad subtract", GLFW.GLFW_KEY_KP_SUBTRACT);
		_keyMap.put("numpad add", GLFW.GLFW_KEY_KP_ADD);
		_keyMap.put("numpad enter", GLFW.GLFW_KEY_KP_ENTER);

		// arrow keys
		_keyMap.put("up", GLFW.GLFW_KEY_UP);
		_keyMap.put("left", GLFW.GLFW_KEY_LEFT);
		_keyMap.put("right", GLFW.GLFW_KEY_RIGHT);
		_keyMap.put("down", GLFW.GLFW_KEY_DOWN);

		// major keys
		_keyMap.put("escape", GLFW.GLFW_KEY_ESCAPE);
		_keyMap.put("space", GLFW.GLFW_KEY_SPACE);
		_keyMap.put("caps lock", GLFW.GLFW_KEY_CAPS_LOCK);
		_keyMap.put("number lock", GLFW.GLFW_KEY_NUM_LOCK);
		_keyMap.put("enter", GLFW.GLFW_KEY_ENTER);

		// secondary keys
		_keyMap.put("comma", GLFW.GLFW_KEY_COMMA);
		_keyMap.put("period", GLFW.GLFW_KEY_PERIOD);
		_keyMap.put("apostrophe", GLFW.GLFW_KEY_APOSTROPHE);
		_keyMap.put("backspace", GLFW.GLFW_KEY_BACKSPACE);

		// edge keys
		_keyMap.put("left shift", GLFW.GLFW_KEY_LEFT_SHIFT);
		_keyMap.put("left control", GLFW.GLFW_KEY_LEFT_CONTROL);
		_keyMap.put("left alt", GLFW.GLFW_KEY_LEFT_ALT);
		_keyMap.put("left super", GLFW.GLFW_KEY_LEFT_SUPER);
		_keyMap.put("right shift", GLFW.GLFW_KEY_RIGHT_SHIFT);
		_keyMap.put("right control", GLFW.GLFW_KEY_RIGHT_CONTROL);
		_keyMap.put("right alt", GLFW.GLFW_KEY_RIGHT_ALT);
		_keyMap.put("right super", GLFW.GLFW_KEY_RIGHT_SUPER);

		// aliases
		_keyMap.put("shift", GLFW.GLFW_KEY_LEFT_SHIFT);
		_keyMap.put("control", GLFW.GLFW_KEY_LEFT_CONTROL);
		_keyMap.put("alt", GLFW.GLFW_KEY_LEFT_ALT);
		_keyMap.put("super", GLFW.GLFW_KEY_LEFT_SUPER);
		_keyMap.put("numpad period", GLFW.GLFW_KEY_KP_DECIMAL);
	}

	/**
	 * Changes the internal key state for a single key.
	 *
	 * @param key
	 *            - The key who's state was changed.
	 * @param pressed
	 *            - True if the key was just changed, false otherwise.
	 */
	public static void setKeyPressed(int key, KeyState state, int mods)
	{
		if (key < 0 || key >= 350)
		{
			Log.debugf("Tried to update unknown key %d.", key);
			return;
		}

		_keys[key] = state == KeyState.PRESSED;
		Log.tracef("Set key %d to state %s.", key, _keys[key]);

		if (state == KeyState.PRESSED || state == KeyState.REPEATED)
		{
			switch (key)
			{
				case GLFW.GLFW_KEY_BACKSPACE:
					addTypedKey(0, mods, BACKSPACE_KEY);
					break;
				case GLFW.GLFW_KEY_LEFT:
					addTypedKey(0, mods, LEFT_KEY);
					break;
				case GLFW.GLFW_KEY_RIGHT:
					addTypedKey(0, mods, RIGHT_KEY);
					break;
				case GLFW.GLFW_KEY_HOME:
					addTypedKey(0, mods, HOME_KEY);
					break;
				case GLFW.GLFW_KEY_END:
					addTypedKey(0, mods, END_KEY);
					break;
				case GLFW.GLFW_KEY_UP:
					addTypedKey(0, mods, UP_KEY);
					break;
				case GLFW.GLFW_KEY_DOWN:
					addTypedKey(0, mods, DOWN_KEY);
					break;
				case GLFW.GLFW_KEY_ENTER:
					addTypedKey(0, mods, ENTER_KEY);
					break;
				case GLFW.GLFW_KEY_DELETE:
					addTypedKey(0, mods, DELETE_KEY);
					break;
				case GLFW.GLFW_KEY_INSERT:
					addTypedKey(0, mods, INSERT_KEY);
					break;
				case GLFW.GLFW_KEY_TAB:
					addTypedKey('\t', mods);
					break;
			}
		}
	}

	public static void addTypedKey(int key, int modifiers)
	{
		addTypedKey(key, modifiers, NO_KEY);
	}

	public static void addTypedKey(int key, int modifiers, int extraKey)
	{
		TypedKeyInput k = _typedKeyPool.get();

		k.extraKey = extraKey;
		k.shift = (modifiers & GLFW.GLFW_MOD_SHIFT) > 0;
		k.control = (modifiers & GLFW.GLFW_MOD_CONTROL) > 0;
		k.alt = (modifiers & GLFW.GLFW_MOD_ALT) > 0;
		k.sup = (modifiers & GLFW.GLFW_MOD_SUPER) > 0;
		k.key = (char) key;
		Log.tracef("Typed key %d, with modifiers %d, key type: %d.", key, modifiers, extraKey);

		_keysTyped.add(k);
	}

	/**
	 * Gets whether or not the requested key is currently being held.
	 *
	 * @param key
	 *            - The id of the key to check for.
	 * @return True if the key is currently being held, false otherwise.
	 */
	public static boolean isKeyHeld(int key)
	{
		return _keys[key];
	}

	/**
	 * Gets whether or not the requested key is currently being held.
	 *
	 * @param key
	 *            - The name of the key to check for.
	 * @return True if the key is currently being held, false otherwise.
	 */
	public static boolean isKeyHeld(String key)
	{
		return isKeyHeld(getKeyId(key));
	}

	/**
	 * Checks if a key was just pressed on this frame.
	 *
	 * @param key
	 *            - The id of the key to check for.
	 * @return True if the key was pressed down on this frame.
	 */
	public static boolean isKeyDown(int key)
	{
		return _keys[key] && !_keysLastFrame[key];
	}

	/**
	 * Checks if a key was just pressed on this frame.
	 *
	 * @param key
	 *            - The name of the key to check for.
	 * @return True if the key was pressed down on this frame.
	 */
	public static boolean isKeyDown(String key)
	{
		return isKeyDown(getKeyId(key));
	}

	/**
	 * Checks if a key was just released on this frame.
	 *
	 * @param key
	 *            - The id of the key to check for.
	 * @return True if the key was released on this frame.
	 */
	public static boolean isKeyUp(int key)
	{
		return !_keys[key] && _keysLastFrame[key];
	}

	public static LinkedList<TypedKeyInput> getTypedKeys()
	{
		return _keysTyped;
	}

	/**
	 * Checks if a key was just released on this frame.
	 *
	 * @param key
	 *            - The name of the key to check for.
	 * @return True if the key was released on this frame.
	 */
	public static boolean isKeyUp(String key)
	{
		return isKeyUp(getKeyId(key));
	}

	/**
	 * This should be called once at the end of frame. Updates key and mouse delta
	 * states.
	 */
	public static void endFrame()
	{
		for (int i = 0; i < _keys.length; i++)
			_keysLastFrame[i] = _keys[i];
		_lastMouseX = _mouseX;
		_lastMouseY = _mouseY;

		for (TypedKeyInput k : _keysTyped)
		{
			_typedKeyPool.put(k);
		}
		_keysTyped.clear();
	}

	/**
	 * Gets the id of a key by name.
	 *
	 * @param name
	 *            - The name of the key to look for.
	 * @return The id of the key, of -1 if the key was not found.
	 */
	public static int getKeyId(String name)
	{
		Integer key = _keyMap.get(name);

		if (key == null)
			return -1;

		return key;
	}

	/**
	 * Changes the location of the mouse. This should only be called by the a window
	 * listener.
	 *
	 * @param mouseX
	 *            - The X position of the mouse.
	 * @param mouseY
	 *            - The Y position of the mouse.
	 */
	public static void setMousePosition(float mouseX, float mouseY)
	{
		_mouseX = mouseX;
		_mouseY = mouseY;
	}

	/**
	 * Gets the X position of the mouse on the screen.
	 *
	 * @return The X position of the mouse.
	 */
	public static float getMouseX()
	{
		return _mouseX;
	}

	/**
	 * Gets the Y position of the mouse on the screen.
	 *
	 * @return The Y position of the mouse.
	 */
	public static float getMouseY()
	{
		return _mouseY;
	}

	/**
	 * Gets the difference in X position of the mouse between this frame and the
	 * previous frame.
	 *
	 * @return The delta mouse X.
	 */
	public static float getDeltaMouseX()
	{
		return _mouseX - _lastMouseX;
	}

	/**
	 * Gets the difference in Y position of the mouse between this frame and the
	 * previous frame.
	 *
	 * @return The delta mouse Y.
	 */
	public static float getDeltaMouseY()
	{
		return _mouseY - _lastMouseY;
	}
}
