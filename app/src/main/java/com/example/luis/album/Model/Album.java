package com.example.luis.album.Model;

public class Album {

    private int id;
    private String title;

    public Album() {}

    public Album(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
