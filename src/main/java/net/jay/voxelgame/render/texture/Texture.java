package net.jay.voxelgame.render.texture;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.memSlice;

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
        ByteBuffer image;

        int w;
        int h;
        int comp;
        try(MemoryStack stack = stackPush()) {
            IntBuffer wBuf = stack.mallocInt(1);
            IntBuffer hBuf = stack.mallocInt(1);
            IntBuffer compBuf = stack.mallocInt(1);

            ByteBuffer imageBuffer = resourceToByteBuffer(filePath, 8 * 1024);

            image = stbi_load_from_memory(imageBuffer, wBuf, hBuf, compBuf, 0);
            if(image == null)
                throw new RuntimeException("Failed to load image: " + stbi_failure_reason());

            w = wBuf.get(0);
            h = wBuf.get(0);
            comp = wBuf.get(0);
        }

        Texture texture = new Texture(glGenTextures());
        glBindTexture(GL_TEXTURE_2D, texture.id);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
        glGenerateMipmap(GL_TEXTURE_2D);
        stbi_image_free(image);

        return texture;
    }

    private static ByteBuffer resourceToByteBuffer(String filePath, int bufferSize) throws IOException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        ReadableByteChannel channel = Channels.newChannel(classLoader.getResourceAsStream(filePath));
        ByteBuffer buffer = BufferUtils.createByteBuffer(bufferSize);
        while(channel.read(buffer) != -1) {
            if(buffer.remaining() == 0) {
                ByteBuffer newBuffer = BufferUtils.createByteBuffer(buffer.capacity() * 3 / 2);
                buffer.flip();
                newBuffer.put(buffer);
                buffer = newBuffer;
            }
        }

        buffer.flip();
        return memSlice(buffer);
    }
}
