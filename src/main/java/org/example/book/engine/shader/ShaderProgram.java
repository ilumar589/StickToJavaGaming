package org.example.book.engine.shader;

import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.primitive.ImmutableIntList;
import org.example.streamUtils.Utils;
import org.jspecify.annotations.NullMarked;

import static org.lwjgl.opengl.GL20.*;

@NullMarked
public record ShaderProgram(int programId) implements AutoCloseable {

    public ShaderProgram {
        if (programId == 0) {
            System.err.println("Error creating shader program");
        }
    }

    public static ShaderProgram create(ImmutableList<ShaderModuleData> shaderModules) {
        int programId = glCreateProgram();

        if (programId == 0) {
            throw new RuntimeException("Error creating shader program");
        }

        final var shaderIds = shaderModules
                .collectInt(shaderModule ->
                        createShader(programId, Utils.readFile(shaderModule.shaderFilePath()), shaderModule.shaderType()));

        linkProgram(programId, shaderIds);

        return new ShaderProgram(programId);
    }

    @Override
    public void close() {
        unbind();
        if (programId != 0) {
            glDeleteProgram(programId);
        }
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void validate() {
        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024));
        }
    }

    private static int createShader(int programId, String shaderCode, ShaderTypeEnum shaderTypeEnum) {
        int shaderId = glCreateShader(shaderTypeEnum.shaderType);

        if (shaderId == 0) {
            System.err.println("Error creating shader. Type: " + shaderTypeEnum.name);
            return 0;
        }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new RuntimeException("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
        }

        glAttachShader(programId, shaderId);

        return shaderId;
    }

    private static void linkProgram(int programId, ImmutableIntList shaderIds) {
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new RuntimeException("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024));
        }

        shaderIds.forEach(shaderId -> {
            glDetachShader(programId, shaderId);
            glDeleteShader(shaderId);
        });
    }


}
