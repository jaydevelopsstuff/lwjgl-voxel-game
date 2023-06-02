package net.jay.voxelgame.render.texture;

import net.jay.voxelgame.render.gl.Mesh;
import net.jay.voxelgame.render.gl.vertex.PositionVertex;
import net.jay.voxelgame.util.TextureUtil;
import org.joml.Vector3f;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_WRAP_R;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X;
import static org.lwjgl.stb.STBImage.stbi_image_free;

public class Cubemap {
    private final int id;

    private Cubemap(int id) {
        this.id = id;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_CUBE_MAP, id);
    }

    public int id() {
        return id;
    }

    public static Cubemap loadNewCubemap(String[] filePaths) throws IOException {
        Cubemap cubemap = new Cubemap(glGenTextures());
        glBindTexture(GL_TEXTURE_CUBE_MAP, cubemap.id);

        for(int i = 0; i < filePaths.length; i++) {
            TextureUtil.ImageResult image = TextureUtil.loadImage(filePaths[i]);

            glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGB, image.width, image.height, 0, GL_RGB, GL_UNSIGNED_BYTE, image.data);
            stbi_image_free(image.data);
        }

        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);

        return cubemap;
    }

    public static final Mesh<PositionVertex> cubeMesh = new Mesh<>(false);

    static {
        cubeMesh.addVertices(
                new PositionVertex(new Vector3f(-1.0f,  1.0f, -1.0f)),
                new PositionVertex(new Vector3f(-1.0f, -1.0f, -1.0f)),
                new PositionVertex(new Vector3f(1.0f, -1.0f, -1.0f)),
                new PositionVertex(new Vector3f(1.0f, -1.0f, -1.0f)),
                new PositionVertex(new Vector3f(1.0f,  1.0f, -1.0f)),
                new PositionVertex(new Vector3f(-1.0f,  1.0f, -1.0f)),

                new PositionVertex(new Vector3f(-1.0f, -1.0f,  1.0f)),
                new PositionVertex(new Vector3f(-1.0f, -1.0f, -1.0f)),
                new PositionVertex(new Vector3f(-1.0f,  1.0f, -1.0f)),
                new PositionVertex(new Vector3f(-1.0f,  1.0f, -1.0f)),
                new PositionVertex(new Vector3f(-1.0f,  1.0f,  1.0f)),
                new PositionVertex(new Vector3f(-1.0f, -1.0f,  1.0f)),

                new PositionVertex(new Vector3f(1.0f, -1.0f, -1.0f)),
                new PositionVertex(new Vector3f(1.0f, -1.0f,  1.0f)),
                new PositionVertex(new Vector3f(1.0f,  1.0f,  1.0f)),
                new PositionVertex(new Vector3f(1.0f,  1.0f,  1.0f)),
                new PositionVertex(new Vector3f(1.0f,  1.0f, -1.0f)),
                new PositionVertex(new Vector3f(1.0f, -1.0f, -1.0f)),

                new PositionVertex(new Vector3f(-1.0f, -1.0f,  1.0f)),
                new PositionVertex(new Vector3f(-1.0f,  1.0f,  1.0f)),
                new PositionVertex(new Vector3f(1.0f,  1.0f,  1.0f)),
                new PositionVertex(new Vector3f(1.0f,  1.0f,  1.0f)),
                new PositionVertex(new Vector3f(1.0f, -1.0f,  1.0f)),
                new PositionVertex(new Vector3f(-1.0f, -1.0f,  1.0f)),

                new PositionVertex(new Vector3f(-1.0f,  1.0f, -1.0f)),
                new PositionVertex(new Vector3f(1.0f,  1.0f, -1.0f)),
                new PositionVertex(new Vector3f(1.0f,  1.0f,  1.0f)),
                new PositionVertex(new Vector3f(1.0f,  1.0f,  1.0f)),
                new PositionVertex(new Vector3f(-1.0f,  1.0f,  1.0f)),
                new PositionVertex(new Vector3f(-1.0f,  1.0f, -1.0f)),

                new PositionVertex(new Vector3f(-1.0f, -1.0f, -1.0f)),
                new PositionVertex(new Vector3f(-1.0f, -1.0f,  1.0f)),
                new PositionVertex(new Vector3f(1.0f, -1.0f, -1.0f)),
                new PositionVertex(new Vector3f(1.0f, -1.0f, -1.0f)),
                new PositionVertex(new Vector3f(-1.0f, -1.0f,  1.0f)),
                new PositionVertex(new Vector3f(1.0f, -1.0f,  1.0f))
        );
    }
}
