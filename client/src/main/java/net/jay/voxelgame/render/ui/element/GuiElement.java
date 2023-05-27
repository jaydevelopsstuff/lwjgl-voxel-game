package net.jay.voxelgame.render.ui.element;

import net.jay.voxelgame.render.gl.Mesh;
import net.jay.voxelgame.render.gl.vertex.TextureVertex;
import net.jay.voxelgame.render.texture.Texture;

public class GuiElement {
    protected final Mesh<TextureVertex> mesh;
    private final Texture atlas;

    protected GuiElement(Mesh<TextureVertex> mesh, Texture atlas) {
        this.mesh = mesh;
        this.atlas = atlas;
    }

    public void render() {
        mesh.bindVAO();
        atlas.bind();
        mesh.render();
    }
}
