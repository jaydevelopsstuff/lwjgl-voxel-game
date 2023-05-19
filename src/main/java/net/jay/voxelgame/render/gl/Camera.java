package net.jay.voxelgame.render.gl;

import net.jay.voxelgame.Window;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

public class Camera {

    private Vector3f pos;
    private Vector3f front;
    private Vector3f up;

    private float yaw = -90f;
    private float pitch;

    private double lastMouseX;
    private double lastMouseY;
    private boolean firstMousePass = true;

    public Camera() {
        this.pos = new Vector3f(0, 0, 0);
        this.front = new Vector3f(0, 0, -1);
        this.up = new Vector3f(0, 1, 0);
    }

    public void updateProjectionMatrix(ShaderProgram shaderProgram) {
        try(MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = new Matrix4f()
                    .perspective((float) Math.toRadians(110.0f), 1.0f, 0.01f, 100.0f)
                    .lookAt(pos, new Vector3f(pos).add(front), up)
                    .get(stack.mallocFloat(16));
            glUniformMatrix4fv(shaderProgram.getUniformLocation("projectionMatrix"), false, fb);
        }
    }

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

    public Vector3f pos() {
        return pos;
    }

    public void setPos(double x, double y, double z) {
        pos.x = (float)x;
        pos.y = (float)y;
        pos.z = (float)z;
    }

    public Vector3f front() {
        return front;
    }
}
