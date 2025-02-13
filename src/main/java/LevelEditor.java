package src.main.java;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.ImGuiImplGlfw;
import imgui.ImGuiImplGl3;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class LevelEditor {
    private long window;
    private final int width = 1280;
    private final int height = 720;

    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    private ArrayList<Rectangle> tiles;
    private Rectangle selectedTile;

    public LevelEditor() {
        tiles = new ArrayList<>();
        selectedTile = new Rectangle(0, 0, 50, 50); // Default tile size
    }

    public void run() {
        init();
        loop();
        cleanup();
    }

    private void init() {
        GLFW.glfwInit();

        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL11.GL_TRUE);

        window = GLFW.glfwCreateWindow(width, height, "Level Editor", 0, 0);
        if (window == 0) {
            throw new RuntimeException("Failed to create GLFW window");
        }

        GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        GLFW.glfwSetWindowPos(window, (vidMode.width() - width) / 2, (vidMode.height() - height) / 2);

        GLFW.glfwMakeContextCurrent(window);
        GLFW.glfwSwapInterval(1);
        GLFW.glfwShowWindow(window);

        GL.createCapabilities();

        ImGui.createContext();
        ImGuiIO io = ImGui.getIO();
        io.setIniFilename(null); // Avoid saving .ini file
        imGuiGlfw.init(window, true);
        imGuiGl3.init("#version 150");
    }

    private void loop() {
        while (!GLFW.glfwWindowShouldClose(window)) {
            GLFW.glfwPollEvents();

            imGuiGlfw.newFrame();
            ImGui.newFrame();

            showLevelEditor();

            ImGui.render();
            imGuiGl3.renderDrawData(ImGui.getDrawData());

            GLFW.glfwSwapBuffers(window);
        }
    }

    private void showLevelEditor() {
        ImGui.begin("Level Editor");

        if (ImGui.button("Save")) {
            saveLevel();
        }
        ImGui.sameLine();
        if (ImGui.button("Load")) {
            loadLevel();
        }

        // Add more GUI elements here like tile selection, level grid, etc.
        ImGui.text("Select Tile");
        if (ImGui.button("50x50 Tile")) {
            selectedTile = new Rectangle(0, 0, 50, 50);
        }
        if (ImGui.button("100x100 Tile")) {
            selectedTile = new Rectangle(0, 0, 100, 100);
        }

        ImGui.end();
    }

    private void saveLevel() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("level.dat"))) {
            oos.writeObject(tiles);
            System.out.println("Level saved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to save level.");
        }
    }

    private void loadLevel() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("level.dat"))) {
            tiles = (ArrayList<Rectangle>) ois.readObject();
            System.out.println("Level loaded successfully!");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Failed to load level.");
            tiles = new ArrayList<>();
        }
    }

    private void cleanup() {
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();

        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
    }

    public static void main(String[] args) {
        new LevelEditor().run();
    }
}