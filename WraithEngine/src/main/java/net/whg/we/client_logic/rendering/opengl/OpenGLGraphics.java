package net.whg.we.client_logic.rendering.opengl;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL45;
import net.whg.frameworks.logging.Log;
import net.whg.we.client_logic.rendering.Graphics;
import net.whg.we.client_logic.rendering.ScreenClearType;
import net.whg.we.client_logic.rendering.VMesh;
import net.whg.we.client_logic.rendering.VShader;
import net.whg.we.client_logic.rendering.VTexture;
import net.whg.we.client_logic.rendering.VertexData;
import net.whg.we.legacy.Color;
import net.whg.we.resource.TextureProperties;

public class OpenGLGraphics implements Graphics
{
    public static final int TEXTURE_SLOTS = 8;

    private int _boundShaderId;
    private int[] _boundTextureIds = new int[TEXTURE_SLOTS];

    @Override
    public void init()
    {
        GL.createCapabilities();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        checkForErrors("Init OpenGL");
    }

    @Override
    public VMesh prepareMesh(VertexData vertexData)
    {
        return new GLVMesh(this, vertexData);
    }

    @Override
    public void clearScreenPass(ScreenClearType screenClear)
    {
        switch (screenClear)
        {
            case CLEAR_COLOR:
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
                return;
            case CLEAR_DEPTH:
                GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
                return;
            case CLEAR_COLOR_AND_DEPTH:
                GL11.glClear(
                        GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
                return;
            default:
                throw new IllegalArgumentException(
                        "Unknown screen clear operation!");
        }
    }

    @Override
    public void setClearScreenColor(Color color)
    {
        GL11.glClearColor(color.r, color.g, color.b, color.a);
    }

    @Override
    public void dispose()
    {
        Log.debug("Disposing OpenGL.");
        GL.setCapabilities(null);
    }

    @Override
    public VTexture prepareTexture(TextureProperties properties)
    {
        return new GLVTexture(this, properties);
    }

    public void checkForErrors(String state)
    {
        checkForErrors(Log.DEBUG, state);
    }

    public void checkForErrors(int logLevel, String state)
    {
        if (Log.getLogLevel() > logLevel)
            return;

        int error;
        while ((error = GL11.glGetError()) != GL11.GL_NO_ERROR)
        {
            String errorName;

            switch (error)
            {
                case GL11.GL_INVALID_ENUM:
                    errorName = "Invalid Enum";
                    break;
                case GL11.GL_INVALID_VALUE:
                    errorName = "Invalid Value";
                    break;
                case GL11.GL_INVALID_OPERATION:
                    errorName = "Invalid Operation";
                    break;
                case GL11.GL_STACK_OVERFLOW:
                    errorName = "Stack Overflow";
                    break;
                case GL11.GL_STACK_UNDERFLOW:
                    errorName = "Stack Underflow";
                    break;
                case GL11.GL_OUT_OF_MEMORY:
                    errorName = "Out of Memory";
                    break;
                case GL30.GL_INVALID_FRAMEBUFFER_OPERATION:
                    errorName = "Invalid Framebuffer Operation";
                    break;
                case GL45.GL_CONTEXT_LOST:
                    errorName = "Context Lost";
                    break;
                default:
                    errorName = "Unknown Error";
                    break;
            }

            Log.errorf("OpenGL Error Detected! %s (%s)", errorName, state);
        }
    }

    int getBoundShaderId()
    {
        return _boundShaderId;
    }

    void setBoundShaderId(int boundShaderId)
    {
        _boundShaderId = boundShaderId;
    }

    int getBoundTextureId(int slot)
    {
        return _boundTextureIds[slot];
    }

    void setBoundTextureId(int slot, int boundTextureId)
    {
        _boundTextureIds[slot] = boundTextureId;
    }

    @Override
    public VShader prepareShader(String vert, String geo, String frag)
    {
        return new GLVShader(this, vert, geo, frag);
    }
}
