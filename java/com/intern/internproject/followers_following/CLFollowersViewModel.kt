package com.intern.internproject.followers_following

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.CLLoginResponseToken
import com.google.gson.Gson
import com.intern.internproject.R
import com.intern.internproject.base.CLBaseViewModel
import com.intern.internproject.respository.CLRepository
import com.intern.internproject.respository.model.CLFollowRequestResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class CLFollowersViewModel(mApplication: Application) : CLBaseViewModel(mApplication) {
    private var savedToken: CLLoginResponseToken? = null
    var responseFailure=MutableLiveData<String?>()
    var responseSuccess=MutableLiveData<String?>()
    var listOfUsers=MutableLiveData<List<CLFollowRequestResponse>>()

    private fun retrieveToken(): CLLoginResponseToken? {
        val gson = Gson()
        savedToken = gson.fromJson(
            CLRepository.retriveUserFromSharedPreference(),
            CLLoginResponseToken::class.java
        )
        return savedToken
    }

    fun requestFromServer() {
        val token=retrieveToken()
        val call=CLRepository.getRequestUsers(token?.authToken,token?.refreshToken)
        call?.enqueue(object : retrofit2.Callback<List<CLFollowRequestResponse>> {
            override fun onFailure(call: Call<List<CLFollowRequestResponse>>, t: Throwable) {
              responseFailure.value=mApplication.getString(R.string.went_wrong)
            }

            override fun onResponse(
                call: Call<List<CLFollowRequestResponse>>,
                response: Response<List<CLFollowRequestResponse>>
            ) {
                if(response.isSuccessful){
                    responseSuccess.value=mApplication.getString(R.string.request_response_success)
                    listOfUsers.value=response.body()
                }else{
                    responseFailure.value=mApplication.getString(R.string.request_Response_fail)
                }
            }
        })
    }
    fun followersFromServer(){
        val token=retrieveToken()
        val call=CLRepository.getFollowers(token?.authToken,token?.refreshToken)
        call?.enqueue(object:retrofit2.Callback<ResponseBody>{
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                responseFailure.value=mApplication.getString(R.string.went_wrong)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful){
                    responseSuccess.value=mApplication.getString(R.string.followers_request_success)
                }else{
                    responseFailure.value=mApplication.getString(R.string.followers_request_failure)
                }
            }

        })
    }
}