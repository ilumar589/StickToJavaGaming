package org.example.book.engine;

public record WindowOptions(boolean compatibleProfile,
                            int fps,
                            int width,
                            int height,
                            int ups) {


    public static WindowOptions getDefault() {
        return new WindowOptions(false, 60, 1280, 720, Engine.TARGET_UPS);
    }
}
