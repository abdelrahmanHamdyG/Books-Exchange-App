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


    lateinit var registerationViewModel:RegisterationViewModel
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registeration)


        registerationViewModel = ViewModelProvider(this)[RegisterationViewModel::class.java]




        val button=findViewById<Button>(R.id.registeration_button)
        val spinner=findViewById<Spinner>(R.id.registeration_spinner)
        val edit_pass=findViewById<EditText>(R.id.registeration_edit_pass)
        val edit_email=findViewById<EditText>(R.id.registeration_edit_email)
        val edit_detailed=findViewById<EditText>(R.id.registeration_edit_detailed)
        val edit_name=findViewById<EditText>(R.id.registeration_edit_name)
        val layout_pass=findViewById<TextInputLayout>(R.id.registeration_layout_pass)
        val layout_email=findViewById<TextInputLayout>(R.id.registeration_layout_email)
        val layout_detailed=findViewById<TextInputLayout>(R.id.registeration_layout_detailed)
        val layout_name=findViewById<TextInputLayout>(R.id.registeration_layout_name)

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
                val nameText=edit_name.text.toString()
                val emailText=edit_email.text.toString()
                val detailedText=edit_detailed.text.toString()
                if(passwordText.isEmpty()||nameText.isEmpty()||emailText.isEmpty()||detailedText.isEmpty()){



                    if(emailText.isEmpty())
                        layout_email.error="Empty"

                    if(passwordText.isEmpty())
                        layout_pass.error="Empty"

                    if(nameText.isEmpty())
                        layout_name.error="Empty"

                    if(detailedText.isEmpty())
                        layout_detailed.error="Empty"


                    return@setOnClickListener
                }
                val userData= UserData(nameText,emailText,passwordText,governorate,detailedText)
                registerationViewModel.registerAndWriteData(userData)

            }

            registerationViewModel.registerResult.observe(this){
                AppUtils.LOG("this is  observer in registeration")
                if(it=="Success"){
                    AppUtils.showToast(this, "Congratulations")
                    startActivity(Intent(this, MainPage::class.java))
                    finish()
                }
                AppUtils.showToast(this, it)

            }



            }


    }
