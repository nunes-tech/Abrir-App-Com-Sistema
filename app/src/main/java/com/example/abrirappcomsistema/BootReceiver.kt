package com.example.abrirappcomsistema

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val appLauncherWorkRequest = OneTimeWorkRequestBuilder<AppLauncherWorker>().build()
            WorkManager.getInstance(context).enqueue(appLauncherWorkRequest)
            Log.d("teste", "Trabalho agendado com sucesso")
        }
    }
}

/*override fun onReceive(context: Context, intent: Intent) {

       if (
           intent.action == Intent.ACTION_BOOT_COMPLETED ||
           (intent.action == Intent.ACTION_LOCKED_BOOT_COMPLETED && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
           ) {

           try {
               val packageApp = context.applicationContext.getAppUser()
               if (packageApp.isNotEmpty()) {
                   context.applicationContext.openAppByPackageName( packageApp )
               }
               Log.i("teste", "onReceive: Reiniciado!")
               Log.i("teste", "o packegeName: ${packageApp}")
           } catch (erro: Exception) {
               Log.i("teste", "onReceive: ${erro.message}")
           }
       }
   }*/