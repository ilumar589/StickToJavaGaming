package org.example.book.engine;

import org.jspecify.annotations.NullMarked;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.system.MemoryUtil;
import org.tinylog.Logger;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

@NullMarked
public final class Window implements AutoCloseable {

    private final long windowHandle;
    private int width;
    private int height;
    private final Runnable resizeFunction;

    public Window(String title, WindowOptions windowOptions, Runnable resizeFunction) {
        this.resizeFunction = resizeFunction;

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);

        if (windowOptions.compatibleProfile()) {
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_COMPAT_PROFILE);
        } else {
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        }

        int width, height;
        if (windowOptions.width() > 0 && windowOptions.height() > 0) {
            width = windowOptions.width();
            height = windowOptions.height();
        } else {
            glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
            final var videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            if (videoMode == null) {
                throw new IllegalStateException("Unable to get primary monitor video mode");
            }
            width = videoMode.width();
            height = videoMode.height();
        }

        this.windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
        if (windowHandle == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwSetFramebufferSizeCallback(windowHandle, (window, w, h) -> resized(w, h));
        glfwSetErrorCallback((int errorCode, long msgPtr) -> Logger.error("Error code [{}], msg [{}]", errorCode, MemoryUtil.memUTF8(msgPtr)));
        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> keyCallback(key, action));

        glfwMakeContextCurrent(windowHandle);

        if (windowOptions.fps() > 0) {
            glfwSwapInterval(0);
        } else {
            glfwSwapInterval(1);
        }

        glfwShowWindow(windowHandle);

        int[] arrWidth = new int[1];
        int[] arrHeight = new int[1];
        glfwGetFramebufferSize(windowHandle, arrWidth, arrHeight);
        this.width = arrWidth[0];
        this.height = arrHeight[0];
    }

    @Override
    public void close() {
        glfwFreeCallbacks(windowHandle);
        glfwDestroyWindow(windowHandle);
        glfwTerminate();
        GLFWErrorCallback callback = glfwSetErrorCallback(null);
        if (callback != null) {
            callback.free();
        }
    }

    public boolean windowShouldClose() {
        return glfwWindowShouldClose(windowHandle);
    }

    public void update() {
        glfwSwapBuffers(windowHandle);
    }

    public static void pollEvents() {
        glfwPollEvents();
    }

    public boolean isKeyPressed(int key) {
        return glfwGetKey(windowHandle, key) == GLFW_PRESS;
    }

    private void resized(int width, int height) {
        this.width = width;
        this.height = height;
        try {
            this.resizeFunction.run();
        } catch (Exception ex) {
            Logger.error("Error calling resize callback", ex.getMessage());
        }
    }

    private static void keyCallback(int key, int action) {
        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
            glfwSetWindowShouldClose(glfwGetCurrentContext(), true);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
