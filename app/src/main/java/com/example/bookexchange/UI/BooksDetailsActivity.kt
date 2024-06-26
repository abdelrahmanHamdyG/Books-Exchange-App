package com.example.bookexchange.UI

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import com.example.bookexchange.AppUtils
import com.example.bookexchange.Models.Book
import com.example.bookexchange.R
import com.example.bookexchange.ViewModels.AddBookViewModel
import com.example.bookexchange.ViewModels.MyBooksViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import java.io.ByteArrayOutputStream

class BooksDetailsActivity : AppCompatActivity(),TextWatcher {
    lateinit var image: ImageView
    lateinit var myBooksViewModel: MyBooksViewModel
    lateinit var addBookViewModel:AddBookViewModel
    lateinit var dialogg:Dialog
    private var imageChoosen=false;
    lateinit var job1: Job;
    private val IMAGE_PICK_REQUEST_CODE=5;
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var editName:TextInputEditText
    lateinit var editDetails:TextInputEditText
    lateinit var bookName:String
    lateinit var bookCategory:String
    lateinit var bookDetails:String
    lateinit var textCategory: TextView
    lateinit var spinner:Spinner
    lateinit var save:AppCompatButton
    var flag=false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_books_details)


        val editable=intent.getBooleanExtra("editable",true);


        addBookViewModel=ViewModelProvider(this)[AddBookViewModel::class.java]
        myBooksViewModel= MyBooksViewModel()

        image=findViewById<ImageView>(R.id.book_details_image)
        textCategory=findViewById<TextView>(R.id.book_details_text_category)
        editName=findViewById<TextInputEditText>(R.id.book_details_edit_name)
        editDetails=findViewById<TextInputEditText>(R.id.book_details_edit_details)
        spinner=findViewById<Spinner>(R.id.book_details_spinner)
        save=findViewById<AppCompatButton>(R.id.books_details_save)

        spinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {


                save.isEnabled = editDetails.text.toString()!=bookDetails||editName.text.toString()!=bookName||
                        spinner.selectedItem.toString()!=bookCategory||imageChoosen
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }



        if(!editable){
            textCategory.visibility=View.VISIBLE

            editName.isEnabled=false;
            editDetails.isEnabled=false;
            spinner.visibility=View.GONE;
            image.isEnabled=false;
            save.visibility=View.GONE;
        }

        val bookKey=intent.getStringExtra("book_key")
        bookName= intent.getStringExtra("book_name").toString()
        val bookImage=intent.getStringExtra("image_uri")
        val bookBitMap=intent.getByteArrayExtra("image_bitmap")
        bookDetails= intent.getStringExtra("book_details")!!
        val book_bid=intent.getIntExtra("book_bid",300)
        bookCategory= intent.getStringExtra("book_category")!!
        val imageBitmap=BitmapFactory.decodeByteArray(bookBitMap,0,bookBitMap!!.size)

        editName.setText(bookName)
        editDetails.setText(bookDetails)
        textCategory.text="Category: $bookCategory"
        editName.addTextChangedListener(this)
        editDetails.addTextChangedListener(this)

        firebaseAuth=FirebaseAuth.getInstance()

        spinner.setSelection(AppUtils.categoryMap[bookCategory]!!)

        save.isEnabled=false;

        image.setImageBitmap(imageBitmap)

        val dialogView = layoutInflater.inflate(R.layout.progress_dialog, null)
        dialogg = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false).create()

        addBookViewModel.result.observe(this){

            dialogg.dismiss()
            if(it){
                AppUtils.showToast(this,"Done Successfully")
                finish()
            }else{
                AppUtils.showToast(this,"Please check you connection ")
            }


        }



        save.setOnClickListener {

            val spinnerText=spinner.selectedItem.toString()
            val bookNameText=editName.text.toString()
            val bookDetailsText=editDetails.text.toString()

            if(spinnerText!=bookCategory||bookNameText!=bookName||bookDetailsText!=bookDetails||imageChoosen){



                dialogg.show()

                flag=true;
                job1=GlobalScope.launch {
                    val job1 = async {
                        myBooksViewModel.removeValue(bookKey.toString(), firebaseAuth.currentUser!!.uid,bookImage.toString());
                    }

                    if(job1.await()){
                        val bitmap = (image.drawable as BitmapDrawable).bitmap
                        val byteArrayOutputStream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                        val imageByteArray = byteArrayOutputStream.toByteArray()
                        val imagename = "${firebaseAuth.currentUser!!.uid.toString()}${System.currentTimeMillis()}"





                        addBookViewModel.uploadBookAndTheImage(firebaseAuth.currentUser!!.uid,
                            Book(book_bid,bookNameText,bookDetailsText,spinnerText,imagename,firebaseAuth.currentUser!!.uid,"Available"),imageByteArray,2)

                    }


                }

            }

        }
        image.setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

            startActivityForResult(intent, IMAGE_PICK_REQUEST_CODE)
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==IMAGE_PICK_REQUEST_CODE&& resultCode == Activity.RESULT_OK){

            val  selectedImage=data!!.data
            image.setImageURI(selectedImage)

            imageChoosen=true;
            save.isEnabled=true;


        }

    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {


        save.isEnabled = editDetails.text.toString()!=bookDetails||editName.text.toString()!=bookName||
                spinner.selectedItem.toString()!=bookCategory||imageChoosen



    }

    override fun afterTextChanged(p0: Editable?) {

    }

    override fun onStop() {
        super.onStop()
        if(flag)
            job1.cancel()
    }

}

