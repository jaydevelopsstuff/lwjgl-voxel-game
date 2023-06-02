package net.jay.voxelgame.render;

import net.jay.voxelgame.api.entity.SPlayer;
import net.jay.voxelgame.render.gl.Mesh;
import net.jay.voxelgame.render.gl.ShaderProgram;
import net.jay.voxelgame.render.gl.vertex.ColorVertex;
import net.jay.voxelgame.render.gl.vertex.PositionVertex;
import net.jay.voxelgame.render.gl.vertex.TextureVertex;
import net.jay.voxelgame.render.texture.Cubemap;
import net.jay.voxelgame.render.texture.Texture;
import net.jay.voxelgame.render.ui.GuiAtlases;
import net.jay.voxelgame.render.ui.GuiRenderer;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.FloatBuffer;

import static net.jay.voxelgame.Game.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

public class Renderer {
    private GuiRenderer guiRenderer;

    private Mesh<TextureVertex> blockMesh;
    private boolean meshUpdate;
    private Mesh<PositionVertex> skyboxMesh;
    private Mesh<ColorVertex> playerMesh;


    private ShaderProgram textureShaderProgram;
    private ShaderProgram colorShaderProgram;
    private ShaderProgram vertexShaderProgram;


    private Texture blockAtlas;
    private Cubemap skyboxTexture;

    public void init() {
        GL.createCapabilities();

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);

        vertexShaderProgram = initVertexShaderProgram();
        colorShaderProgram = initColorShaderProgram();
        textureShaderProgram = initTextureShaderProgram();

        try {
            loadTextures();
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        skyboxMesh = Cubemap.cubeMesh;
        skyboxMesh.init();

        blockMesh = world().generateMesh();
        blockMesh.init();

        playerMesh = new Mesh<>(true);
        playerMesh.addIndices(
                0, 1, 2, // Front
                0, 3, 4, // Right
                0, 5, 6, // Back (CCW)
                0, 7, 8, // Left
                // Bottom
                9, 10, 11,
                11, 12, 9
        );
        playerMesh.addVertices(
                new ColorVertex(new Vector3f(0, 1, 0), new Vector4f(0, 0, 1f / 2, 1)), // Apex

                // Front
                new ColorVertex(new Vector3f(-0.5f, -0.5f, 0.5f), new Vector4f(1f / 2, 0, 0, 1)), // L
                new ColorVertex(new Vector3f(0.5f, -0.5f, 0.5f), new Vector4f(1f / 2, 0, 0, 1)), // R

                // Right
                new ColorVertex(new Vector3f(0.5f, -0.5f, 0.5f), new Vector4f(1f / 2, 0, 0, 1)), // F
                new ColorVertex(new Vector3f(0.5f, -0.5f, -0.5f), new Vector4f(1f / 2, 0, 0, 1)), // B

                // Back
                new ColorVertex(new Vector3f(0.5f, -0.5f, -0.5f), new Vector4f(1f / 2, 0, 0, 1)), // R
                new ColorVertex(new Vector3f(-0.5f, -0.5f, -0.5f), new Vector4f(1f / 2, 0, 0, 1)), // L

                // Left
                new ColorVertex(new Vector3f(-0.5f, -0.5f, -0.5f), new Vector4f(1f / 2, 0, 0, 1)), // B
                new ColorVertex(new Vector3f(-0.5f, -0.5f, 0.5f), new Vector4f(1f / 2, 0, 0, 1)), // F

                // Bottom
                new ColorVertex(new Vector3f(-0.5f, -0.5f, -0.5f), new Vector4f(1f / 2, 0, 0, 1)), // Back Left
                new ColorVertex(new Vector3f(0.5f, -0.5f, -0.5f), new Vector4f(1f / 2, 0, 0, 1)), // Back Right
                new ColorVertex(new Vector3f(0.5f, -0.5f, 0.5f), new Vector4f(1f / 2, 0, 0, 1)), // Front Right
                new ColorVertex(new Vector3f(-0.5f, -0.5f, 0.5f), new Vector4f(1f / 2, 0, 0, 1)) // Front Left
        );
        playerMesh.init();

        guiRenderer = new GuiRenderer();
        guiRenderer.init();
    }

    public void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

        // Skybox
        glDepthMask(false);
        vertexShaderProgram.bind();
        try(MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = new Matrix4f(new Matrix3f(player().camera().getViewMatrix())).get(stack.mallocFloat(16)); // Get rid of translation
            FloatBuffer fb1 = player().camera().getProjectionMatrix().get(stack.mallocFloat(16));
            glUniformMatrix4fv(vertexShaderProgram.getUniformLocation("viewMatrix"), false, fb);
            glUniformMatrix4fv(vertexShaderProgram.getUniformLocation("projectionMatrix"), false, fb1);
        }
        skyboxMesh.bindVAO();
        skyboxTexture.bind();
        skyboxMesh.render();

        // Blocks
        glEnable(GL_DEPTH_TEST);
        glDepthMask(true);
        if(meshUpdate) {
            blockMesh = new Mesh<>(true);
            blockMesh = world().generateMesh();
            blockMesh.init();
            meshUpdate = false;
        }
        textureShaderProgram.bind();
        player().camera().updateViewProjectionMatrix(textureShaderProgram);
        blockMesh.bindVAO();
        blockAtlas.bind();
        blockMesh.render();

        if(network().isConnected()) {
            colorShaderProgram.bind();
            player().camera().updateViewProjectionMatrix(colorShaderProgram);
            playerMesh.bindVAO();
            try(MemoryStack stack = MemoryStack.stackPush()) {
                for(SPlayer player : network().players()) {
                    if(player == null)
                        continue;
                    FloatBuffer playerTransform = new Matrix4f().setTranslation(player.x(), player.y() - 1.5f, player.z()).get(stack.mallocFloat(16));
                    colorShaderProgram.updateUniformMatrix4f("transformationMatrix", playerTransform);
                    playerMesh.render();
                }
            }
        }

        // GUI
        guiRenderer.render(textureShaderProgram);

        glEnable(GL_DEPTH_TEST);
        glDepthMask(true);
    }

    private void loadTextures() throws IOException {
        skyboxTexture = Cubemap.loadNewCubemap(new String[] {
                "assets/textures/skybox/right.jpg",
                "assets/textures/skybox/left.jpg",
                "assets/textures/skybox/top.jpg",
                "assets/textures/skybox/bottom.jpg",
                "assets/textures/skybox/front.jpg",
                "assets/textures/skybox/back.jpg"
        });
        blockAtlas = Texture.loadNewTexture("assets/textures/atlas.png");
        GuiAtlases.initAtlases();
    }

    private static ShaderProgram initTextureShaderProgram() {
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

    private static ShaderProgram initColorShaderProgram() {
        ShaderProgram shaderProgram = new ShaderProgram();
        try {
            shaderProgram.createVertexShader("shaders/poscolor.vert");
            shaderProgram.createFragmentShader("shaders/poscolor.frag");
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        shaderProgram.link();
        return shaderProgram;
    }

    private static ShaderProgram initVertexShaderProgram() {
        ShaderProgram shaderProgram = new ShaderProgram();
        try {
            shaderProgram.createVertexShader("shaders/pos.vert");
            shaderProgram.createFragmentShader("shaders/pos.frag");
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
