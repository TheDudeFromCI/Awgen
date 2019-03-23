package net.whg.we.client_logic.rendering;

import net.whg.we.utils.Color;

public interface Graphics
{
    public void init();

    public VMesh prepareMesh(VertexData vertexData);

    public VTexture prepareTexture(TextureProperties properties);

    public VShader prepareShader(String vert, String geo, String frag);

    public void clearScreenPass(ScreenClearType screenClear);

    public void setClearScreenColor(Color color);

    public void dispose();
}
