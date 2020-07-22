package com.intern.internproject.contact_list

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.CLLoginResponseUser
import com.intern.internproject.R
import com.intern.internproject.base.CLBaseActivity
import com.intern.internproject.description.CLDescriptionActivity
import com.intern.internproject.followers_following.CLFollowersActivity
import kotlinx.android.synthetic.main.cl_activity_contact_list.*
import kotlinx.android.synthetic.main.cl_toolbar_contactlist.*


class CLContactListActivity : CLBaseActivity() {

    var users = ArrayList<CLLoginResponseUser>()
    var contactListViewModel: CLContactListViewModel? = null
    private var contactListAdapter: CLContacListAdapter? = null
    var visibleItemCount: Int = 0
    var pastVisibleCountItem: Int = 0
    var totalItemCount: Int = 0
    var loading: Boolean = false
    var pageId: Int = 1
    var loginEmailID: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideProgressBar()
        setContentView(R.layout.cl_activity_contact_list)
        loginEmailID = intent.getStringExtra("EMAIL_ID")
        contactListViewModel = ViewModelProvider(this).get(CLContactListViewModel::class.java)
        rv_contactlist.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false) as RecyclerView.LayoutManager?

        contactListAdapter =
            CLContacListAdapter(this, users, object : CLContacListAdapter.ItemClickInterfaces {
                override fun itemClick(email: String?) {
                    val intent =
                        Intent(this@CLContactListActivity, CLDescriptionActivity::class.java)
                    intent.putExtra("EMAIL", email)
                    intent.putExtra("LOGIN_EMAIL_ID", loginEmailID)
                    this@CLContactListActivity.startActivity(intent)
                    finish()
                }

                override fun imageClick(path1: String?) {

                }

                override fun followClick(userId: Int) {
                    showProgressBar()
                    contactListViewModel?.followRequest(userId)
                }
            })

        rv_contactlist.adapter = contactListAdapter
        et_search.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) { // TODO Auto-generated method stub
            }

            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) { // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) { // filter your list from your input
                contactListViewModel?.filter(s.toString())
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        })
        btn_group_tool.setOnClickListener {
            val intent = Intent(this, CLFollowersActivity::class.java)
            startActivity(intent)
        }
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        progress_bar.indeterminateDrawable?.setColorFilter(
            Color.parseColor("#D81B60"),
            PorterDuff.Mode.MULTIPLY
        )
        progress_bar.visibility = View.VISIBLE
        contactListViewModel?.retrieveFromDatabase(pageId)

    }

    private fun observeViewModel() {
        contactListViewModel?.dblist?.observe(this, Observer { valve ->
            contactListAdapter?.setItem(valve as ArrayList<CLLoginResponseUser>)
            setUpAdapter()
        })
        contactListViewModel?.searchResult?.observe(this, Observer { valve ->
            contactListAdapter?.updateList(valve)
        })
        contactListViewModel?.serverSuccess?.observe(this, Observer { resSuc ->
            Toast.makeText(this, resSuc, Toast.LENGTH_SHORT).show()
            progress_bar.visibility = View.GONE
            loading = true
        })
        contactListViewModel?.serverFailure?.observe(this, Observer { resfail ->
            Toast.makeText(this, resfail, Toast.LENGTH_SHORT).show()
            progress_bar.visibility = View.GONE
        })
        contactListViewModel?.followSuccess?.observe(this, Observer { success ->
            hideProgressBar()
            progress_bar.visibility = View.GONE
            Toast.makeText(this, success, Toast.LENGTH_SHORT).show()
        })
        contactListViewModel?.followFail?.observe(this, Observer { fail ->
            hideProgressBar()
            progress_bar.visibility = View.GONE
            Toast.makeText(this, fail, Toast.LENGTH_SHORT).show()
        })

    }

    private fun setUpAdapter() {
        val currentposition =
            (rv_contactlist.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        rv_contactlist.scrollToPosition(currentposition)
        rv_contactlist.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    visibleItemCount = recyclerView.layoutManager!!.childCount
                    totalItemCount = recyclerView.layoutManager!!.itemCount
                    pastVisibleCountItem =
                        (rv_contactlist.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                    if (loading) {
                        if ((visibleItemCount + pastVisibleCountItem) >= totalItemCount) {
                            loading = false
                            pageId++
                            progress_bar.indeterminateDrawable?.setColorFilter(
                                Color.parseColor("#D81B60"),
                                PorterDuff.Mode.MULTIPLY
                            )
                            progress_bar.visibility = View.VISIBLE
                            contactListViewModel?.retrieveFromDatabase(pageId)
                        }
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }


        })
    }
}