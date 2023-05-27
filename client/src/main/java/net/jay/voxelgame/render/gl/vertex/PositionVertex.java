package net.jay.voxelgame.render.gl.vertex;

import org.joml.Vector2f;
import org.joml.Vector3f;

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
    public Vector2f uv() {
        return null;
    }

    @Override
    public int stride() {
        return 3;
    }
}
