package com.example.howlslecture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.howlslecture.model.ContentDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_detail.view.*
import kotlinx.android.synthetic.main.item_detail.view.*

class  DetailViewFragment : Fragment(){
    var firestore : FirebaseFirestore? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        firestore = FirebaseFirestore.getInstance()
        var view = LayoutInflater.from(inflater.context).inflate(R.layout.fragment_detail,container,false)
        view.detailview_recyclerview.adapter = DetailRecyclerViewAdapter()
        view.detailview_recyclerview.layoutManager = LinearLayoutManager(activity)
        return view

    }
    inner class DetailRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        val contentDTOs : ArrayList<ContentDTO>?
        val contentUidList : ArrayList<String>?
        init {
            contentDTOs = ArrayList()     // contentDTOs 초기화
            contentUidList = ArrayList()  // contentUidList 초기화
            // 현재 로그인된 유저의 uid(주민등록번호)
            var uid = FirebaseAuth.getInstance().currentUser?.uid

            firestore?.collection("images")?.orderBy("timestamp")?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                contentDTOs.clear()  // 화면 다시 그리기
                contentUidList.clear()  // 화면 다시.
                for(snapshot in querySnapshot!!.documents){
                    var item = snapshot.toObject(ContentDTO::class.java)
                    contentDTOs!!.add(item!!)

                    contentUidList!!.add(snapshot.id)
                }
                notifyDataSetChanged()

            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view =LayoutInflater.from(parent.context).inflate(R.layout.item_detail, parent, false)
            return CustomDetailHolder(view)
        }

        private inner class CustomDetailHolder(view:View) : RecyclerView.ViewHolder(view)

        override fun getItemCount(): Int {
            return contentDTOs!!.size

        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewholder = (holder as CustomDetailHolder).itemView
            // 유저 아이디
            viewholder.detailviewitem_profile__textview.text = contentDTOs!![position].userID
            // 이미지

            Glide.with(holder.itemView.context).load(contentDTOs!![position].imageUrl).into(viewholder.detailviewitem_content_imageview)

            // 설명 텍스트
            viewholder.detailviewitem_explain_textview.text = contentDTOs!![position].explain

            // 좋아요 카운터 설정
            viewholder.detailviewitem_count_textview.text = "좋아요 " + contentDTOs!![position].favoriteCount


        }

    }
}