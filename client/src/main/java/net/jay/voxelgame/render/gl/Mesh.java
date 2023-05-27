package net.jay.voxelgame.render.gl;

import net.jay.voxelgame.render.gl.vertex.Vertex;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Mesh<T extends Vertex> {
    private final List<T> vertices;
    private final List<Integer> indices;
    private final boolean useEbo;

    private int vao;
    private int vbo;
    private int ebo;

    public Mesh(boolean useEbo) {
        this.vertices = new ArrayList<>();
        this.indices = new ArrayList<>();
        this.useEbo = useEbo;
    }

    public void addVertex(T vertex) {
        vertices.add(vertex);
    }

    public void addVertices(T... vertices) {
        this.vertices.addAll(Arrays.asList(vertices));
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
        int[] rawIndices = indices.stream().mapToInt(Integer::intValue).toArray();

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, rawVertices, GL_STATIC_DRAW);

        if(useEbo) {
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, rawIndices, GL_STATIC_DRAW);
        }

        glVertexAttribPointer(0, 3, GL_FLOAT, false, vertices.get(0).stride() * 4, 0);
        glEnableVertexAttribArray(0);
        if(vertices.get(0).stride() <= 3)
            return;
        glVertexAttribPointer(1, 2, GL_FLOAT, false, vertices.get(0).stride() * 4, 3 * 4);
        glEnableVertexAttribArray(1);
    }

    public void bindVAO() {
        glBindVertexArray(vao);
    }

    private float[] toRawVertices() {
        float[] rawVertices = new float[vertices.size() * vertices.get(0).stride()];
        for(int i = 0; i < vertices.size(); i++) {
            T vertex = vertices.get(i);
            Vector3f position = vertex.position();
            Vector2f uv = vertex.uv();
            int rawIndex = i * vertex.stride();

            rawVertices[rawIndex] = position.x;
            rawVertices[rawIndex + 1] = position.y;
            rawVertices[rawIndex + 2] = position.z;
            if(uv != null && vertex.stride() >= 5) {
                rawVertices[rawIndex + 3] = uv.x;
                rawVertices[rawIndex + 4] = uv.y;
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
