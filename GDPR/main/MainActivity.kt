package staircalculator.classic.com.classicstairscalculator

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.MobileAds
import java.util.*

class MainActivity : AppCompatActivity(), DialogUnits.OnUnitSelected, DialogAbout.OnAboutSelect {

    private val consentFormGDPR = GdprConsentForm(this)
    private var mainAds: MainAds = MainAds(this)
    private lateinit var bindView: ActivityMainBinding

    override fun onUnitAccept(unit: String) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindView = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindView.root)

        MobileAds.initialize(this) {}

        openStairsType()

    }

    private fun openStairsType(){
        bindView.btnStairCalculator.setOnClickListener {
            startActivity(Intent(this, StairsTypeActivity::class.java))
            mainAds.showInterstitial(this)
        }
    }

    override fun onResume() {
        super.onResume()
        mainAds.requestNewInterstitialAdWithConsentFormForGDPR(this, consentFormGDPR)
    }
}
