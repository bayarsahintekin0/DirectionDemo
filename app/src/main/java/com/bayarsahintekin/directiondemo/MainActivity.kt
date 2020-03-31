package com.bayarsahintekin.directiondemo

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.tabs.TabLayout
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.Polyline
import com.huawei.hms.maps.model.PolylineOptions
import kotlinx.android.synthetic.main.activity_main.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION")
class MainActivity : AppCompatActivity(),MainActivityContract.View{


    private lateinit var currentLocation: Location
    private lateinit var map : HuaweiMap

    private lateinit var currentPolyline : Polyline
    private var selectedTab = 0
    private lateinit var presenter: MainActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        presenter = MainActivityPresenter(this)

        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle("MapViewBundleKey")
        }

        mapView.onCreate(mapViewBundle)

        mapView.getMapAsync {
             if (!::map.isInitialized)
                 map = it
             map.isMyLocationEnabled = true

            progress.smoothToHide()

             map.setOnMapClickListener {
                 Log.e("--------->",it.toString())
                 if (::currentPolyline.isInitialized)
                     currentPolyline.remove()
                 when(selectedTab){
                     0 -> presenter.requestGetDirectionWalking(LatLng(it.latitude, it.longitude))
                     1 -> presenter.requestGetDirectionDriving(LatLng(it.latitude, it.longitude))
                     2 -> presenter.requestGetDirectionBicycling(LatLng(it.latitude, it.longitude))
                 }
             }

         }

        if (checkPermission()) {
            Log.e("-------->","permisiion granted")
            progress.smoothToShow()
            presenter.initLocation(this)
            presenter.requestLocationUpdate()
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                selectedTab = p0?.position!!
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }
        })
    }

    override fun getDirectionFailature(message: String) {
        Log.e("-------------","failture")
    }

    override fun getDirectionSuccess(pathList: ArrayList<LatLng>) {
        currentPolyline = map.addPolyline(
            PolylineOptions().addAll(pathList)
                .color(resources.getColor(R.color.colorPrimary)))
        progress.smoothToHide()
    }

    override fun hideProgress() {
        progress.smoothToHide()
    }

    override fun showProgress() {
        progress.smoothToShow()
    }

    fun checkPermission():Boolean{
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val strings = arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                Log.e("-------->","permisiion request")
                ActivityCompat.requestPermissions(this, strings, 1)
                return false
            }else
                return true
        } else {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    this,
                    "android.permission.ACCESS_BACKGROUND_LOCATION"
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val strings = arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    "android.permission.ACCESS_BACKGROUND_LOCATION"
                )
                Log.e("-------->","permisiion request")
                ActivityCompat.requestPermissions(this, strings, 2)
                return false
            }else
                return true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.e("-------->",requestCode.toString())
        Log.e("-------->",grantResults.toString())
        if (requestCode == Activity.RESULT_OK){
            progress.smoothToShow()
            presenter.initLocation(this)
            presenter.requestLocationUpdate()
        }

    }





}

