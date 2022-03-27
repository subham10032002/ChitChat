package com.example.chitchat

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    val storage by lazy {
        FirebaseStorage.getInstance()
    }
    val auth by lazy {
        FirebaseAuth.getInstance()
    }
    val database by lazy {
        FirebaseFirestore.getInstance()
    }
    lateinit var downloadUrl: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        userImgView.setOnClickListener{
            checkPermissionImage()
        }

        btn_next.setOnClickListener {
            btn_next.isEnabled=false
            val name = nameEt.text.toString()
            if(name.isEmpty())
                Toast.makeText(this,"Name cannot be empty",Toast.LENGTH_SHORT).show()
            else if(!::downloadUrl.isInitialized)
                Toast.makeText(this,"Image cannot be empty",Toast.LENGTH_SHORT).show()
            else{
                val bundle: Bundle? = intent.extras
               val email: String? = bundle?.getString("emaill")
                val user = UserModel(name,email,downloadUrl,downloadUrl,auth.uid!!)
                database.collection("users").document(auth.uid!!).set(user).addOnSuccessListener {
                    startActivity(
                        Intent(this,HomeActivity::class.java)
                    )
                    finish()
                }.addOnFailureListener{
                    btn_next.isEnabled=true
                }

            }

        }
    }

    private fun checkPermissionImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                && (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
            ) {
                val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                val permissionWrite = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

                requestPermissions(
                    permission,
                    1001
                ) // GIVE AN INTEGER VALUE FOR PERMISSION_CODE_READ LIKE 1001
                requestPermissions(
                    permissionWrite,
                    1002
                ) // GIVE AN INTEGER VALUE FOR PERMISSION_CODE_WRITE LIKE 1002
            } else {
                pickImageFromGallery()
            }
        }
    }

    private fun pickImageFromGallery() {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,1000)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode==1000){
            data?.data?.let{
                userImgView.setImageURI(it)
                uploadImage(it)
            }
        }
    }

    private fun uploadImage(it: Uri) {
        btn_next.isEnabled=false
        val ref = storage.reference.child("uploads/"+auth.uid.toString())
        val uploadTask = ref.putFile(it)
        uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if(!task.isSuccessful){
                task.exception?.let {
                    throw  it
                }
            }
            return@Continuation ref.downloadUrl
        }).addOnCompleteListener{ task ->
            btn_next.isEnabled=true
            if(task.isSuccessful){
                downloadUrl = task.result.toString()
                Log.i("URL","downloadUrl: $downloadUrl")
            }

        }.addOnFailureListener {
            Toast.makeText(this, "Error occurred $it",Toast.LENGTH_SHORT).show()
        }
    }

}