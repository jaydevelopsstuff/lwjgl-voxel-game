package net.jay.voxelgame.render.ui;

import net.jay.voxelgame.render.texture.Texture;

import java.io.IOException;

public class GuiAtlases {
    public static Texture widgets;

    public static void initAtlases() throws IOException {
        widgets = Texture.loadNewTexture("assets/textures/widgets.png");
    }
}
