package com.example.helsinkimap.model

import com.example.helsinkimap.specs.entity.ActivityDto
import com.example.helsinkimap.specs.entity.ActivityLocationDto
import com.google.android.gms.maps.model.LatLng

object Models {
    val HELSINKI_LAT_LNG = LatLng(60.192059, 24.945831)

    val listActivityDto = listOf(
        ActivityDto(
            id = "833281ac-bbbd-480f-87d7-5fb6c3679bdd",
            name = "Accessible Helsinki Van Tour",
            infoUrl = "https://accessiblehelsinki.com/index.html",
            location = ActivityLocationDto(
                lat = 60.170074462890625,
                lon = 24.938390731811523,
                locality = "Helsinki",
                postalCode = "",
                streetAddress = "Helsinki"
            ),
            description = "description",
            imageLinkList = listOf()
        )
    )
}
