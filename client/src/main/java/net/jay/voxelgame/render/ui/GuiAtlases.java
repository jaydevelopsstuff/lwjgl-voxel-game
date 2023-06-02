package net.jay.voxelgame.render.ui;

import net.jay.voxelgame.render.texture.Texture;

import java.io.IOException;

public class GuiAtlases {
    public static Texture instructions;
    public static Texture widgets;

    public static void initAtlases() throws IOException {
        instructions = Texture.loadNewTexture("assets/textures/instructions.png");
        widgets = Texture.loadNewTexture("assets/textures/widgets.png");
    }
}
