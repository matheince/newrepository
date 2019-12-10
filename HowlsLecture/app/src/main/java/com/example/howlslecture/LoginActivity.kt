package com.example.howlslecture

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    lateinit var auth : FirebaseAuth
    var googleSignInClient : GoogleSignInClient? = null
    var GOOGLE_lOGIN_CODE = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        emailloginButton.setOnClickListener {
            createloginandemail()
        }
        google_sign_in_button.setOnClickListener {
            googleLogin()
        }
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()   // 코드를 끝내겠다. 조립 완성
        googleSignInClient = GoogleSignIn.getClient(this,gso)

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
    fun googleLogin(){
        var signInIntent = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent,GOOGLE_lOGIN_CODE)

    }
    fun firebaseAuthWithGoogle(accout : GoogleSignInAccount){
        var credential = GoogleAuthProvider.getCredential(accout.idToken,null)
        auth.signInWithCredential(credential)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==GOOGLE_lOGIN_CODE){
            var result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if(result.isSuccess){
                var accout = result.signInAccount
                firebaseAuthWithGoogle(accout!!)
            }
        }
    }

}
