package com.intern.internproject.sign_up

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.intern.internproject.R
import com.intern.internproject.base.CLBaseActivity
import com.intern.internproject.common.CLAlert
import com.intern.internproject.contact_list.CLContactListActivity
import kotlinx.android.synthetic.main.cl_fragment_signup.*
import kotlinx.android.synthetic.main.cl_toolbar_signup.*


class CLSignupActivity : CLBaseActivity() {
    private lateinit var signupViewModel: CLSignupViewModel
    lateinit var clAlert: CLAlert
    private val alertFragment = supportFragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.cl_fragment_signup)
        signupViewModel = ViewModelProvider(this).get(CLSignupViewModel::class.java)


        et_firstname.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(edit: Editable?) {
                signupViewModel.firstName = edit.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        et_lastname.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(edit: Editable?) {
                signupViewModel.lastName=edit.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        et_companyname.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(edit: Editable?) {
                signupViewModel.companyName=edit.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        et_email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(edit: Editable?) {
                signupViewModel.eMail=edit.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        et_phone_number.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(edit: Editable?) {
                signupViewModel.phoneNumber=edit.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        et_password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(edit: Editable?) {
                signupViewModel.passWord=edit.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        et_confirmpassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(edit: Editable?) {
                signupViewModel.confirmPassword=edit.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        et_street1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(edit: Editable?) {
                signupViewModel.street1=edit.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        et_street2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(edit: Editable?) {
                signupViewModel.street2=edit.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        et_city.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(edit: Editable?) {
                signupViewModel.city=edit.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        et_state.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(edit: Editable?) {
                signupViewModel.state=edit.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        et_postcode.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(edit: Editable?) {
                signupViewModel.postCode=edit.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        btn_save_tool.setOnClickListener {
            signupViewModel.validation()
        }
        observeViewModel()
    }

    private fun observeViewModel() {
        signupViewModel.error.observe(this, Observer { error1 ->
            val positiveClickListener = DialogInterface.OnClickListener { dialog, which ->
                dialog?.dismiss()
            }
            clAlert = CLAlert.newInstance("Alert", error1,positiveClickListener)
            clAlert.show(alertFragment, "fragment_confirm_dialog")})
        signupViewModel.success.observe(this, Observer { seccess-> Toast.makeText(this, "details saved", Toast.LENGTH_SHORT).show()
               var contactListIntent = Intent(
                    this,
                    CLContactListActivity::class.java
                )
                startActivity(contactListIntent)
                finish()
        })
    }
}

