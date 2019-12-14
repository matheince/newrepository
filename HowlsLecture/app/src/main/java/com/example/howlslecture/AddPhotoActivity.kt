package com.example.howlslecture

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.howlslecture.model.ContentDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import kotlinx.android.synthetic.main.activity_add_photo.*
import java.text.SimpleDateFormat
import java.util.*


class AddPhotoActivity : AppCompatActivity() {
    val PICK_IMAGE_FROM_ALBUM = 0

    var photoUri : Uri? = null
    var auth : FirebaseAuth? = null //FirebaseAuth 선언
    var storage : FirebaseStorage? = null
    var firestore : FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_photo)
        storage = FirebaseStorage.getInstance()     // FirebaseStorage 초기화
        auth = FirebaseAuth.getInstance()           // FirebaseAuth 초기화
        firestore = FirebaseFirestore.getInstance() // FirebaseFirestore  초기화

        var photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent,PICK_IMAGE_FROM_ALBUM)

        addphoto_image.setOnClickListener {

            photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent,PICK_IMAGE_FROM_ALBUM)
        }
        image_upload_btn.setOnClickListener {
            contentUpload()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_FROM_ALBUM) {
            if (resultCode == Activity.RESULT_OK) {
                addphoto_image.setImageURI(data?.data)

                photoUri = data?.data
                System.out.println(photoUri)

            }
            else{
                finish()   // 사진 선택이 안될 시 사진올리기 창 닫음
            }

        }
    }
    fun contentUpload(){
        val timeStamp = SimpleDateFormat("yyyymmdd_hhmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_.png"
        val storageRef = storage?.reference?.child("images")?.child(imageFileName)

        storageRef?.putFile(photoUri!!)?.addOnSuccessListener { taskSnapshot ->


            Toast.makeText(this,getString(R.string.upload_success),Toast.LENGTH_LONG).show()
            // 업로드된 이미지 주소
            var contentDTO = ContentDTO()

            var uri =taskSnapshot.downloadUrl

            // 이미지 주소
            contentDTO.imageUrl = uri.toString()

            // 유저의 UID
            contentDTO.uid =auth?.currentUser?.uid

            // 게시물 설명
            contentDTO.explain = addphoto_explain.text.toString()

            // 유저 아이디
            contentDTO.userID = auth?.currentUser?.email

            // 게시물 업로드 시간
            contentDTO.timestamp = System.currentTimeMillis()
            firestore?.collection("images")?.document()?.set(contentDTO)
            setResult(Activity.RESULT_OK)
            finish()
        }

    }
}
