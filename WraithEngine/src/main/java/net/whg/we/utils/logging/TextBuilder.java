package net.whg.we.utils.logging;

import net.whg.we.rendering.Graphics;
import net.whg.we.rendering.Material;
import net.whg.we.rendering.Mesh;
import net.whg.we.resources.ResourceDatabase;
import net.whg.we.resources.graphics.MeshResource;
import net.whg.we.ui.UIImage;
import net.whg.we.ui.UIUtils;
import net.whg.we.ui.font.Font;

public class TextBuilder
{
	private Material _material;
	private Font _font;
	private Graphics _graphics;
	private ResourceDatabase _database;

	public TextBuilder(Material material, Font font, Graphics graphics, ResourceDatabase database)
	{
		_material = material;
		_font = font;
		_graphics = graphics;
		_database = database;
	}

	public Material getMaterial()
	{
		return _material;
	}

	public Font getFont()
	{
		return _font;
	}

	public UIImage buildTextImage(String text, float size)
	{
		Mesh fontMesh = new Mesh("Mesh", UIUtils.textVertexData(_font, text), _graphics);
		_database.addResource(new MeshResource(null, null, fontMesh));

		UIImage textImage = new UIImage(fontMesh, _material);
		textImage.getPosition().set(0f, 50f);
		textImage.getSize().set(size, size);

		return textImage;
	}
}
