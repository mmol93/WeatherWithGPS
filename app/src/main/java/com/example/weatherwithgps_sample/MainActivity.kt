package com.example.weatherwithgps_sample

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.weatherwithgps_sample.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binder : ActivityMainBinding
    // 얻어야 하는 권한 리스트
    val permission_list = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binder.root)

        // frameLayout를 fragment로 교체하기
        val weatherTap = WeatherTap()
        val trans = supportFragmentManager.beginTransaction()
        trans.replace(binder.container.id, weatherTap)
        trans.commit()

        // 권한 리스트에 있는 권한 요청하기
        requestPermissions(permission_list, 0)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // 권한 얻지 않은 것이 있으면 함수를 종료하여 GPS 정보를 얻지 않게 하기
        for (permission in grantResults){
            if (permission == PackageManager.PERMISSION_DENIED){
                Toast.makeText(this, "권한을 얻지않으면 GPS 사용불가", Toast.LENGTH_SHORT).show()
                return
            }
        }
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        // 권한을 얻었는지 확인(getLastKnownLocation을 사용하기 위해서 반드시 필요한 사전 확인임)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        // 제일 최근 위치 정보값을 가져온다
        val gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        // GPS가 제일 정확하기 때문에 먼저 발동되게 한다
        if (gpsLocation != null){
            showInfo(gpsLocation)
        }else if (networkLocation != null){
            showInfo(networkLocation)
        }else{
            Log.d("location", "GPS 연결 실패")
        }
    }
    fun showInfo(location : Location){
        if (location != null){
            // 위도와 경도를 이용하여 도시 이름 가져오기
            val geocoder = Geocoder(this, Locale.getDefault())
            // address에는 GPS 결과에 따른 후보군들이 리스트 형태로 들어간다
            // maxResults는 해당 후보를 몇 개를 선정할지 결정(숫자가 낮은 것을 권장함)
            // 즉, address[0]에는 1번 후보가 들어가 있는 것임
            val addresses: List<Address> = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            if (addresses.isNotEmpty()) {
                // 경도와 위도를 이용하여 주소 이름 알아내기
                App.provider = location.provider
                App.countryName = addresses[0].countryName
                App.stateName = addresses[0].adminArea
                App.cityName = addresses[0].locality
                Log.d("location", "provider: ${location.provider}")
                Log.d("location", "countryName: ${addresses[0].countryName}")
                Log.d("location", "stateName: ${addresses[0].adminArea}")
                Log.d("location", "cityName: ${addresses[0].locality}")
            }
        }
    }
}