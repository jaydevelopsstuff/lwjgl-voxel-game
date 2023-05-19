package net.jay.voxelgame.render;

import static net.jay.voxelgame.Game.*;
import net.jay.voxelgame.render.gl.Mesh;
import net.jay.voxelgame.render.gl.ShaderProgram;
import net.jay.voxelgame.render.texture.Texture;
import org.lwjgl.opengl.GL;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {
    private Mesh blockMesh;
    private boolean meshUpdate;

    private ShaderProgram shaderProgram;
    private Texture blockAtlas;

    public void init() {
        GL.createCapabilities();

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);

        shaderProgram = initShaderProgram();
        try {
            blockAtlas = Texture.loadNewTexture("assets/atlas.png");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        blockAtlas.bind();

        blockMesh = world().generateMesh();
        blockMesh.init();
    }

    public void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

        if(meshUpdate) {
            blockMesh = new Mesh();
            blockMesh = world().generateMesh();
            blockMesh.init();
            meshUpdate = false;
        }
        shaderProgram.bind();
        player().camera().updateProjectionMatrix(shaderProgram);

        blockMesh.render();
    }

    private static ShaderProgram initShaderProgram() {
        ShaderProgram shaderProgram = new ShaderProgram();
        try {
            shaderProgram.createVertexShader("shaders/shader.vert");
            shaderProgram.createFragmentShader("shaders/shader.frag");
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        shaderProgram.link();
        return shaderProgram;
    }

    public void queueMeshUpdate() {
        meshUpdate = true;
    }
}
