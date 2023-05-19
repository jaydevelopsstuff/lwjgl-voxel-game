package net.jay.voxelgame.render.gl;

import org.joml.Vector2f;
import org.joml.Vector3f;

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
    public Vector2f uv() {
        return uv;
    }

    @Override
    public int stride() {
        return 5;
    }
}
