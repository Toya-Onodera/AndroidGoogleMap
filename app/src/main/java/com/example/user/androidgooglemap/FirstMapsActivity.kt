package com.example.user.androidgooglemap

import android.app.AlertDialog
import android.app.PendingIntent.getActivity
import android.app.Service
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.content.Intent
import android.provider.Settings
import android.content.DialogInterface




class FirstMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mLocationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mLocationManager = this.getSystemService(Service.LOCATION_SERVICE) as LocationManager

        val gpsFlg = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!gpsFlg) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("GPSがオンになっていません。")
                    .setMessage("設定画面からGPSをオンにしてください")
                    .setPositiveButton("OK", { dialog, id ->
                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        startActivityForResult(intent, 0)
                    })
                    .setNegativeButton("Cancel", null)
                    .show()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mLocationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                3000,
                10f,
                object: LocationListener {
                    override fun onLocationChanged(location: Location) {
                        val mPosition = LatLng(location.latitude, location.longitude)
                        mMap.addMarker(MarkerOptions().position(mPosition).title("現在地です。"))
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mPosition, 18f))
                        mLocationManager.removeUpdates(this)
                    }

                    override fun onProviderDisabled(provider: String) {}
                    override fun onProviderEnabled(provider: String) {}
                    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
                }
        )

        mLocationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            3000,
            10f,
            object: LocationListener {
                override fun onLocationChanged(location: Location) {
                    val mPosition = LatLng(location.latitude, location.longitude)
                    mMap.addMarker(MarkerOptions().position(mPosition).title("現在地です。"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mPosition, 18f))
                    mLocationManager.removeUpdates(this)
                }

                override fun onProviderDisabled(provider: String) {}
                override fun onProviderEnabled(provider: String) {}
                override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            }
        )
    }
}
