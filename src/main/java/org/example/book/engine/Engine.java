package org.example.book.engine;

import org.example.book.engine.graph.Renderer;
import org.example.book.engine.scene.Scene;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class Engine implements AutoCloseable {

    public static final int TARGET_UPS = 30;
    private final IAppLogic appLogic;
    private final Window window;
    private final Renderer renderer;
    private final Scene scene;
    private final int targetFps;
    private final int targetUps;
    private boolean running = false;


    public Engine(String windowTitle, WindowOptions windowOptions, IAppLogic appLogic) {
        window = new Window(windowTitle, windowOptions, this::resize);
        targetFps = windowOptions.fps();
        targetUps = windowOptions.ups();
        this.appLogic = appLogic;
        scene = new Scene();
        renderer = new Renderer();

        appLogic.init(window, scene, renderer);
        running = true;
    }

    @Override
    public void close() {
        appLogic.cleanup();
        renderer.close();
        scene.close();
        window.close();
    }

    private void resize() {
        //nothing to do yet
    }

    private void run() {
        long initialTime = System.currentTimeMillis();
        float timeU = 1000.0f / targetUps;
        float timeR = targetFps > 0 ? 1000.0f / targetFps : 0;
        float deltaUpdate = 0;
        float deltaFps = 0;

        long updateTime = initialTime;
        while (running && !window.windowShouldClose()) {
            Window.pollEvents();

            long now = System.currentTimeMillis();
            deltaUpdate += (now - initialTime) / timeU;
            deltaFps += (now - initialTime) / timeR;

            if (targetFps <= 0 || deltaFps >= 1) {
                appLogic.input(window, scene, now - initialTime);
            }

            if (deltaUpdate >= 1) {
                long diffTimeMillis = now - updateTime;
                appLogic.update(window, scene, diffTimeMillis);
                updateTime = now;
                deltaUpdate -= 1;
            }

            if (targetUps <= 0 || deltaFps >= 1) {
                renderer.render(window, scene);
                deltaFps -= 1;
                window.update();
            }

            initialTime = now;
        }
    }

    public void start() {
        running = true;
        run();
    }

    public void stop() {
        running = false;
    }
}
