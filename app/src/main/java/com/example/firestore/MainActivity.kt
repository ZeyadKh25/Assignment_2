package com.example.firestore

import android.app.DownloadManager
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.Video.Media
import android.util.Log
import android.widget.Toast
import com.example.firestore.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class MainActivity : AppCompatActivity() {
    val reqCode: Int = 100
    lateinit var pdfPath: Uri
    lateinit var name: String
    lateinit var process: ProgressDialog
    lateinit var db: FirebaseFirestore
    lateinit var storage: FirebaseStorage
    lateinit var binding: ActivityMainBinding

    companion object {
        lateinit var storageRef: StorageReference
        lateinit var ref: StorageReference
        lateinit var dm: DownloadManager
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        dm = getSystemService(AppCompatActivity.DOWNLOAD_SERVICE) as DownloadManager


        process = ProgressDialog(this)
        process.setCancelable(false)
        process.setMessage("Uploading PDF..")
        val i = Intent(this, MainActivity2::class.java)



        storageRef = FirebaseStorage.getInstance().reference
        db = Firebase.firestore
        ref = storageRef
        storage = Firebase.storage


        binding.choosePdf.setOnClickListener {
            name = "2023" + ((10000..99999).random()).toString()
//            name = binding.edPdfName.text.toString()
            val ii = Intent()
            ii.type = "application/pdf"
            ii.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(ii, "SELECT PDF FILE"), reqCode)
        }


        binding.show.setOnClickListener {
            startActivity(i)
        }

        binding.UploadePdf.setOnClickListener {
            if (name.isEmpty()) {
                Toast.makeText(this, "FILL DATA !!", Toast.LENGTH_SHORT).show()
            } else {
                name = "2023" + ((10000..99999).random()).toString()
                process.show()
                ref.child("$name.pdf/").putFile(pdfPath).addOnSuccessListener {
                    Toast.makeText(this, "Sucsses", Toast.LENGTH_SHORT).show()
                    if (process.isShowing) {
                        process.dismiss()
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                    if (process.isShowing) {
                        process.dismiss()
                    }
                }
                addToDB("$name.pdf")
            }
        }


    }
//=====================================================================================================

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == reqCode) {
            pdfPath = data!!.data!!
        }
    }


    fun addToDB(name: String) {
        val pdf = hashMapOf("name" to name)

        db.collection("pdfNames").add(pdf).addOnSuccessListener {
            Toast.makeText(this, "sucsess ${it.id}", Toast.LENGTH_SHORT).show()
            if (process.isShowing) {
                process.dismiss()
            }
            Log.e("zzzzz", "sucsess")
        }.addOnFailureListener {
            Toast.makeText(this, "faild ${it.message}", Toast.LENGTH_SHORT).show()
            Log.e("zzzzz", "faild ${it.message}")
        }
    }
}