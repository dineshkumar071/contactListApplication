package com.intern.internproject.description

import android.os.Bundle
import com.intern.internproject.R
import com.intern.internproject.base.CLBaseActivity

class CLDescriptionActivity : CLBaseActivity() {
    val manager=supportFragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.cl_activity_viewprofile_edit)
        showDescriptionFragment(intent.getStringExtra("EMAIL"),intent.getStringExtra("LOGIN_EMAIL_ID"))

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

   private fun showDescriptionFragment(email:String?, loginEmailId:String?)
   {
       val transaction=manager.beginTransaction()
       val fragment=CLDescriptionFragment().newInstance(email,loginEmailId)
       transaction.replace(R.id.fl_frameHolder_edit,fragment).addToBackStack(null)
       transaction.commit()
   }
}
