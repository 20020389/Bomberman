package com.snow.bomberman.something;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.LinkedList;

public class ComboKey {
    LinkedList<KeyCode> key;
    LinkedList<Runnable> task;
    int lastSize;

    public ComboKey(Scene scene) {
        key = new LinkedList<>();
        task = new LinkedList<>();
        lastSize = 0;
        addListener(scene);
    }

    public void addListener(Scene scene) {
        scene.addEventHandler(KeyEvent.KEY_PRESSED, (e) -> {
            key.add(e.getCode());
//            System.out.println("down: " + e.getCode());
        });

        scene.addEventHandler(KeyEvent.KEY_RELEASED, (e) -> {
            for (int i = 0; i < key.size();) {
                if (key.get(i) == e.getCode()) {
                    key.remove(i);
                    continue;
                }
                i++;
            }
//            System.out.println("up: " + e.getCode());
        });
    }

    public void clearCode() {
        key.clear();
    }

    public void run() {
        if (lastSize != key.size()) {
            for (Runnable i : task) {
                i.run();
            }
            lastSize = key.size();
        }
    }

    public void addTask(Runnable e, KeyCode keyCode1 , KeyCode keyCode2) {
        task.add(() -> {
            if (key.size() == 2) {
                if (key.get(0).equals(keyCode1) && key.get(1).equals(keyCode2)) {
                    e.run();
                }
            }
        });
    }

    public void addTask(Runnable e, KeyCode keyCode1 , KeyCode keyCode2, KeyCode keyCode3) {
        task.add(() -> {
            if (key.size() == 3) {
                if (key.get(0).equals(keyCode1)
                        && key.get(1).equals(keyCode2)
                        && key.get(2).equals(keyCode3)) {
                    e.run();
                }
            }
        });
    }

    public void removeTask(Runnable e) {
        this.task.remove(e);
    }
}
