package staircalculator.classic.com.classicstairscalculator

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.MobileAds
import staircalculator.classic.com.classicstairscalculator.activity.StairsTypeActivity
import staircalculator.classic.com.classicstairscalculator.ads.MainAds
import staircalculator.classic.com.classicstairscalculator.databinding.ActivityMainBinding
import staircalculator.classic.com.classicstairscalculator.dialogs.DialogAbout
import staircalculator.classic.com.classicstairscalculator.dialogs.DialogUnits
import staircalculator.classic.com.classicstairscalculator.gdpr.GdprConsentForm
import java.util.*

class MainActivity : AppCompatActivity(), DialogUnits.OnUnitSelected, DialogAbout.OnAboutSelect {

    private val gdprConsentForm = GdprConsentForm(this)
    private var mainAds: MainAds = MainAds(this)
    private lateinit var bindView: ActivityMainBinding

    override fun onUnitAccept(unit: String) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindView = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindView.root)

        MobileAds.initialize(this) {}

        openStairsType()

        bindView.btnExit.setOnClickListener { onBackPressed() }
        bindView.btnSettings.setOnClickListener { openDialogUnits() }
        bindView.btnAbout.setOnClickListener { openDialogAbout() }

    }

    fun applyLanguage(activity: Activity, language: String){
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        //config.setLocale(locale)
        config.locale = locale
        activity.baseContext.resources.updateConfiguration(
            config,
            activity.baseContext.resources.displayMetrics
        )
        activity.baseContext.applicationContext.resources.updateConfiguration(
            config,
            activity.baseContext.resources.displayMetrics
        )
    }

    private fun openStairsType(){
        bindView.btnStairCalculator.setOnClickListener {
            startActivity(Intent(this, StairsTypeActivity::class.java))
            mainAds.showInterstitial(this)
        }
    }

    private fun openDialogUnits() {
        val transaction = supportFragmentManager.beginTransaction()
        val unit = DialogUnits()
        unit.show(transaction, "units")
    }

    private fun openDialogAbout() {
        val transaction = supportFragmentManager.beginTransaction()
        val units = DialogAbout()
        units.show(transaction, "about")
    }

    override fun onResume() {
        super.onResume()
        mainAds.requestNewInterstitialForGDPR(this, gdprConsentForm)
    }
}
