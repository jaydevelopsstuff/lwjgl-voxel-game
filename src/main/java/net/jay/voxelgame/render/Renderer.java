package net.jay.voxelgame.render;

import static net.jay.voxelgame.Game.*;
import net.jay.voxelgame.render.gl.Mesh;
import net.jay.voxelgame.render.gl.ShaderProgram;
import net.jay.voxelgame.render.gl.vertex.PositionVertex;
import net.jay.voxelgame.render.gl.vertex.TextureVertex;
import net.jay.voxelgame.render.texture.Cubemap;
import net.jay.voxelgame.render.texture.Texture;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30C.glGenVertexArrays;

public class Renderer {
    private Mesh<TextureVertex> blockMesh;
    private boolean meshUpdate;
    private Mesh<PositionVertex> skyboxMesh;

    private ShaderProgram blockShaderProgram;
    private ShaderProgram skyboxShaderProgram;


    private Texture blockAtlas;
    private Cubemap skyboxTexture;

    private int skyboxVAO;
    private int skyboxVBO;

    public void init() {
        GL.createCapabilities();

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);

        skyboxShaderProgram = initSkyboxShaderProgram();
        try {
            skyboxTexture = Cubemap.loadNewCubemap(new String[] {
                    "assets/textures/skybox/right.jpg",
                    "assets/textures/skybox/left.jpg",
                    "assets/textures/skybox/top.jpg",
                    "assets/textures/skybox/bottom.jpg",
                    "assets/textures/skybox/front.jpg",
                    "assets/textures/skybox/back.jpg"
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        skyboxMesh = new Mesh<>(false);
        skyboxMesh.addVertices(
                new PositionVertex(new Vector3f(-1.0f,  1.0f, -1.0f)),
                new PositionVertex(new Vector3f(-1.0f, -1.0f, -1.0f)),
                new PositionVertex(new Vector3f(1.0f, -1.0f, -1.0f)),
                new PositionVertex(new Vector3f(1.0f, -1.0f, -1.0f)),
                new PositionVertex(new Vector3f(1.0f,  1.0f, -1.0f)),
                new PositionVertex(new Vector3f(-1.0f,  1.0f, -1.0f)),

                new PositionVertex(new Vector3f(-1.0f, -1.0f,  1.0f)),
                new PositionVertex(new Vector3f(-1.0f, -1.0f, -1.0f)),
                new PositionVertex(new Vector3f(-1.0f,  1.0f, -1.0f)),
                new PositionVertex(new Vector3f(-1.0f,  1.0f, -1.0f)),
                new PositionVertex(new Vector3f(-1.0f,  1.0f,  1.0f)),
                new PositionVertex(new Vector3f(-1.0f, -1.0f,  1.0f)),

                new PositionVertex(new Vector3f(1.0f, -1.0f, -1.0f)),
                new PositionVertex(new Vector3f(1.0f, -1.0f,  1.0f)),
                new PositionVertex(new Vector3f(1.0f,  1.0f,  1.0f)),
                new PositionVertex(new Vector3f(1.0f,  1.0f,  1.0f)),
                new PositionVertex(new Vector3f(1.0f,  1.0f, -1.0f)),
                new PositionVertex(new Vector3f(1.0f, -1.0f, -1.0f)),

                new PositionVertex(new Vector3f(-1.0f, -1.0f,  1.0f)),
                new PositionVertex(new Vector3f(-1.0f,  1.0f,  1.0f)),
                new PositionVertex(new Vector3f(1.0f,  1.0f,  1.0f)),
                new PositionVertex(new Vector3f(1.0f,  1.0f,  1.0f)),
                new PositionVertex(new Vector3f(1.0f, -1.0f,  1.0f)),
                new PositionVertex(new Vector3f(-1.0f, -1.0f,  1.0f)),

                new PositionVertex(new Vector3f(-1.0f,  1.0f, -1.0f)),
                new PositionVertex(new Vector3f(1.0f,  1.0f, -1.0f)),
                new PositionVertex(new Vector3f(1.0f,  1.0f,  1.0f)),
                new PositionVertex(new Vector3f(1.0f,  1.0f,  1.0f)),
                new PositionVertex(new Vector3f(-1.0f,  1.0f,  1.0f)),
                new PositionVertex(new Vector3f(-1.0f,  1.0f, -1.0f)),

                new PositionVertex(new Vector3f(-1.0f, -1.0f, -1.0f)),
                new PositionVertex(new Vector3f(-1.0f, -1.0f,  1.0f)),
                new PositionVertex(new Vector3f(1.0f, -1.0f, -1.0f)),
                new PositionVertex(new Vector3f(1.0f, -1.0f, -1.0f)),
                new PositionVertex(new Vector3f(-1.0f, -1.0f,  1.0f)),
                new PositionVertex(new Vector3f(1.0f, -1.0f,  1.0f))
        );
        skyboxMesh.init();

        blockShaderProgram = initBlockShaderProgram();
        try {
            blockAtlas = Texture.loadNewTexture("assets/textures/atlas.png");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        blockMesh = world().generateMesh();
        blockMesh.init();
    }

    public void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

        // Skybox
        glDepthMask(false);
        skyboxShaderProgram.bind();
        try(MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = new Matrix4f(new Matrix3f(player().camera().getViewMatrix())).get(stack.mallocFloat(16));
            FloatBuffer fb1 = player().camera().getProjectionMatrix().get(stack.mallocFloat(16));
            glUniformMatrix4fv(skyboxShaderProgram.getUniformLocation("viewMatrix"), false, fb);
            glUniformMatrix4fv(skyboxShaderProgram.getUniformLocation("projectionMatrix"), false, fb1);
        }
        skyboxMesh.bindVAO();
        skyboxTexture.bind();
        skyboxMesh.render();
        glDepthMask(true);

        // Blocks
        if(meshUpdate) {
            blockMesh = new Mesh<>(true);
            blockMesh = world().generateMesh();
            blockMesh.init();
            meshUpdate = false;
        }
        blockShaderProgram.bind();
        player().camera().updateViewProjectionMatrix(blockShaderProgram);
        blockMesh.bindVAO();
        blockAtlas.bind();
        blockMesh.render();
    }

    private static ShaderProgram initBlockShaderProgram() {
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

    private static ShaderProgram initSkyboxShaderProgram() {
        ShaderProgram shaderProgram = new ShaderProgram();
        try {
            shaderProgram.createVertexShader("shaders/skybox.vert");
            shaderProgram.createFragmentShader("shaders/skybox.frag");
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
