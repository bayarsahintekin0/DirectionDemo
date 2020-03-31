package com.bayarsahintekin.directiondemo.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class DirectionModel {
    @SerializedName("routes")
    @Expose
    var routes: List<Route>? = null

    @SerializedName("returnCode")
    @Expose
    var returnCode: String? = null

    @SerializedName("returnDesc")
    @Expose
    var returnDesc: String? = null

    class EndLocation {
        @SerializedName("lng")
        @Expose
        var lng: Double? = null

        @SerializedName("lat")
        @Expose
        var lat: Double? = null

    }

    class Northeast {
        @SerializedName("lng")
        @Expose
        var lng: Double? = null

        @SerializedName("lat")
        @Expose
        var lat: Double? = null

    }

    class Path {
        @SerializedName("duration")
        @Expose
        var duration: Double? = null

        @SerializedName("durationInTraffic")
        @Expose
        var durationInTraffic: Double? = null

        @SerializedName("distance")
        @Expose
        var distance: Double? = null

        @SerializedName("startLocation")
        @Expose
        var startLocation: StartLocation? = null

        @SerializedName("steps")
        @Expose
        var steps: List<Step>? = null

        @SerializedName("endLocation")
        @Expose
        var endLocation: EndLocation? = null

    }

    class Polyline {
        @SerializedName("lng")
        @Expose
        var lng: Double? = null

        @SerializedName("lat")
        @Expose
        var lat: Double? = null

    }

    class Route {
        @SerializedName("paths")
        @Expose
        var paths: List<Path>? = null

        @SerializedName("bounds")
        @Expose
        var bounds: Bounds? = null

    }

    class Southwest {
        @SerializedName("lng")
        @Expose
        var lng: Double? = null

        @SerializedName("lat")
        @Expose
        var lat: Double? = null

    }

    class StartLocation {
        @SerializedName("lng")
        @Expose
        var lng: Double? = null

        @SerializedName("lat")
        @Expose
        var lat: Double? = null

    }

    class Step {
        @SerializedName("duration")
        @Expose
        var duration: Double? = null

        @SerializedName("orientation")
        @Expose
        var orientation: Double? = null

        @SerializedName("distance")
        @Expose
        var distance: Double? = null

        @SerializedName("startLocation")
        @Expose
        var startLocation: StartLocation? = null

        @SerializedName("action")
        @Expose
        var action: String? = null

        @SerializedName("endLocation")
        @Expose
        var endLocation: EndLocation? = null

        @SerializedName("polyline")
        @Expose
        var polyline: List<Polyline>? = null

        @SerializedName("roadName")
        @Expose
        var roadName: String? = null

    }

    class Bounds {
        @SerializedName("southwest")
        @Expose
        var southwest: Southwest? = null

        @SerializedName("northeast")
        @Expose
        var northeast: Northeast? = null

    }

}