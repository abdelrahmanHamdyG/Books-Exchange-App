package com.example.bookexchange.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.bookexchange.Models.UserData
import com.example.bookexchange.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class InformationOfContactActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information_of_contact)


        val textPhone=findViewById<TextView>(R.id.textViewPhone)
        val textAddress=findViewById<TextView>(R.id.textViewAddress)
        val textName=findViewById<TextView>(R.id.textViewName)


        val dialogView = layoutInflater.inflate(R.layout.progress_dialog, null)
        val dialogg = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false).create()






        dialogg.show()
        val hisKey=intent.extras?.getString("hisKey");
        val databaseReference = FirebaseDatabase.getInstance().getReference("All Users").child(hisKey.toString()).child("DATA")

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userData = snapshot.getValue(UserData::class.java)


                    val name = userData?.name
                    val governorate = userData?.governorate
                    val detailed = userData?.detailed

                    textAddress.text="Address: $governorate, $detailed"
                    textPhone.text="phone:01061571364"
                    textName.text="name : $name";


                    dialogg.dismiss()
                } else {

                    dialogg.dismiss()
                }
            }

            override fun onCancelled(error: DatabaseError) {

                dialogg.dismiss()
            }
        })




    }
}