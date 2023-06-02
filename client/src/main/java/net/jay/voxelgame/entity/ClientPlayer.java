package net.jay.voxelgame.entity;

import net.jay.voxelgame.Game;
import net.jay.voxelgame.Window;
import net.jay.voxelgame.api.block.BlockType;
import net.jay.voxelgame.render.camera.Camera;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class ClientPlayer extends CEntity {
    private static final float movementSpeed = 0.01f;

    private final Camera camera;
    private BlockType selectedBlock;

    private boolean wPressed, sPressed, aPressed, dPressed, spacePressed;

    private double lastMouseX;
    private double lastMouseY;
    private boolean firstMousePass = true;

    public ClientPlayer() {
        super();
        this.camera = new Camera();
        this.selectedBlock = BlockType.Dirt;
        setYaw(-90f);
    }

    public void tick() {
        super.tick();

        tickMovement();

        if(!onGround) {
           velocity().add(0, -0.002f, 0);
        } else {
            velocity().sub(0, velocity().y, 0);
        }

        if(!pos().equals(prevPos()))
            Game.network().sendPosition(pos().x, pos().y, pos().z);
    }

    private void tickMovement() {
        Vector3f front = camera.front();
        Vector3f up = camera.up();

        velocity().sub(velocity().x / 8f, 0, velocity().z / 8f);

        if(wPressed) {
            velocity().add(new Vector3f(front.x, 0, front.z).mul(movementSpeed));
        }
        if(sPressed) {
            velocity().sub(new Vector3f(front.x, 0, front.z).mul(movementSpeed));
        }
        if(aPressed) {
            velocity().sub(new Vector3f(front).cross(up).normalize().mul(movementSpeed));
        }
        if(dPressed) {
            velocity().add(new Vector3f(front).cross(up).normalize().mul(movementSpeed));
        }
        if(spacePressed && onGround) {
            velocity().add(new Vector3f(up).mul(0.1f));
            onGround = false;
        }
    }

    public void handleKeyboardInput(Window window) {
        if(window.getKey(GLFW_KEY_W) == GLFW_PRESS) {
            wPressed = true;
        }
        if(window.getKey(GLFW_KEY_S) == GLFW_PRESS) {
            sPressed = true;
        }
        if(window.getKey(GLFW_KEY_A) == GLFW_PRESS) {
            aPressed = true;
        }
        if(window.getKey(GLFW_KEY_D) == GLFW_PRESS) {
            dPressed = true;
        }
        if(window.getKey(GLFW_KEY_SPACE) == GLFW_PRESS) {
            spacePressed = true;
        }


        if(window.getKey(GLFW_KEY_W) == GLFW_RELEASE) {
            wPressed = false;
        }
        if(window.getKey(GLFW_KEY_S) == GLFW_RELEASE) {
            sPressed = false;
        }
        if(window.getKey(GLFW_KEY_A) == GLFW_RELEASE) {
            aPressed = false;
        }
        if(window.getKey(GLFW_KEY_D) == GLFW_RELEASE) {
            dPressed = false;
        }
        if(window.getKey(GLFW_KEY_SPACE) == GLFW_RELEASE) {
            spacePressed = false;
        }
    }

    public void handleCursorPosInput(double x, double y) {
        if(firstMousePass) {
            lastMouseX = x;
            lastMouseY = y;
            firstMousePass = false;
        }

        float xOffset = (float)(x - lastMouseX);
        float yOffset = (float)(y - lastMouseY);

        float sensitivity = 0.05f;
        xOffset *= sensitivity;
        yOffset *= sensitivity;

        setYaw(yaw() + xOffset);
        setPitch(pitch() - yOffset);

        if(pitch() > 89.0f)
            setPitch(89.0f);
        if(pitch() < -89.0f)
            setPitch(-89.0f);

        Vector3f direction = new Vector3f();
        direction.x = (float)(Math.cos(Math.toRadians(yaw())) * Math.cos(Math.toRadians(pitch())));
        direction.y = (float)Math.sin(Math.toRadians(pitch()));
        direction.z = (float)(Math.sin(Math.toRadians(yaw())) * Math.cos(Math.toRadians(pitch())));
        camera.front().set(direction.normalize());


        lastMouseX = x;
        lastMouseY = y;
    }

    public Camera camera() {
        return camera;
    }

    @Override
    public void setPos(double x, double y, double z) {
        super.setPos(x, y, z);
        camera.setPos(x, y, z);
    }

    public BlockType selectedBlock() {
        return selectedBlock;
    }

    public void setSelectedBlock(BlockType selectedBlock) {
        this.selectedBlock = selectedBlock;
    }
}
