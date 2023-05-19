package net.jay.voxelgame.world.block;

import net.jay.voxelgame.render.gl.Mesh;
import net.jay.voxelgame.render.gl.ShaderProgram;
import net.jay.voxelgame.render.gl.Vertex;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

public class Block {
    private final Type type;

    private final Vector2f[] texCoordsUp;
    private final Vector2f[] texCoordsDown;
    private final Vector2f[] texCoordsNorth;
    private final Vector2f[] texCoordsEast;
    private final Vector2f[] texCoordsSouth;
    private final Vector2f[] texCoordsWest;

    public Block(Type type, float xStart, float xEnd, float yStart, float yEnd) {
        this.type = type;
        this.texCoordsUp = new Vector2f[] {
                new Vector2f(xStart, yEnd), new Vector2f(xEnd, yEnd),
                new Vector2f(xEnd, yStart), new Vector2f(xStart, yStart)
        };
        this.texCoordsDown = new Vector2f[] {
                new Vector2f(xStart, yStart), new Vector2f(xEnd, yStart),
                new Vector2f(xEnd, yEnd), new Vector2f(xStart, yEnd)
        };
        this.texCoordsNorth = new Vector2f[] {
                new Vector2f(xEnd, yStart), new Vector2f(xEnd, yEnd),
                new Vector2f(xStart, yEnd), new Vector2f(xStart, yStart)
        };
        this.texCoordsEast = new Vector2f[] {
                new Vector2f(xStart, yEnd), new Vector2f(xStart, yStart),
                new Vector2f(xEnd, yStart), new Vector2f(xEnd, yEnd)
        };
        this.texCoordsSouth = new Vector2f[] {
                new Vector2f(xStart, yStart), new Vector2f(xStart, yEnd),
                new Vector2f(xEnd, yEnd), new Vector2f(xEnd, yStart)
        };
        this.texCoordsWest = new Vector2f[] {
                new Vector2f(xEnd, yEnd), new Vector2f(xEnd, yStart),
                new Vector2f(xStart, yStart), new Vector2f(xStart, yEnd)
        };
    }

    public Block(Type type, Vector2f[] texCoordsUp, Vector2f[] texCoordsDown, Vector2f[] texCoordsNorth, Vector2f[] texCoordsEast, Vector2f[] texCoordsSouth, Vector2f[] texCoordsWest) {
        this.type = type;
        this.texCoordsUp = texCoordsUp;
        this.texCoordsDown = texCoordsDown;
        this.texCoordsNorth = texCoordsNorth;
        this.texCoordsEast = texCoordsEast;
        this.texCoordsSouth = texCoordsSouth;
        this.texCoordsWest = texCoordsWest;
    }

    public void render(Mesh mesh, ShaderProgram shaderProgram) {

    }

    public Mesh faceDataUp(int x, int y, int z, Mesh mesh) {
        // Counter-Clockwise
        mesh.addIndices(
                0, 1, 2,
                2, 3, 0
        );

        mesh.addVertex(new Vertex(new Vector3f(x - 0.5f, y + 0.5f, z + 0.5f), texCoordsUp[0])); // 0 - Front Left (TC - 0, 1)
        mesh.addVertex(new Vertex(new Vector3f(x + 0.5f, y + 0.5f, z + 0.5f), texCoordsUp[1])); // 1 - Front Right (TC - 1, 1)
        mesh.addVertex(new Vertex(new Vector3f(x + 0.5f, y + 0.5f, z - 0.5f), texCoordsUp[2])); // 2 - Back Right (TC - 1, 0)
        mesh.addVertex(new Vertex(new Vector3f(x - 0.5f, y + 0.5f, z - 0.5f), texCoordsUp[3])); // 3 - Back Left (TC - 0, 0)

        return mesh;
    }

    public Mesh faceDataDown(int x, int y, int z, Mesh mesh) {
        // Clockwise
        mesh.addIndices(
                0, 1, 2,
                2, 3, 0
        );

        mesh.addVertex(new Vertex(new Vector3f(x - 0.5f, y - 0.5f, z - 0.5f), texCoordsDown[0])); // 0 - Back Left (TC - 0, 0)
        mesh.addVertex(new Vertex(new Vector3f(x + 0.5f, y - 0.5f, z - 0.5f), texCoordsDown[1])); // 1 - Back Right (TC - 1, 0)
        mesh.addVertex(new Vertex(new Vector3f(x + 0.5f, y - 0.5f, z + 0.5f), texCoordsDown[2])); // 2 - Front Right (TC - 1, 1)
        mesh.addVertex(new Vertex(new Vector3f(x - 0.5f, y - 0.5f, z + 0.5f), texCoordsDown[3])); // 3 - Front Left (TC - 0, 1)

        return mesh;
    }

