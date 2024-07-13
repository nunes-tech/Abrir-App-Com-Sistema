package com.example.abrirappcomsistema

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.abrirappcomsistema.utils.getAppUser
import com.example.abrirappcomsistema.utils.openAppByPackageName

class AppLauncherWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val packageName = applicationContext.getAppUser()
        Log.d("AppLauncherWorker", "PackageName recuperado: $packageName")

        if (packageName.isNotEmpty()) {
            Log.d("AppLauncherWorker", "Iniciando app: $packageName")
            applicationContext.openAppByPackageName(packageName)
            return Result.success()
        } else {
            Log.e("AppLauncherWorker", "PackageName não encontrado")
            return Result.failure()
        }
    }
}