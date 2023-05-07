package net.jay.voxelgame;

import net.jay.voxelgame.render.Camera;
import net.jay.voxelgame.render.Mesh;
import net.jay.voxelgame.render.ShaderProgram;
import net.jay.voxelgame.render.Texture;
import org.joml.Matrix4f;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Game {
    private static Window window;
    private static Camera camera = new Camera();

    public static void start() {
        System.out.println("Starting...");

        init();
        loop();
    }

    private static void loop() {
        // Don't remove
        GL.createCapabilities();

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        Mesh mesh = new Mesh();
        mesh.bind();

        ShaderProgram shaderProgram = new ShaderProgram();
        try {
            shaderProgram.createVertexShader("shaders/shader.vert");
            shaderProgram.createFragmentShader("shaders/shader.frag");
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        shaderProgram.link();

        Texture texture;
        try {
            texture = Texture.loadNewTexture("assets/dirt.png");
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        while(!window.shouldClose()) {
            processKeyboard();

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            texture.bind();
            shaderProgram.bind();
            camera.updateProjectionMatrix(shaderProgram);
            for(int i = 0; i < 5; i++) {
                try(MemoryStack stack = MemoryStack.stackPush()) {
                    FloatBuffer fb1 = new Matrix4f()
                            .translation(0, 0, i)
                            .get(stack.mallocFloat(16));

                    glUniformMatrix4fv(shaderProgram.getUniformLocation("worldMatrix"), false, fb1);
                }
                mesh.render();
            }

            glfwSwapBuffers(window.handle()); // swap the color buffers
            glfwPollEvents();
        }
    }

    private static void processKeyboard() {
        camera.handleKeyboard(window);
    }

    private static void init() {
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

        window.setKeyCallback((long window, int key, int scancode, int action, int mods) -> {

        });
    }
}
