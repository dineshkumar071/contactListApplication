package com.intern.internproject.description

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import com.example.CLLoginResponseToken
import com.example.CLLoginResponseUser
import com.google.gson.Gson
import com.intern.internproject.R
import com.intern.internproject.base.CLBaseViewModel
import com.intern.internproject.respository.CLRepository
import com.intern.internproject.respository.model.CLUserDetails
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CLDescriptionViewModal(mApplication: Application): CLBaseViewModel(mApplication) {
    var users:List<CLLoginResponseUser>?=null
    var logInUser:String?=null
    var dbList=MutableLiveData<List<CLLoginResponseUser>>()
    var logoutResponseSuccess=MutableLiveData<String?>()
    var logoutResponseFailure=MutableLiveData<String?>()
    private fun retrieveToken(): CLLoginResponseToken? {
        val gson = Gson()
        return gson.fromJson(CLRepository.retriveUserFromSharedPreference(), CLLoginResponseToken::class.java)
    }

    fun logoutRequest()
    {
        val token=retrieveToken()
        val res=CLRepository.logoutUser(token?.authToken,token?.refreshToken)
        res?.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                logoutResponseFailure.value=mApplication.getString(R.string.logout_failure)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful)
                {
                    logoutResponseSuccess.value=mApplication.getString(R.string.logout_success)
                    CLRepository.deleteDataFromDatabase()
                    CLRepository.eraseFromPreference()
                }
                else
                {
                    logoutResponseFailure.value=mApplication.getString(R.string.logout_failure)
                }
            }

        })
    }

    fun retrieve()
    {

        CLRepository.getUserDetails(object :CLRepository.callback{
            override fun getList1(userList: List<CLLoginResponseUser>?) {

                users=userList
                Handler(Looper.getMainLooper()).post {
                    // things to do on the main thread
                   dbList.value=users

                }
            }
        })
    }
    fun retrieveFromDatabase(){
        retrieve()
    }
    fun getDetails(){
        CLRepository.getUser(object :CLRepository.secondDatabaseCallBack{
            override fun getUserDetail(user: CLUserDetails) {
                Handler(Looper.getMainLooper()).post{
                    logInUser= user.Email
                }
            }

        })
    }
}