    public Mesh faceDataNorth(int x, int y, int z, Mesh mesh) {
        // Clockwise
        mesh.addIndices(
                0, 1, 2,
                2, 3, 0
        );

        mesh.addVertex(new Vertex(new Vector3f(x + 0.5f, y - 0.5f, z + 0.5f), texCoordsNorth[0])); // 0 - Top Right (TC - 1, 0)
        mesh.addVertex(new Vertex(new Vector3f(x + 0.5f, y + 0.5f, z + 0.5f), texCoordsNorth[1])); // 1 - Bottom Right (TC - 1, 1)
        mesh.addVertex(new Vertex(new Vector3f(x - 0.5f, y + 0.5f, z + 0.5f), texCoordsNorth[2])); // 2 - Bottom Left (TC - 0, 1)
        mesh.addVertex(new Vertex(new Vector3f(x - 0.5f, y - 0.5f, z + 0.5f), texCoordsNorth[3])); // 3 - Top Left (TC - 0, 0)

        return mesh;
    }

    public Mesh faceDataEast(int x, int y, int z, Mesh mesh) {
        // Counter-Clockwise?
        mesh.addIndices(
                0, 1, 2,
                2, 3, 0
        );

        mesh.addVertex(new Vertex(new Vector3f(x + 0.5f, y - 0.5f, z - 0.5f), texCoordsEast[0])); // 0 - Back Bottom (TC - 0, 1)
        mesh.addVertex(new Vertex(new Vector3f(x + 0.5f, y + 0.5f, z - 0.5f), texCoordsEast[1])); // 1 - Back Top (TC - 0, 0)
        mesh.addVertex(new Vertex(new Vector3f(x + 0.5f, y + 0.5f, z + 0.5f), texCoordsEast[2])); // 2 - Front Top (TC - 1, 0)
        mesh.addVertex(new Vertex(new Vector3f(x + 0.5f, y - 0.5f, z + 0.5f), texCoordsEast[3])); // 3 - Front Bottom (TC - 1, 1)

        return mesh;
    }

    public Mesh faceDataSouth(int x, int y, int z, Mesh mesh) {
        // Counter-Clockwise
        mesh.addIndices(
                0, 1, 2,
                2, 3, 0
        );

        mesh.addVertex(new Vertex(new Vector3f(x - 0.5f, y - 0.5f, z - 0.5f), texCoordsSouth[0])); // 0 - Top Left (TC - 0, 0)
        mesh.addVertex(new Vertex(new Vector3f(x - 0.5f, y + 0.5f, z - 0.5f), texCoordsSouth[1])); // 1 - Bottom Left (TC - 0, 1)
        mesh.addVertex(new Vertex(new Vector3f(x + 0.5f, y + 0.5f, z - 0.5f), texCoordsSouth[2])); // 2 - Bottom Right (TC - 1, 1)
        mesh.addVertex(new Vertex(new Vector3f(x + 0.5f, y - 0.5f, z - 0.5f), texCoordsSouth[3])); // 3 - Top Right (TC - 1, 0)

        return mesh;
    }

    public Mesh faceDataWest(int x, int y, int z, Mesh mesh) {
        mesh.addIndices(
                0, 1, 2,
                2, 3, 0
        );

        mesh.addVertex(new Vertex(new Vector3f(x - 0.5f, y - 0.5f, z + 0.5f), texCoordsWest[0])); // 0 - Front Bottom (TC - 1, 1)
        mesh.addVertex(new Vertex(new Vector3f(x - 0.5f, y + 0.5f, z + 0.5f), texCoordsWest[1])); // 1 - Front Top (TC - 1, 0)
        mesh.addVertex(new Vertex(new Vector3f(x - 0.5f, y + 0.5f, z - 0.5f), texCoordsWest[2])); // 2 - Back Top (TC - 0, 0)
        mesh.addVertex(new Vertex(new Vector3f(x - 0.5f, y - 0.5f, z - 0.5f), texCoordsWest[3])); // 3 - Back Bottom (TC - 0, 1)

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

    public Type type() {
        return type;
    }

    public enum Type {
        Air,
        Dirt,
        Grass,
        Stone
    }
}
