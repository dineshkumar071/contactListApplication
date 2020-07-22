package com.intern.internproject.contact_list

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.CLLoginResponseToken
import com.example.CLLoginResponseUser
import com.google.gson.Gson
import com.intern.internproject.R
import com.intern.internproject.base.CLBaseViewModel
import com.intern.internproject.respository.CLRepository
import com.intern.internproject.respository.model.CLContacListUsers1
import com.intern.internproject.respository.model.CLRefreshTokenResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CLContactListViewModel(mApplication: Application) : CLBaseViewModel(mApplication) {
    var searchResult = MutableLiveData<ArrayList<CLLoginResponseUser>>()
    var useres: List<CLLoginResponseUser>? = null
    var dblist = MutableLiveData<List<CLLoginResponseUser>>()
    var savedToken: CLLoginResponseToken? = null
    var serverSuccess = MutableLiveData<String?>()
    var serverFailure = MutableLiveData<String?>()
    val followSuccess = MutableLiveData<String?>()
    val followFail = MutableLiveData<String?>()

    private fun retrieveToken(): CLLoginResponseToken? {
        val gson = Gson()
        savedToken = gson.fromJson(
            CLRepository.retriveUserFromSharedPreference(),
            CLLoginResponseToken::class.java
        )
        return savedToken
    }

    fun retrieve() {
        CLRepository.getUserDetails(object : CLRepository.callback {
            override fun getList1(userList: List<CLLoginResponseUser>?) {
                useres = userList
                Handler(Looper.getMainLooper()).post {
                    dblist.value = useres

                }
            }
        })
    }

    fun followRequest(userId:Int) {
        val token = retrieveToken()
        val call = CLRepository.follow(userId,token?.authToken, token?.refreshToken)
        call?.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                followFail.value=mApplication.getString(R.string.follow_fail)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    useres?.let {
                        for (user in it){
                            if(user.id == userId){
                                user.isFollowing = true
                                followSuccess.value = mApplication.getString(R.string.follow_success)
                                break
                            }
                        }
                    }
                } else {
                   followFail.value=mApplication.getString(R.string.follow_fail)
                }
            }
        })
    }

    private fun retrieveFromServer(pageId: Int) {
        val token1 = retrieveToken()
        val tokenUpdate = CLRepository.toGetRefreshToken(token1?.refreshToken)
        tokenUpdate?.enqueue(object : Callback<CLRefreshTokenResponse> {
            override fun onFailure(call: Call<CLRefreshTokenResponse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<CLRefreshTokenResponse>,
                response: Response<CLRefreshTokenResponse>
            ) {
                val author = response.body()
                val newTokens = CLLoginResponseToken()
                newTokens.authToken = author?.authToken
                newTokens.refreshToken = token1?.refreshToken
                CLRepository.saveUserInSharedPreference(newTokens)
            }
        })
        val token = retrieveToken()
        val response = CLRepository.getUsers(token?.authToken, pageId.toString())
        response?.enqueue(object : Callback<CLContacListUsers1> {
            override fun onFailure(call: Call<CLContacListUsers1>, t: Throwable) {
                serverFailure.value = mApplication.getString(R.string.response_fails)

            }

            override fun onResponse(
                call: Call<CLContacListUsers1>,
                response: Response<CLContacListUsers1>
            ) {
                if (response.code() == 200) {
                    serverSuccess.value = mApplication.getString(R.string.response_success)
                    val res = response.body()
                    CLRepository.insertList(res?.allUsers)
                    retrieve()

                } else {
                    serverFailure.value = mApplication.getString(R.string.response_fails)

                }
            }

        })
    }

    fun retrieveFromDatabase(page: Int) {
        retrieveFromServer(page)
    }

    fun filter(text: String?) {
        val temp = ArrayList<CLLoginResponseUser>()

        CLRepository.getUserDetails(object : CLRepository.callback {
            override fun getList1(userList: List<CLLoginResponseUser>?) {

                useres = userList
                Handler(Looper.getMainLooper()).post {
                    // things to do on the main thread
                    useres?.forEach { d ->
                        val b = text?.let { d.firstName?.contains(it) }
                        val c = text?.let { d.lastName?.contains(it) }
                        if (b == true || c == true) {
                            temp.add(d)
                        }
                    }
                    searchResult.value = temp

                }
            }
        })

    }
}