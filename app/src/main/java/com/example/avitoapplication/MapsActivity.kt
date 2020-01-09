package com.example.avitoapplication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.avitoapplication.models.GsonObject
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.GsonBuilder
import com.google.gson.stream.MalformedJsonException
import java.io.FileNotFoundException
import java.io.InputStream

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var pins = mutableListOf<Marker>()
    private var services = HashMap<String, Boolean>()
    private val RESULT_CODE = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Parsing JSON and creating objects
        val parsedObject = parseJSON()
        if (parsedObject != null) {
            initPins(pins, services, parsedObject)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val intent = Intent(this, FilterActivity::class.java)

        intent.putExtra("services", services)
        startActivityForResult(intent, RESULT_CODE)
        return super.onOptionsItemSelected(item)
    }

    // Method to parse JSON and create object with pins
    private fun parseJSON(): GsonObject? {
        val string = readJSONFromAsset()
        if (string != null) {
            val gsonBuilder = GsonBuilder()
            gsonBuilder.registerTypeAdapter(LatLng::class.java, CustomDeserializer())
            val gson = gsonBuilder.create()
            try {
                return gson.fromJson(string, GsonObject::class.java)
            } catch (ex: MalformedJsonException) {
                Toast.makeText(this, "Your JSON file is broken", Toast.LENGTH_LONG).show()
            }
        }
        return null
    }

    // Method to read file with JSON
    private fun readJSONFromAsset(): String? {
        val json: String?
        try {
            val inputStream: InputStream = assets.open("pins.json")
            json = inputStream.bufferedReader().use { it.readText() }
        } catch (ex: FileNotFoundException) {
            Toast.makeText(this, "File with JSON data not found!", Toast.LENGTH_LONG).show()
            return null
        } catch (ex: Exception) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
            return null
        }
        return json
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_CODE) {
            try {
                val services = data?.extras?.get("services") as HashMap<String, Boolean>
                this.services = services
                updatePins(services, pins)
            } catch (e: Exception) {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updatePins(services: HashMap<String, Boolean>, pins: MutableList<Marker>) {
        for (marker in pins) {
            if (services[marker.title] != null)
                marker.isVisible = services[marker.title]!!
            else
                marker.isVisible = false
        }
        if (pins.size != 0) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pins[0].position, 12f))

        }
    }

    private fun initPins(
        pins: MutableList<Marker>,
        services: HashMap<String, Boolean>,
        parsedObject: GsonObject
    ) {
        for (pin in parsedObject.pins) {
            val marker =
                mMap.addMarker(MarkerOptions().position(pin.coordinates).title(pin.service))
            pins.add(marker)
        }
        for (service in parsedObject.services)
            services[service] = true
        if (pins.size != 0) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pins[0].position, 12f))

        }
    }
}
