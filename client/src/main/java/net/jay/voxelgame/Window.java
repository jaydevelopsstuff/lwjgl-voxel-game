package net.jay.voxelgame;

import org.joml.Vector2i;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class Window {
    private long handle;
    private final String title;

    public Window(String title) {
        this.title = title;
    }

    public void init() {
        handle = glfwCreateWindow(800, 800, title, MemoryUtil.NULL, MemoryUtil.NULL);
        if(handle == MemoryUtil.NULL) throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        setKeyCallback((window, key, scancode, action, mods) -> {
            if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

        // Get the thread stack and push a new frame
        try(MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwSetWindowSize(handle, vidmode.width(), vidmode.height());

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(handle, pWidth, pHeight);

            // Center the window
            glfwSetWindowPos(
                    handle,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(handle);
        // Enable v-sync
        glfwSwapInterval(1);

        show();
    }

    public int getKey(int key) {
        return glfwGetKey(handle, key);
    }

    public void show() {
        glfwShowWindow(handle);
    }

    public void hide() {
        glfwHideWindow(handle);
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(handle);
    }

    public void setKeyCallback(GLFWKeyCallbackI callback) {
        glfwSetKeyCallback(handle, callback);
    }

    public void setMouseButtonCallback(GLFWMouseButtonCallbackI callback) {
        glfwSetMouseButtonCallback(handle, callback);
    }

    public void setCursorPosCallback(GLFWCursorPosCallbackI callback) {
        glfwSetCursorPosCallback(handle, callback);
    }

    public Vector2i getSize() {
        int[] w = new int[1];
        int[] h = new int[1];
        glfwGetWindowSize(handle, w, h);
        return new Vector2i(w[0], h[0]);
    }

    public void setSize(int width, int height) {
        glfwSetWindowSize(handle, width, height);
    }

    public long handle() {
        return handle;
    }
}
