package com.example.luis.album.Retrofit;


import com.example.luis.album.Model.Album;
import com.example.luis.album.Model.Photo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IMyAPI {
//    @GET("photos?albumId=1")
//    Observable<ArrayList<Photo>> getPhotos();

    @GET("photos")
    Observable<ArrayList<Photo>> getPhotos(@Query("albumId") String albumId);
    @GET("albums?userId=1")
    Observable<ArrayList<Album>> getAlbums();


}
