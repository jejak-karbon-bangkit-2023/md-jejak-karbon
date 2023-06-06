package com.jejakkarbon.preferences

import com.jejakkarbon.model.UserToken
import android.content.Context

internal class Preferences(context: Context) {

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setToken(value: UserToken) {
        val editor = preferences.edit()
        editor.putString(TOKEN, value.token)
        editor.putBoolean(LOGIN, value.isLogin)
        editor.apply()
    }

    fun getToken(): UserToken {
        val user = UserToken()
        user.token = preferences.getString(TOKEN, "")
        user.isLogin = preferences.getBoolean(LOGIN, false)
        return user
    }

    fun resetToken() {
        val editor = preferences.edit()
        editor.remove(TOKEN)
        editor.putBoolean(LOGIN, false)
        editor.apply()
    }

    fun isFirstLogin(): Boolean {
        return preferences.getBoolean(FIRST_LOGIN, true)
    }

    fun setFirstLogin(value: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(FIRST_LOGIN, value)
        editor.apply()
    }


    companion object {
        private const val PREFS_NAME = "token_pref"
        private const val TOKEN = "token"
        private const val FIRST_LOGIN = "isFirstLogin"
        private const val LOGIN = "isLogin"
    }
}