package net.jay.voxelgame.render.texture;

import net.jay.voxelgame.util.TextureUtil;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL45.*;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.system.MemoryUtil.memSlice;

public class TextureArray {
    private final int id;

    private TextureArray(int id) {
        this.id = id;
    }

    public void bind() {
        glBindTextureUnit(0, id);
    }

    public static TextureArray loadNewTextureArray() {
        TextureArray texArray = new TextureArray(glCreateTextures(GL_TEXTURE_2D_ARRAY));

        TextureUtil.ImageResult image = TextureUtil.loadImage("assets/textures/blocks/dirt.png");

        glTextureStorage3D(texArray.id, 1, GL_RGBA8, image.width, image.height, 1);

        glTextureSubImage2D(texArray.id, 1, 0, 0, image.width, image.height, GL_RGBA, GL_UNSIGNED_BYTE, image.data);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        stbi_image_free(image.data);

        glBindTextureUnit(0, texArray.id);
        return texArray;
    }

    public int id() {
        return id;
    }
}
