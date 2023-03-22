package com.example.firestore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.firestore.Adapter.pdf_adapter
import com.example.firestore.databinding.ActivityMain2Binding
import com.example.testfirebase.data.pdf
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity2 : AppCompatActivity() {
    lateinit var data: ArrayList<pdf>
    lateinit var pdf_adapter: pdf_adapter
    lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        data = ArrayList<pdf>()
        data.clear()
        db = Firebase.firestore


        pdf_adapter = pdf_adapter(this, data)
        binding.listV.adapter = pdf_adapter
        fetchData()

        binding.imageView2.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }
    }

    fun fetchData() {
        var id = 0
        db.collection("pdfNames").get().addOnSuccessListener { result ->
            for (document in result) {
                val name = document.data.get("name").toString()

                val contactObj = pdf(id, name)
                data.add(contactObj)
                id++

                Log.d(
                    "zizo", "$id $name .... ${data.size}"
                )
                pdf_adapter.notifyDataSetChanged()

            }
        }.addOnFailureListener { exception ->
            Log.w("TAGGGGGG", "Error getting documents.", exception)
        }
    }

}
