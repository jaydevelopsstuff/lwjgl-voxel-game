package net.jay.voxelgame;

import net.jay.voxelgame.api.block.BlockType;
import net.jay.voxelgame.api.packet.ModifyBlockPacket;
import net.jay.voxelgame.api.packet.ResetWorldPacket;
import net.jay.voxelgame.entity.ClientPlayer;
import net.jay.voxelgame.network.NetworkManager;
import net.jay.voxelgame.render.Renderer;
import net.jay.voxelgame.util.AudioManager;
import net.jay.voxelgame.util.MyArrayList;
import net.jay.voxelgame.world.World;
import net.jay.voxelgame.world.block.Blocks;
import org.joml.Vector3i;
import org.lwjgl.glfw.GLFWErrorCallback;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

import static org.lwjgl.glfw.GLFW.*;


public class Game {
    private static final String Name = "Voxel Game";

    public static String host;
    public static boolean showInstructions = true;
    private static NetworkManager network;
    private static Window window;
    private static ClientPlayer player;
    private static World world;
    private static Renderer renderer;
    public static AudioManager audioManager;
    private static double delta;

    public static void start() {
        System.out.println("Starting...");

        network = new NetworkManager();
        network.connectToServer(host);

        world = new World();
        player = new ClientPlayer();
        player.setPos(0, 10, 0);
        renderer = new Renderer();
        audioManager = new AudioManager();
        try {
            audioManager.init();
        } catch(LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException(e);
        }

        initGLFW();
        loop();
    }

    private static void loop() {
        renderer.init();

        while(!window.shouldClose()) {
            double previousFrameTime = (System.nanoTime() / 1000000000D);
            processInputs();
            player.tick(delta);

            renderer.render();

            glfwSwapBuffers(window.handle()); // swap the color buffers
            glfwPollEvents();
            delta = (System.nanoTime() / 1000000000D) - previousFrameTime;
        }
    }

    private static void processInputs() {
        player.handleKeyboardInput(window);
        if(window.getKey(GLFW_KEY_R) == GLFW_PRESS) {
            world.resetChunks();
            renderer.queueMeshUpdate();
            try {
                network.sendPacket(new ResetWorldPacket());
            } catch(IOException e) {
                System.out.println("Failed to send reset world packet");
            }
        }

        // Bad solution, change this
        player.setPos(player.camera().pos().x, player.camera().pos().y, player.camera().pos().z);
    }

    private static void initGLFW() {
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize  Most GLFW functions will not work before doing this.
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
                Vector3i result = world.rayTrace(player.pos(), player.camera().front(), null, 6f);
                if(result == null)
                    return;
                ModifyBlockPacket packet = new ModifyBlockPacket();
                packet.blockType = BlockType.Air;
                packet.x = result.x;
                packet.y = result.y;
                packet.z = result.z;
                try {
                    network.sendPacket(packet);
                } catch(IOException e) {
                    throw new RuntimeException(e);
                }

                world.setBlock(Blocks.Air, result.x, result.y, result.z);
                renderer.queueMeshUpdate();
            } else if(action == GLFW_PRESS && button == GLFW_MOUSE_BUTTON_2) {
                Vector3i result = new Vector3i();
                world.rayTrace(player.pos(), player.camera().front(), result, 6f);

                ModifyBlockPacket packet = new ModifyBlockPacket();
                packet.blockType = player.selectedBlock();
                packet.x = result.x;
                packet.y = result.y;
                packet.z = result.z;
                try {
                    network.sendPacket(packet);
                } catch(IOException e) {
                    throw new RuntimeException(e);
                }

                world.setBlock(Blocks.getBlock(player.selectedBlock()), result.x, result.y, result.z);
                renderer.queueMeshUpdate();
                if(player.selectedBlock() == BlockType.Stone) {
                    audioManager.queueSound(1);
                } else {
                    audioManager.queueSound(0);
                }
            }
        });

        window.setKeyCallback((long window, int key, int scancode, int action, int mods) -> {
            switch(key) {
                case GLFW_KEY_1: {
                    player.setSelectedBlock(BlockType.Dirt);
                    break;
                }
                case GLFW_KEY_2: {
                    player.setSelectedBlock(BlockType.Stone);
                    break;
                }
                case GLFW_KEY_3: {
                    player.setSelectedBlock(BlockType.Grass);
                    break;
                }
                case GLFW_KEY_I: {
                    if(action == GLFW_PRESS)
                        showInstructions = !showInstructions;
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

    public static NetworkManager network() {
        return network;
    }

    public static Renderer renderer() {
        return renderer;
    }
}
