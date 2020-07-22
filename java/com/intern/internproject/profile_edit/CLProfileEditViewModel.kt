package com.intern.internproject.sign_up

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.example.CLLoginResponseToken
import com.example.CLLoginResponseUser
import com.google.gson.Gson
import com.intern.internproject.R
import com.intern.internproject.base.CLBaseViewModel
import com.intern.internproject.respository.CLRepository
import com.intern.internproject.respository.model.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CLProfileEditViewModel(mApplication:Application) : CLBaseViewModel(mApplication){
    lateinit var pojoobj: CLUSerEntity
    var firstName: String? = null
    var lastName: String? = null
    var companyName: String? = null
    var eMail: String? = null
    var phoneNumber: String? = null
    var passWord: String?=null
    var confirmPassword: String?=null
    var street1: String? = null
    var street2: String? = null
    var city: String? = null
    var state: String? = null
    var postCode: String? = null
    var path:String?=null
    var useres:List<CLLoginResponseUser>?=null
    var dbList=MutableLiveData<List<CLLoginResponseUser>>()
    var loginlivedata= MutableLiveData<Boolean>()
    var error=MutableLiveData<String?>()
    var success=MutableLiveData<String?>()
    var responseSuccess=MutableLiveData<String?>()
    var responseFail=MutableLiveData<String?>()



    fun settFstName(fstName1:String?) {
        this.firstName =fstName1
    }
    fun settLstName(lstName1:String?) {
        this.lastName=lstName1
    }
    var address=street1+street2+city+state+postCode


    fun retriveFromDatabase(){

        CLRepository.getUserDetails(object :CLRepository.callback{
            override fun getList1(userList: List<CLLoginResponseUser>?) {

                useres=userList
                Handler(Looper.getMainLooper()).post {
                    // things to do on the main thread
                   dbList.value=useres

                }

            }
        })
    }
    fun retriveToken(): CLLoginResponseToken? {
        val gson = Gson()
        return gson.fromJson(CLRepository.retriveUserFromSharedPreference(), CLLoginResponseToken::class.java)

    }
    fun signupvalidation()
    {

        if (TextUtils.isEmpty(firstName) ||TextUtils.isEmpty(lastName)||TextUtils.isEmpty(companyName)
            ||TextUtils.isEmpty(street1)||TextUtils.isEmpty(street2)||TextUtils.isEmpty(state)||TextUtils.isEmpty(city)||TextUtils.isEmpty(postCode)
        ){
            error.value=mApplication.getString(R.string.all_field)
        }
        else {

                pojoobj= CLUSerEntity(
                    firstName,
                    lastName,
                    companyName,
                    eMail,
                    phoneNumber,
                    passWord,
                    address,
                    true,
                    path
                )
            updateFromServerInRepository()
            //success.value="Sucess"


        }
    }

    fun updateFromServerInRepository() {
        val users = CLEditUser()
        val user = CLEditFirstName(firstName, lastName)
        users.user = user
        val token = retriveToken()
        val res=CLRepository.updateUser(token?.authToken, token?.refreshToken, users)
        res?.enqueue(object : Callback<ResponseBody>{
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
               responseFail.value=mApplication.getString(R.string.update_fail)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
               if(response.isSuccessful)
               {
                   responseSuccess.value=mApplication.getString(R.string.update_success)

               }
                else
               {
                   responseFail.value=mApplication.getString(R.string.update_fail)
               }
            }

        } )
    }


}