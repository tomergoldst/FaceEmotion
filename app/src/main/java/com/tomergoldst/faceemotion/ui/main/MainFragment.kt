package com.tomergoldst.faceemotion.ui.main

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tomergoldst.faceemotion.R
import com.tomergoldst.faceemotion.model.Face
import kotlinx.android.synthetic.main.main_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException


class MainFragment : Fragment() {

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // init view
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)
        face_emotion.isVisible = false
        progress_bar.isVisible = false

        // assign browse button click listener
        browse_images_btn.setOnClickListener { pickImageFromGallery() }
    }

    private fun pickImageFromGallery() {
        face_emotion.isVisible = false

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(
            Intent.createChooser(
                intent,
                getString(R.string.title_select_picture)
            ), PICK_IMAGE
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // register observers
        viewModel.face.observe(viewLifecycleOwner, Observer { face ->
            onFaceDetected(face)
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { error ->
            onError(error)
        })
    }

    private fun onFaceDetected(face: Face) {
        progress_bar.isVisible = false
        face_imv.setImageBitmap(face.bitmap)
        face_emotion.text = face.emotion.toString()
        face_emotion.isVisible = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK &&
            data != null && data.data != null
        ) {
            val uri = data.data!!
            try {
                val bitmap =
                    MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
                // show original image
                face_imv.setImageBitmap(bitmap)
                // show progress bar
                progress_bar.isVisible = true
                // start detection process
                viewModel.detect(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun onError(message: String) {
        progress_bar.isVisible = false

        AlertDialog.Builder(context)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK") { _, _ -> }
            .create()
            .show()
    }

    companion object {
        private const val PICK_IMAGE = 1

        fun newInstance() = MainFragment()
    }

}