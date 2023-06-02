package net.jay.voxelgame.render.gl.vertex;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class ColorVertex implements Vertex {
    private final Vector3f position;
    private final Vector4f color;

    public ColorVertex(Vector3f position, Vector4f color) {
        this.position = position;
        this.color = color;
    }


    @Override
    public Vector3f position() {
        return position;
    }

    @Override
    public Vector4f color() {
        return color;
    }

    @Override
    public Vector2f uv() {
        return null;
    }

    @Override
    public int stride() {
        return 7;
    }

    @Override
    public boolean hasColor() {
        return true;
    }

    @Override
    public boolean hasUV() {
        return false;
    }
}
