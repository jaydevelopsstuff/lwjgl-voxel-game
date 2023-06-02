package net.jay.voxelgame.render.gl.vertex;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class TextureVertex implements Vertex {
    private final Vector3f position;
    private final Vector2f uv;

    public TextureVertex(Vector3f position, Vector2f uv) {
        this.position = position;
        this.uv = uv;
    }

    @Override
    public Vector3f position() {
        return position;
    }

    @Override
    public Vector4f color() {
        return null;
    }

    @Override
    public Vector2f uv() {
        return uv;
    }

    @Override
    public int stride() {
        return 5;
    }

    @Override
    public boolean hasColor() {
        return false;
    }

    @Override
    public boolean hasUV() {
        return true;
    }
}
