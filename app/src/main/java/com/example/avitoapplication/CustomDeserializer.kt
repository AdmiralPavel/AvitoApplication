package com.example.avitoapplication

import com.google.android.gms.maps.model.LatLng
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.lang.reflect.Type


class CustomDeserializer :JsonDeserializer<LatLng> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LatLng {
        val jObject: JsonObject = json!!.asJsonObject
        val lat = jObject["lat"].asDouble
        val lng = jObject["lng"].asDouble
        return LatLng(lat, lng)
    }

}
