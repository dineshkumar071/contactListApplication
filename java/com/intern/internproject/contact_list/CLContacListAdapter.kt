package com.intern.internproject.contact_list

import com.intern.internproject.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.CLLoginResponseUser

class CLContacListAdapter(
    var nContext: Context,
    var userList: ArrayList<CLLoginResponseUser>,
    private var itemClickListener: ItemClickInterfaces
) : RecyclerView.Adapter<CLContacListAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contactListAdapterName = itemView.findViewById(R.id.tv_name_contact_list) as TextView
        val contactListAdapterFollow = itemView.findViewById(R.id.tv_follow_contact_list) as TextView
        val contacListAdapterImage = itemView.findViewById(R.id.iv_contact_list) as ImageView
        val contactListAdapterCard = itemView.findViewById(R.id.cv_login_user_name) as CardView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.cl_contactlistadapter, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    /**to update the list for database*/
    fun updateList(users: ArrayList<CLLoginResponseUser>) {
        userList.clear()
        userList.addAll(users)
        notifyDataSetChanged()
    }

    /**set the adaptor*/
    fun setItem(valve: ArrayList<CLLoginResponseUser>) {
        userList = valve
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val user: CLLoginResponseUser = userList[position]
        val following = user.isFollowing
        if(following){
            holder.contactListAdapterFollow.text ="Following"
        } else{
            holder.contactListAdapterFollow.text ="Follow"
        }
        holder.contactListAdapterName.text = user.firstName + user.lastName
        holder.contactListAdapterCard.setOnClickListener {
            itemClickListener.itemClick(user.email) }
        holder.contactListAdapterFollow.setOnClickListener {
            val userId = user.id
            userId?.let {
                itemClickListener.followClick(it)
            }
        }

    }

    interface ItemClickInterfaces {
        fun itemClick(email: String?)
        fun imageClick(path1: String?)
        fun followClick(userId: Int)
    }
}