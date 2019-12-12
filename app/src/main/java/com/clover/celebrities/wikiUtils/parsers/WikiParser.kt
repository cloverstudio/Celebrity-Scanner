package com.clover.celebrities.wikiUtils.parsers

import org.jsoup.nodes.Element
import java.text.ParseException

interface WikiParser {
    @Throws(ParseException::class)
    fun parseHtml(element: Element): String?
    fun getWantedTag(): String
}