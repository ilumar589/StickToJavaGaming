package org.example.book.engine.shader;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record ShaderModuleData(String shaderFilePath, ShaderTypeEnum shaderType) {
}
