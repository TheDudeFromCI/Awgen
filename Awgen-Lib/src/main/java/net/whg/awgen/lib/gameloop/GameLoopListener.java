package net.whg.awgen.lib.gameloop;

import net.whg.stlib.event.Listener;

/**
 * A listener handler for events which occur within a game loop object. Contains
 * events for various stages throuhout each frame.
 *
 * @author TheDudeFromCI
 */
public interface GameLoopListener extends Listener
{
	/**
	 * Called during the initalization phase of the loop, as it's preparing to run.
	 */
	void onInit();

	/**
	 * Called at the begining of the frame. Often used for actions such as polling
	 * player input.
	 */
	void onStartFrame();

	/**
	 * Called during each physics tick within the game loop. This may be called
	 * multiple times per frame, or may skip frames, depending on the
	 * synchronization between the rendering speed and the physics speed. All game
	 * logic should be executed in this function. This is called before Update on
	 * frames where both are called.
	 */
	void onPhysics();

	/**
	 * Called right before the rendering phase. This method is used to prepare the
	 * scene for being rendered.
	 */
	void onUpdate();

	/**
	 * Called to render the scene.
	 */
	void onRender();

	/**
	 * Called at the end of the frame. Often used for actions such as pushing the
	 * frame to the window.
	 */
	void onEndFrame();

	/**
	 * Called after the game loop is finished and is used to clean up any remaining
	 * resources, or preform addtional closing actions as needed.
	 */
	void onDispose();
}
