package org.example.shader;

import org.eclipse.collections.api.list.ImmutableList;
import org.jspecify.annotations.NullMarked;

import static org.lwjgl.opengl.GL20.*;

@NullMarked
public record ShaderProgram(int programId) {

    public ShaderProgram {
        if (programId == 0) {
            System.err.println("Error creating shader program");
        }
    }

    public ShaderProgram(ImmutableList<ShaderModuleData> shaderModules) {
        int programId = glCreateProgram();
        this(programId);

    }

    public ShaderProgram testNullness() {
        return null;
    }

    private int createShader(String shaderCode, ShaderTypeEnum shaderTypeEnum) {
        int shaderId = glCreateShader(shaderTypeEnum.shaderType);

        if (shaderId == 0) {
            System.err.println("Error creating shader. Type: " + shaderTypeEnum.name);
            return 0;
        }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            System.err.println("Error compiling shader code: " + glGetShaderInfoLog(shaderId, 1024));
        }

        return shaderId;
    }
}
