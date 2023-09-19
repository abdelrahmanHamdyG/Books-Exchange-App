package com.example.bookexchange.UI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import com.example.bookexchange.AppUtils
import com.example.bookexchange.R
import com.google.android.material.textfield.TextInputEditText

class BooksDetailsActivity : AppCompatActivity() {
    lateinit var image: ImageView
    var imageChoosen=false;
    val IMAGE_PICK_REQUEST_CODE=5;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_books_details)


        image=findViewById<ImageView>(R.id.book_details_image)
        val editName=findViewById<TextInputEditText>(R.id.book_details_edit_name)
        val editDetails=findViewById<TextInputEditText>(R.id.book_details_edit_details)
        val spinner=findViewById<Spinner>(R.id.book_details_spinner)
        val save=findViewById<Button>(R.id.books_details_save)

        val bookKey=intent.getStringExtra("book_key")
        val bookName=intent.getStringExtra("book_name")
        val bookImage=intent.getStringExtra("image_uri")
        val bookBitMap=intent.getByteArrayExtra("image_bitmap")
        val bookDetails=intent.getStringExtra("book_details")
        val bookCategory=intent.getStringExtra("book_category")
        val imageBitmap=BitmapFactory.decodeByteArray(bookBitMap,0,bookBitMap!!.size)




        spinner.setSelection(AppUtils.categoryMap[bookCategory]!!)
        image.setImageBitmap(imageBitmap)
        editName.setText(bookName)
        editDetails.setText(bookDetails)

        save.setOnClickListener {

            val spinnerText=spinner.selectedItem.toString()
            val bookNameText=editName.text.toString()
            val bookDetailsText=editDetails.text.toString()

            if(spinnerText!=bookCategory||bookNameText!=bookName||bookDetailsText!=bookDetails||imageChoosen){





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


        }

    }


}
class CustomSpinnerAdapter(
    context: Context,
    private val items: List<String>
) : ArrayAdapter<String>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent)
    }

    private fun createItemView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item = items[position]

        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.spinner, parent, false)

        val textImageView = view.findViewById<TextView>(R.id.spinner_textView)
        textImageView.text = item

        return view
    }
}
