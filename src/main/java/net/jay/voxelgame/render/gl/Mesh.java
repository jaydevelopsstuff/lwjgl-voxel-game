package net.jay.voxelgame.render.gl;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Mesh {
    private List<Vertex> vertices;
    private List<Integer> indices;

    private int vao;
    private int vbo;
    private int ebo;

    public Mesh() {
        this.vertices = new ArrayList<>();
        this.indices = new ArrayList<>();
    }

    public void addVertex(Vertex vertex) {
        vertices.add(vertex);
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
        ebo = glGenBuffers();
        glBindVertexArray(vao);

        float[] rawVertices = toRawVertices();
        int[] rawIndices = indices.stream().mapToInt(Integer::intValue).toArray();

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, rawVertices, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, rawIndices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * 4, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * 4, 3 * 4);
        glEnableVertexAttribArray(1);
    }

    public void bindVAO() {
        glBindVertexArray(vao);
    }

    private float[] toRawVertices() {
        float[] rawVertices = new float[vertices.size() * 5];
        for(int i = 0; i < vertices.size(); i++) {
            int rawIndex = i * 5;
            Vertex vertex = vertices.get(i);
            Vector3f positions = vertex.getPositions();
            Vector2f uvs = vertex.getUvs();
            rawVertices[rawIndex] = positions.x;
            rawVertices[rawIndex + 1] = positions.y;
            rawVertices[rawIndex + 2] = positions.z;
            rawVertices[rawIndex + 3] = uvs.x;
            rawVertices[rawIndex + 4] = uvs.y;
        }
        return rawVertices;
    }

    public void render() {
        glDrawElements(GL_TRIANGLES, indices.size(), GL_UNSIGNED_INT, 0);
    }
}
