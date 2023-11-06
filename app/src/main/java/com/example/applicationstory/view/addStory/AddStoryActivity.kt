package com.example.applicationstory.view.addStory

import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.applicationstory.R
import com.example.applicationstory.databinding.ActivityAddStoryBinding
import com.example.applicationstory.view.main.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private var currentImageUri: Uri? = null
    private lateinit var viewModel: AddStoryViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLat: Float? = null
    private var currentLon: Float? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(AddStoryViewModel::class.java)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.uploadButton.setOnClickListener { uploadImage() }

        fun allPermissionsGranted() =
            ContextCompat.checkSelfPermission(
                this,
                REQUIRED_PERMISSION
            ) == PackageManager.PERMISSION_GRANTED

        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
                }
            }

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.switchLocation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                requestLocationPermission()
            } else {
                currentLat = null
                currentLon = null
            }
        }

        setupView()
        playAnimation()
    }

    private fun requestLocationPermission() {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        val requestCode = 123

        if (ContextCompat.checkSelfPermission(this, permission)
            == PackageManager.PERMISSION_GRANTED
        ) {
            getMyLocation()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(permission),
                requestCode
            )
        }
    }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        currentLat = location.latitude.toFloat()
                        currentLon = location.longitude.toFloat()
                    }
                }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            val description = (binding.editDesciption.text.toString())

            showLoading(true)

            val token = intent.getStringExtra(EXTRA_TOKEN)

            viewModel.uploadImage(
                imageFile,
                description,
                token,
                currentLat,
                currentLon,
                onSuccess = { successResponse ->
                    showToast(successResponse.message)
                    showLoading(false)
                    val intent = Intent(this@AddStoryActivity, MainActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                },
                onError = { errorMessage ->
                    showToast(errorMessage)
                    showLoading(false)
                }
            )

        } ?: showToast(getString(R.string.empty_image_warning))
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingSpinner.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
        const val EXTRA_TOKEN = "extra_token"
    }

    private fun setupView() {
        supportActionBar?.apply {
            title = getString(R.string.add_story)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun playAnimation() {

        val gallery = ObjectAnimator.ofFloat(binding.galleryButton, View.ALPHA, 1f).setDuration(200)
        val camera = ObjectAnimator.ofFloat(binding.cameraButton, View.ALPHA, 1f).setDuration(200)
        val upload = ObjectAnimator.ofFloat(binding.uploadButton, View.ALPHA, 1f).setDuration(200)
        val preview = ObjectAnimator.ofFloat(binding.previewImageView, View.ALPHA, 1f).setDuration(200)
        val switchLocation = ObjectAnimator.ofFloat(binding.switchLocation, View.ALPHA, 1f).setDuration(200)

        AnimatorSet().apply {
            playSequentially(gallery, camera, upload, preview, switchLocation)
            start()
        }
    }
}