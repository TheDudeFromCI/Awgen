package net.whg.we.ui;

import org.joml.Vector2f;
import org.joml.Vector4f;
import net.whg.we.client_logic.rendering.ShaderAttributes;
import net.whg.we.client_logic.rendering.VertexData;
import net.whg.we.ui.font.Font;
import net.whg.we.ui.font.Glyph;

public class UIUtils
{
    public static VertexData defaultImageVertexData()
    {
        ShaderAttributes att = new ShaderAttributes();
        att.addAttribute("pos", 2);
        att.addAttribute("uv", 2);

        int v = 0;
        float[] vertices = new float[4 * att.getVertexSize()];

        // bottom left
        vertices[v++] = -0.5f;
        vertices[v++] = -0.5f;
        vertices[v++] = 0f;
        vertices[v++] = 1f;

        // bottom right
        vertices[v++] = 0.5f;
        vertices[v++] = -0.5f;
        vertices[v++] = 1f;
        vertices[v++] = 1f;

        // top left
        vertices[v++] = -0.5f;
        vertices[v++] = 0.5f;
        vertices[v++] = 0f;
        vertices[v++] = 0f;

        // top right
        vertices[v++] = 0.5f;
        vertices[v++] = 0.5f;
        vertices[v++] = 1f;
        vertices[v++] = 0f;

        int t = 0;
        short[] tris = new short[6];

        tris[t++] = 0;
        tris[t++] = 1;
        tris[t++] = 3;
        tris[t++] = 0;
        tris[t++] = 3;
        tris[t++] = 2;

        return new VertexData(vertices, tris, att);
    }

    public static VertexData textVertexData(Font font, String text)
    {
        ShaderAttributes att = new ShaderAttributes();
        att.addAttribute("pos", 2);
        att.addAttribute("uv", 2);

        float[] vertices = new float[4 * att.getVertexSize() * text.length()];
        short[] tris = new short[6 * text.length()];

        int v = 0;
        int t = 0;
        int c = 0;

        float posX = 0f;
        float posY = 0f;

        for (int i = 0; i < text.length(); i++)
        {
            if (text.charAt(i) == '\n')
            {
                posX = 0f;
                posY -= 1f;
                continue;
            }

            Glyph g = font.getGlyph(text.charAt(i));
            if (g == null)
            {
                v += 4 * att.getVertexSize();
                t += 6;
                continue;
            }

            Vector4f pos = g.getPosition();
            Vector2f size = g.getSize();
            Vector2f off = g.getOffset();

            // bottom left
            vertices[v++] = posX + off.x;
            vertices[v++] = posY + off.y;
            vertices[v++] = pos.x;
            vertices[v++] = pos.y + pos.w;

            // bottom right
            vertices[v++] = posX + size.x + off.x;
            vertices[v++] = posY + off.y;
            vertices[v++] = pos.x + pos.z;
            vertices[v++] = pos.y + pos.w;

            // top left
            vertices[v++] = posX + off.x;
            vertices[v++] = posY + size.y + off.y;
            vertices[v++] = pos.x;
            vertices[v++] = pos.y;

            // top right
            vertices[v++] = posX + size.x + off.x;
            vertices[v++] = posY + size.y + off.y;
            vertices[v++] = pos.x + pos.z;
            vertices[v++] = pos.y;

            tris[t++] = (short) (0 + c);
            tris[t++] = (short) (1 + c);
            tris[t++] = (short) (3 + c);
            tris[t++] = (short) (0 + c);
            tris[t++] = (short) (3 + c);
            tris[t++] = (short) (2 + c);

            posX += g.getWidth();
            c += 4;
        }

        return new VertexData(vertices, tris, att);
    }
}
