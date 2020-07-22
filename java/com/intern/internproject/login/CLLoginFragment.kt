package com.intern.internproject.login

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.intern.internproject.R
import com.intern.internproject.common.CLAlert
import com.intern.internproject.contact_list.CLContactListActivity
import com.intern.internproject.reset_password.CLForgotPasswordFragment
import com.intern.internproject.sign_up.CLSignupFragment
import kotlinx.android.synthetic.main.cl_activity_contact_list.*
import kotlinx.android.synthetic.main.cl_fragment_login.*

class CLLoginFragment : Fragment() ,View.OnClickListener{
    lateinit var clAlert: CLAlert
    private lateinit var loginViewModel: CLLoginViewModal
    private lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as FragmentActivity

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loginViewModel = ViewModelProvider(this).get(CLLoginViewModal::class.java)
        observerViewModal()
    }

    private fun observerViewModal() {
        loginViewModel.empty.observe(viewLifecycleOwner, Observer { alert ->
            (activity as CLLoginActivity).hideProgressBar()
            val positiveClickListener = DialogInterface.OnClickListener { dialog, which ->
                dialog?.dismiss()
            }
            clAlert = CLAlert.newInstance(getString(R.string.alert), alert,positiveClickListener)
            clAlert.show(
                (mContext as FragmentActivity).supportFragmentManager,
                getString(R.string.fragement_dialogue)
            )
        })
        loginViewModel.textError.observe(viewLifecycleOwner, Observer { error ->
            (activity as CLLoginActivity).hideProgressBar()
            it_username_input_text?.error = getString(R.string.inavlidEmail)
            it_password_inputtext?.error = getString(R.string.invalidPassword)
        })
        loginViewModel.success.observe(viewLifecycleOwner, Observer { success ->
            var signupActivityIntent = Intent(mContext, CLContactListActivity::class.java)
            signupActivityIntent.putExtra("KEY", 1)
            startActivity(signupActivityIntent)
            (mContext as Activity).finish()
        })
        loginViewModel.success1.observe(viewLifecycleOwner, Observer { success1 ->
            Toast.makeText(mContext, success1, Toast.LENGTH_SHORT).show()
            var signupActivityIntent = Intent(mContext, CLContactListActivity::class.java)
            signupActivityIntent.putExtra("EMAIL_ID", loginViewModel.userName)
            startActivity(signupActivityIntent)
            (mContext as Activity).finish()
        })
        loginViewModel.failure.observe(viewLifecycleOwner, Observer { fail ->
            (activity as CLLoginActivity).hideProgressBar()
            Toast.makeText(mContext, fail, Toast.LENGTH_SHORT).show()
            (activity as CLLoginActivity).hideProgressBar()
        })
    }

    private fun showSignupFragment() {
        val transaction = (mContext as FragmentActivity).supportFragmentManager.beginTransaction()
        val fragment = CLSignupFragment()
        transaction.replace(R.id.fl_frameHolder, fragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.cl_fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        et_username.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                loginViewModel.userName=editable.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        et_password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                loginViewModel.password=editable.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        btn_login.setOnClickListener(this)
        tv_forgotpassword.setOnClickListener(this)
        tv_signup.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_login->{
                (activity as CLLoginActivity).showProgressBar()
                loginViewModel.validation()
            }
            R.id.tv_forgotpassword->{
                val fm = (mContext as FragmentActivity).supportFragmentManager
                val myFragement = CLForgotPasswordFragment()
                myFragement.show(fm, "simple fragment")
            }
            R.id.tv_signup->{
                showSignupFragment()
            }
        }
    }
}