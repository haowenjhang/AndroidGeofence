package com.wen.geofencelib

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices

class MainActivity : UnityPlayerActivity() {

    var Tag:String  = "MyLog"

    private val gadgetQ = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q

    private lateinit var geoClient: GeofencingClient
    var geofenceList: MutableList<Geofence> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 建立 Geofencing 用戶端的例項
        geoClient = LocationServices.getGeofencingClient(this)

        Log.d(Tag, "建立 Geofencing 用戶端的例項 OnCreate!!! " + approveForegroundAndBackgroundLocation())

    }

    // 檢查授權
    @TargetApi(29)
    private fun approveForegroundAndBackgroundLocation(): Boolean {
        val foregroundLocationApproved = (
                PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_FINE_LOCATION
                ))
        val backgroundPermissionApproved =
            if (gadgetQ) {
                PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            } else {
                true
            }
        return foregroundLocationApproved && backgroundPermissionApproved
    }


    // 定義地理圍欄轉換作業的廣播接收器
    private val geofenceIntent: PendingIntent by lazy {

        Log.d(Tag, "定義地理圍欄轉換作業的廣播接收器")

        val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
        PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

    }


    // 指定地理圍欄和初始觸發條件
    private fun getGeofencingRequest(): GeofencingRequest {

        return GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER or  GeofencingRequest.INITIAL_TRIGGER_EXIT)
            addGeofences(geofenceList)
            Log.d(Tag, "指定地理圍欄和初始觸發條件")
        }.build()
    }


    // 建立地理圍欄物件
    fun AddPos(){
        //24.997232700439735, 121.4528951711794
        //25.014157173670394, 121.46225629838243
        //25.06071026633133, 121.45406200000014
        val latitude =  25.06071026633133
        val longitude = 121.45406200000014
        val radius = 100f

        geofenceList.add(
            Geofence.Builder()
                .setRequestId("Place_1")
                .setCircularRegion(latitude,longitude,radius)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
                .build())

        Log.d(Tag, "添加地域座標" + geofenceList.toString())
    }

    // 新增地理圍欄
    fun AddGeofence(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        geoClient?.addGeofences(getGeofencingRequest(), geofenceIntent)?.run {
            addOnSuccessListener {
                Log.d(Tag, "Geofence 添加成功！" + getGeofencingRequest().toString())

            }
            addOnFailureListener {
                Log.d(Tag, "Geofence 添加失敗！")

            }
        }
    }

    fun removeGeofence(){
        geoClient?.removeGeofences(geofenceIntent)?.run {
            addOnSuccessListener {
                Log.d(Tag, "Geofence 刪除成功！")
            }
            addOnFailureListener {
                Log.d(Tag, "Geofence 刪除失敗！")
            }
        }
    }
}