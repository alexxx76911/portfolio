package com.example.aperture.util

import android.os.Build

fun haveQ() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
fun haveO() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O