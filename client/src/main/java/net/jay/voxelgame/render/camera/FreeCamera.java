package net.jay.voxelgame.render.camera;

import net.jay.voxelgame.Window;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class FreeCamera extends Camera {
    protected float yaw = -90f;
    protected float pitch;

    protected double lastMouseX;
    protected double lastMouseY;
    protected boolean firstMousePass = true;

    public void handleKeyboard(Window window) {
        float cameraSpeed = 0.08f;
        if(window.getKey(GLFW_KEY_W) == GLFW_PRESS) {
            pos.add(new Vector3f(front).mul(cameraSpeed));
        }
        if(window.getKey(GLFW_KEY_S) == GLFW_PRESS) {
            pos.sub(new Vector3f(front).mul(cameraSpeed));
        }
        if(window.getKey(GLFW_KEY_A) == GLFW_PRESS) {
            pos.sub(new Vector3f(front).cross(up).normalize().mul(cameraSpeed));
        }
        if(window.getKey(GLFW_KEY_D) == GLFW_PRESS) {
            pos.add(new Vector3f(front).cross(up).normalize().mul(cameraSpeed));
        }
        if(window.getKey(GLFW_KEY_SPACE) == GLFW_PRESS) {
            pos.add(new Vector3f(up).mul(cameraSpeed));
        }
        if(window.getKey(GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS) {
            pos.sub(new Vector3f(up).mul(cameraSpeed));
        }
    }

    public void handleCursorPos(double x, double y) {
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

        yaw += xOffset;
        pitch -= yOffset;

        if(pitch > 89.0f)
            pitch = 89.0f;
        if(pitch < -89.0f)
            pitch = -89.0f;

        Vector3f direction = new Vector3f();
        direction.x = (float)(Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        direction.y = (float)Math.sin(Math.toRadians(pitch));
        direction.z = (float)(Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        front = direction.normalize();


        lastMouseX = x;
        lastMouseY = y;
    }
}
