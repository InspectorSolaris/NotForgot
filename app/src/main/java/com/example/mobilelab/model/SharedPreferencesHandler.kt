package com.example.mobilelab.model

import android.content.Context

class SharedPreferencesHandler(
    private val context: Context,
    private val sharedPreferencesName: String
) {

    fun saveString(
        stringKey: String,
        string: String
    ) {
        val sharedPreferences = context.getSharedPreferences(
            sharedPreferencesName,
            Context.MODE_PRIVATE
        )

        val editor = sharedPreferences.edit()

        editor.putString(stringKey, string)
        editor.apply()
    }

    fun readString(
        stringKey: String
    ): String {
        val sharedPreferences = context.getSharedPreferences(
            sharedPreferencesName,
            Context.MODE_PRIVATE
        )

        return sharedPreferences.getString(stringKey, "") ?: ""
    }
}