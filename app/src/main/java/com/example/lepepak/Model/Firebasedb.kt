package com.example.lepepak.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Firebasedb(
    var bentuk : String = "",
    var desc : String = "",
    var link : String = ""
): Parcelable
