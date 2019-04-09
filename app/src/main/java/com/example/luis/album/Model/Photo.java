package com.example.luis.album.Model;

public class Photo {

    private String url;
    private String thumbnailUrl;

    public Photo() {}

    public Photo(String url, String thumbnailUrl) {
        this.url = url;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getUrl() {
        return url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
}
