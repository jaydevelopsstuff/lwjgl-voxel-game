package net.jay.voxelgame;

import net.jay.voxelgame.entity.player.ClientPlayer;
import net.jay.voxelgame.render.Renderer;
import net.jay.voxelgame.render.gl.Mesh;
import net.jay.voxelgame.render.gl.ShaderProgram;
import net.jay.voxelgame.render.texture.Texture;
import net.jay.voxelgame.util.Raycast;
import net.jay.voxelgame.world.World;
import net.jay.voxelgame.world.block.Blocks;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import java.io.IOException;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Game {
    private static final String Name = "Voxel Game";

    private static Window window;
    private static ClientPlayer player;
    private static World world;
    private static Renderer renderer;

    public static void start() {
        System.out.println("Starting...");

        world = new World();
        player = new ClientPlayer();
        renderer = new Renderer();
        initGLFW();
        loop();
    }

    private static void loop() {
        renderer.init();

        while(!window.shouldClose()) {
            processInputs();

            renderer.render();

            glfwSwapBuffers(window.handle()); // swap the color buffers
            glfwPollEvents();
        }
    }

    private static void processInputs() {
        player.camera().handleKeyboard(window);
        // Bad solution, change this
        player.setPos(player.camera().pos().x, player.camera().pos().y, player.camera().pos().z);
    }

    private static void initGLFW() {
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if(!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        // glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        window = new Window(Name);
        window.init();

        glfwSetInputMode(window.handle(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        window.setCursorPosCallback((long window, double x, double y) -> {
            player.camera().handleCursorPos(x, y);
        });

        window.setMouseButtonCallback((long window, int button, int action, int mods) -> {
            if(action == GLFW_PRESS && button == GLFW_MOUSE_BUTTON_1) {
                Vector2i chunkCoord = world.getChunkCoords(player.camera().pos().x, player.camera().pos().z);
                if(chunkCoord == null) return;
                Vector3i result = Raycast.traceRay(world.getLoadedChunks()[chunkCoord.x][chunkCoord.y].blocks(), new Vector3f(chunkCoord.x * 16, 0, chunkCoord.y * 16), true, player.camera().pos(), player.camera().front(), new Vector3i(), 50);
                if(result == null) return;
                world.getLoadedChunks()[chunkCoord.x][chunkCoord.y].blocks()[result.x][result.y][result.z] = Blocks.Air;
                renderer.queueMeshUpdate();
            } else if(action == GLFW_PRESS && button == GLFW_MOUSE_BUTTON_2) {
                Vector3i result = new Vector3i();
                Vector2i chunkCoord = world.getChunkCoords(player.camera().pos().x, player.camera().pos().z);
                if(chunkCoord == null) return;
                Raycast.traceRay(world.getLoadedChunks()[chunkCoord.x][chunkCoord.y].blocks(), new Vector3f(chunkCoord.x * 16, 0, chunkCoord.y * 16), true, player.camera().pos(), player.camera().front(), result, 50);
                world.getLoadedChunks()[chunkCoord.x][chunkCoord.y].blocks()[result.x][result.y][result.z] = Blocks.Dirt;
                renderer.queueMeshUpdate();
            }
        });

        window.setKeyCallback((long window, int key, int scancode, int action, int mods) -> {

        });
    }

    public static Window window() {
        return window;
    }

    public static ClientPlayer player() {
        return player;
    }

    public static World world() {
        return world;
    }
}
