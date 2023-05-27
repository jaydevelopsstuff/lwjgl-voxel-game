package net.jay.voxelgame.render.camera;

import net.jay.voxelgame.render.gl.ShaderProgram;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static net.jay.voxelgame.Game.*;

public class Camera {

    protected final Vector3f pos;
    protected Vector3f front;
    protected final Vector3f up;

    public Camera() {
        this.pos = new Vector3f(0, 0, 0);
        this.front = new Vector3f(0, 0, -1);
        this.up = new Vector3f(0, 1, 0);
    }

    public void updateViewProjectionMatrix(ShaderProgram shaderProgram) {
        try(MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = getViewMatrix().get(stack.mallocFloat(16));
            FloatBuffer fb1 = getProjectionMatrix().get(stack.mallocFloat(16));
            glUniformMatrix4fv(shaderProgram.getUniformLocation("viewMatrix"), false, fb);
            glUniformMatrix4fv(shaderProgram.getUniformLocation("projectionMatrix"), false, fb1);
        }
    }

    public Matrix4f getViewMatrix() {
        return new Matrix4f()
                .lookAt(pos, new Vector3f(pos).add(front), up);
    }

    public Matrix4f getProjectionMatrix() {
        return new Matrix4f().perspective((float) Math.toRadians(110.0f), (float)window().getSize().x / window().getSize().y, 0.01f, 5000.0f);
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

    public Vector3f up() {
        return up;
    }
}
