package com.example.toolsdisplay.utilities

import org.threeten.bp.ZoneId
import java.util.*


class ZoneUtility {

    companion object {

        const val timezone: String = "\"\""

        @JvmStatic
        fun appConfigZoneInstance(): ZoneId {
            if(!TextUtilities.isNullOrEmpty(timezone)){
                return ZoneId.of(timezone)
            }
            return ZoneId.systemDefault()
        }

        @JvmStatic
        fun appConfigTimeZoneInstance(): TimeZone {
            if(!TextUtilities.isNullOrEmpty(timezone)){
                return TimeZone.getTimeZone(timezone)
            }
            return TimeZone.getDefault()
        }
    }

}