package com.droid.foodio

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GeminiActivity : AppCompatActivity() {

    private lateinit var selectedImageView: ImageView
    private lateinit var uploadImageButton: Button
    private lateinit var generateRecipesButton: Button
    private lateinit var recipesTextView: TextView

    private lateinit var generativeModel: GenerativeModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gemini)

        initializeViews()
        setupGenerativeModel()
        setupListeners()
    }

    private fun initializeViews() {
        selectedImageView = findViewById(R.id.selectedImageView)
        uploadImageButton = findViewById(R.id.uploadImageButton)
        generateRecipesButton = findViewById(R.id.generateRecipesButton)
        recipesTextView = findViewById(R.id.recipesTextView)
    }

    private fun setupGenerativeModel() {
        generativeModel = GenerativeModel(
            modelName = "gemini-1.5-pro-latest",
            apiKey = "AIzaSyAXMMfxPjR9Vnfv3Ue2ESNrzjBuGK4wtw0"
        )
    }

    private fun setupListeners() {
        uploadImageButton.setOnClickListener { showImageSourceOptions() }
        generateRecipesButton.setOnClickListener { generateRecipes() }
    }

    private fun showImageSourceOptions() {
        val options = arrayOf("Camera", "Gallery")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Image Source")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> checkCameraPermission()  // Check camera permission
                1 -> openGallery()  // Gallery option selected
            }
        }
        builder.show()
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
        } else {
            openCamera()  // Permission granted, open camera
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GALLERY_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    data?.data?.let { uri ->
                        loadBitmapFromUri(uri)
                    }
                }
            }
            CAMERA_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    val bitmap = data?.extras?.get("data") as Bitmap
                    selectedImageView.setImageBitmap(bitmap)
                    generateRecipesButton.isEnabled = true // Enable the button after image is loaded
                }
            }
        }
    }

    private fun loadBitmapFromUri(uri: Uri) {
        val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
        selectedImageView.setImageBitmap(bitmap)
        generateRecipesButton.isEnabled = true // Enable the button after image is loaded
    }

    private fun generateRecipes() {
        val cookieImage = (selectedImageView.drawable as BitmapDrawable).bitmap // Get the bitmap from the ImageView
        val inputContent = content {
            image(cookieImage)
            text("I have the ingredients above. Not sure what to cook. Show me a list of ingredients which I provided and foods with the recipes for breakfast, lunch, snacks, and dinner.")
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = generativeModel.generateContent(inputContent)
                withContext(Dispatchers.Main) {
                    recipesTextView.text = response.text // Update the TextView with the generated text
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Optionally, handle the error (e.g., show a toast message)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Finish this activity and return to MainActivity
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Finish this activity to remove it from the back stack
    }

    companion object {
        private const val GALLERY_REQUEST_CODE = 2000
        private const val CAMERA_REQUEST_CODE = 2001 // Added for camera requests
        private const val CAMERA_PERMISSION_REQUEST_CODE = 3000 // Request code for camera permission
    }
}
