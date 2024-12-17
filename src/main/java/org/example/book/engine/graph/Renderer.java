package org.example.book.engine.graph;

import org.example.book.engine.Window;
import org.example.book.engine.scene.Scene;
import org.jspecify.annotations.NullMarked;
import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL11.*;

@NullMarked
public final class Renderer implements AutoCloseable {

    public Renderer() {
        GL.createCapabilities();
    }

    @Override
    public void close() {

    }

    public void render(Window window, Scene scene) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }
}
