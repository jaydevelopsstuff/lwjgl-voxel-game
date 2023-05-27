package net.jay.voxelgame.render.gl;

import net.jay.voxelgame.util.FileUtil;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {
    private final int programId;
    private int vertexShaderId;
    private int fragmentShaderId;

    public ShaderProgram() {
        this.programId = glCreateProgram();
    }

    public int getUniformLocation(String name) {
        return glGetUniformLocation(programId, name);
    }

    public void updateUniformMatrix4f(String name, FloatBuffer fb) {
        glUniformMatrix4fv(getUniformLocation(name), false, fb);
    }

    public void createVertexShader(String filePath) throws IOException {
        createVertexShaderCode(FileUtil.getResourceFileAsString(filePath));
    }

    public void createFragmentShader(String filePath) throws IOException {
        createFragmentShaderCode(FileUtil.getResourceFileAsString(filePath));
    }

    public void createVertexShaderCode(String code) {
        vertexShaderId = createShader(code, GL_VERTEX_SHADER);
        glAttachShader(programId, vertexShaderId);
    }

    public void createFragmentShaderCode(String code) {
        fragmentShaderId = createShader(code, GL_FRAGMENT_SHADER);
        glAttachShader(programId, fragmentShaderId);
    }

    public void link() {
        glLinkProgram(programId);
        if(glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new RuntimeException("Error linking Shader code: " + glGetShaderInfoLog(programId, 1024));
        }

        glValidateProgram(programId);
        if(glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetShaderInfoLog(programId, 1024));
        }
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void destroy() {
        if(programId == 0) return;
        if(vertexShaderId != 0) glDetachShader(programId, vertexShaderId);
        if(fragmentShaderId != 0)
            glDetachShader(programId, fragmentShaderId);
        glDeleteProgram(programId);
    }

    protected static int createShader(String code, int type) {
        int shaderId = glCreateShader(type);
        if(shaderId == MemoryUtil.NULL) {
            throw new RuntimeException("Failed to create shader!");
        }

        glShaderSource(shaderId, code);
        glCompileShader(shaderId);

        if(glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new RuntimeException("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
        }

        return shaderId;
    }

    public int programId() {
        return programId;
    }

    public int vertexShaderId() {
        return vertexShaderId;
    }

    public int fragmentShaderId() {
        return fragmentShaderId;
    }
}
