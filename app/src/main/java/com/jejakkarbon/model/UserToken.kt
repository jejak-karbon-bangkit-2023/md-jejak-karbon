package com.jejakkarbon.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserToken(
    var token: String? = null,
    var isLogin: Boolean = false
): Parcelable