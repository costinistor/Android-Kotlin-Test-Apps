package staircalculator.classic.com.classicstairscalculator.gdpr

import android.content.Context
import androidx.preference.PreferenceManager

class PrefAcceptConsentFormEU(val context: Context) {

    private val key = "KEY_FORM_EU"

    fun setAccept(isSet: Boolean){
        val editor = sharedPrefs().edit()
        editor.putBoolean(key, isSet).apply()
    }

    fun getAccept(): Boolean = sharedPrefs().getBoolean(key, false)

    private fun sharedPrefs() = PreferenceManager.getDefaultSharedPreferences(context)
}