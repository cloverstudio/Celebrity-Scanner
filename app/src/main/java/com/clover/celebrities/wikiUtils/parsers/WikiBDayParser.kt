package com.clover.celebrities.wikiUtils.parsers

import android.util.Log
import org.jsoup.nodes.Element
import java.text.SimpleDateFormat
import java.util.*

class WikiBDayParser: WikiParser {

    private val bdayTag = "span"

    override fun parseHtml(element: Element): String? {
        val sth = formatDate(element.html())
        Log.d("Debugging", sth)
        return sth
    }


    override fun getWantedTag() = bdayTag

    private fun formatDate(date: String): String {
        val fromUser = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val myFormat = SimpleDateFormat("MMMM d, yyyy", Locale.US)
        return myFormat.format(fromUser.parse(date) ?: Date())
    }

}