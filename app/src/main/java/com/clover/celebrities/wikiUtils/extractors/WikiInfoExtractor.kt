package com.clover.celebrities.wikiUtils.extractors

import com.clover.celebrities.wikiUtils.parsers.WikiParser
import org.jsoup.Jsoup
import java.text.ParseException

abstract class WikiInfoExtractor(
    celebrityName: String,
    private val wikiParser: WikiParser
) {

    private val baseUrl = "https://en.wikipedia.org/wiki/"
    private val wikiUrl: String

    init {
        wikiUrl = createWikiUrl(celebrityName)
    }

    fun getCelebrityInfo(): String? {
        val doc = Jsoup.connect(wikiUrl).get()
        val spans = doc.select(wikiParser.getWantedTag())

        for (span in spans) {
            try {
                return wikiParser.parseHtml(span)
            } catch (ignore: ParseException) {
            }

        }
        return null
    }

    fun getWikiUrlString() = wikiUrl


    private fun createWikiUrl(name: String): String {
        val formattedName = createWikiName(name)
        return "$baseUrl$formattedName"
    }

    private fun createWikiName(name: String): String {
        return name.replace(" ", "_")
    }

}