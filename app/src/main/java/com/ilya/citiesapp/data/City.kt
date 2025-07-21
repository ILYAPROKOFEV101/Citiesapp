package com.ilya.citiesapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class City(val name: String, val foundation: String) : Parcelable
