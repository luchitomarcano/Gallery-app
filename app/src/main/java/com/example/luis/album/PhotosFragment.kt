package com.example.luis.album

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.example.luis.album.Adapter.PhotoAdapter
import com.example.luis.album.Model.Photo
import com.example.luis.album.Retrofit.IMyAPI
import com.example.luis.album.Retrofit.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_photos.view.*
import java.util.*

class PhotosFragment : Fragment() {

    private var subscribe: Disposable? = null
    private lateinit var progressBar: ProgressBar

    private var mAlbum: String? = null
    private var mAlbumId: Int = 0
    private var mPhotos = ArrayList<Photo>()

    private var listener: OnFragmentInteractionListener? = null

    private lateinit var myAPI: IMyAPI
    private var compositeDisposable = CompositeDisposable()

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): PhotosFragment {
            val fragment = PhotosFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mAlbum = arguments!!.getString("ALBUM_TITLE")
            mAlbumId = arguments!!.getInt("ALBUM_ID")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_photos, container, false)
        view.titlePhotos.text = mAlbum
        progressBar = view.progressBarPhotosFragment

        //init API
        val retrofit = RetrofitClient.getInstance()
        myAPI = retrofit.create(IMyAPI::class.java)

        fetchData(view)

        return view
    }

    private fun fetchData(view: View) {

        compositeDisposable.add(myAPI.getPhotos(mAlbumId.toString() + "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { photos ->
                    mPhotos = photos
                    displayData(photos, view)
                })
    }

    private fun displayData(photos: ArrayList<Photo>, view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerviewPhoto)
        val photoAdapter = PhotoAdapter(recyclerView.context, photos)

        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        recyclerView.layoutManager = staggeredGridLayoutManager
        recyclerView.adapter = photoAdapter
        subscribe = photoAdapter.clickEvent.subscribe { openImageLarge(it) }
        progressBar.visibility = View.INVISIBLE
    }

    private fun openImageLarge(imageLargeUrl: String) {
        val bundle = Bundle()
        bundle.putString("IMAGE_LARGE_URL", imageLargeUrl)
        val detailPhotoFragment = ImageLargeFragment.newInstance(bundle)
        val fragmentManager = activity!!.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, detailPhotoFragment, "findThisFragment")
                .addToBackStack(null).commit()
    }

    override fun onStop() {
        compositeDisposable.clear()
        super.onStop()
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }
}