package com.example.pogo_framework.ui.fragments

import android.graphics.BitmapFactory
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pogo_framework.R
import com.example.pogo_framework.ui.activities.DetailType
import com.example.pogo_framework.ui.activities.PokemonDetailView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_explore.*
import kotlinx.android.synthetic.main.fragment_explore.view.*
import java.util.*

class ExploreFragment : Fragment() {

    var googleMap: GoogleMap? = null
    private val callback = OnMapReadyCallback { googleMap ->
        this.googleMap = googleMap
        attachListeners()
        initMap()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_explore, container, false)
        view.custom_map.onCreate(null)
        view.custom_map.getMapAsync(callback)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    private fun initMap() {
        addRandomPokemon()
        googleMap?.uiSettings?.isCompassEnabled = false
        googleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(-34.0, 151.0))
            .zoom(18f)
            .tilt(67.5f)
            .bearing(314f)
            .build()
        googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun addRandomPokemon() {
        val sydney = LatLng(-34.0, 151.0)
        googleMap?.addMarker(MarkerOptions().position(sydney).title("Sample Location"))

        val icon = BitmapFactory.decodeResource(
            context?.resources,
            R.drawable.pokeball_small
        )
        for (i in 0..100) {
            val marker = googleMap?.addMarker(
                MarkerOptions().position(getRandomLocation(sydney, 200)).title("").icon(
                    BitmapDescriptorFactory.fromBitmap(icon)
                )
            )
            marker?.tag = Random().nextInt(50)
        }
    }

    private fun getRandomLocation(point: LatLng, radius: Int): LatLng {
        val randomPoints: MutableList<LatLng> = ArrayList()
        val randomDistances: MutableList<Float> = ArrayList()
        val myLocation = Location("")
        myLocation.latitude = point.latitude
        myLocation.longitude = point.longitude

        for (i in 0..9) {
            val x0 = point.latitude
            val y0 = point.longitude
            val random = Random()

            // Convert radius from meters to degrees
            val radiusInDegrees = (radius / 111000f).toDouble()
            val u: Double = random.nextDouble()
            val v: Double = random.nextDouble()
            val w = radiusInDegrees * Math.sqrt(u)
            val t = 2 * Math.PI * v
            val x = w * Math.cos(t)
            val y = w * Math.sin(t)

            // Adjust the x-coordinate for the shrinking of the east-west distances
            val new_x = x / Math.cos(y0)
            val foundLatitude = new_x + x0
            val foundLongitude = y + y0
            val randomLatLng = LatLng(foundLatitude, foundLongitude)
            randomPoints.add(randomLatLng)
            val l1 = Location("")
            l1.latitude = randomLatLng.latitude
            l1.longitude = randomLatLng.longitude
            randomDistances.add(l1.distanceTo(myLocation))
        }
        //Get nearest point to the centre
        val indexOfNearestPointToCentre = randomDistances.indexOf(Collections.min(randomDistances))
        return randomPoints[indexOfNearestPointToCentre]
    }

    fun attachListeners() {
        googleMap?.setOnMarkerClickListener {
            redirectToCapture(it.tag as Int?)
            false
        }
    }

    private fun redirectToCapture(tag: Int?) {
        tag?.let {
            PokemonDetailView.startActivity(requireActivity(), DetailType.CAPTURE, it)
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            custom_map.onResume()
        } catch (e: Exception) {
        }
    }

    override fun onStart() {
        super.onStart()
        try {
            custom_map.onStart()
        } catch (e: Exception) {
        }
    }

    override fun onStop() {
        super.onStop()
        try {
            custom_map.onStop()
        } catch (e: Exception) {
        }
    }

    override fun onPause() {
        try {
            custom_map?.onPause()
        } catch (e: Exception) {
        }
        super.onPause()
    }

    override fun onDestroy() {
        try {
            custom_map.onDestroy()
        } catch (e: Exception) {
        }
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        try {
            custom_map.onLowMemory()
        } catch (e: Exception) {
        }
    }
}