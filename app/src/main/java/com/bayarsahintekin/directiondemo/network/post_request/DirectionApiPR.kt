package com.bayarsahintekin.directiondemo.network.post_request

class DirectionApiPR(
    var origin: Destination,
    var destination: Origin
){
    class Origin(var lng: Double,var lat: Double)
    class Destination(var lng: Double,var lat: Double)
}