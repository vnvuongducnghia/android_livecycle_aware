package com.nghia.androidlivecycleaware

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

/**
 * Created by nghia.vuong on 13,May,2021
 * Lớp này phụ trách việc kết nối lấy thông tin location của user, và trả thông tin này về cho lớp gọi
 * context: Context của lớp gọi
 * callback: Trả kết quả về cho callback
 */

@SuppressLint("MissingPermission")
class MyLocationManager(private val context: Context, private val callback: (Location) -> Unit) :
    LifecycleObserver {

    private var mLocationManager: LocationManager? = null

    /*Phuong thuc bat dau ket noi voi Google Service de lay toa do*/
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun start() {
        mLocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        mLocationManager?.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            0,
            0f,
            locationListener
        )
        // Force an update with the lass location, if available.
        mLocationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let {
            locationListener.onLocationChanged(it)
        }

        Toast.makeText(context, "MyLocationManager started", Toast.LENGTH_SHORT).show()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun stop() {
        if (mLocationManager == null) {
            return
        }
        mLocationManager?.removeUpdates(locationListener)
        mLocationManager = null
        Toast.makeText(context, "MyLocationManager paused", Toast.LENGTH_SHORT).show()
    }

    /* Custom lai LocationListen de co the tra thong tin location ve cho lop goi thong qua callback */
    private val locationListener = LocationListener { location -> callback.invoke(location) }
}