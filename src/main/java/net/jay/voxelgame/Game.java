package net.jay.voxelgame;

import net.jay.voxelgame.entity.player.ClientPlayer;
import net.jay.voxelgame.render.Renderer;
import net.jay.voxelgame.util.Raycast;
import net.jay.voxelgame.world.World;
import net.jay.voxelgame.world.block.Block;
import net.jay.voxelgame.world.block.Blocks;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.GLFW.*;


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
        player.setPos(3, 10, 3);
        renderer = new Renderer();

        initGLFW();
        loop();
    }

    private static void loop() {
        renderer.init();

        while(!window.shouldClose()) {
            processInputs();
            player.tick();

            renderer.render();

            glfwSwapBuffers(window.handle()); // swap the color buffers
            glfwPollEvents();
        }
    }

    private static void processInputs() {
        player.handleKeyboardInput(window);
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
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        window = new Window(Name);
        window.init();

        glfwSetInputMode(window.handle(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        window.setCursorPosCallback((long window, double x, double y) -> {
            player.handleCursorPosInput(x, y);
        });

        window.setMouseButtonCallback((long window, int button, int action, int mods) -> {
            if(action == GLFW_PRESS && button == GLFW_MOUSE_BUTTON_1) {
                Vector3i result = world.rayTrace(player.pos(), player.camera().front(), new Vector3i(), 10f);
                world.setBlock(Blocks.Air, result.x, result.y, result.z);
                System.out.println(player.pos().x + "," + player.pos().z);
                System.out.println(result.x + "," + result.y + "," + result.z);
                renderer.queueMeshUpdate();
            } else if(action == GLFW_PRESS && button == GLFW_MOUSE_BUTTON_2) {
                Vector3i result = new Vector3i();
                world.rayTrace(player.pos(), player.camera().front(), result, 10f);
                world.setBlock(Blocks.getBlock(player.selectedBlock()), result.x, result.y, result.z);
                renderer.queueMeshUpdate();
            }
        });

        window.setKeyCallback((long window, int key, int scancode, int action, int mods) -> {
            switch(key) {
                case GLFW_KEY_1: {
                    player.setSelectedBlock(Block.Type.Dirt);
                    break;
                }
                case GLFW_KEY_2: {
                    player.setSelectedBlock(Block.Type.Stone);
                    break;
                }
                case GLFW_KEY_3: {
                    player.setSelectedBlock(Block.Type.Grass);
                    break;
                }
            }
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
