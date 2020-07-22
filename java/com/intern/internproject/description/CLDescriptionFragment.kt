package com.intern.internproject.description

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.CLLoginResponseUser
import com.intern.internproject.R
import com.intern.internproject.login.CLLoginActivity
import com.intern.internproject.profile_edit.CLProfileEditFragment
import kotlinx.android.synthetic.main.cl_fragment_cldescription.*
import kotlinx.android.synthetic.main.cl_toolbar_decription.*

class CLDescriptionFragment : Fragment() {
    private lateinit var descriptionViewmodel: CLDescriptionViewModal
    var users = ArrayList<CLLoginResponseUser>()
    var email: String? = null
    private var logInEmail: String? = null
    private lateinit var contest: Context
    override fun onAttach(context: Context) {
        super.onAttach(context)
        contest = context as FragmentActivity

    }

    fun newInstance(email: String?, loginEmailId: String?): CLDescriptionFragment {
        val obj = CLDescriptionFragment()
        val bundle = Bundle()
        bundle.putString("EMAIL", email)
        bundle.putString("LOG_IN_EMAIL", loginEmailId)
        obj.arguments = bundle
        return obj

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        email = bundle?.getString("EMAIL")
        logInEmail = bundle?.getString("LOG_IN_EMAIL")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_logout.setOnClickListener {
            descriptionViewmodel.logoutRequest()
        }
        btn_cancel_tool.setOnClickListener {
            (contest as Activity).finish()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.cl_fragment_cldescription, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        descriptionViewmodel =
            ViewModelProvider(this).get(CLDescriptionViewModal::class.java)
        descriptionViewmodel.retrieve()
        descriptionViewmodel.getDetails()
        observeViewModel()
    }

    private fun observeViewModel() {
        descriptionViewmodel.dbList.observe(viewLifecycleOwner, Observer { valve ->
            for (i in valve) {
                if (i.email == email) {
                    tv_name.text = i.firstName
                    tv_company_name.text = getString(R.string.company)
                    tv_phone_number.text = i.phoneNumber
                    tv_email_content.text = i.email
                    tv_address_content.text = i.address
                    val logIn=descriptionViewmodel.logInUser
                    if (i.email == logIn) {
                        btn_logout.visibility = View.VISIBLE
                        btn_save_tool.visibility = View.VISIBLE
                        btn_save_tool.setOnClickListener {
                            showSignUpEditFragment()
                        }
                    }

                }
            }
        })
        descriptionViewmodel.logoutResponseSuccess.observe(viewLifecycleOwner, Observer { success ->
            Toast.makeText(contest, success, Toast.LENGTH_SHORT).show()
            val intent = Intent(contest, CLLoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            (contest as Activity).finish()
        })
        descriptionViewmodel.logoutResponseFailure.observe(viewLifecycleOwner, Observer { fail ->
            Toast.makeText(contest, fail, Toast.LENGTH_SHORT).show()
        })
    }

    private fun showSignUpEditFragment() {
        val transaction = (contest as FragmentActivity).supportFragmentManager.beginTransaction()
        val fragment = CLProfileEditFragment()
        transaction.replace(R.id.fl_frameHolder_edit, fragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }

    override fun onDestroy() {
        super.onDestroy()
        (contest as Activity).finish()
    }
}