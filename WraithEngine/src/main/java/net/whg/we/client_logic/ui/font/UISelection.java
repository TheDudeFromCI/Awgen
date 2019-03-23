package net.whg.we.client_logic.ui.font;

import java.util.LinkedList;
import net.whg.we.client_logic.rendering.Material;
import net.whg.we.client_logic.rendering.Mesh;
import net.whg.we.client_logic.ui.UIImage;

public class UISelection implements TextSelection
{
    private int _selStart = -1;
    private int _selOrigin = -1;
    private int _selEnd = -1;
    private Mesh _mesh;
    private Material _material;
    private UIString _uiString;
    private LinkedList<UIImage> _imagePool = new LinkedList<>();
    private LinkedList<UIImage> _render = new LinkedList<>();

    public UISelection(Mesh mesh, Material material, UIString uiString)
    {
        _mesh = mesh;
        _material = material;
        _uiString = uiString;
    }

    @Override
    public int selStart()
    {
        return _selStart;
    }

    @Override
    public int selOrigin()
    {
        return _selOrigin;
    }

    @Override
    public int selEnd()
    {
        return _selEnd;
    }

    @Override
    public void setSelection(int start, int origin, int end)
    {
        if (_selStart == start && _selOrigin == origin && _selEnd == end)
            return;

        _selStart = start;
        _selOrigin = origin;
        _selEnd = end;

        updateSelectionPos();
    }

    private void clearImages()
    {
        _imagePool.addAll(_render);
        _render.clear();
    }

    private UIImage newImage()
    {
        if (_imagePool.isEmpty())
        {
            UIImage img = new UIImage(_mesh, _material);
            img.getTransform().setParent(_uiString.getTransform());
            _render.add(img);
            return img;
        }

        UIImage img = _imagePool.removeFirst();
        _render.add(img);
        return img;
    }

    public void updateSelectionPos()
    {
        clearImages();

        float x = 0;
        float sx1 = 0f;
        float sx2 = 0f;
        float sy = 0.5f;

        UIImage current = null;
        for (int i = 0; i < _uiString.getText().length(); i++)
        {
            if (i >= _selEnd)
            {
                if (current != null)
                {
                    current.getTransform().setSize(sx2 - sx1, 1f);
                    current.getTransform().setPosition((sx1 + sx2) / 2f, sy);
                    current = null;
                }
                break;
            }

            char c = _uiString.getText().charAt(i);
            if (c == '\n')
            {
                if (current != null)
                {
                    current.getTransform().setSize(sx2 - sx1, 1f);
                    current.getTransform().setPosition((sx1 + sx2) / 2f, sy);
                    current = null;
                }

                x = 0f;
                sy -= 1f;
                continue;
            }

            Glyph g = _uiString.getFont().getGlyph(c);
            if (g == null)
                continue;

            if (i >= _selStart && current == null)
            {
                current = newImage();
                sx1 = x;
            }

            x += g.getWidth();
            sx2 = x;
        }

        if (current != null)
        {
            current.getTransform().setSize(sx2 - sx1, 1f);
            current.getTransform().setPosition((sx1 + sx2) / 2f, sy);
        }
    }

    public void render()
    {
        for (UIImage image : _render)
            image.render();
    }

    @Override
    public boolean hasSelection()
    {
        return _selStart != -1;
    }

    @Override
    public void clearSelection()
    {
        setSelection(-1, -1, -1);
    }
}
