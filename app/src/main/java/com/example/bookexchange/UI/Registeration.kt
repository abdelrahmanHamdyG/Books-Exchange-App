package com.example.bookexchange.UI

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.lifecycle.ViewModelProvider
import com.example.bookexchange.AppUtils
import com.example.bookexchange.Models.UserData
import com.example.bookexchange.R
import com.example.bookexchange.ViewModels.RegisterationViewModel
import com.google.android.material.textfield.TextInputLayout

class Registeration : AppCompatActivity() {


    lateinit var registirationViewModel:RegisterationViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registeration)

        //registeration
        registirationViewModel = ViewModelProvider(this)[RegisterationViewModel::class.java]




        val button=findViewById<Button>(R.id.registeration_button)
        val spinner=findViewById<Spinner>(R.id.registeration_spinner)
        val edit_pass=findViewById<EditText>(R.id.registeration_edit_pass)
        val edit_phone=findViewById<EditText>(R.id.registeration_edit_phone)
        val edit_email=findViewById<EditText>(R.id.registeration_edit_email)
        val edit_detailed=findViewById<EditText>(R.id.registeration_edit_detailed)
        val edit_fname=findViewById<EditText>(R.id.registeration_edit_fname)
        val edit_lname=findViewById<EditText>(R.id.registeration_edit_lname)
        val layout_pass=findViewById<TextInputLayout>(R.id.registeration_layout_pass)
        val layout_email=findViewById<TextInputLayout>(R.id.registeration_layout_email)
        val layout_detailed=findViewById<TextInputLayout>(R.id.registeration_layout_detailed)
        val layout_phone=findViewById<TextInputLayout>(R.id.registeration_layout_phone)
        val layout_fname=findViewById<TextInputLayout>(R.id.registeration_layout_fname)
        val layout_lname=findViewById<TextInputLayout>(R.id.registeration_layout_lname)

        val list: MutableList<String> = ArrayList()
        list.add("Cairo")
        list.add("Alexandria")
        list.add("KafrElshekh")
        list.add("Gize")
        list.add("Sinaa")
        spinner.adapter=ArrayAdapter(this, R.layout.spinner,list)


        button.setOnClickListener{

                val governorate=spinner.selectedItem.toString()
                val passwordText=edit_pass.text.toString()
                val phoneText=edit_phone.text.toString()
                val fnameText=edit_fname.text.toString()
                val lnameText=edit_lname.text.toString()
                val emailText=edit_email.text.toString()
                val detailedText=edit_detailed.text.toString()
                if(passwordText.isEmpty()||lnameText.isEmpty()||fnameText.isEmpty()||emailText.isEmpty()||detailedText.isEmpty()||phoneText.isEmpty()){



                    if(phoneText.isEmpty())
                        layout_phone.error="Empty"
                    
                    if(emailText.isEmpty())
                        layout_email.error="Empty"

                    if(passwordText.isEmpty())
                        layout_pass.error="Empty"

                    if(fnameText.isEmpty())
                        layout_fname.error="Empty"

                    if(lnameText.isEmpty())
                        layout_lname.error="Empty"

                    if(detailedText.isEmpty())
                        layout_detailed.error="Empty"


                    return@setOnClickListener
                }
                val userData= UserData(fnameText,lnameText,emailText,passwordText,governorate,detailedText,phoneText)
                registirationViewModel.registerAndWriteData(userData)

            }

        registirationViewModel.registerResult.observe(this){

                if(it=="Success"){
                    AppUtils.showToast(this, "Congratulations")
                    startActivity(Intent(this, MainPage::class.java))
                    finish()
                }
                AppUtils.showToast(this, it)

            }



            }


    }
