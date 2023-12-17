package staircalculator.classic.com.classicstairscalculator.gdpr

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.ump.ConsentForm
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import java.util.concurrent.atomic.AtomicBoolean

class ConsentFormGDPR(val context: Context) {

    private val TAG = "MyGdpr"

    private lateinit var consentInformation: ConsentInformation
    // Use an atomic boolean to initialize the Google Mobile Ads SDK and load ads once.
    private var isMobileAdsInitializeCalled = AtomicBoolean(false)

    fun consentForm(activity: Activity){
//        val debugSettings = ConsentDebugSettings.Builder(context)
//            .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
//            .addTestDeviceHashedId("B6DF5BD354F90F063A3592DFB67C973D")
//            .build()

        // Set tag for under age of consent. false means users are not under age of consent.
        val params = ConsentRequestParameters
            .Builder()
            .setTagForUnderAgeOfConsent(false)
            //.setConsentDebugSettings(debugSettings)
            .build()

        consentInformation = UserMessagingPlatform.getConsentInformation(context)

        consentInformation.requestConsentInfoUpdate(
            activity,
            params,
            onConsentInfoUpdateSuccess(activity),
            onConsentInfoUpdateFailure()
        )

        // Check if you can initialize the Google Mobile Ads SDK in parallel while checking for new consent information.
        // Consent obtained in the previous session can be used to request ads.
        if (consentInformation.canRequestAds()) {
            initializeMobileAdsSdk()
        }
    }

    private fun onConsentInfoUpdateSuccess(activity: Activity): ConsentInformation.OnConsentInfoUpdateSuccessListener{
        return ConsentInformation.OnConsentInfoUpdateSuccessListener {
            // Load and show the consent form.
            UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                activity,
                onConsentFormDismissed()
            )
        }
    }

    private fun onConsentFormDismissed(): ConsentForm.OnConsentFormDismissedListener{
        return ConsentForm.OnConsentFormDismissedListener { loadAndShowError ->
            // Consent gathering failed.
            Log.w(TAG, "--- loadAndShowError --- " + String.format("%s: %s", loadAndShowError?.errorCode, loadAndShowError?.message))

            // Consent has been gathered.
            if (consentInformation.canRequestAds()) {
                initializeMobileAdsSdk()
            }
        }
    }

    private fun onConsentInfoUpdateFailure(): ConsentInformation.OnConsentInfoUpdateFailureListener {
        return ConsentInformation.OnConsentInfoUpdateFailureListener {
                requestConsentError ->
            // Consent gathering failed.
            Log.w(TAG, "--- requestConsentError --- " + String.format("%s: %s", requestConsentError.errorCode, requestConsentError.message))
        }
    }

    private fun initializeMobileAdsSdk() {
        val isAccepted = PrefAcceptConsentFormEU(context).getAccept()
        if(!isAccepted){
            consentInformation.reset()
        }

        if (isMobileAdsInitializeCalled.getAndSet(true)) {
            return
        }

        // Initialize the Google Mobile Ads SDK.
        //MobileAds.initialize(context)
        // TODO: Request an ad.
        // InterstitialAd.load(...)
        //iMain.mainAds.requestNewInterstitial()
    }
}
