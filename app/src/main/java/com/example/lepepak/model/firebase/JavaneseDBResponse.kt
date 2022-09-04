package com.example.lepepak.model.firebase

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class JavaneseDBResponse(
    var bentuk : String = "",
    var desc : String = "",
    var link : String = ""
): Parcelable
