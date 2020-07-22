package com.intern.internproject.followers_following

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.CLLoginResponseUser
import com.intern.internproject.R
import com.intern.internproject.contact_list.CLContacListAdapter
import kotlinx.android.synthetic.main.cl_fragment_following.*

class CLFollowingFragment : Fragment() {

    private lateinit var mContext: Context
    private lateinit var followerViewModel:CLFollowersViewModel
    private var followerAdapter: CLContacListAdapter? = null
    var users = ArrayList<CLLoginResponseUser>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext=context
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.cl_fragment_following, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        followerViewModel= ViewModelProvider(this).get(CLFollowersViewModel::class.java)
        users.clear()
         users.add(CLLoginResponseUser(null,null,null,null,null,"kumar","dinesh","dinu@mail.com"))
        followerAdapter?.setItem(users)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_following.layoutManager= LinearLayoutManager(mContext, RecyclerView.VERTICAL,false)
        followerAdapter = CLContacListAdapter(mContext, users, object : CLContacListAdapter.ItemClickInterfaces {
            override fun itemClick(email: String?) {


            }
            override fun imageClick(path1: String?) {

            }

            override fun followClick(userId:Int) {

            }
        })
        rv_following.adapter=followerAdapter
    }

}
