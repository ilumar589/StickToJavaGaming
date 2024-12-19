package org.example.book.engine.graph;

import org.example.book.engine.Window;
import org.example.book.engine.scene.Scene;
import org.jspecify.annotations.NullMarked;
import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL11.*;

@NullMarked
public record Renderer(SceneRenderer sceneRenderer) implements AutoCloseable {

    public static Renderer getDefaultRenderer() {
        GL.createCapabilities();
        return new Renderer(SceneRenderer.getDefaultSceneRenderer());
    }

    @Override
    public void close() {
        sceneRenderer.close();
    }

    public void render(Window window, Scene scene) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glViewport(0,0, window.getWidth(), window.getHeight());

        sceneRenderer.render(scene);
    }
}
