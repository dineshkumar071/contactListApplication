package com.intern.internproject.reset_password

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.intern.internproject.R
import kotlinx.android.synthetic.main.cl_alertbox_send_otp.*

class CLOtpFragment:DialogFragment() {

    private lateinit var mContext:Context
    override fun onAttach(context: Context) {
        super.onAttach(context)
         mContext=context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_cancel.setOnClickListener {
            dismiss()
        }
        btn_okay.setOnClickListener {
            val otp=ev_otp.text.toString()
            if(otp=="") {
                Toast.makeText(mContext,"enter the OTP", Toast.LENGTH_LONG).show()
            }else {
                var intent = Intent(mContext, CLResetPasswordActivity::class.java)
                intent.putExtra("OTP", otp)
                dismiss()
                startActivity(intent)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.cl_alertbox_send_otp,container,false)
    }
}