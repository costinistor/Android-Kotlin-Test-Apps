package staircalculator.classic.com.classicstairscalculator.ads

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import androidx.core.os.ConfigurationCompat
import staircalculator.classic.com.classicstairscalculator.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import staircalculator.classic.com.classicstairscalculator.gdpr.GdprConsentForm
import staircalculator.classic.com.classicstairscalculator.gdpr.PrefAcceptConsentFormEU
import staircalculator.classic.com.classicstairscalculator.utilities.CheckInternet

class MainAds (val context: Context) {

    var mInterstitialAd: InterstitialAd? = null

    fun bannerAds(view: AdView){
        if(!isChinaLocale())
            view.loadAd(AdRequest.Builder().build())
    }

    fun showInterstitial(activity: Activity){
        if (mInterstitialAd != null && !isChinaLocale())
            mInterstitialAd?.show(activity)
    }

    fun destroyAds(view: AdView){
        view.destroy()
    }

    fun requestNewInterstitial(){
        if (mInterstitialAd == null && !isChinaLocale())
            loadInterstitialAd()
    }

    private fun loadInterstitialAd(){
        val adUnitId = context.getString(R.string.interstitial_ad)
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(context, adUnitId, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
            }
        })
    }

    fun requestNewInterstitialForGDPR(activity: Activity, consentFormGDPR: ConsentFormGDPR){
        if (mInterstitialAd == null && !isChinaLocale())
            loadInterstitialAdForGDPR(activity, gdprConsentForm)
    }

    private fun loadInterstitialAdForGDPR(activity: Activity, consentFormGDPR: ConsentFormGDPR){
        val adUnitId = context.getString(R.string.interstitial_ad)
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(context, adUnitId, adRequest, object : InterstitialAdLoadCallback() {

            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
                setConsentFormGDPR(activity, gdprConsentForm)
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
                PrefAcceptConsentFormEU(context).setAccept(true)
                gdprConsentForm.consentForm(activity)
            }
        })
    }

    private fun setConsentFormGDPR(activity: Activity, consentFormGDPR: ConsentFormGDPR){
        val isInternetConnected = CheckInternet().checkConnection(context)
        if(isInternetConnected){
            PrefAcceptConsentFormEU(context).setAccept(false)
            gdprConsentForm.consentForm(activity)
        }
    }

    private fun isChinaLocale(): Boolean {
        val locale = ConfigurationCompat.getLocales(Resources.getSystem().configuration).get(0)
        return when(locale?.country){
            "CN","HK" -> true
            else -> false
        }
    }
}
