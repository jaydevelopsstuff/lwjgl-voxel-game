package net.jay.voxelgame.render.gl.vertex;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class PositionVertex implements Vertex {
    private final Vector3f position;

    public PositionVertex(Vector3f position) {
        this.position = position;
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
        return null;
    }

    @Override
    public int stride() {
        return 3;
    }

    @Override
    public boolean hasColor() {
        return false;
    }

    @Override
    public boolean hasUV() {
        return false;
    }
}
