package tools

import java.time.DayOfWeek

class CDate {
    companion object {
        fun toChinese(dayOfWeek: DayOfWeek): String {
            return when(dayOfWeek) {
                DayOfWeek.MONDAY -> "星期一"
                DayOfWeek.TUESDAY -> "星期二"
                DayOfWeek.WEDNESDAY -> "星期三"
                DayOfWeek.THURSDAY -> "星期四"
                DayOfWeek.FRIDAY -> "星期五"
                DayOfWeek.SATURDAY -> "星期六"
                DayOfWeek.SUNDAY -> "星期天"
            }
        }
    }
}