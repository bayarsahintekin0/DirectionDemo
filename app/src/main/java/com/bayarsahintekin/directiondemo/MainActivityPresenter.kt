package com.bayarsahintekin.directiondemo

import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.bayarsahintekin.directiondemo.model.DirectionModel
import com.bayarsahintekin.directiondemo.network.IServices
import com.bayarsahintekin.directiondemo.network.post_request.DirectionApiPR
import com.huawei.hms.location.*
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.PolylineOptions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


class MainActivityPresenter() : MainActivityContract.Presenter {

    /**  Retrofit **/
    private var disposable: Disposable? = null
    private val services by lazy {
        IServices.create()
    }
    val API_KEY = "CV6Z1ND6P71OVoSaTKtQRWgTUXmLt84k+0iDb9zxd/svOay5nojYCblcsPlooCEhcfjPmF4DPckMk7YoxxaKrOgkBaen"

    private lateinit var settingsClient : SettingsClient
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var mLocationRequest: LocationRequest? = null
    private lateinit var  mLocationCallback: LocationCallback
    private lateinit var currentLocation: Location

    private lateinit var view: MainActivityContract.View
    constructor(view :MainActivityContract.View) :this(){
        this.view = view
    }

    override fun requestGetDirectionBicycling(destination: LatLng) {
        if (::currentLocation.isInitialized) {
            Log.e("-------->", "bicycling")
            view.showProgress()
            val post = DirectionApiPR(
                DirectionApiPR.Destination(destination.longitude, destination.latitude),
                DirectionApiPR.Origin(currentLocation.longitude, currentLocation.latitude)
            )
            disposable = services.getDirectionWalking(post, API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        {
                            getDirectionSuccess(
                                result,
                                LatLng(currentLocation.latitude, currentLocation.longitude)
                            )
                        }
                    },
                    { error ->
                        error.message?.let { view.getDirectionFailature(it) }
                        view.hideProgress()
                    }
                )
        }else
            Log.e("Warning","Please wait location obtaining")
    }

    override fun requestGetDirectionDriving(destination: LatLng) {
        Log.e("-------->","driving")
        if (::currentLocation.isInitialized) {
            view.showProgress()
            val post = DirectionApiPR(
                DirectionApiPR.Destination(destination.longitude, destination.latitude),
                DirectionApiPR.Origin(currentLocation.longitude, currentLocation.latitude)
            )
            disposable = services.getDirectionWalking(post, API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        getDirectionSuccess(
                            result,
                            LatLng(currentLocation.latitude, currentLocation.longitude)
                        )
                    },
                    { error ->
                        error.message?.let { view.getDirectionFailature(it) }
                        view.hideProgress()
                    }

                )
        }else
            Log.e("Warning","Please wait location obtaining")
    }

    override fun requestGetDirectionWalking(destination: LatLng) {
        if (::currentLocation.isInitialized) {
            Log.e("-------->", "walking")
            view.showProgress()
            val post = DirectionApiPR(
                DirectionApiPR.Destination(destination.longitude, destination.latitude),
                DirectionApiPR.Origin(currentLocation.longitude, currentLocation.latitude)
            )
            disposable = services.getDirectionWalking(post, API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        getDirectionSuccess(
                            result,
                            LatLng(currentLocation.latitude, currentLocation.longitude)
                        )
                    },
                    { error ->
                        error.message?.let { view.getDirectionFailature(it) }
                        view.hideProgress()
                    }
                )
        }else
            Log.e("Warning","Please wait location obtaining")
    }

    private fun getDirectionSuccess(direction : DirectionModel, origin: LatLng){
        Log.e("-------------","success")
        val pathList : ArrayList<LatLng> = arrayListOf()
        if (direction.routes != null && direction.routes!!.isNotEmpty()){
            val route = direction.routes!![0]
            if (route.paths != null){
                for (i in route.paths!!){
                    val path = i
                    if (path.steps != null) {
                        for (j in path.steps!!){
                            if (j.polyline != null && j.polyline!!.isNotEmpty()){
                                for (k in j.polyline!!){
                                    pathList.add(LatLng(k.lat!!,k.lng!!))
                                }
                            }
                        }
                    }
                }
            }
        }

        view.getDirectionSuccess(pathList)
    }

    override fun initLocation(context :Context) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        // create settingsClient
        settingsClient = LocationServices.getSettingsClient(context)
        mLocationRequest = LocationRequest()
        // Set the interval for location updates, in milliseconds.
        mLocationRequest!!.interval = 10000
        // set the priority of the request
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val locations =
                    locationResult.locations
                if (!locations.isEmpty()) {
                    for (location in locations) {
                        currentLocation = location

                    }
                }
            }

            override fun onLocationAvailability(locationAvailability: LocationAvailability) {
                val flag = locationAvailability.isLocationAvailable
            }
        }
    }

    override fun requestLocationUpdate(){
        fusedLocationProviderClient
            ?.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper())
            ?.addOnSuccessListener {
                //Processing when the API call is successful.
            }
            ?.addOnFailureListener {
                //Processing when the API call fails.
            }
    }

    override fun requestRemoveLocationUpdate(){
        fusedLocationProviderClient!!.removeLocationUpdates(mLocationCallback)
            .addOnSuccessListener {
                //Location updates are stopped successfully.
            }
            .addOnFailureListener {
                //Failed to stop location updates.
            }
    }

    override fun getLastKnownLocation(): Location? {
        var lastLocation : Location? = null
        val task =
            fusedLocationProviderClient!!.lastLocation
                .addOnSuccessListener { location ->
                    lastLocation = location
                    return@addOnSuccessListener
                }
                .addOnFailureListener {
                    return@addOnFailureListener
                }
        return lastLocation
    }



}