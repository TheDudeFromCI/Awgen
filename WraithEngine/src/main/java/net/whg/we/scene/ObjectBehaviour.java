package net.whg.we.scene;

public interface ObjectBehaviour
{
	public void init(GameObject owner);

	public GameObject getOwner();

	public void dispose();
}
