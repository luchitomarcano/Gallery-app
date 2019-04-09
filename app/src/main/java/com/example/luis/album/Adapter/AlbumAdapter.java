package com.example.luis.album.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.luis.album.Model.Album;
import com.example.luis.album.R;

import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private static final String TAG = "AlbumAdapter";
    private ArrayList<Album> mAlbums = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private Context mContext;

    public AlbumAdapter(Context context, ArrayList<Album> albums, OnItemClickListener onItemClickListener) {
        this.mAlbums = albums;
        this.mContext = context;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album, parent, false);
        return new ViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder called");
        holder.title.setText(mAlbums.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mAlbums.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView title;
        OnItemClickListener onItemClickListener;

        public ViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            this.title = itemView.findViewById(R.id.albumTitle);
            this.onItemClickListener = onItemClickListener;
            title.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onAlbumClick(getAdapterPosition());
        }
    }

    public interface OnItemClickListener {
        void onAlbumClick(int position);
    }
}
