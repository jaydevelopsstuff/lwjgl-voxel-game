package net.jay.voxelgame;

import net.jay.voxelgame.render.Camera;
import net.jay.voxelgame.render.Mesh;
import net.jay.voxelgame.render.ShaderProgram;
import net.jay.voxelgame.render.Texture;
import net.jay.voxelgame.util.Raycast;
import net.jay.voxelgame.world.Block;
import net.jay.voxelgame.world.BlockType;
import net.jay.voxelgame.world.World;
import org.joml.Vector3i;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import java.io.IOException;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Game {
    private static Window window;
    private static Camera camera = new Camera();
    private static World world;
    private static Mesh mesh;
    private static boolean meshUpdate = false;

    public static void start() {
        System.out.println("Starting...");

        world = new World();
        initGLFW();
        loop();
    }

    private static void loop() {
        // Don't remove
        GL.createCapabilities();

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glEnable(GL_DEPTH_TEST);

        ShaderProgram shaderProgram = initShaderProgram();

        Texture texture;
        try {
            texture = Texture.loadNewTexture("assets/dirt.png");
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        world.generateBlocks();
        mesh = world.generateMesh();
        mesh.init();

        glEnable(GL_CULL_FACE);
        while(!window.shouldClose()) {
            processInputs();

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            if(meshUpdate) {
                mesh = new Mesh();
                mesh = world.generateMesh();
                mesh.init();
                meshUpdate = false;
            }
            texture.bind();
            shaderProgram.bind();
            camera.updateProjectionMatrix(shaderProgram);

            mesh.render();

            glfwSwapBuffers(window.handle()); // swap the color buffers
            glfwPollEvents();
        }
    }

    private static void processInputs() {
        camera.handleKeyboard(window);
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

    private static void initGLFW() {
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if(!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        window = new Window("Voxel Game");
        window.init();

        glfwSetInputMode(window.handle(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        window.setCursorPosCallback((long window, double x, double y) -> {
            camera.handleCursorPos(x, y);
        });

        window.setMouseButtonCallback((long window, int button, int action, int mods) -> {
            if(action == GLFW_PRESS && button == GLFW_MOUSE_BUTTON_1) {
                Vector3i result = Raycast.traceRay(world.blocks, camera.getPos(), camera.getFront(), new Vector3i(), 50);
                if(result == null) return;
                world.blocks[result.x][result.y][result.z] = Block.AIR;
                meshUpdate = true;
            } else if(action == GLFW_PRESS && button == GLFW_MOUSE_BUTTON_2) {
                Vector3i result = new Vector3i();
                Raycast.traceRay(world.blocks, camera.getPos(), camera.getFront(), result, 50);
                world.blocks[result.x][result.y][result.z] = new Block(BlockType.Dirt);
                meshUpdate = true;
            }
        });

        window.setKeyCallback((long window, int key, int scancode, int action, int mods) -> {

        });
    }
}
