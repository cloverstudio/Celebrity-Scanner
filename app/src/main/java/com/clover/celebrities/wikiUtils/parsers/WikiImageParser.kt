package com.clover.celebrities.wikiUtils.parsers

import org.jsoup.nodes.Element
import java.text.ParseException

class WikiImageParser : WikiParser {

    private val imageTag = "table"

    override fun parseHtml(element: Element): String? {
        try {
            val image = element
                .select("tbody")
                .select("tr")
            for (el in image) {
                val selection = el.select("td")
                if(selection.isEmpty()) {
                    continue
                }
                val url = selection.select("a")
                    .first()
                    .child(0)
                    .attr("src")
                return "https:$url"
            }
        } catch (ignore: Exception) {
            throw ParseException("No image found!", 0)
        }
        return null
    }

    override fun getWantedTag() = imageTag
}
