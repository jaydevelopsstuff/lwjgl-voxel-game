package net.jay.voxelgame.render.ui.element;

import net.jay.voxelgame.Game;
import net.jay.voxelgame.render.gl.Mesh;
import net.jay.voxelgame.render.gl.vertex.TextureVertex;
import net.jay.voxelgame.render.ui.GuiAtlases;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Instructions extends GuiElement {
    public Instructions() {
        super(new Mesh<>(true), GuiAtlases.instructions);
        this.mesh.addIndices(
                0, 1, 2,
                2, 3, 0
        );
        this.mesh.addVertices(
                new TextureVertex(new Vector3f(0.5f, -0.5f, 0f), new Vector2f(1, 0)), // 0 - Top Right (TC - 1, 0)
                new TextureVertex(new Vector3f(0.5f, 0.5f, 0f), new Vector2f(1, 1)), // 1 - Bottom Right (TC - 1, 1)
                new TextureVertex(new Vector3f(-0.5f, 0.5f, 0f), new Vector2f(0, 1)), // 2 - Bottom Left (TC - 0, 1)
                new TextureVertex(new Vector3f(-0.5f, -0.5f, 0f), new Vector2f(0, 0)) // 3 - Top Left
        );
        this.mesh.init();
    }

    @Override
    public void render() {
        if(Game.showInstructions)
            super.render();
    }
}
