package com.example.luis.album;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.example.luis.album.Adapter.AlbumAdapter;
import com.example.luis.album.Model.Album;
import com.example.luis.album.Retrofit.IMyAPI;
import com.example.luis.album.Retrofit.RetrofitClient;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements PhotosFragment.OnFragmentInteractionListener, AlbumAdapter.OnItemClickListener, ImageLargeFragment.OnFragmentInteractionListener {

    private ArrayList<Album> mAlbums = new ArrayList<>();

    FrameLayout fragmentContainer;
    ProgressBar progressBar;

    IMyAPI myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentContainer = findViewById(R.id.fragment_container);
        progressBar = findViewById(R.id.progressBarMainActivity);
        //init API
        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(IMyAPI.class);

        fetchAlbumsData();
    }

    private void fetchAlbumsData() {
        compositeDisposable.add(myAPI.getAlbums()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ArrayList<Album>>() {
                    @Override
                    public void accept(ArrayList<Album> albums) throws Exception {
                        mAlbums = albums;
                        fillRecycleView();
                    }
                }));
    }

    private void fillRecycleView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        AlbumAdapter albumAdapter = new AlbumAdapter(MainActivity.this, mAlbums, MainActivity.this);
        GridLayoutManager staggeredGridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(albumAdapter);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onAlbumClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("ALBUM_ID", position + 1);
        bundle.putString("ALBUM_TITLE", mAlbums.get(position).getTitle());
        navigateToAlbumsFragment(bundle);
    }

    private void navigateToAlbumsFragment(Bundle bundle) {
        PhotosFragment photosFragment = PhotosFragment.newInstance(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.add(R.id.fragment_container, photosFragment).commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        compositeDisposable.clear();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}
