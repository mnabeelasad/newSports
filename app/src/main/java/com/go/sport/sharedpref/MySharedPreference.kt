package com.go.sport.sharedpref

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.go.sport.model.login.LoginUser
import com.google.gson.Gson

class MySharedPreference(var context: Context) {

    private var sp: SharedPreferences? = null

    fun saveUserObject(user: LoginUser?): Boolean {
        val gson = Gson()
        val stringUser = gson.toJson(user)
        sp = context.getSharedPreferences("SHARED_FILE", Context.MODE_PRIVATE)
        return sp?.edit()?.putString("userObj", stringUser)?.commit()!!
    }

    fun getUserObject(): LoginUser? {
        sp = context.getSharedPreferences("SHARED_FILE", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sp?.getString("userObj", "")
        return gson.fromJson<LoginUser>(json, LoginUser::class.java)
    }

    /*fun saveSearchHistoryArrayList(history: ArrayList<String>) {
        val gson = Gson()
        val jsonString = gson.toJson(history)
        sp = context.getSharedPreferences("SHARED_FILE", Context.MODE_PRIVATE)
        sp!!.edit().putString("search_arraylist", jsonString).apply()
    }

    fun getSearchHistoryArrayList(): ArrayList<String> {
        var history: ArrayList<String> = ArrayList()
        sp = context.getSharedPreferences("SHARED_FILE", Context.MODE_PRIVATE)
        val gson = Gson()
        val listOfObjects =
            object : TypeToken<List<String?>?>() {}.type
        val json = sp!!.getString("search_arraylist", "")
        if (json != "")
            history = gson.fromJson(json, listOfObjects)
        return history
    }*/

    fun saveTemporaryId(value: String) {
        sp = context.getSharedPreferences("SHARED_FILE", Context.MODE_PRIVATE)
        sp?.edit()?.putString("temp_id", value)?.apply()
    }

    fun getTemporaryId(): String {
        sp = context.getSharedPreferences("SHARED_FILE", Context.MODE_PRIVATE)
        return sp?.getString("temp_id", "")!!
    }

    fun saveUserEmail(email: String) {
        sp = context.getSharedPreferences("LOGIN_FILE", Context.MODE_PRIVATE)
        sp?.edit()?.putString("email", email)?.apply()
    }

    fun getUserEmail(): String {
        sp = context.getSharedPreferences("LOGIN_FILE", Context.MODE_PRIVATE)
        return sp?.getString("email", "")!!
    }

    fun saveUserPassword(password: String) {
        sp = context.getSharedPreferences("LOGIN_FILE", Context.MODE_PRIVATE)
        sp?.edit()?.putString("password", password)?.apply()
    }

    fun getUserPassword(): String {
        sp = context.getSharedPreferences("LOGIN_FILE", Context.MODE_PRIVATE)
        return sp?.getString("password", "")!!
    }

    fun clearAllPreferences() {
        clearPreferences("SHARED_FILE")
    }

    private fun clearPreferences(pref: String?) {
        try {
            val prefs = context.getSharedPreferences(
                pref,
                Context.MODE_PRIVATE
            )
            val prefEditor = prefs.edit()
            prefEditor.clear().apply()
        } catch (e: Exception) {
            Log.i("", "Exception : $e")
        }
    }
}