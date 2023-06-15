package com.jejakkarbon.preferences

import com.jejakkarbon.model.UserToken
import android.content.Context

internal class Preferences(context: Context) {

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setToken(value: UserToken) {
        val editor = preferences.edit()
        editor.putString(TOKEN, value.token)
        editor.putBoolean(LOGIN, value.isLogin)
        editor.putBoolean(FIRST_LOGIN, value.isFirstLogin)
        editor.apply()
    }

    fun getToken(): UserToken {
        val user = UserToken()
        user.token = preferences.getString(TOKEN, "")
        user.isLogin = preferences.getBoolean(LOGIN, false)
        user.isFirstLogin = preferences.getBoolean(FIRST_LOGIN, true)
        return user
    }

    fun setRide(ride: String) {
        val editor = preferences.edit()
        editor.putString(RIDE, ride)
        editor.apply()
    }

    fun setDistance(distance: Int) {
        val editor = preferences.edit()
        editor.putInt(DISTANCE, distance)
        editor.apply()
    }

    fun getRideAndDistance(): Pair<String, Int> {
        val addRide = preferences.getString(RIDE, "") ?: ""
        val distance = preferences.getInt(DISTANCE, 0)
        return Pair(addRide, distance)
    }

    fun resetToken() {
        val editor = preferences.edit()
        editor.remove(TOKEN)
        editor.putBoolean(LOGIN, false)
        editor.putBoolean(FIRST_LOGIN, false)
        editor.apply()
    }


    companion object {
        private const val PREFS_NAME = "token_pref"
        private const val TOKEN = "token"
        private const val FIRST_LOGIN = "isFirstLogin"
        private const val LOGIN = "isLogin"
        private const val RIDE = "ride"
        private const val DISTANCE = "distance"
    }
}