package com.alexandreladriere.f1companion

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.alexandreladriere.f1companion.utils.TWITTER_PROFILE_NAME
import com.alexandreladriere.f1companion.utils.TWITTER_USERID


class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        // Get a support ActionBar corresponding to this toolbar and enable the Up button
        setVersion()
        val button: Button = findViewById(R.id.button_twitter)
        button.setOnClickListener(View.OnClickListener {
            openTwitter()
        })
    }

    private fun setVersion() {
        var versionName = "Version: "
        try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            versionName += packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        val tv = findViewById<View>(R.id.textview_info_activity_app_version) as TextView
        tv.text = versionName
    }

    private fun openTwitter() {
        var intent: Intent? = null
        try {
            // get the Twitter app if possible
            this.packageManager.getPackageInfo("com.twitter.android", 0)
            intent = Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=$TWITTER_USERID"))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        } catch (e: Exception) {
            // no Twitter app, revert to browser
            intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/$TWITTER_PROFILE_NAME"))
        }
        this.startActivity(intent)
    }
}