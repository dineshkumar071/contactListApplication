package com.intern.internproject.login

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.CLLoginRespsonse
import com.intern.internproject.R
import com.intern.internproject.base.CLBaseViewModel
import com.intern.internproject.respository.CLRepository
import com.intern.internproject.respository.model.CLUserLogin
import com.intern.internproject.utility.CLUtilities
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CLLoginViewModal(mApplication: Application) : CLBaseViewModel(mApplication) {
    var userName: String? = null
    var password: String? = null
    var empty = MutableLiveData<String>()
    var textError = MutableLiveData<String>()
    var success = MutableLiveData<String>()
    var success1 = MutableLiveData<String?>()
    var failure=MutableLiveData<String?>()
    var login: CLUserLogin? = null
    var singleUser:CLLoginRespsonse?=null


    fun validation() {

        if (userName.equals("") || password.equals("")) {
            empty.value = mApplication.getString(R.string.All_fields)
        } else {
            val e = CLUtilities.isValidEmail(userName ?: "")
            val p = CLUtilities.isValidPassword(password ?: "")
            if (!e) {
                empty.value = mApplication.getString(R.string.invalid_user)
            } else if (!p) {
                empty.value = mApplication.getString(R.string.invalis_password)
            } else {
                login = CLUserLogin(userName, password)
                val user = CLRepository.activateUser(login)
                user?.enqueue(object : Callback<CLLoginRespsonse> {
                    override fun onFailure(call: Call<CLLoginRespsonse>, t: Throwable) {
                        failure.value=mApplication.getString(R.string.went_wrong)

                    }

                    override fun onResponse(
                        call: Call<CLLoginRespsonse>,
                        response: Response<CLLoginRespsonse>
                    ) {
                        if (response.isSuccessful) {

                            val response=response.body()
                            singleUser=response
                            val token=singleUser?.tokens
                            val user=singleUser?.user
                            CLRepository.saveUserInSharedPreference(token)
                            CLRepository.insert(user)
                            CLRepository.insertDetails(userName,password)
                            success1.value=mApplication.getString(R.string.login_success)
                            // Log.d("NEW", singleUser?.user.toString())
                        } else {
                            failure.value=mApplication.getString(R.string.invalid_login)

                        }

                    }

                })
            }
        }
    }
}