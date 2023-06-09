package net.jay.voxelgame.render.ui;

import net.jay.voxelgame.render.gl.ShaderProgram;
import net.jay.voxelgame.render.ui.element.GuiElement;
import net.jay.voxelgame.render.ui.element.Hotbar;
import net.jay.voxelgame.render.ui.element.HotbarSelection;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

public class GuiRenderer {
    private final List<GuiElement> elements = new ArrayList<>();

    public void init() {
        elements.add(new Hotbar());
        elements.add(new HotbarSelection());
    }

    public void render(ShaderProgram textureShader) {
        glDepthMask(false);
        glDisable(GL_DEPTH_TEST);
        try(MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = new Matrix4f().get(stack.mallocFloat(16));
            textureShader.bind();
            glUniformMatrix4fv(textureShader.getUniformLocation("viewMatrix"), false, fb);
            glUniformMatrix4fv(textureShader.getUniformLocation("projectionMatrix"), false, fb);

            for(GuiElement element : elements) {
                element.render();
            }
        }
    }
}
