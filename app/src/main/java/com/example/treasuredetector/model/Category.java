package com.example.treasuredetector.model;

public class Category {
    private final String name;
    private final int iconResourceId;

    public Category(String name, int iconResourceId) {
        this.name = name;
        this.iconResourceId = iconResourceId;
    }

    public String getName() {
        return name;
    }

    public int getIconResourceId() {
        return iconResourceId;
    }
}
