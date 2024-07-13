package com.example.abrirappcomsistema.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.example.abrirappcomsistema.App

fun Context.openAppByPackageName(packageName: String) {
    //val intent = packageManager.getLaunchIntentForPackage(packageName)
    val intent = packageManager.getLaunchIntentForPackage(packageName)?.apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    }
    if (intent != null) {
        startActivity(intent)
        Log.i("teste", "openAppByPackageName: Intent não é nula OK")
        Log.i("teste", "openAppByPackageName: a intent é: $intent")
    } else {
        // App não encontrado, redirecionar para a Play Store
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
            Log.i("teste", "openAppByPackageName: App não encontrado, redirecionar para a Play Store")
        } catch (e: Exception) {
            // Caso Play Store não esteja instalada, abrir no navegador
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
            Log.i("teste", "openAppByPackageName: Caso Play Store não esteja instalada, abrir no navegador")
        }
    }
}

fun Context.getInstalledApps(): List<App> {
    val intent = Intent(Intent.ACTION_MAIN, null)
    intent.addCategory(Intent.CATEGORY_LAUNCHER)

    val resolveInfoList = packageManager.queryIntentActivities(intent, 0)
    val installedApps = resolveInfoList.filter {
        it.activityInfo.packageName != null
    }

    return installedApps.mapNotNull {
        val name = packageManager.getApplicationLabel(it.activityInfo.applicationInfo)
        val icon = packageManager.getApplicationIcon(it.activityInfo.applicationInfo)
        val packageName = it.activityInfo.packageName
        App(name.toString(), icon, packageName)
    }

}

private fun Context.sharedPreferencesAppSaved(): SharedPreferences? {
    return this.getSharedPreferences("app_open_system", Context.MODE_PRIVATE)
}

@SuppressLint("CommitPrefEdits")
fun Context.saveAppUser(packageName: String): Boolean {

    try {
        val editor = sharedPreferencesAppSaved()?.edit() ?: return false

        editor.putString("app_selected", packageName).apply()

        return true

    } catch (erro:Exception) {
        erro.printStackTrace()
        return false
    }
}

fun Context.getAppUser() : String {
    return sharedPreferencesAppSaved()?.getString("app_selected", "") ?: ""
}