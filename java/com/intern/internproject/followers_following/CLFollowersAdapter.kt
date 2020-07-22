package com.intern.internproject.followers_following

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.CLLoginResponseUser
import com.intern.internproject.R
import com.intern.internproject.respository.model.CLFollowRequestResponse
import kotlinx.android.synthetic.main.cl_adapter_followers.view.*

class CLFollowersAdapter (
    var c: Context,
    var userList: ArrayList<CLFollowRequestResponse>,
    var itemClickListener: ItemClickInterface
) : RecyclerView.Adapter<CLFollowersAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val followerImageView:ImageView=itemView.iv_follower
        val followerName:TextView=itemView.tv_name_follow
        val followerTick:ImageView=itemView.iv_tick
        val followerCross:ImageView=itemView.iv_cross
    }

    fun setItem(valve: ArrayList<CLFollowRequestResponse>) {
        userList = valve
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CLFollowersAdapter.ViewHolder {
       return ViewHolder( LayoutInflater.from(parent.context)
           .inflate(R.layout.cl_adapter_followers, parent, false))
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: CLFollowersAdapter.ViewHolder, position: Int) {
        val user: CLFollowRequestResponse = userList[position]
        holder.followerName.text=user.firstName+" "+user.lastName
    }
    interface  ItemClickInterface{

    }
}