package com.hms.cookingreceipes.utils

import com.hms.cookingreceipes.data.model.Entry

class AppConstants {
    companion object {
        const val KEY_START = "Start"
        const val KEY_END = "End"
        const val BASE_URL = "https://myanmarreceipes.blogspot.com/"
    }
}

internal typealias Feeds = List<Entry>