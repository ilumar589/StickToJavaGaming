package org.example.book.game;

import org.eclipse.collections.api.factory.primitive.FloatLists;
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
                0.0f, 0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f
        );

        final var mesh = Mesh.create(positions, 3);
        scene.addMesh("cube", mesh);
    }

    @Override
    public void input(Window window, Scene scene, long deltaTime) {

    }

    @Override
    public void update(Window window, Scene scene, long deltaTime) {

    }
}
