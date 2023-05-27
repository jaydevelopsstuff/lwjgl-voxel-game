package net.jay.voxelgame.render.gl.vertex;

import org.joml.Vector2f;
import org.joml.Vector3f;

public interface Vertex {
    Vector3f position();
    Vector2f uv();

    int stride();
}
