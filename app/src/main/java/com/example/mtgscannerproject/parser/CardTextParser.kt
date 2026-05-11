package com.example.mtgscannerproject.parser

import android.util.Log

class CardTextParser {

    //Try and regex match the patterns of set codes/collector numbers probably move to utils later
    private fun parsePossibleCardData(setCode: String, cardNum: String) {

        val setCodeRegex = Regex("\\b[A-Z0-9]{3}\\b")
        val collectorRegex = Regex("\\b\\d{1,3}[a-zA-Z]?\\b")

        val setMatches = setCodeRegex.findAll(setCode)
        val collectorMatches = collectorRegex.findAll(cardNum)

        for (match in setMatches) {
            Log.d("SET_CODE", match.value)
        }

        for (match in collectorMatches) {
            Log.d("COLLECTOR_NUM", match.value)
        }
    }
}