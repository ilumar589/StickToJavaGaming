package org.example.book.engine;

import org.example.book.engine.graph.Renderer;
import org.example.book.engine.scene.Scene;
import org.example.book.game.GameLogic;

public sealed interface IAppLogic permits GameLogic {
    void cleanup();
    void init(Window window, Scene scene, Renderer renderer);
    void input(Window window, Scene scene, long deltaTime);
    void update(Window window, Scene scene,long deltaTime);
}
