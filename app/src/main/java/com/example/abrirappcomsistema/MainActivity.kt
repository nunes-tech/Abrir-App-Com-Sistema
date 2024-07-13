package com.example.abrirappcomsistema

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.abrirappcomsistema.databinding.ActivityMainBinding
import com.example.abrirappcomsistema.utils.checkBatteryOptimization
import com.example.abrirappcomsistema.utils.checkOverlayPermission
import com.example.abrirappcomsistema.utils.getAppUser
import com.example.abrirappcomsistema.utils.getInstalledApps
import com.example.abrirappcomsistema.utils.openAppByPackageName
import com.example.abrirappcomsistema.utils.saveAppUser

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val adapter by lazy {
        ListAppsAdapter { packageName ->
            if ( saveAppUser( packageName ) ) {
                Toast.makeText(this, "App salvo!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        checkBatteryOptimization()
        checkOverlayPermission()

        val apps = getInstalledApps()
        binding.rvLista.adapter = adapter
        adapter.updateList( apps )
        binding.rvLista.layoutManager = LinearLayoutManager(this)

        binding.fabTeste.setOnClickListener {
            val appSelected = getAppUser()
            if ( appSelected.isNotEmpty() ) {
                openAppByPackageName( appSelected )
            }
        }
    }

}