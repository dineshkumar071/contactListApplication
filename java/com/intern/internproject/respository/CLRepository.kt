package com.intern.internproject.respository

import com.example.CLLoginResponseToken
import com.example.CLLoginResponseUser
import com.example.CLLoginRespsonse
import com.google.gson.GsonBuilder
import com.intern.internproject.application_database.CLUserDAO
import com.intern.internproject.application_database.CLUserDetailsDAO
import com.intern.internproject.common.CLApplication.Companion.dbInstance
import com.intern.internproject.common.CLApplication.Companion.instance
import com.intern.internproject.respository.model.*
import com.intern.internproject.retrofit.CLRetrofit
import com.intern.internproject.services.CLUserClient
import okhttp3.ResponseBody
import retrofit2.Call


object CLRepository {

    private var userDao: CLUserDAO
    var userDetail:CLUserDetailsDAO
    var BASE_URL: String = "https://jwt-api-test.herokuapp.com/"
    var userActivation:CLUserActivation?=null

  /**
   * save the authorization token and refresh token in the shared preference*/
    fun saveUserInSharedPreference(user: CLLoginResponseToken?) {
        val userGson = GsonBuilder().create()
        var listOfUsers: String?  = userGson.toJson(user)
        instance.getPrefer()?.listOfUsers = listOfUsers
    }

    /**
     * retrieve the authorization token and refresh token from the shared preference*/
    fun retriveUserFromSharedPreference(): String? = instance.getPrefer()?.listOfUsers

    /**
     * delete the refresh token and authorization token when we logout */
    fun eraseFromPreference() {
        instance.getPrefer()?.clearSharedPreference()
    }

    /**
     * save the login user details*/
    fun insertUser(user: CLUSerEntity?):Call<CLUSerEntity>? {
        val retrofit = CLRetrofit.retrofit()
        val client = retrofit?.create(CLUserClient::class.java)
        val userModel = CLUserModel()
        userModel.user = user
        userActivation?.Email = user?.Email
        userActivation?.passWord = user?.passWord
        return client?.createAccount(userModel)
    }

    /**
     * register the user details
     * otp should be sent to the given email*/
    fun registerUser(token:String?): Call<CLUSerEntity>?{
        val retrofit = CLRetrofit.retrofit()
        val client = retrofit?.create(CLUserClient::class.java)
        return client?.getActivation(token)
    }

    /**
     * activate the user after verified*/
    fun activateUser(id:CLUserLogin?): Call<CLLoginRespsonse>?
    {
        val retrofit = CLRetrofit.retrofit()
        val client = retrofit?.create(CLUserClient::class.java)
        return client?.loginActivation(id?.Email,id?.passWord)
    }

    /**
     * get the list of users*/
    fun getUsers(authorisation:String?, page:String?):Call<CLContacListUsers1>?
    {
        val retrofit = CLRetrofit.retrofit()
        val client = retrofit?.create(CLUserClient::class.java)
        return client?.getUsersAPI(authorisation,page)
    }

    /**
    * update the user detail*/
    fun updateUser(authoriseToken:String?, refreshToken:String?, user:CLEditUser?):Call<ResponseBody>?
    {
        val retrofit = CLRetrofit.retrofit()
        val client = retrofit?.create(CLUserClient::class.java)
        return client?.updateUser(authoriseToken,refreshToken,user)
    }

    /**
     * logout the user*/
    fun logoutUser(authorisation: String?, refreshToken: String?):Call<ResponseBody>?
    {
        val retrofit = CLRetrofit.retrofit()
        val client = retrofit?.create(CLUserClient::class.java)
        return client?.logOut(authorisation,refreshToken)
    }

    /**
     * verify the email of the user if it is correct or not*/
    fun emailVerification(email:String?):Call<ResponseBody>?
    {
        val retrofit = CLRetrofit.retrofit()
        val client = retrofit?.create(CLUserClient::class.java)
        return client?.sendEmailForGenerateOtp(email)
    }

    /**
     * reset the user password*/
    fun resetPassword(otp:String?,password:String?):Call<ResponseBody>?{
        val retrofit = CLRetrofit.retrofit()
        val client = retrofit?.create(CLUserClient::class.java)
        return client?.setPasswordSuccessfully(otp,password)
    }

    /**
     * to get the refresh token and authorization token of the user when it will get fail*/
    fun toGetRefreshToken(token:String?):Call<CLRefreshTokenResponse>?{
        val retrofit = CLRetrofit.retrofit()
        val client = retrofit?.create(CLUserClient::class.java)
        return client?.refreshToken(token)
    }

    /**
     * search the first and last name when the user given in the search bar*/
    fun search(text:String?,token:CLLoginResponseToken?):Call<ResponseBody>?
    {
        val retrofit = CLRetrofit.retrofit()
        val client = retrofit?.create(CLUserClient::class.java)
        return client?.onlineSearch(text,token?.authToken,token?.refreshToken)
    }

    /**
     * if the user should click the follow button ,the request will be send*/
    fun follow(userId:Int,authorisation: String?,refreshToken: String?):Call<ResponseBody>? {
        val retrofit = CLRetrofit.retrofit()
        val client = retrofit?.create(CLUserClient::class.java)
        return client?.requestFollow(userId,authorisation,refreshToken)
    }

    /**
     * fetch the list of users ,they should give request*/
    fun getRequestUsers(authoriseToken: String?,refreshToken: String?):Call<List<CLFollowRequestResponse>>?{
        val retrofit = CLRetrofit.retrofit()
        val client = retrofit?.create(CLUserClient::class.java)
        return client?.listOfRequests(authoriseToken,refreshToken)
    }


    fun getFollowers(authorisation: String?,refreshToken: String?):Call<ResponseBody>?{
        val retrofit = CLRetrofit.retrofit()
        val client = retrofit?.create(CLUserClient::class.java)
        return client?.listOfFollowers(authorisation,refreshToken)
    }

    fun updateVersionName(name:String?):Call<ResponseBody>?{
        val retrofit = CLRetrofit.retrofit()
        val client = retrofit?.create(CLUserClient::class.java)
        return client?.updateAndroid("android",name)
    }

    init {
        userDao = dbInstance.userDOA()
        userDetail= dbInstance.userDetailsDOA()
    }

    fun insert(userRoom: CLLoginResponseUser?) {
        Thread {
            userRoom?.let { userDao.saveUser(it) }
        }.start()

    }

    fun insertDetails(userName:String?,password:String?){
        val user=CLUserDetails(userName,password)
        Thread {
            userDetail.insertUserDetails(user)
        }.start()
    }

    fun insertList(userRoom:List<CLLoginResponseUser>?)
    {
        Thread {
            userRoom?.let { userDao.saveUsers(it) }
        }.start()

    }

    fun deleteDataFromDatabase()
    {
        Thread {
            userDao.deleteDataBase()
            userDetail.deleteUser()
        }.start()
    }

    fun getUserDetails(callback: callback) {
        Thread {
            val userDetails = userDao.readUser()
            callback.getList1(userDetails)
        }.start()
    }

    fun getUser(callback:secondDatabaseCallBack){
        Thread{
            val user= userDetail.userDetail()
            callback.getUserDetail(user)
        }.start()
    }



    interface callback {

        fun getList1(userList: List<CLLoginResponseUser>?)

    }
    interface secondDatabaseCallBack{
        fun getUserDetail(user:CLUserDetails)
    }

}