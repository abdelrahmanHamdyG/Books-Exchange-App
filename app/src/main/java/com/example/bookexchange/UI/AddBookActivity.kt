package com.example.bookexchange.UI

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import com.example.bookexchange.AppUtils
import com.example.bookexchange.Models.Book
import com.example.bookexchange.Models.UserData
import com.example.bookexchange.R
import com.example.bookexchange.ViewModels.AddBookViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream


class AddBookActivity : AppCompatActivity() {
     val IMAGE_PICK_REQUEST_CODE=5
     lateinit var immage:ImageView
     lateinit var error:ImageView
     lateinit var firebaseAuth:FirebaseAuth
     lateinit var addBookViewModel:AddBookViewModel
     lateinit var dialogg: AlertDialog
     var  imageChoosen=false;

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)


        val spinner=findViewById<Spinner>(R.id.add_book_spinner)
        val layoutName =findViewById<TextInputLayout>(R.id.add_book_layout_name)
        val layoutDetails=findViewById<TextInputLayout>(R.id.add_book_layout_details)
        val editName=findViewById<TextInputEditText>(R.id.add_book_edit_name)
        val editDetails=findViewById<TextInputEditText>(R.id.add_book_edit_details)
        val button=findViewById<AppCompatButton>(R.id.add_book_button)
        immage=findViewById<ImageView>(R.id.add_book_image)
        error=findViewById(R.id.add_book_error)


        editName.addTextChangedListener(textWatcher(editName,layoutName))
        editDetails.addTextChangedListener(textWatcher(editDetails,layoutDetails))

        firebaseAuth=FirebaseAuth.getInstance()

        addBookViewModel=ViewModelProvider(this)[AddBookViewModel::class.java]


        immage.setOnClickListener {

            var intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

            startActivityForResult(intent, IMAGE_PICK_REQUEST_CODE)
        }




        val list: MutableList<String> = ArrayList()
        list.add("Scientific")
        list.add("Religious")
        list.add("Novels")
        list.add("Historic")
        spinner.adapter= ArrayAdapter(this, R.layout.spinner,list)

        addBookViewModel.result.observe(this){

            dialogg.dismiss()

            if(it){
                AppUtils.showToast(this,"the Book is uploaded successfully ")
                finish()
            }else{

                AppUtils.showToast(this,"please check your network")

            }


        }




        button.setOnClickListener {
            var nameText = editName.text.toString().trim()
            var detailsText = editDetails.text.toString().trim()
            var category = spinner.selectedItem.toString()


            if (!imageChoosen || nameText.isEmpty() || detailsText.isEmpty()) {


                if (nameText.isEmpty())
                    layoutName.error = "Empty"

                if (detailsText.isEmpty())
                    layoutDetails.error = "Empty"

                if (!imageChoosen) {
                    error.visibility = View.VISIBLE
                }

                if (!imageChoosen && nameText.isNotEmpty() && detailsText.isNotEmpty()) {
                    Toast.makeText(this, "You have to select image", Toast.LENGTH_SHORT).show()

                }

                return@setOnClickListener
            }

            val dialogView = layoutInflater.inflate(R.layout.progress_dialog, null)
            dialogg = AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false).create()
            dialogg.show()


            val bitmap = (immage.drawable as BitmapDrawable).bitmap
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream)
            val imageByteArray = byteArrayOutputStream.toByteArray()

            val imagename = "${firebaseAuth.currentUser!!.uid.toString()}${System.currentTimeMillis()}"

            val theBook = Book(nameText, detailsText, category, imagename, firebaseAuth.currentUser!!.uid.toString(),"","","Available")

            addBookViewModel.uploadBookAndTheImage(firebaseAuth.currentUser!!.uid,theBook,imageByteArray)


        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==IMAGE_PICK_REQUEST_CODE&& resultCode == Activity.RESULT_OK){

            var  selectedImage=data!!.data
            immage.setImageURI(selectedImage)

            imageChoosen=true;
            error.visibility=View.GONE

        }

    }
    inner class textWatcher(var editText: TextInputEditText,var TIL: TextInputLayout): TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            if(editText.text!!.isNotEmpty()){

                TIL.isErrorEnabled=false;
            }

        }

        override fun afterTextChanged(p0: Editable?) {

        }

    }


}