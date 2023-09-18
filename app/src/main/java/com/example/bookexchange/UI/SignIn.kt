package com.example.bookexchange.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import com.example.bookexchange.AppUtils
import com.example.bookexchange.R
import com.example.bookexchange.ViewModels.SignInViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class SignIn : AppCompatActivity() {

    private lateinit var signInViewModel: SignInViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)


        signInViewModel = ViewModelProvider(this)[SignInViewModel::class.java]


        val button=findViewById<AppCompatButton>(R.id.sign_in_button)
        val edit_email=findViewById<TextInputEditText>(R.id.sign_in_edit_email)
        val edit_pass=findViewById<TextInputEditText>(R.id.sign_in_edit_pass)
        val layout_email=findViewById<TextInputLayout>(R.id.sign_in_layout_email)
        val layout_pass=findViewById<TextInputLayout>(R.id.sign_in_layout_password)
        val text_go_to_register=findViewById<TextView>(R.id.sign_in_text_go_to_register)

        val dialogView = layoutInflater.inflate(R.layout.progress_dialog, null)
        val dialogg = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false).create()


        edit_email.addTextChangedListener(textWatcher(edit_email,layout_email))
        edit_pass.addTextChangedListener(textWatcher(edit_pass,layout_pass))

        text_go_to_register.setOnClickListener {

            var intent=Intent(this, Registeration::class.java).apply {
                startActivity(this@apply)
            }

        }

        button.setOnClickListener {


            val emailText = edit_email.text.toString().trim()
            val passwordText = edit_pass.text.toString().trim()

            if(emailText.isEmpty()||passwordText.isEmpty()){

                if(emailText.isEmpty())
                    layout_email.error="Empty"

                if(passwordText.isEmpty())
                    layout_pass.error="Empty"

                return@setOnClickListener
            }

            dialogg.show()
            signInViewModel.login(emailText,passwordText)

        }

        signInViewModel.loginResult.observe(this) {

            AppUtils.LOG("login result observer")
            if(it=="Success"){
                AppUtils.showToast(this, "Welcome My Friend")
                startActivity(Intent(this, MainPage::class.java))
                finish()

            }else{
                AppUtils.showToast(this, it)
            }

            dialogg.dismiss()
        }


    }

    inner class textWatcher(var editText: TextInputEditText,var TIL: TextInputLayout):TextWatcher{
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
