package com.example.avitoapplication.models

import com.google.android.gms.maps.model.LatLng

data class Pin (
    val id: String,
    val service: String,
    val coordinates: LatLng
)