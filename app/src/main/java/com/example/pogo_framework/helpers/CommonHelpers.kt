package com.example.pogo_framework.helpers

import java.text.SimpleDateFormat
import java.util.*
import android.text.format.DateUtils
import java.text.ParseException


class CommonHelpers {

    companion object {

        fun getFormattedDate(dateStr: String): String? {
            var formatter: SimpleDateFormat? = SimpleDateFormat("yyyy-MM-dd")
            val date = formatter?.parse(dateStr) // this never ends while debugging
            val cal: Calendar = Calendar.getInstance()
            cal.time = date
            val day: Int = cal.get(Calendar.DATE)
            return if (!(day in 11..18)) {
                when (day % 10) {
                    1 -> SimpleDateFormat("MMMM d'st' yyyy").format(date)
                    2 -> SimpleDateFormat("MMMM d'nd' yyyy").format(date)
                    3 -> SimpleDateFormat("MMMM d'rd' yyyy").format(date)
                    else -> SimpleDateFormat("MMMM d'th' yyyy").format(date)
                }
            } else SimpleDateFormat("MMMM d'th' yyyy").format(date)
        }

        fun getRelativeTime(time:String): CharSequence? {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            sdf.timeZone = TimeZone.getTimeZone("GMT")
            try {
                val time = sdf.parse(time).time
                val now = System.currentTimeMillis()
                val ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)
                return ago
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return  ""
        }
    }
}