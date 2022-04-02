package com.example.a27_hamyarservice

import android.location.Location

class Student {
    var name:String? = null
    var desc:String? = null
    var image:Int? = null
    var location:Location? = null

    constructor(name:String,desc:String,image:Int,lat:Double,long:Double) {
        this.name = name
        this.desc = desc
        this.image = image
        this.location = Location("Student")
        this.location!!.latitude = lat
        this.location!!.longitude = long
    }
}