package org.example.book.engine.graph;

import org.eclipse.collections.api.factory.Lists;
import org.example.book.engine.scene.Scene;
import org.example.book.engine.shader.ShaderModuleData;
import org.example.book.engine.shader.ShaderProgram;
import org.example.book.engine.shader.ShaderTypeEnum;
import org.jspecify.annotations.NullMarked;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

@NullMarked
public record SceneRenderer(ShaderProgram shaderProgram) implements AutoCloseable {

    public static SceneRenderer getDefaultSceneRenderer() {
        return new SceneRenderer(ShaderProgram.create(Lists.immutable.of(
                new ShaderModuleData("resources/shaders/scene.vert", ShaderTypeEnum.VERTEX_SHADER),
                new ShaderModuleData("resources/shaders/scene.frag", ShaderTypeEnum.FRAGMENT_SHADER)
        )));
    }

    @Override
    public void close() {
        shaderProgram.close();
    }

    public void render(Scene scene) {
        shaderProgram.bind();

        scene.meshMap()
                .values()
                .forEach(mesh -> {
                    glBindVertexArray(mesh.vertexArrayObjectId());
                    glDrawElements(GL_TRIANGLES, mesh.numberOfVertices(), GL_UNSIGNED_INT, 0);
                });

        glBindVertexArray(0);

        shaderProgram.unbind();
    }
}
