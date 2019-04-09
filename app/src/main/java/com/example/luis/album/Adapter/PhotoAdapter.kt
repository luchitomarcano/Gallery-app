package com.example.luis.album.Adapter

import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.luis.album.ImageLargeFragment
import com.example.luis.album.Model.Photo
import com.example.luis.album.R

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class PhotoAdapter(private val mContext: Context, private val mPhotos: List<Photo>) : RecyclerView.Adapter<PhotoAdapter.ViewHolder>(), ImageLargeFragment.OnFragmentInteractionListener {

    private val clickSubject = PublishSubject.create<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val requestOptions = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)

        Glide.with(mContext)
                .load(mPhotos[position].thumbnailUrl)
                .apply(requestOptions)
                .into(holder.photo)

        holder.photo.setOnClickListener {
            clickSubject.onNext(mPhotos[position].url)
        }
    }

    override fun getItemCount(): Int {
        return mPhotos.size
    }

    val clickEvent: Observable<String> = clickSubject

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var photo = itemView.findViewById(R.id.photo) as ImageView
    }

    override fun onFragmentInteraction(uri: Uri?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
