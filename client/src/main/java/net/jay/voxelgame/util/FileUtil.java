package net.jay.voxelgame.util;

import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.stream.Collectors;

import static org.lwjgl.system.MemoryUtil.memSlice;

public class FileUtil {
    /**
     * Reads given resource file as a string.
     *
     * @param fileName path to the resource file
     * @return the file's contents
     * @throws IOException if read fails for any reason
     */
    public static String getResourceFileAsString(String fileName) throws IOException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        try (InputStream is = classLoader.getResourceAsStream(fileName)) {
            if (is == null) return null;
            try (InputStreamReader isr = new InputStreamReader(is);
                 BufferedReader reader = new BufferedReader(isr)) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        }
    }

    public static ByteBuffer resourceToByteBuffer(String filePath, int bufferSize) throws IOException {
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
