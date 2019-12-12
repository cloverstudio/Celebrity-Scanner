package com.clover.celebrities.wikiUtils.extractors

import com.clover.celebrities.wikiUtils.parsers.WikiBDayParser
import com.clover.celebrities.wikiUtils.parsers.WikiImageParser


//concrete implementations of the WikiInfoExtractor
class WikiImageExtractor(celebrityName: String):
    WikiInfoExtractor(celebrityName, WikiImageParser())

class WikiBDayExtractor(celebrityName: String):
    WikiInfoExtractor(celebrityName, WikiBDayParser())