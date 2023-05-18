package net.jay.voxelgame.world;

import net.jay.voxelgame.render.Mesh;
import net.jay.voxelgame.render.ShaderProgram;
import net.jay.voxelgame.render.Vertex;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

public class Block {
    public static final Block AIR = new Block(BlockType.Air);

    private final BlockType block;

    public Block(BlockType block) {
        this.block = block;
    }

    public void render(Mesh mesh, ShaderProgram shaderProgram) {

    }

    public Mesh faceDataUp(int x, int y, int z, Mesh mesh) {
        // Counter-Clockwise
        mesh.addIndices(
                0, 1, 2,
                2, 3, 0
        );

        mesh.addVertex(new Vertex(new Vector3f(x - 0.5f, y + 0.5f, z + 0.5f), new Vector2f(0, 1))); // 0 - Front Left
        mesh.addVertex(new Vertex(new Vector3f(x + 0.5f, y + 0.5f, z + 0.5f), new Vector2f(1, 1))); // 1 - Front Right
        mesh.addVertex(new Vertex(new Vector3f(x + 0.5f, y + 0.5f, z - 0.5f), new Vector2f(1, 0))); // 2 - Back Right
        mesh.addVertex(new Vertex(new Vector3f(x - 0.5f, y + 0.5f, z - 0.5f), new Vector2f(0, 0))); // 3 - Back Left

        return mesh;
    }

    public Mesh faceDataDown(int x, int y, int z, Mesh mesh) {
        // Clockwise
        mesh.addIndices(
                0, 1, 2,
                2, 3, 0
        );

        mesh.addVertex(new Vertex(new Vector3f(x - 0.5f, y - 0.5f, z - 0.5f), new Vector2f(0, 0))); // 0 - Back Left
        mesh.addVertex(new Vertex(new Vector3f(x + 0.5f, y - 0.5f, z - 0.5f), new Vector2f(1, 0))); // 1 - Back Right
        mesh.addVertex(new Vertex(new Vector3f(x + 0.5f, y - 0.5f, z + 0.5f), new Vector2f(1, 1))); // 2 - Front Right
        mesh.addVertex(new Vertex(new Vector3f(x - 0.5f, y - 0.5f, z + 0.5f), new Vector2f(0, 1))); // 3 - Front Left

        return mesh;
    }

    public Mesh faceDataNorth(int x, int y, int z, Mesh mesh) {
        // Clockwise
        mesh.addIndices(
                0, 1, 2,
                2, 3, 0
        );

        mesh.addVertex(new Vertex(new Vector3f(x + 0.5f, y - 0.5f, z + 0.5f), new Vector2f(1, 0))); // 0 - Top Right
        mesh.addVertex(new Vertex(new Vector3f(x + 0.5f, y + 0.5f, z + 0.5f), new Vector2f(1, 1))); // 1 - Bottom Right
        mesh.addVertex(new Vertex(new Vector3f(x - 0.5f, y + 0.5f, z + 0.5f), new Vector2f(0, 1))); // 2 - Bottom Left
        mesh.addVertex(new Vertex(new Vector3f(x - 0.5f, y - 0.5f, z + 0.5f), new Vector2f(0, 0))); // 3 - Top Left

        return mesh;
    }

    public Mesh faceDataEast(int x, int y, int z, Mesh mesh) {
        // Counter-Clockwise?
        mesh.addIndices(
                0, 1, 2,
                2, 3, 0
        );

        mesh.addVertex(new Vertex(new Vector3f(x + 0.5f, y - 0.5f, z - 0.5f), new Vector2f(0, 1))); // 0 - Back Bottom
        mesh.addVertex(new Vertex(new Vector3f(x + 0.5f, y + 0.5f, z - 0.5f), new Vector2f(0, 0))); // 1 - Back Top
        mesh.addVertex(new Vertex(new Vector3f(x + 0.5f, y + 0.5f, z + 0.5f), new Vector2f(1, 0))); // 2 - Front Top
        mesh.addVertex(new Vertex(new Vector3f(x + 0.5f, y - 0.5f, z + 0.5f), new Vector2f(1, 1))); // 3 - Front Bottom

        return mesh;
    }

    public Mesh faceDataSouth(int x, int y, int z, Mesh mesh) {
        // Counter-Clockwise
        mesh.addIndices(
                0, 1, 2,
                2, 3, 0
        );

        mesh.addVertex(new Vertex(new Vector3f(x - 0.5f, y - 0.5f, z - 0.5f), new Vector2f(0, 0))); // 0 - Top Left
        mesh.addVertex(new Vertex(new Vector3f(x - 0.5f, y + 0.5f, z - 0.5f), new Vector2f(0, 1))); // 1 - Bottom Left
        mesh.addVertex(new Vertex(new Vector3f(x + 0.5f, y + 0.5f, z - 0.5f), new Vector2f(1, 1))); // 2 - Bottom Right
        mesh.addVertex(new Vertex(new Vector3f(x + 0.5f, y - 0.5f, z - 0.5f), new Vector2f(1, 0))); // 3 - Top Right

        return mesh;
    }

    public Mesh faceDataWest(int x, int y, int z, Mesh mesh) {
        mesh.addIndices(
                0, 1, 2,
                2, 3, 0
        );

        mesh.addVertex(new Vertex(new Vector3f(x - 0.5f, y - 0.5f, z + 0.5f), new Vector2f(1, 1))); // 0 - Front Bottom
        mesh.addVertex(new Vertex(new Vector3f(x - 0.5f, y + 0.5f, z + 0.5f), new Vector2f(1, 0))); // 1 - Front Top
        mesh.addVertex(new Vertex(new Vector3f(x - 0.5f, y + 0.5f, z - 0.5f), new Vector2f(0, 0))); // 2 - Back Top
        mesh.addVertex(new Vertex(new Vector3f(x - 0.5f, y - 0.5f, z - 0.5f), new Vector2f(0, 1))); // 3 - Back Bottom

        return mesh;
    }

    public void allFaceData(int x, int y, int z, Mesh mesh) {
        faceDataNorth(x, y, z, mesh);
        faceDataSouth(x, y, z, mesh);
        faceDataUp(x, y, z, mesh);
        faceDataDown(x, y, z, mesh);
        faceDataEast(x, y, z, mesh);
        faceDataWest(x, y, z, mesh);
    }

    public BlockType block() {
        return block;
    }
}
