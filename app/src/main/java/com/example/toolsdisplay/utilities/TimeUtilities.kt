package com.example.toolsdisplay.utilities

import org.threeten.bp.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class TimeUtilities {



    companion object {
        @JvmStatic
        fun getStringOfISODateWithZoneId(date: org.threeten.bp.ZonedDateTime): String? {
            if (!TextUtilities.isNullOrEmpty(ZoneUtility.timezone)) {
                return org.threeten.bp.format.DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneUtility.appConfigZoneInstance())
                    .format(date)
            }
            return org.threeten.bp.format.DateTimeFormatter.ISO_DATE_TIME.format(date)

        }

        @JvmStatic
        fun getTimeZoneAlignedCalendar(): Calendar {
            return Calendar.getInstance(ZoneUtility.appConfigTimeZoneInstance())
        }
    }

}