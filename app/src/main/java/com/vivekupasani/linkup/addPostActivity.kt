package com.vivekupasani.linkup

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.vivekupasani.linkup.databinding.ActivityAddpostBinding
import com.vivekupasani.linkup.models.postModel
import com.vivekupasani.linkup.models.userModel

class addPostActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddpostBinding
    private var postUri: Uri? = null

    lateinit var storageReference: StorageReference
    lateinit var firestore: FirebaseFirestore
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddpostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        storageReference = FirebaseStorage.getInstance().reference.child("Posts")
        auth = FirebaseAuth.getInstance()

        pickImage()

        binding.btnCanclePost.setOnClickListener {
            finish()
        }

        binding.btnPost.setOnClickListener {
            if (postUri == null) {
                Toast.makeText(this, "Select Image", Toast.LENGTH_SHORT).show()
            } else {
                fetchUserDataAndUploadPost()
            }
        }

    }

    private fun fetchUserDataAndUploadPost() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            firestore.collection("Users").document(currentUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val user = document.toObject(userModel::class.java)
                        if (user != null) {
                            uploadToFirestore(user)
                        } else {
                            Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to fetch user data", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun uploadToFirestore(user: userModel) {
        setProgressBar(true)
        postUri?.let {
            val fileReference = storageReference.child(auth.currentUser!!.uid + System.currentTimeMillis())
            fileReference.putFile(it)
                .addOnSuccessListener {
                    fileReference.downloadUrl.addOnSuccessListener { url ->
                        val caption = binding.postCaption.text.toString()
                        val postId = auth.currentUser!!.uid + System.currentTimeMillis().toString()
                        val post = postModel(postId, auth.currentUser!!.uid, user.name, user.profilePic, caption, url.toString(),System.currentTimeMillis())

                        firestore.collection("Posts")
                            .document(postId)
                            .set(post)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Uploaded Successfully", Toast.LENGTH_SHORT).show()
                                setProgressBar(false)
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Unable to upload", Toast.LENGTH_SHORT).show()
                                setProgressBar(false)
                            }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Upload Failed", Toast.LENGTH_SHORT).show()
                    setProgressBar(false)
                }
        }
    }

    private fun setProgressBar(inProgress: Boolean) {
        binding.progressBar.isVisible = inProgress
    }

    private fun pickImage() {
        val pickPost =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val intent = result.data
                    val post = intent?.data
                    binding.afterAdd.setImageURI(post)
                    postUri = post

                    binding.beforeAdd.isVisible = false
                    binding.afterAdd.isVisible = true
                }
            }

        binding.getImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickPost.launch(intent)
        }

        binding.beforeAdd.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickPost.launch(intent)
        }

        binding.beforeAdd.isVisible = true
        binding.afterAdd.isVisible = false
    }
}
