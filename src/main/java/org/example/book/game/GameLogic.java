package org.example.book.game;

import org.eclipse.collections.api.factory.primitive.FloatLists;
import org.eclipse.collections.api.factory.primitive.IntLists;
import org.example.book.engine.IAppLogic;
import org.example.book.engine.Window;
import org.example.book.engine.graph.Mesh;
import org.example.book.engine.graph.Renderer;
import org.example.book.engine.scene.Scene;

public final class GameLogic implements IAppLogic {
    @Override
    public void cleanup() {

    }

    @Override
    public void init(Window window, Scene scene, Renderer renderer) {
        final var positions = FloatLists.immutable.of(
                -0.5f, 0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
                0.5f, 0.5f, 0.0f
        );

        final var colors = FloatLists.immutable.of(
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f
        );

        final var indices = IntLists.immutable.of(
                0, 1, 3, 3, 1, 2
        );

        final var mesh = Mesh.create(positions, colors, indices);
        scene.addMesh("cube", mesh);
    }

    @Override
    public void input(Window window, Scene scene, long deltaTime) {

    }

    @Override
    public void update(Window window, Scene scene, long deltaTime) {

    }
}
