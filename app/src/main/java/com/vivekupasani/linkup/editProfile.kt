package com.vivekupasani.linkup

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import com.vivekupasani.linkup.databinding.ActivityEditprofileBinding
import com.vivekupasani.linkup.models.UserModel

class editProfile : AppCompatActivity() {

    private lateinit var binding: ActivityEditprofileBinding
    private var imageUrl: Uri? = null
    private var currentImageUrl: String? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var storage: StorageReference
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(SystemBarStyle.dark(Color.TRANSPARENT))
        binding = ActivityEditprofileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance().getReference("profilePics")

        binding.btnCancelUpdate.setOnClickListener {
            val builder = AlertDialog.Builder(this)
                .setTitle("Link Up")
                .setMessage("Discard changes?")
                .setPositiveButton("Yes") { dialog, which ->
                    finish()
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }

        getdataforEdit() // fetch data for edit
        selectImagefromDevice()  // image select

        binding.btnUpdate.setOnClickListener {
            val builder = AlertDialog.Builder(this)
                .setTitle("Link Up")
                .setMessage("Update data?")
                .setPositiveButton("Yes") { dialog, which ->
                    updateData()
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    fun setProgressBar(inProgress: Boolean) {
        binding.progressBar.isVisible = inProgress
    }

    private fun updateData() {
        setProgressBar(true)
        // Update data here
        imageUrl?.let {
            storage.child(auth.currentUser!!.uid)
                .putFile(it)
                .addOnSuccessListener {
                    storage.child(auth.currentUser!!.uid).downloadUrl.addOnSuccessListener { uri ->
                        updateAtFirestore(uri.toString())
                    }.addOnSuccessListener {
                        finish()
                    }
                }
        } ?: run {
            // If no image is selected, update Firestore with the current image URL
            updateAtFirestore(currentImageUrl)
        }
    }

    private fun updateAtFirestore(profilePicUrl: String?) {
        val name = binding.updateName.text.toString()
        val profession = binding.updateProfession.text.toString()
        val bio = binding.updateBio.text.toString()
        val email = binding.editEmail.text.toString()
        val password = binding.editPassword.text.toString()

        val user =
            UserModel(auth.currentUser!!.uid, name, email, password, profession, bio, profilePicUrl)

        firestore.collection("Users").document(auth.currentUser!!.uid)
            .set(user)
            .addOnSuccessListener {
                // Optionally show a success message
                finish()
                Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show()
                setProgressBar(false)
            }
            .addOnFailureListener { e ->
                // Optionally handle the error
                Toast.makeText(this, "Failed to Update", Toast.LENGTH_SHORT).show()
                setProgressBar(false)
            }

    }

    private fun getdataforEdit() {
        // Retrieve data from intent extras
        val name = intent.getStringExtra("name")
        val profession = intent.getStringExtra("profession")
        val bio = intent.getStringExtra("bio")
        val mail = intent.getStringExtra("email")
        val pass = intent.getStringExtra("password")
        val propic = intent.getStringExtra("profilepic")
        currentImageUrl = propic // Store the current image URL

        // Populate EditText fields with retrieved data
        binding.updateName.setText(name)
        binding.updateProfession.setText(profession)
        binding.updateBio.setText(bio)
        binding.editEmail.setText(mail)
        binding.editPassword.setText(pass)
        setProgressBar(true)
        Picasso.get().load(propic).noFade().into(binding.updateImage)
        setProgressBar(false)
    }

    private fun selectImagefromDevice() {
        // Setup image picker using ActivityResultContracts.StartActivityForResult()
        val pickImage =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val intent = result.data
                    val selectedImageUri = intent?.data
                    binding.updateImage.setImageURI(selectedImageUri)
                    imageUrl = selectedImageUri
                }
            }
        // Set up click listener for selecting an image
        binding.updateImage.setOnClickListener {
            val builder = AlertDialog.Builder(this)
                .setTitle("Link Up")
                .setMessage("What do you want to do?")
                .setPositiveButton("Select") { dialog, which ->
                    val intent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    pickImage.launch(intent)
                }
                .setNegativeButton("Remove") { dialog, which ->
                    binding.updateImage.setImageResource(R.drawable.default_user_photo)
                    imageUrl = null // Clear the selected image URL
                    currentImageUrl = null // Clear the current image URL
                }
                .setNeutralButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()

                })
                .show()
        }
    }
}
