package com.hms.cookingreceipes.utils

import android.content.Context
import android.content.SharedPreferences
import com.hms.cookingreceipes.utils.AppConstants.Companion.KEY_END
import com.hms.cookingreceipes.utils.AppConstants.Companion.KEY_START

class PreferenceUtils(context: Context) {

    private var mSharedPreferences: SharedPreferences =
        context.getSharedPreferences("ConfigUtils", Context.MODE_PRIVATE)

    fun saveStartPage(num: Int) {
        mSharedPreferences.edit().putInt(KEY_START, num).apply()
    }

    fun loadStartPage(): Int {
        return mSharedPreferences.getInt(KEY_START, 0)
    }

    fun saveEndPage(num: Int) {
        mSharedPreferences.edit().putInt(KEY_END, num).apply()
    }

    fun loadEndPage(): Int {
        return mSharedPreferences.getInt(KEY_END, 0)
    }


}