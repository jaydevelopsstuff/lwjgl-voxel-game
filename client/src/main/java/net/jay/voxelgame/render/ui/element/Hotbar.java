package net.jay.voxelgame.render.ui.element;

import net.jay.voxelgame.render.gl.Mesh;
import net.jay.voxelgame.render.gl.vertex.TextureVertex;
import net.jay.voxelgame.render.ui.GuiAtlases;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Hotbar extends GuiElement {
    public Hotbar() {
        super(new Mesh<>(true), GuiAtlases.widgets);
        this.mesh.addIndices(
                0, 1, 2,
                2, 3, 0
        );
        this.mesh.addVertices(
                new TextureVertex(new Vector3f(0.5f, -1f, 0f), new Vector2f(1, 0)), // 0 - Top Right (TC - 1, 0)
                new TextureVertex(new Vector3f(0.5f, -0.80f, 0f), new Vector2f(1, 0.12f)), // 1 - Bottom Right (TC - 1, 1)
                new TextureVertex(new Vector3f(-0.5f, -0.80f, 0f), new Vector2f(0, 0.12f)), // 2 - Bottom Left (TC - 0, 1)
                new TextureVertex(new Vector3f(-0.5f, -1f, 0f), new Vector2f(0, 0))
        );
        /*this.mesh.addVertices(
                new TextureVertex(new Vector3f(0.5f, -0.5f, 0f), new Vector2f(1, 0)), // 0 - Top Right (TC - 1, 0)
                new TextureVertex(new Vector3f(0.5f, 0.5f, 0f), new Vector2f(1, 0.12f)), // 1 - Bottom Right (TC - 1, 1)
                new TextureVertex(new Vector3f(-0.5f, 0.5f, 0f), new Vector2f(0, 0.12f)), // 2 - Bottom Left (TC - 0, 1)
                new TextureVertex(new Vector3f(-0.5f, -0.5f, 0f), new Vector2f(0, 0))
        );*/
        this.mesh.init();
    }
}
