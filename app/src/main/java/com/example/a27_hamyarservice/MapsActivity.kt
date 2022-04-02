package com.example.a27_hamyarservice

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.a27_hamyarservice.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        checkPermission()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    private fun checkPermission(){
        if(Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),222)
                return
            }
        } else {
            getUserLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getUserLocation() {
        Toast.makeText(applicationContext,"دسترسی به موقعیت فعال شد",Toast.LENGTH_LONG).show()

        var myLocation = myLocationListener()
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3,3f,myLocation)

        var myThred = myThred()
        myThred.start()
    }

    inner class myThred:Thread(){
      
        override fun run() {
            super.run()
            while (true){
                loadStudentsMarkers()
                try {
                    runOnUiThread {
                        mMap!!.clear()

                        //نمایش مارکر راننده
                        val hafshejan = LatLng(driverLocation!!.latitude, driverLocation!!.longitude)
                        mMap.addMarker(
                            MarkerOptions()
                                .position(hafshejan)
                                .title("راننده سرویس کامران")
                                .snippet("09932659878")
                        )
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hafshejan,20f))



                        //اضافه کردن مارکر دانش آموز
                        for (i in 0 until listOfStudent.size) {
                            var stu = listOfStudent[i]
                            var stuLocation = LatLng(stu.location!!.latitude,stu.location!!.longitude)
                            mMap.addMarker(
                                MarkerOptions()
                                    .position(stuLocation)
                                    .title(stu.name)
                                    .snippet(stu.desc)
                            )
                        }
                    }
                    Thread.sleep(1000)

                } catch (e:Exception){ //
                }
            }

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<out String>,grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            222 -> {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getUserLocation()
                } else {
                    Toast.makeText(applicationContext,"برای ادامه اجرای برنامه نیاز به دسترسی موقعیت است",Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    var driverLocation:Location? = null
    inner class myLocationListener:LocationListener{
        constructor(){
            driverLocation = Location("DriverLocation")
            driverLocation!!.longitude = 0.0
            driverLocation!!.latitude = 0.0
        }
        override fun onLocationChanged(location: Location) {
            driverLocation = location
        }
    }


    var listOfStudent = ArrayList<Student>()
    private fun loadStudentsMarkers(){
        listOfStudent.add(Student("Reza","Karimi",R.drawable.ic_baseline_account_boy,15.0,36.0))
        listOfStudent.add(Student("Reza","Karimi",R.drawable.ic_baseline_account_boy,10.0,30.0))
        listOfStudent.add(Student("Reza","Karimi",R.drawable.ic_baseline_account_boy,20.0,40.0))
    }
}