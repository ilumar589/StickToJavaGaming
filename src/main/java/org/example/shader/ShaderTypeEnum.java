package org.example.shader;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

public enum ShaderTypeEnum {
    VERTEX_SHADER("Vertex shader type", GL_VERTEX_SHADER),
    FRAGMENT_SHADER("Fragment shader type", GL_FRAGMENT_SHADER);

    public final String name;
    public final int shaderType;

    ShaderTypeEnum(String name, int shaderType) {
        this.name = name;
        this.shaderType = shaderType;
    }
}
