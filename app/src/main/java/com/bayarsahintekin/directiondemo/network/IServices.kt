package com.bayarsahintekin.directiondemo.network

import com.bayarsahintekin.directiondemo.model.DirectionModel
import com.bayarsahintekin.directiondemo.network.post_request.DirectionApiPR
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface IServices {
    @POST("walking")
    fun getDirectionWalking(@Body body: DirectionApiPR,
                            @Query("key") key :String)
    :Observable<DirectionModel>

    @POST("bicycling")
    fun getDirectionBicyling(@Body body: DirectionApiPR,
                            @Query("key") key :String)
    :Observable<DirectionModel>

    @POST("driving")
    fun getDirectionDriving(@Body body: DirectionApiPR,
                            @Query("key") key :String)
    :Observable<DirectionModel>


    companion object {
        fun create(): IServices {

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://mapapi.cloud.huawei.com/mapApi/v1/routeService/")
                .build()

            return retrofit.create(IServices::class.java)
        }
    }
}