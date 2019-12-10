package com.example.howlslecture

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    lateinit var auth : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        emailloginButton.setOnClickListener {
            createloginandemail()
        }
    }
    fun createloginandemail() {
        auth.createUserWithEmailAndPassword(
            emailEdittext.text.toString(),
            passwordEdittext.text.toString()
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "아이디 생성 성공", Toast.LENGTH_LONG).show() // Toast 메세지를 띄워줌
            } else if (task.exception?.message.isNullOrEmpty()) {
                Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
            } else {
                // 로그인
                signInEmail()


            }
        }
    }
    fun signInEmail() {
        auth.signInWithEmailAndPassword(
            emailEdittext.text.toString(),
            passwordEdittext.text.toString()
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "로그인에 성공하였습니다.", Toast.LENGTH_LONG).show()
                moveMainPage(auth.currentUser)



            } else {
                Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
            }
        }
    }
    fun moveMainPage(user : FirebaseUser?){
            if(user!=null){
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
        }


}
