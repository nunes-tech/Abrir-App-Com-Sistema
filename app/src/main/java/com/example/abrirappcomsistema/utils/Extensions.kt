package com.example.abrirappcomsistema.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.provider.Settings
import android.net.Uri
import android.os.PowerManager
import android.provider.Settings.canDrawOverlays
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

fun Context.showMessageToast(message: String) {
    Toast.makeText(
        this,
        message,
        Toast.LENGTH_LONG)
        .show()
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


fun Context.abrirTelaOtimizacaoBateria(activity: Activity) {
    val intent = Intent()
    // Tenta abrir a tela de otimização de bateria do sistema
    intent.action = Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
    if (intent.resolveActivity(this.packageManager) != null) {
        this.startActivity(intent)
    } else {
        // Se a tela não existir, tenta abrir a tela de informações do app
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", this.packageName, null)
        intent.data = uri
        this.startActivity(intent)
        // Exibe uma mensagem explicando ao usuário como desativar a otimização manualmente
        showMessageToast("Por favor, encontre o app na lista e desative a otimização de bateria.")
    }
}

fun Context.abrirTelaPermissaoSobreposicao(activity: Activity) {
    val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
    val uri = Uri.fromParts("package", this.packageName, null)
    intent.data = uri
    if (intent.resolveActivity(this.packageManager) != null) {
        this.startActivity(intent)
    } else {
        // Se a tela não existir, exibe uma mensagem explicando ao usuário
        showMessageToast("Não foi possível abrir a tela de permissão de sobreposição.")
    }
}

@SuppressLint("ServiceCast")
fun Activity.isBatteryOptimizationDisabled(activity: Activity): Boolean {
    val powerManager = this.getSystemService(Context.POWER_SERVICE) as PowerManager
    return powerManager.isIgnoringBatteryOptimizations(this.packageName)
}

fun Activity.checkBatteryOptimization() {
    if (!isBatteryOptimizationDisabled(this)) {
        AlertDialog.Builder(this)
            .setTitle("Otimização de Bateria")
            .setMessage("Para o funcionamento correto do app, é necessário desativar a otimização de bateria.")
            .setPositiveButton("Abrir Configurações") { dialog, _ ->
                abrirTelaOtimizacaoBateria(this)
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}

fun Activity.checkOverlayPermission() {
    if (!canDrawOverlays(this)) {
        AlertDialog.Builder(this)
            .setTitle("Permissão de Sobreposição")
            .setMessage("Para o funcionamento correto do app, é necessário conceder a permissão de sobreposição.")
            .setPositiveButton("Abrir Configurações") { dialog, _ ->
                abrirTelaPermissaoSobreposicao(this)
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}