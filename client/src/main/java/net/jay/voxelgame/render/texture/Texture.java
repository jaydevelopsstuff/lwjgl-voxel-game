package net.jay.voxelgame.render.texture;

import net.jay.voxelgame.util.TextureUtil;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.stbi_image_free;

public class Texture {
    private final int id;

    private Texture(int id) {
        this.id = id;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public int id() {
        return id;
    }

    public static Texture loadNewTexture(String filePath) throws IOException {
        TextureUtil.ImageResult image = TextureUtil.loadImage(filePath);

        Texture texture = new Texture(glGenTextures());
        glBindTexture(GL_TEXTURE_2D, texture.id);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        // glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_NEAREST);
        // glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.width, image.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image.data);
        //glGenerateMipmap(GL_TEXTURE_2D);
        stbi_image_free(image.data);

        return texture;
    }
}
