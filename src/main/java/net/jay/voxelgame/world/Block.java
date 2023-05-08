package net.jay.voxelgame.world;

import net.jay.voxelgame.render.Mesh;
import net.jay.voxelgame.render.ShaderProgram;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

public class Block {
    private final BlockType block;

    public Block(BlockType block) {
        this.block = block;
    }

    public void render(Mesh mesh, ShaderProgram shaderProgram) {
        try(MemoryStack stack = MemoryStack.stackPush()) {
            Matrix4f matrix = new Matrix4f();

            // Front face
            shaderProgram.updateUniformMatrix4f("worldMatrix", matrix.get(stack.mallocFloat(16)));
            mesh.render();

            // Back face
            matrix.setTranslation(0f, 0f, -1f);
            shaderProgram.updateUniformMatrix4f("worldMatrix", matrix.get(stack.mallocFloat(16)));
            mesh.render();

            // Left Face
            matrix.setRotationXYZ(0, (float)Math.toRadians(90), 0);
            matrix.setTranslation(-0.5f, 0f, -0.5f);
            shaderProgram.updateUniformMatrix4f("worldMatrix", matrix.get(stack.mallocFloat(16)));
            mesh.render();

            // Right Face
            matrix.setTranslation(0.5f, 0f, -0.5f);
            shaderProgram.updateUniformMatrix4f("worldMatrix", matrix.get(stack.mallocFloat(16)));
            mesh.render();

            // Top Face
            matrix.setRotationXYZ((float)Math.toRadians(90), 0, 0);
            matrix.setTranslation(0f, 0.5f, -0.5f);
            shaderProgram.updateUniformMatrix4f("worldMatrix", matrix.get(stack.mallocFloat(16)));
            mesh.render();

            // Bottom Face
            matrix.setTranslation(0f, -0.5f, -0.5f);
            shaderProgram.updateUniformMatrix4f("worldMatrix", matrix.get(stack.mallocFloat(16)));
            mesh.render();
        }
    }

    public BlockType block() {
        return block;
    }
}
