package net.jay.voxelgame.render.gl;

import net.jay.voxelgame.render.gl.vertex.Vertex;
import net.jay.voxelgame.util.DLList;
import net.jay.voxelgame.util.MyArrayList;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Mesh<T extends Vertex> {
    private final MyArrayList<T> vertices;
    private final MyArrayList<Integer> indices;
    private final boolean useEbo;

    private int vao;
    private int vbo;
    private int ebo;

    public Mesh(boolean useEbo) {
        this.vertices = new MyArrayList<>(4096, 512);
        this.indices = new MyArrayList<>(4096, 512);
        this.useEbo = useEbo;
    }

    public void addVertex(T vertex) {
        vertices.add(vertex);
    }

    public void addVertices(T... vertices) {
        for(T vertex : vertices) {
            this.vertices.add(vertex);
        }
    }

    public void addIndices(int... indices) {
        int originalSize = vertices.size();
        for(int index : indices) {
            this.indices.add(originalSize + index);
        }
    }

    public void init() {
        vao = glGenVertexArrays();
        vbo = glGenBuffers();
        if(useEbo) ebo = glGenBuffers();
        glBindVertexArray(vao);

        float[] rawVertices = toRawVertices();
        int[] rawIndices = new int[indices.size()];
        int i = 0;
        for(Integer index : indices) {
            rawIndices[i] = index;
            i++;
        }

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, rawVertices, GL_STATIC_DRAW);

        if(useEbo) {
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, rawIndices, GL_STATIC_DRAW);
        }

        glVertexAttribPointer(0, 3, GL_FLOAT, false, vertices.get(0).stride() * 4, 0);
        glEnableVertexAttribArray(0);

        boolean hasUV = vertices.get(0).hasUV();
        boolean hasColor = vertices.get(0).hasColor();
        int uvIndex = -1;
        int colorIndex = -1;
        if(hasUV && hasColor) {
            uvIndex = 1;
            colorIndex = 2;
        } else if(hasUV) {
            uvIndex = 1;
        } else {
            colorIndex = 1;
        }

        if(hasUV) {
            glVertexAttribPointer(uvIndex, 2, GL_FLOAT, false, vertices.get(0).stride() * 4, 3 * 4);
            glEnableVertexAttribArray(uvIndex);
        }
        if(hasColor) {
            glVertexAttribPointer(colorIndex, 4, GL_FLOAT, false, vertices.get(0).stride() * 4, hasUV ? 5 * 4 : 3 * 4);
            glEnableVertexAttribArray(colorIndex);
        }
    }

    public void bindVAO() {
        glBindVertexArray(vao);
    }

    private float[] toRawVertices() {
        boolean hasUV = vertices.get(0).hasUV();
        boolean hasColor = vertices.get(0).hasColor();
        float[] rawVertices = new float[vertices.size() * vertices.get(0).stride()];
        for(int i = 0; i < vertices.size(); i++) {
            T vertex = vertices.get(i);
            Vector3f position = vertex.position();
            Vector2f uv = vertex.uv();
            Vector4f color = vertex.color();
            int rawIndex = i * vertex.stride();

            rawVertices[rawIndex] = position.x;
            rawVertices[rawIndex + 1] = position.y;
            rawVertices[rawIndex + 2] = position.z;
            if(hasUV && hasColor) {
                rawVertices[rawIndex + 3] = uv.x;
                rawVertices[rawIndex + 4] = uv.y;
                rawVertices[rawIndex + 5] = color.x;
                rawVertices[rawIndex + 6] = color.y;
                rawVertices[rawIndex + 7] = color.z;
                rawVertices[rawIndex + 8] = color.w;
            } else if(hasUV) {
                rawVertices[rawIndex + 3] = uv.x;
                rawVertices[rawIndex + 4] = uv.y;
            } else if(hasColor) {
                rawVertices[rawIndex + 3] = color.x;
                rawVertices[rawIndex + 4] = color.y;
                rawVertices[rawIndex + 5] = color.z;
                rawVertices[rawIndex + 6] = color.w;
            }
        }
        return rawVertices;
    }

    public void render() {
        if(useEbo)
            glDrawElements(GL_TRIANGLES, indices.size(), GL_UNSIGNED_INT, 0);
        else glDrawArrays(GL_TRIANGLES, 0, vertices.size());
    }
}
