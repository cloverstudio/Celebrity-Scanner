package com.clover.celebrities.fragments.home

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.clover.celebrities.Constants
import com.clover.celebrities.R
import com.clover.celebrities.adapters.CelebrityAdapter
import kotlinx.android.synthetic.main.fragment_main.*


class HomeFragment : Fragment() {

    val LOG = "HomeFragment"
    val cameraHeadstartTime = 1000L //in ms

    lateinit var viewModel: HomeViewModel
    lateinit var celebritiesAdapter: CelebrityAdapter
    var firstCreate = true
    var currentImage: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!(::viewModel.isInitialized)) {
            viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(firstCreate) {
            dispatchTakePictureIntent()
            firstCreate = false
            Thread.sleep(cameraHeadstartTime)
        }
        configureActiobBar()
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViewModel()
        configureRecyclerView()
        configureActionButton()
    }

    private fun configureRecyclerView() {
        celebritiesAdapter = CelebrityAdapter()
        rv_celebrity_list.let {
            it.adapter = celebritiesAdapter
            it.layoutManager = LinearLayoutManager(activity!!)
        }
    }

    private fun configureActiobBar() {
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.show()
        actionBar?.setDisplayHomeAsUpEnabled(false)
        actionBar?.setHomeButtonEnabled(false)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_CAMERA && resultCode == RESULT_OK) {
            resetViews()
            val imageBitmap = data?.extras?.get("data") as Bitmap
            currentImage = imageBitmap
            updateUiWith(imageBitmap)
        }
    }

    private fun resetViews() {
        celebritiesAdapter.updateCelebrityList(listOf())
        tv_unrecognized.visibility = View.GONE
        ll_no_match.visibility = View.GONE
    }

    private fun initializeViewModel() {
        viewModel.subscribeToProgressBarVisibility().observe(this, Observer {
            progressBar.visibility = it
            tv_processing.visibility = it
        })
        viewModel.subscribeToRecognizedCelebrities().observe(this, Observer {
            if (it.isEmpty()) {
                ll_no_match.visibility = View.VISIBLE
            } else {
                celebritiesAdapter.updateCelebrityList(it)
            }
        })
        viewModel.subscribeToUnrecognizedCelebrities().observe(this, Observer {
            if (it != 0) {
                val unidentifiedLabel = resources.getString(R.string.unidentified_label)
                tv_unrecognized.text = String.format(unidentifiedLabel, it)
                tv_unrecognized.visibility = View.VISIBLE
            }
        })
    }

    private fun configureActionButton() {
        btn_open_camera.setOnClickListener {
            dispatchTakePictureIntent()
        }
    }

    private fun updateUiWith(imageBitmap: Bitmap) {
        viewModel.getCelebritiesFromImage(imageBitmap)

    }

    fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                startActivityForResult(
                    takePictureIntent,
                    Constants.REQUEST_CAMERA
                )
            }
        }
    }
}

