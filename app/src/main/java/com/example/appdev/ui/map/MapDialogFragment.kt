package com.example.appdev.ui.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import com.example.appdev.R
import com.example.appdev.network.ATMResponse
import com.example.appdev.network.RetrofitClient
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapDialogFragment : DialogFragment() {

    private lateinit var mapView: MapView
    private lateinit var locationOverlay: MyLocationNewOverlay
    private lateinit var compassOverlay: CompassOverlay
    private val mainHandler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        // Handle the close button
        view.findViewById<Button>(R.id.closeButton).setOnClickListener {
            dismiss()
        }

        mapView = view.findViewById(R.id.map)
        Configuration.getInstance().load(context, context?.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))
        mapView.setTileSource(TileSourceFactory.MAPNIK)

        // Initialize map view
        mapView.setMultiTouchControls(true)

        // Set up location overlay
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1)
        } else {
            setupLocationOverlay()
        }

        return view
    }

    private fun setupLocationOverlay() {
        locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), mapView)
        locationOverlay.enableMyLocation()
        mapView.overlays.add(locationOverlay)

        // Set up compass overlay
        compassOverlay = CompassOverlay(context, mapView)
        compassOverlay.enableCompass()
        mapView.overlays.add(compassOverlay)

        // Center map on the current location
        locationOverlay.runOnFirstFix {
            mainHandler.post {
                val mapController: IMapController = mapView.controller
                mapController.setZoom(15.0)
                val startPoint = locationOverlay.myLocation
                if (startPoint != null) {
                    mapController.setCenter(GeoPoint(startPoint.latitude, startPoint.longitude))
                    fetchNearbyATMs(startPoint.latitude, startPoint.longitude)
                }
            }
        }
    }

    private fun fetchNearbyATMs(latitude: Double, longitude: Double) {
        val apiService = RetrofitClient.apiServiceNominatim
        val call = apiService.searchATMs("ATM", "json", 50, latitude, longitude, 5000) // Increased limit to 50 and radius to 5000 meters

        call.enqueue(object : Callback<List<ATMResponse>> {
            override fun onResponse(call: Call<List<ATMResponse>>, response: Response<List<ATMResponse>>) {
                Log.d("MapDialogFragment", "Response code: ${response.code()}")
                Log.d("MapDialogFragment", "Response body: ${response.body()}")

                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.isEmpty()) {
                            Log.d("MapDialogFragment", "No ATMs found")
                            Toast.makeText(context, "No ATMs found", Toast.LENGTH_SHORT).show()
                        } else {
                            Log.d("MapDialogFragment", "ATMs found: ${it.size}")
                            Toast.makeText(context, "ATMs found: ${it.size}", Toast.LENGTH_SHORT).show()
                            addATMMarkers(it)
                        }
                    } ?: run {
                        Log.d("MapDialogFragment", "Response body is null")
                        Toast.makeText(context, "No data received", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.d("MapDialogFragment", "Failed to get response: ${response.code()}")
                    Toast.makeText(context, "Failed to get response: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ATMResponse>>, t: Throwable) {
                Log.e("MapDialogFragment", "Error fetching ATMs", t)
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addATMMarkers(atmResponses: List<ATMResponse>) {
        for (atm in atmResponses) {
            Log.d("MapDialogFragment", "Adding ATM marker at: ${atm.lat}, ${atm.lon}")
            val marker = Marker(mapView)
            marker.position = GeoPoint(atm.lat, atm.lon)
            marker.title = atm.display_name
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            mapView.overlays.add(marker)
        }
        mapView.invalidate() // Refresh the map to show markers
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 1) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                setupLocationOverlay()
            } else {
                // Permission denied, show a message to the user
            }
            return
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    override fun onResume() {
        super.onResume()
        dialog?.setOnKeyListener { _, keyCode, event ->
            if (keyCode == android.view.KeyEvent.KEYCODE_BACK && event.action == android.view.KeyEvent.ACTION_UP) {
                dismiss()
                true
            } else {
                false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDetach()
    }
}
