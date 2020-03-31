package com.bayarsahintekin.directiondemo

import android.content.Context
import android.location.Location
import com.huawei.hms.maps.model.LatLng

class MainActivityContract {
    interface View {
        fun showProgress()
        fun hideProgress()
        fun getDirectionSuccess(pathList :ArrayList<LatLng>)
        fun getDirectionFailature(message :String)
    }

    interface Presenter {
        fun requestGetDirectionWalking(destination : LatLng)
        fun requestGetDirectionDriving(destination : LatLng)
        fun requestGetDirectionBicycling(destination : LatLng)

        fun initLocation(context: Context)
        fun requestLocationUpdate()
        fun requestRemoveLocationUpdate()
        fun getLastKnownLocation(): Location?
    }
}