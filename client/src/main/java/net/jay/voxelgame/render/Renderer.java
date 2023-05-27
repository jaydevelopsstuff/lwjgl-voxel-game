package net.jay.voxelgame.render;

import static net.jay.voxelgame.Game.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

import net.jay.voxelgame.render.gl.Mesh;
import net.jay.voxelgame.render.gl.ShaderProgram;
import net.jay.voxelgame.render.gl.vertex.PositionVertex;
import net.jay.voxelgame.render.gl.vertex.TextureVertex;
import net.jay.voxelgame.render.texture.Cubemap;
import net.jay.voxelgame.render.texture.Texture;
import net.jay.voxelgame.render.ui.GuiAtlases;
import net.jay.voxelgame.render.ui.GuiRenderer;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.FloatBuffer;

public class Renderer {
    private GuiRenderer guiRenderer;

    private Mesh<TextureVertex> blockMesh;
    private boolean meshUpdate;
    private Mesh<PositionVertex> skyboxMesh;

    private ShaderProgram textureShaderProgram;
    private ShaderProgram vertexShaderProgram;


    private Texture blockAtlas;
    private Cubemap skyboxTexture;

    public void init() {
        GL.createCapabilities();

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);

        vertexShaderProgram = initVertexShaderProgram();
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

        // GUI
        // guiRenderer.render(textureShaderProgram);

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

    private static ShaderProgram initVertexShaderProgram() {
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
