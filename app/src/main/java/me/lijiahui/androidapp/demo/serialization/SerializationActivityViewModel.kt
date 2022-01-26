package me.lijiahui.androidapp.demo.serialization

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import me.lijiahui.androidapp.extension.fromJson
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SerializationActivityViewModel : ViewModel() {
    companion object {
        private const val TAG = "SerializationActivityViewModel"

        private const val HOST_URL = "https://reqres.in"
    }

    private val gson = Gson()

    private val retrofit = Retrofit.Builder().baseUrl(HOST_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    private val userService = retrofit.create(UserService::class.java)

    fun requestUsers() {
        Log.d(TAG, "requestUsers")
        val client = OkHttpClient()
        val request = Request.Builder().url("$HOST_URL/api/users?page=2").build()
        val response = client.newCall(request).execute()
        if (response.body() == null) {
            Log.e(TAG, "request body empty")
            return
        }
        val jsonString = response.body()!!.string()
        try {
            val json = JSONObject(jsonString)
            val data = json.optString("data")
            val users: List<User> = gson.fromJson(data)
            for (user in users) {
                Log.d(TAG, "user $user")
                if (DatabaseHelper.userDb.userDao().getUserById(userId = user.id) != null) {
                    Log.d(TAG, "user exist, update it")
                    DatabaseHelper.userDb.userDao().update(user)
                } else {
                    Log.d(TAG, "new user, insert it")
                    DatabaseHelper.userDb.userDao().insertAll(user)
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, e.message, e)
        }
    }
}