package com.alexandreladriere.f1companion

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        // Get a support ActionBar corresponding to this toolbar and enable the Up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setVersion()
    }

    fun setVersion() {
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
}