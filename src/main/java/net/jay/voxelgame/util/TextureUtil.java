package net.jay.voxelgame.util;

import org.joml.Vector2f;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.stb.STBImage.stbi_failure_reason;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;
import static org.lwjgl.system.MemoryStack.stackPush;

public class TextureUtil {
    public static ImageResult loadImage(String filePath) {
        ByteBuffer image;

        int w;
        int h;
        int comp;
        try(MemoryStack stack = stackPush()) {
            IntBuffer wBuf = stack.mallocInt(1);
            IntBuffer hBuf = stack.mallocInt(1);
            IntBuffer compBuf = stack.mallocInt(1);

            ByteBuffer imageBuffer = FileUtil.resourceToByteBuffer(filePath, 8 * 1024);

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

    public static class ImageResult {
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
