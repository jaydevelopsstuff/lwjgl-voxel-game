package net.jay.voxelgame.render;

import net.jay.voxelgame.Window;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

public class Camera {

    public Vector3f pos;
    public Vector3f front;
    public Vector3f up;

    private static float yaw = -90f;
    private static float pitch;

    private double lastMouseX = 400;
    private double lastMouseY = 400;

    public Camera() {
        this.pos = new Vector3f(0, 0, 10);
        this.front = new Vector3f(0, 0, -1);
        this.up = new Vector3f(0, 1, 0);
    }

    public void handleKeyboard(Window window) {
        float cameraSpeed = 0.05f;
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
