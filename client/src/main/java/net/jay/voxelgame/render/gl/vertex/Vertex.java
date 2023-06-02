package net.jay.voxelgame.render.gl.vertex;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public interface Vertex {
    Vector3f position();
    Vector4f color();
    Vector2f uv();

    int stride();
    boolean hasColor();
    boolean hasUV();
}
