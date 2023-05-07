package net.jay.voxelgame;

import net.jay.voxelgame.render.Camera;
import net.jay.voxelgame.render.ShaderProgram;
import net.jay.voxelgame.render.Texture;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
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

        float[] vertices = {
                0.5f,  0.5f, 0.0f, 1, 0,  // top right
                0.5f, -0.5f, 0.0f, 1, 1,  // bottom right
                -0.5f, -0.5f, 0.0f, 0, 1,  // bottom left
                -0.5f,  0.5f, 0.0f, 0, 0  // top left
        };
        int[] indices = {
                0, 1, 3,
                1, 2, 3
        };

        int vao = glGenVertexArrays();
        int vbo = glGenBuffers();
        int ebo = glGenBuffers();
        glBindVertexArray(vao);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * 4, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * 4, 3 * 4);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, 0);

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
            glBindVertexArray(vao);
            try(MemoryStack stack = MemoryStack.stackPush()) {
                FloatBuffer fb = new Matrix4f()
                        .perspective((float) Math.toRadians(45.0f), 1.0f, 0.01f, 100.0f)
                        .lookAt(camera.pos,
                                new Vector3f(camera.pos).add(camera.front),
                                camera.up)
                        .get(stack.mallocFloat(16));
                glUniformMatrix4fv(shaderProgram.getUniformLocation("projectionMatrix"), false, fb);
            }
            //glDrawArrays(GL_TRIANGLES, 0, 3);
            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

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
