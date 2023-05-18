package net.jay.voxelgame.render;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Vertex {
    private Vector3f positions;
    private Vector2f uvs;

    public Vertex(Vector3f positions, Vector2f uvs) {
        this.positions = positions;
        this.uvs = uvs;
    }

    public Vector3f getPositions() {
        return positions;
    }

    public Vector2f getUvs() {
        return uvs;
    }
}
