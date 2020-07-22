package com.intern.internproject.utility

import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.intern.internproject.common.CLApplication.Companion.instance
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

object CLUtilities {
    var mContext=instance
    var longitude: Double? = null
    var latitude: Double? = null

    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        var validNum: Int = 1
        var validCaps: Int = 1
        var validSpcl: Int = 1
        var exp: String
        var pattern: Pattern
        var matcher: Matcher
        if (password.length > 6) {
            exp = ".*[A-Z].*"
            pattern = Pattern.compile(exp)
            matcher = pattern.matcher(password)
            if (matcher.matches()) {
                validNum = 0
            }
            exp = ".*[0-9].*"
            pattern = Pattern.compile(exp)
            matcher = pattern.matcher(password)
            if (matcher.matches()) {
                validCaps = 0
            }
            exp = ".*[!@#\$%^&*].*"
            pattern = Pattern.compile(exp)
            matcher = pattern.matcher(password)
            if (matcher.matches()) {
                validSpcl = 0
            }
            return (validNum == 0) && (validCaps == 0) && (validSpcl == 0)
        } else {
            return false
        }
    }
}