package net.jay.voxelgame.render.texture;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL30.GL_TEXTURE_2D_ARRAY;
import static org.lwjgl.opengl.GL45.*;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryStack.stackPush;
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

        ImageResult image = loadImage("assets/blocks/dirt.png");

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

    public static ImageResult loadImage(String filePath) {
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
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        return new ImageResult(image, w, h);
    }

    public int id() {
        return id;
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

    private static class ImageResult {
        public ByteBuffer data;
        public int width;
        public int height;

        public ImageResult(ByteBuffer data, int width, int height) {
            this.data = data;
            this.width = width;
            this.height = height;
        }
    }
}
