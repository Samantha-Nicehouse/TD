package com.example.treasuredetector.model;

public class ItemModel {

    int id;
    String name;
    int image;
    public ItemModel(String name, int id, int image) {
        this.name = name;

        this.id = id;
        this.image=image;
    }

    public ItemModel(Integer id, String name, Integer image) {
        this.name = name;

        this.id = id;
        this.image=image;
    }

    public String getName() {
        return name;
    }



    public int getImage() {
        return image;
    }

    public int getId() {
        return id;
    }
}

